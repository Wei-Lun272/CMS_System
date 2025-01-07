package com.wellan.Construction_Management_System.controller;

import com.wellan.Construction_Management_System.dto.ConsumptionHistoryDTO;
import com.wellan.Construction_Management_System.entity.ConsumptionHistory;
import com.wellan.Construction_Management_System.repository.ConsumptionHistoryRepository;
import com.wellan.Construction_Management_System.service.StockOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consumption-histories")
public class ConsumptionHistoryController {
    private StockOperationService stockOperationService;
    @Autowired
    public ConsumptionHistoryController(StockOperationService stockOperationService) {
        this.stockOperationService = stockOperationService;
    }
    @PutMapping("{id}")
    public ResponseEntity<ConsumptionHistory> updateConsumptionHistory(
            @PathVariable int id,
            @RequestBody ConsumptionHistoryDTO dto
    ) {
        ConsumptionHistory updatedHistory = stockOperationService.updatePendingConsumptionHistory(
                id,
                dto.getAmount(),
                dto.getEffectiveDate()
        );
        return ResponseEntity.ok(updatedHistory);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteConsumptionHistory(@PathVariable int id) {
        stockOperationService.deletePendingConsumptionHistory(id);
        return ResponseEntity.noContent().build();
    }


}
