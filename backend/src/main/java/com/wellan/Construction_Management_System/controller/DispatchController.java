package com.wellan.Construction_Management_System.controller;

import com.wellan.Construction_Management_System.service.StockOperationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dispatch")
public class DispatchController {
    private final StockOperationService stockOperationService;
    private static final Logger logger = LoggerFactory.getLogger(DispatchController.class);

    @Autowired
    public DispatchController(StockOperationService stockOperationService) {
        this.stockOperationService = stockOperationService;
    }
    //派發並且創建對應的ConsumptionHistory
//    @PostMapping
//    public ResponseEntity handleDispatch(@RequestBody DispatchDTO dispatchDTO){
//        try {
//            logger.info("接收到派發請求：{}", dispatchDTO);
//            SiteMaterial dispatch = stockOperationService.batchDispatchMaterials(dispatchDTO);
//            ConsumptionHistory consumptionHistory = stockOperationService.createConsumptionHistory(dispatch, dispatchDTO);
//            return ResponseEntity.ok().body("");
//        }catch (Exception e){
//            logger.error("派發請求處理失敗: {}", e.getMessage());
//            throw e;
//        }
//
//    }
}
