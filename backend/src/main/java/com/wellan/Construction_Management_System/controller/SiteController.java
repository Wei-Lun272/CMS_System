package com.wellan.Construction_Management_System.controller;

import com.wellan.Construction_Management_System.dto.*;
import com.wellan.Construction_Management_System.entity.Site;
import com.wellan.Construction_Management_System.entity.SiteMaterial;
import com.wellan.Construction_Management_System.service.SiteService;
import com.wellan.Construction_Management_System.service.StockOperationService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sites")
public class SiteController {

    private final SiteService siteService;
    private final StockOperationService stockOperationService;
    private static final Logger logger = LoggerFactory.getLogger(SiteController.class);
    @Autowired
    private Environment environment;

    @PostConstruct
    public void checkRedis() {
        System.out.println("🔍 Redis host: " + environment.getProperty("spring.redis.host"));
        System.out.println("🔍 Redis port: " + environment.getProperty("spring.redis.port"));
    }
    @Autowired
    public SiteController(SiteService siteService, StockOperationService stockOperationService) {
        this.siteService = siteService;
        this.stockOperationService = stockOperationService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Site> addSite(@Valid @RequestBody Site site) {
        logger.info("收到新增工地的請求: {}", site);
        Site addedSite = siteService.addSite(site);
        logger.info("工地新增成功，ID: {}", addedSite.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(addedSite);
    }

    // READ
    @GetMapping
    public ResponseEntity<List<Site>> findAllSite() {
        logger.info("收到查詢所有工地的請求");
        List<Site> allSite = siteService.getAllSite();
        logger.info("查詢到 {} 個工地", allSite.size());
        return ResponseEntity.ok(allSite);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Site> findSiteById(@PathVariable int id) {
        logger.info("收到查詢工地 ID {} 的請求", id);
        Site site = siteService.getSiteById(id);
        logger.info("成功查詢到工地: {}", site);
        return ResponseEntity.ok(site);
    }
    @GetMapping("/{id}/detail")
    public ResponseEntity<SiteDetailDTO> findSiteDetailById(@PathVariable int id) {
        logger.info("收到查詢工地 ID {} 的詳細資料請求", id);
        Site site = siteService.getSiteById(id);
        List<SiteMaterial> materialsFromSite = stockOperationService.getMaterialsFromSite(id);
        System.out.println(materialsFromSite);
        SiteDetailDTO siteDetailDTO = SiteDetailDTO.createFromSite(site,materialsFromSite);
        System.out.println(siteDetailDTO);
        logger.info("成功查詢工地 ID {} 的詳細資料: {}", id, siteDetailDTO);
        return ResponseEntity.ok().body(siteDetailDTO);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Site> updateSiteById(@PathVariable int id,
                                               @Valid @RequestBody Site updateSite) {
        logger.info("收到更新工地 ID {} 的請求，更新資料: {}", id, updateSite);
        Site site = siteService.updateSiteById(id, updateSite);
        logger.info("工地 ID {} 更新成功: {}", id, site);
        return ResponseEntity.ok(site);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSiteById(@PathVariable int id) {
        logger.info("收到刪除工地 ID {} 的請求", id);
        siteService.deleteSite(id);
        logger.info("工地 ID {} 刪除成功", id);
        return ResponseEntity.noContent().build();
    }

    // 工地派發原物料相關邏輯
    @PostMapping("/{id}/dispatch")
    public ResponseEntity<Integer> dispatchMaterialToSite(
            @PathVariable int id,
            @RequestBody List<SingleDispatchDTO> dispatchList) {
        logger.info("收到派發原物料到工地 ID {} 的請求，派發列表: {}", id, dispatchList);
        try {
            Integer siteId = stockOperationService.batchDispatchMaterials(id, dispatchList);
            logger.info("原物料成功派發到工地 ID {}，返回工地ID: {}", id, siteId);
            return ResponseEntity.ok(siteId);
        } catch (Exception e) {
            logger.error("派發原物料到工地 ID {} 失敗: {}", id, e.getMessage());
            throw e;
        }
    }
    @PostMapping("/{id}/consume")
    public ResponseEntity<Integer> consumeMaterialsFromSite(
            @PathVariable int id,
            @RequestBody List<SingleConsumeDTO> consumeList) {
        logger.info("收到工地 ID 為 {} 的批量消耗請求，消耗列表: {}", id, consumeList);
        logger.debug("接收到的批量消耗請求數據: {}", consumeList);

        try {
            Integer siteId = stockOperationService.batchConsumeMaterials(id, consumeList);
            logger.info("成功紀錄工地 ID {}的消耗，返回工地ID: {}", id, siteId);
            return ResponseEntity.ok(siteId);
        } catch (Exception e) {
            logger.error("批量消耗工地 ID {}原物料的請求失敗: {}", id, e.getMessage());
            throw e;
        }
    }
    @GetMapping("/{id}/materials")
    public ResponseEntity<List<SiteMaterialDetailDTO>> getMaterialById(@PathVariable Integer id){
        List<SiteMaterial> materialsFromSite = stockOperationService.getMaterialsFromSite(id);
        List<SiteMaterialDetailDTO> list = materialsFromSite.stream().map(SiteMaterialDetailDTO::createFromSiteMaterial).toList();
        return ResponseEntity.ok(list);

    }


    @GetMapping("/{id}/alert")
    public ResponseEntity<Boolean> getSiteAlertStatus(@PathVariable Integer id) {
        List<AlertPredictionDTO> predictions = stockOperationService.predictStockAlert(id);
        logger.info("收到工地 ID {} 的警戒資訊請求", id);
        // 判斷是否有任一原物料即將低於警戒值
        boolean hasAlert = predictions.stream().anyMatch(pred -> pred.getAlertDate() != null);
        logger.info("工地 ID {} 的警戒資訊: {}", id, predictions);

        return ResponseEntity.ok(hasAlert);
    }


    @GetMapping("/{id}/alert-predictions")
    public List<AlertPredictionDTO> getStockAlertPredictions(@PathVariable int id) {
        logger.info("收到工地 ID {} 的警戒資訊請求", id);

        List<AlertPredictionDTO> alertPredictionDTOS = stockOperationService.predictStockAlert(id);
        logger.info("工地 ID {} 的警戒資訊: {}", id, alertPredictionDTOS);

        return alertPredictionDTOS;

    }

}
