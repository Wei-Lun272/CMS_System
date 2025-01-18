package com.wellan.Construction_Management_System.service;

import com.wellan.Construction_Management_System.dto.AlertPredictionDTO;
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
import java.util.*;


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
     * 執行批量派發，並記錄派發歷史。
     *
     * @param siteId 工地 ID
     * @param dispatchList 派發請求的原物料清單
     */
    @Transactional
    public Integer batchDispatchMaterials(int siteId, List<SingleDispatchDTO> dispatchList) {
        logger.info("執行工地 ID {} 的批量派發請求: {}", siteId, dispatchList);

        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> new IllegalArgumentException("指定的工地不存在"));

        for (SingleDispatchDTO singleDispatch : dispatchList) {
            Material material = materialRepository.findById(singleDispatch.getMaterialId())
                    .orElseThrow(() -> new IllegalArgumentException("原物料 ID 不存在: " + singleDispatch.getMaterialId()));

            SiteMaterial siteMaterial = siteMaterialRepository.findBySiteAndMaterial(site, material)
                    .orElseThrow(() -> new IllegalArgumentException("指定的工地與原物料關係不存在"));

            // 更新庫存並記錄派發
            executeDispatch(siteMaterial, singleDispatch.getDispatchAmount());

            ConsumptionHistory history = new ConsumptionHistory(
                    siteMaterial,
                    singleDispatch.getDispatchAmount(),
                    ConsumeType.DISPATCH,
                    new Timestamp(System.currentTimeMillis()) // 當天日期
            );
            consumptionHistoryRepository.save(history);
            logger.info("派發完成，工地 ID: {}, 原物料 ID: {}, 數量: {}", siteId, material.getId(), singleDispatch.getDispatchAmount());
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
            logger.debug("處理消耗 DTO: materialId={}, consumeAmount={}, consumeType={}, effectiveDate={}",
                    consumeDTO.getMaterialId(), consumeDTO.getConsumeAmount(), consumeDTO.getConsumeType(), consumeDTO.getEffectiveDate());

            try {
                // 2.1 查找對應的原物料
                Material material = materialRepository.findById(consumeDTO.getMaterialId())
                        .orElseThrow(() -> new IllegalArgumentException("原物料 ID 不存在: " + consumeDTO.getMaterialId()));

                // 2.2 查找或驗證 SiteMaterial 是否存在
                SiteMaterial siteMaterial = siteMaterialRepository.findBySiteAndMaterial(site, material)
                        .orElseThrow(() -> new IllegalArgumentException("工地無對應的原物料，無法消耗"));
                logger.debug("查找到原物料: {}", material);
                logger.debug("查找到工地與原物料關係: {}", siteMaterial);
                // 3. 根據消耗類型處理紀錄
                ConsumptionHistory history = new ConsumptionHistory(
                        siteMaterial,
                        consumeDTO.getConsumeAmount(),
                        consumeDTO.getConsumeType(),
                        consumeDTO.getEffectiveDate()
                );
                newHistories.add(history); // 暫存消耗紀錄

                logger.info("準備創建消耗紀錄，原物料 ID {}, 數量 {}, 類型 {}, 起效日期 {}",
                        material.getId(), consumeDTO.getConsumeAmount(), consumeDTO.getConsumeType(), consumeDTO.getEffectiveDate());
                logger.debug("創建消耗紀錄: {}", history);
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
            logger.debug("準備批量保存消耗紀錄，共 {} 條: {}", newHistories.size(), newHistories);
            consumptionHistoryRepository.saveAll(newHistories);
        }

        logger.info("批量消耗操作成功完成，工地 ID: {}", siteId);
        return siteId; // 返回工地 ID，供前端使用
    }



    /**
     * 更新庫存，並記錄快照。
     *
     * @param siteMaterial 工地原物料關係
     * @param dispatchAmount 派發數量
     */
    @Transactional
    public void executeDispatch(SiteMaterial siteMaterial, float dispatchAmount) {
        float previousStock = siteMaterial.getStock();
        float newStock = previousStock + dispatchAmount;

        // 更新庫存
        siteMaterial.setStock(newStock);
        siteMaterialRepository.save(siteMaterial);

        // 記錄庫存快照
        InventorySnapshot snapshot = new InventorySnapshot(
                siteMaterial,
                previousStock,
                newStock,
                ConsumeType.DISPATCH,
                new Timestamp(System.currentTimeMillis()),
                "派發更新"
        );
        inventorySnapshotRepository.save(snapshot);

        logger.info("更新庫存完成，原物料 ID: {}, 原庫存: {}, 新庫存: {}", siteMaterial.getMaterial().getId(), previousStock, newStock);
    }


    @Transactional
    public void handleOnceConsumption(SiteMaterial siteMaterial, float consumeAmount, Timestamp effectiveDate) {
        // 獲取當前日期
        Timestamp today = new Timestamp(System.currentTimeMillis());
        logger.debug("執行一次性消耗，SiteMaterial ID={}, consumeAmount={}, effectiveDate={}",
                siteMaterial.getId(), consumeAmount, effectiveDate);

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

    @Transactional
    public void handleDailyConsumption(SiteMaterial siteMaterial, float consumeAmount, Timestamp effectiveDate) {
        // 1. 查詢該 SiteMaterial 的所有 DAILY 消耗紀錄
        List<ConsumptionHistory> histories = consumptionHistoryRepository.findBySiteMaterialAndConsumeType(siteMaterial, ConsumeType.DAILY);

        // 2. 將早於 effectiveDate 且尚未過期的紀錄設為過期，並設定過期日期
        for (ConsumptionHistory history : histories) {
            if (!history.getExpired() && history.getEffectiveDate().before(effectiveDate)) {
                history.setExpired(true); // 標記過期
                history.setExpirationDate(effectiveDate); // 設置過期日期
                consumptionHistoryRepository.save(history); // 保存更改
            }
        }


        // 4. 執行每日消耗邏輯
        executeDailyConsumption(siteMaterial, consumeAmount);
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
        float newStock = siteMaterial.getStock() - consumptionAmount; // 扣除消耗量

        // 驗證庫存是否足夠
        if (newStock < 0) {
            throw new IllegalArgumentException("庫存不足，無法執行消耗操作");
        }

        // 更新 SiteMaterial 的庫存
        siteMaterial.setStock(newStock);
        //更新SiteMaterial的最後執行時間
        updateLastDailyTaskExecutionDate(siteMaterial);
        siteMaterialRepository.save(siteMaterial);

        // 紀錄庫存變動快照
        InventorySnapshot snapshot = new InventorySnapshot(
                siteMaterial,
                newStock,
                -consumptionAmount, // 此處記錄消耗量為負數以標記扣減
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

    /**
     * 更新siteMaterial的LastDailyTaskExecutionDate，以便進行更新時的驗證
     * @param siteMaterial
     */
    @Transactional
    public void updateLastDailyTaskExecutionDate(SiteMaterial siteMaterial) {
        siteMaterial.setLastDailyTaskExecutionDate(new Timestamp(System.currentTimeMillis()));
        siteMaterialRepository.save(siteMaterial);
    }
    /**
     * 檢測派發請求是否與當天的紀錄重複。
     *
     * @param siteId 工地 ID
     * @param dispatchList 派發請求的原物料清單
     * @return 重複派發的詳細資訊
     */
    public List<ConsumptionHistory> detectDuplicateDispatches(int siteId, List<SingleDispatchDTO> dispatchList) {
        logger.info("檢測工地 ID {} 的重複派發請求: {}", siteId, dispatchList);

        List<ConsumptionHistory> duplicateHistories = new ArrayList<>();

        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> new IllegalArgumentException("指定的工地不存在"));

        for (SingleDispatchDTO singleDispatch : dispatchList) {
            Material material = materialRepository.findById(singleDispatch.getMaterialId())
                    .orElseThrow(() -> new IllegalArgumentException("原物料 ID 不存在: " + singleDispatch.getMaterialId()));

            // 查詢當天是否已有相同原物料與數量的派發紀錄
            List<ConsumptionHistory> existingHistories = consumptionHistoryRepository.findBySiteMaterialAndConsumeTypeAndEffectiveDate(
                    siteMaterialRepository.findBySiteAndMaterial(site, material)
                            .orElseThrow(() -> new IllegalArgumentException("指定的工地與原物料關係不存在")),
                    ConsumeType.DISPATCH,
                    new Timestamp(System.currentTimeMillis()) // 當天日期
            );

            for (ConsumptionHistory history : existingHistories) {
                if (history.getAmount().equals(singleDispatch.getDispatchAmount())) {
                    duplicateHistories.add(history);
                }
            }
        }

        logger.info("檢測完成，找到 {} 條重複派發紀錄", duplicateHistories.size());
        return duplicateHistories;
    }
    /**
     * 更新尚未生效且未過期的消耗紀錄（派發或單次消耗）。
     *
     * @param historyId 消耗紀錄的 ID
     * @param newAmount 新的數量
     * @param newEffectiveDate 新的生效日期
     * @return 更新後的 {@link ConsumptionHistory} 物件
     * @throws IllegalArgumentException 如果紀錄已過期或已生效，或指定的紀錄不存在
     * @see ConsumptionHistory
     */
    @Transactional
    public ConsumptionHistory updatePendingConsumptionHistory(
            int historyId,
            float newAmount,
            Timestamp newEffectiveDate
    ) {
        // 查找對應的紀錄
        ConsumptionHistory history = consumptionHistoryRepository.findById(historyId)
                .orElseThrow(() -> new IllegalArgumentException("未找到對應的消耗紀錄"));

        // 確認紀錄未過期且尚未生效
        Timestamp today = new Timestamp(System.currentTimeMillis());
        if (history.getExpired() || history.getEffectiveDate().before(today)) {
            throw new IllegalArgumentException("無法修改已生效或過期的消耗紀錄");
        }

        // 更新紀錄數據
        logger.info("更新消耗紀錄 ID: {}, 原數量: {}, 新數量: {}, 原生效日期: {}, 新生效日期: {}",
                historyId, history.getAmount(), newAmount, history.getEffectiveDate(), newEffectiveDate);

        history.setAmount(newAmount);
        history.setEffectiveDate(newEffectiveDate);

        return consumptionHistoryRepository.save(history);
    }
    /**
     * 刪除尚未生效且未過期的消耗紀錄（派發或單次消耗）。
     *
     * @param historyId 消耗紀錄的 ID
     * @throws IllegalArgumentException 如果紀錄已過期或已生效，或指定的紀錄不存在
     * @see ConsumptionHistory
     */
    @Transactional
    public void deletePendingConsumptionHistory(int historyId) {
        // 查找對應的紀錄
        ConsumptionHistory history = consumptionHistoryRepository.findById(historyId)
                .orElseThrow(() -> new IllegalArgumentException("未找到對應的消耗紀錄"));

        // 確認紀錄未過期且尚未生效
        Timestamp today = new Timestamp(System.currentTimeMillis());
        if (history.getExpired() || history.getEffectiveDate().before(today)) {
            throw new IllegalArgumentException("無法刪除已生效或過期的消耗紀錄");
        }

        logger.info("刪除消耗紀錄 ID: {}, 數量: {}, 生效日期: {}", historyId, history.getAmount(), history.getEffectiveDate());
        consumptionHistoryRepository.delete(history);
    }

    /**
     * 預測庫存下降到警戒值以下的日期，考慮每日消耗、單次消耗與派發。
     *
     * @param siteId 工地 ID
     * @return 每個原物料的警戒日期及相關數據的清單
     */
    public List<AlertPredictionDTO> predictStockAlert(int siteId) {
        logger.info("計算工地 ID {} 的庫存警戒日期", siteId);

        // 查詢工地是否存在
        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> new IllegalArgumentException("指定的工地不存在"));

        List<AlertPredictionDTO> predictions = new ArrayList<>();

        // 查詢工地所有原物料
        List<SiteMaterial> siteMaterials = siteMaterialRepository.findBySite(site);

        for (SiteMaterial siteMaterial : siteMaterials) {
            float stock = siteMaterial.getStock();
            float alertValue = siteMaterial.getAlert();

            // 確保 Lambda 使用的變數是 final
            Timestamp currentDate = new Timestamp(System.currentTimeMillis());

            // **新增檢查：初始庫存是否已低於警戒值**
            Timestamp alertDate;
            if (stock < alertValue) {
                logger.warn("工地 ID {} 的原物料 {} 初始庫存 ({}) 已低於警戒值 ({})",
                        siteId, siteMaterial.getMaterial().getMaterialName(), stock, alertValue);
                alertDate = currentDate;  // 直接設定警戒日為今日
            } else {
                // 計算每日消耗
                Map<Timestamp, Float> dailyConsumptionMap = calculateDailyConsumption(siteMaterial, currentDate);

                // 查詢未來的單次消耗與派發事件
                List<ConsumptionHistory> futureEvents = getFutureConsumptionAndDispatch(siteMaterial);

                // 模擬未來庫存變化，預測警戒日期
                alertDate = simulateStockChanges(stock, alertValue, dailyConsumptionMap, futureEvents);
            }

            // 將結果加入預測清單
            predictions.add(new AlertPredictionDTO(
                    siteMaterial.getMaterial().getId(),
                    siteMaterial.getMaterial().getMaterialName(),
                    stock,
                    alertValue,
                    calculateDailyConsumption(siteMaterial, currentDate),
                    alertDate
            ));
        }

        return predictions;
    }


    /**
     * 計算未來一段時間內的每日消耗量分佈。
     *
     * @param siteMaterial 原物料-工地關係
     * @param startDate 計算的開始日期
     * @return 每日消耗量的時間與數量對應表
     */
    public Map<Timestamp, Float> calculateDailyConsumption(SiteMaterial siteMaterial, Timestamp startDate) {
        List<ConsumptionHistory> dailyHistories = consumptionHistoryRepository.findBySiteMaterialAndConsumeType(siteMaterial, ConsumeType.DAILY);

        Map<Timestamp, Float> dailyConsumptionMap = new TreeMap<>();
        Timestamp currentDate = startDate;
        Timestamp maxDate = Timestamp.valueOf(startDate.toLocalDateTime().plusDays(365)); // 設置最大模擬範圍

        while (!currentDate.after(maxDate)) {
            // 確保 Lambda 使用的變數是 final
            Timestamp finalCurrentDate = currentDate;

            float dailyConsumption = 0.0F;

            for (ConsumptionHistory history : dailyHistories) {
                if (isActiveOnDate(history, finalCurrentDate)) {
                    dailyConsumption += history.getAmount();
                }
            }

            dailyConsumptionMap.put(currentDate, dailyConsumption);

            // 若未來沒有有效的每日消耗紀錄，提前結束
            Timestamp nextDate = Timestamp.valueOf(currentDate.toLocalDateTime().plusDays(1));
            boolean hasFutureActiveRecords = false;

            for (ConsumptionHistory history : dailyHistories) {
                if (isActiveOnDate(history, nextDate)) {
                    hasFutureActiveRecords = true;
                    break;
                }
            }

            if (dailyConsumption == 0.0F && !hasFutureActiveRecords) {
                break;
            }

            currentDate = nextDate; // 更新為下一天
        }

        return dailyConsumptionMap;
    }


    /**
     * 查詢未來的單次消耗與派發事件。
     *
     * @param siteMaterial 原物料-工地關係
     * @return 未來事件的列表，包括單次消耗與派發
     */
    public List<ConsumptionHistory> getFutureConsumptionAndDispatch(SiteMaterial siteMaterial) {
        Timestamp currentDate = new Timestamp(System.currentTimeMillis());

        List<ConsumptionHistory> futureEvents = consumptionHistoryRepository.findBySiteMaterialAndConsumeTypeInAndEffectiveDateAfter(
                siteMaterial,
                List.of(ConsumeType.ONCE, ConsumeType.DISPATCH),
                currentDate
        );

        // 排序優化：生效日期 + 類型（DISPATCH 優先）
        futureEvents.sort(Comparator
                .comparing(ConsumptionHistory::getEffectiveDate)
                .thenComparing(event -> event.getConsumeType() == ConsumeType.DISPATCH ? -1 : 1));

        return futureEvents;
    }

    /**
     * 模擬未來的庫存變化，預測警戒值觸發日期。
     *
     * @param stock 初始庫存
     * @param alertValue 警戒值
     * @param dailyConsumptionMap 每日消耗量分佈
     * @param futureEvents 未來的單次消耗與派發事件
     * @return 警戒值觸發日期
     */
    public Timestamp simulateStockChanges(float stock, float alertValue, Map<Timestamp, Float> dailyConsumptionMap, List<ConsumptionHistory> futureEvents) {
        Timestamp currentDate = new Timestamp(System.currentTimeMillis());
        int maxSimulationDays = 90;  // 最大模擬天數
        int simulatedDays = 0;

        while (stock >= alertValue && simulatedDays < maxSimulationDays) {
            // 每日消耗
            stock -= dailyConsumptionMap.getOrDefault(currentDate, 0.0F);

            // 當天的單次消耗與派發
            for (ConsumptionHistory event : futureEvents) {
                if (event.getEffectiveDate().equals(currentDate)) {
                    stock += event.getConsumeType() == ConsumeType.DISPATCH ? event.getAmount() : -event.getAmount();
                }
            }

            if (stock < alertValue) {
                return currentDate;
            }

            // 前進到下一天
            currentDate = Timestamp.valueOf(currentDate.toLocalDateTime().plusDays(1));
            simulatedDays++;
        }

        if (simulatedDays >= maxSimulationDays) {
            logger.warn("模擬已達最大天數 {} 天，停止計算", maxSimulationDays);
        }

        return null;  // 無法在期限內達到警戒
    }

    /**
     * 判斷指定日期是否在消耗記錄的有效範圍內。
     *
     * @param history 消耗記錄
     * @param currentDate 要檢查的日期
     * @return 如果記錄在指定日期內有效，返回 true；否則返回 false。
     */
    public boolean isActiveOnDate(ConsumptionHistory history, Timestamp currentDate) {
        return !history.getExpired()
                && !currentDate.before(history.getEffectiveDate())
                && (history.getExpirationDate() == null || !currentDate.after(history.getExpirationDate()));
    }

}



