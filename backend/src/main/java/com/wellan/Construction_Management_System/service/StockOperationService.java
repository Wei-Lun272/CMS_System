package com.wellan.Construction_Management_System.service;

import com.wellan.Construction_Management_System.dto.SingleConsumeDTO;
import com.wellan.Construction_Management_System.dto.SingleDispatchDTO;
import com.wellan.Construction_Management_System.entity.*;
import com.wellan.Construction_Management_System.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Service
public class StockOperationService {
    private final SiteMaterialRepository siteMaterialRepository;
    private final ConsumptionHistoryRepository consumptionHistoryRepository;
    private final SiteRepository siteRepository;
    private final MaterialRepository materialRepository;
    private final InventorySnapshotRepository inventorySnapshotRepository;
    private static final Logger logger = LoggerFactory.getLogger(StockOperationService.class);

    @Autowired
    public StockOperationService(SiteMaterialRepository siteMaterialRepository,
                                 ConsumptionHistoryRepository consumptionHistoryRepository,
                                 SiteRepository siteRepository,
                                 MaterialRepository materialRepository,
                                 InventorySnapshotRepository inventorySnapshotRepository) {
        this.siteMaterialRepository = siteMaterialRepository;
        this.consumptionHistoryRepository = consumptionHistoryRepository;
        this.siteRepository = siteRepository;
        this.materialRepository = materialRepository;
        this.inventorySnapshotRepository=inventorySnapshotRepository;
    }

    /**
     * 批量派發原物料到指定工地，並返回工地詳細信息。
     *
     * @param siteId      工地 ID
     * @param dispatchList 派發請求的原物料清單
     * @return 工地詳細信息 SiteDetailDTO
     */
    @Transactional
    public Integer batchDispatchMaterials(int siteId, List<SingleDispatchDTO> dispatchList) {
        logger.info("收到工地 ID {} 的批量派發請求: {}", siteId, dispatchList);

        // 確認工地是否存在
        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> {
                    logger.warn("工地 ID {} 不存在", siteId);
                    return new IllegalArgumentException("指定的工地不存在");
                });

        for (SingleDispatchDTO singleDispatch : dispatchList) {
            // 查找對應的原物料
            Material material = materialRepository.findById(singleDispatch.getMaterialId())
                    .orElseThrow(() -> {
                        logger.warn("原物料 ID {} 不存在", singleDispatch.getMaterialId());
                        return new IllegalArgumentException("指定的原物料不存在");
                    });

            // 查找或創建 SiteMaterial
            SiteMaterial siteMaterial = siteMaterialRepository.findBySiteAndMaterial(site, material)
                    .orElseGet(() -> {
                        SiteMaterial newSiteMaterial = SiteMaterial.createClearSiteMaterial(
                                site,
                                material,
                                0.0F,
                                singleDispatch.getAlertAmount()
                        );
                        return siteMaterialRepository.save(newSiteMaterial);
                    });

            // 更新 SiteMaterial 的庫存
            executeDispatch(siteMaterial, singleDispatch.getDispatchAmount());

            // 記錄消耗歷史
            ConsumptionHistory consumptionHistory = new ConsumptionHistory(
                    siteMaterial,
                    singleDispatch.getDispatchAmount(),
                    singleDispatch.getConsumeType(),
                    singleDispatch.getEffectiveDate()
            );
            consumptionHistoryRepository.save(consumptionHistory);

            logger.info("成功派發: 工地 ID {}, 原物料 ID {}, 數量 {}, 消耗類型 {}, 生效日期 {}",
                    site.getId(), material.getId(), singleDispatch.getDispatchAmount(),
                    singleDispatch.getConsumeType(), singleDispatch.getEffectiveDate());
        }

        return siteId;
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer batchConsumeMaterials(int siteId, List<SingleConsumeDTO> consumeList) {
        logger.info("收到工地 ID {} 的批量消耗請求: {}", siteId, consumeList);

        // 1. 確認工地是否存在
        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> new IllegalArgumentException("指定的工地不存在"));

        // 暫存所有要創建的消耗紀錄
        List<ConsumptionHistory> newHistories = new ArrayList<>();

        // 2. 處理每筆消耗請求
        for (SingleConsumeDTO consumeDTO : consumeList) {
            try {
                // 2.1 查找對應的原物料
                Material material = materialRepository.findById(consumeDTO.getMaterialId())
                        .orElseThrow(() -> new IllegalArgumentException("原物料 ID 不存在: " + consumeDTO.getMaterialId()));

                // 2.2 查找或驗證 SiteMaterial 是否存在
                SiteMaterial siteMaterial = siteMaterialRepository.findBySiteAndMaterial(site, material)
                        .orElseThrow(() -> new IllegalArgumentException("工地無對應的原物料，無法消耗"));

                // 3. 根據消耗類型處理紀錄
                ConsumptionHistory history = new ConsumptionHistory(
                        siteMaterial,
                        -consumeDTO.getConsumeAmount(),
                        consumeDTO.getConsumeType(),
                        consumeDTO.getEffectiveDate()
                );
                newHistories.add(history); // 暫存消耗紀錄

                logger.info("準備創建消耗紀錄，原物料 ID {}, 數量 {}, 類型 {}, 起效日期 {}",
                        material.getId(), consumeDTO.getConsumeAmount(), consumeDTO.getConsumeType(), consumeDTO.getEffectiveDate());

                // 4. 根據紀錄執行對應消耗邏輯
                if (consumeDTO.getConsumeType() == ConsumeType.ONCE) {
                    handleOnceConsumption(siteMaterial, consumeDTO.getConsumeAmount(), consumeDTO.getEffectiveDate());
                } else if (consumeDTO.getConsumeType() == ConsumeType.DAILY) {
                    handleDailyConsumption(siteMaterial, consumeDTO.getConsumeAmount(), consumeDTO.getEffectiveDate());
                }

            } catch (Exception e) {
                // 捕捉異常並記錄，拋出異常以觸發回滾
                logger.error("批量消耗時發生錯誤，工地 ID {}，錯誤訊息: {}", siteId, e.getMessage());
                throw e;
            }
        }

        // 5. 統一保存所有新增的消耗紀錄
        if (!newHistories.isEmpty()) {
            consumptionHistoryRepository.saveAll(newHistories);
        }

        logger.info("批量消耗操作成功完成，工地 ID: {}", siteId);
        return siteId; // 返回工地 ID，供前端使用
    }



    /**
     * 執行庫存的實際變動操作。
     * 更新 SiteMaterial 的庫存，並記錄對應的庫存變動快照。
     *
     * @param siteMaterial 要消耗的 SiteMaterial 物件
     * @param dispatchAmount 派發的數量
     */
    public void executeDispatch(SiteMaterial siteMaterial, float dispatchAmount) {
        float previousStock = siteMaterial.getStock();
        float newStock = previousStock + dispatchAmount;

        // 更新 SiteMaterial 的庫存
        siteMaterial.setStock(newStock);
        siteMaterialRepository.save(siteMaterial);

        // 紀錄庫存快照
        InventorySnapshot snapshot = new InventorySnapshot(
                siteMaterial,
                previousStock,
                newStock,
                ConsumeType.DISPATCH,
                new Timestamp(System.currentTimeMillis()),
                "派發庫存變動"
        );
        inventorySnapshotRepository.save(snapshot);
    }

    @Transactional
    public void executeConsumption(SiteMaterial siteMaterial, float consumptionAmount, ConsumeType consumeType, String description) {
        // 驗證輸入參數
        if (siteMaterial == null) {
            throw new IllegalArgumentException("SiteMaterial 不能為空");
        }
        if (consumeType == null) {
            throw new IllegalArgumentException("ConsumeType 不能為空");
        }

        // 計算新的庫存
        float newStock = siteMaterial.getStock() + consumptionAmount;

        // 驗證庫存是否足夠
        if (newStock < 0) {
            throw new IllegalArgumentException("庫存不足，無法執行消耗操作");
        }

        // 更新 SiteMaterial 的庫存
        siteMaterial.setStock(newStock);
        siteMaterialRepository.save(siteMaterial);

        // 紀錄庫存變動快照
        InventorySnapshot snapshot = new InventorySnapshot(
                siteMaterial,
                newStock,
                consumptionAmount,
                consumeType,
                new Timestamp(System.currentTimeMillis()),
                description
        );
        inventorySnapshotRepository.save(snapshot);
    }
    @Transactional
    public void executeDailyConsumption(SiteMaterial siteMaterial, float consumptionAmount) {
        executeConsumption(
                siteMaterial,
                consumptionAmount,
                ConsumeType.DAILY,
                "每日例行消耗"
        );
    }

    @Transactional
    public void executeOnceConsumption(SiteMaterial siteMaterial, float consumptionAmount) {
        executeConsumption(
                siteMaterial,
                consumptionAmount,
                ConsumeType.ONCE,
                "單次消耗"
        );
    }

    /**
     * 處理一次性（ONCE）消耗邏輯。
     *
     * @param siteMaterial 對應的 SiteMaterial
     * @param consumeAmount 消耗的數量
     * @param effectiveDate 消耗的起效日期
     */
    @Transactional
    public void handleOnceConsumption(SiteMaterial siteMaterial, float consumeAmount, Timestamp effectiveDate) {
        // 獲取當前日期
        Timestamp today = new Timestamp(System.currentTimeMillis());

        // 驗證是否是起效日期
        if (!effectiveDate.equals(today)) {
            logger.info("一次性消耗尚未生效，SiteMaterial ID {}, 起效日期 {}", siteMaterial.getId(), effectiveDate);
            return; // 尚未生效，直接退出
        }

        // 執行一次性消耗邏輯
        executeOnceConsumption(siteMaterial, consumeAmount);

        // 更新消耗紀錄為過期
        List<ConsumptionHistory> histories = consumptionHistoryRepository.findBySiteMaterialAndConsumeType(siteMaterial, ConsumeType.ONCE);

        for (ConsumptionHistory history : histories) {
            if (!history.getExpired() && history.getEffectiveDate().equals(effectiveDate)) {
                history.setExpired(true);
                consumptionHistoryRepository.save(history); // 保存更改
                logger.info("將一次性消耗紀錄設為過期，ID {}, 起效日期 {}", history.getId(), history.getEffectiveDate());
            }
        }

        logger.info("完成一次性消耗，SiteMaterial ID {}, 消耗數量 {}, 起效日期 {}", siteMaterial.getId(), consumeAmount, effectiveDate);
    }

    /**
     * 處理每日（DAILY）消耗邏輯。
     *
     * @param siteMaterial 對應的 SiteMaterial
     * @param consumeAmount 消耗的數量
     * @param effectiveDate 消耗的起效日期
     */
    @Transactional
    public void handleDailyConsumption(SiteMaterial siteMaterial, float consumeAmount, Timestamp effectiveDate) {
        // 1. 查詢該 SiteMaterial 的所有 DAILY 消耗紀錄
        List<ConsumptionHistory> histories = consumptionHistoryRepository.findBySiteMaterialAndConsumeType(siteMaterial, ConsumeType.DAILY);

        // 2. 將早於 effectiveDate 且尚未過期的紀錄設為過期
        for (ConsumptionHistory history : histories) {
            if (!history.getExpired() && history.getEffectiveDate().before(effectiveDate)) {
                history.setExpired(true);
                consumptionHistoryRepository.save(history);
            }
        }

        // 3. 執行每日消耗邏輯
        executeDailyConsumption(siteMaterial, consumeAmount);
    }




    /**
     * 回傳對應的工地的原物料資訊
     * @param siteId 工地 ID
     * @return 回傳對應的庫存原物料列表
     */
    public List<SiteMaterial> getMaterialsFromSite(Integer siteId){
        logger.info("收到請求工地ID為：{}的庫存原物料資訊",siteId);
        // 確認工地是否存在
        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> {
                    logger.warn("工地 ID {} 不存在", siteId);
                    return new IllegalArgumentException("指定的工地不存在");
                });
        return siteMaterialRepository.findBySite(site);
    }
    /**
     * 回傳工地與原物料對應關係的增減紀錄
     * @param siteMaterial 工地與原物料對應關係
     * @return 回傳對應的增減列表
     */
    public List<ConsumptionHistory> getMaterialsFromSite(SiteMaterial siteMaterial){
        logger.info("收到請求ID為{}的SiteMaterial庫存變動紀錄",siteMaterial.getId());
        List<ConsumptionHistory> bySiteMaterial = consumptionHistoryRepository.findBySiteMaterial(siteMaterial);
        return bySiteMaterial;
    }

}



