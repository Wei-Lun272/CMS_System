package com.wellan.Construction_Management_System.controller;

import com.wellan.Construction_Management_System.dto.ConsumptionHistoryDTO;
import com.wellan.Construction_Management_System.dto.UpdateConsumptionDTO;
import com.wellan.Construction_Management_System.entity.ConsumptionHistory;
import com.wellan.Construction_Management_System.service.StockOperationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consumption-histories")
public class ConsumptionHistoryController {

    private static final Logger logger = LoggerFactory.getLogger(ConsumptionHistoryController.class);

    private final StockOperationService stockOperationService;

    @Autowired
    public ConsumptionHistoryController(StockOperationService stockOperationService) {
        this.stockOperationService = stockOperationService;
    }

    /**
     * 更新指定的消耗紀錄。
     *
     * @param id  消耗紀錄的 ID
     * @param dto 包含更新數據的 DTO
     * @return 更新後的消耗紀錄
     */
    @PutMapping("{id}")
    public ResponseEntity<ConsumptionHistory> updateConsumptionHistory(
            @PathVariable int id,
            @RequestBody UpdateConsumptionDTO dto
    ) {
        logger.info("收到更新消耗紀錄請求，ID: {}, 更新內容: {}", id, dto);

        ConsumptionHistory updatedHistory = stockOperationService.updatePendingConsumptionHistory(
                id,
                dto.getNewAmount(),
                dto.getNewEffectiveDate()
        );

        logger.info("成功更新消耗紀錄，ID: {}, 更新後內容: {}", id, updatedHistory);

        return ResponseEntity.ok(updatedHistory);
    }

    /**
     * 刪除指定的消耗紀錄。
     *
     * @param id 消耗紀錄的 ID
     * @return HTTP 204 無內容響應
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteConsumptionHistory(@PathVariable int id) {
        logger.info("收到刪除消耗紀錄請求，ID: {}", id);

        stockOperationService.deletePendingConsumptionHistory(id);

        logger.info("成功刪除消耗紀錄，ID: {}", id);

        return ResponseEntity.noContent().build();
    }
}
