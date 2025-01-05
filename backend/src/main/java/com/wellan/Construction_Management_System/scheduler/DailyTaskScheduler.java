package com.wellan.Construction_Management_System.scheduler;

import com.wellan.Construction_Management_System.service.StockOperationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyTaskScheduler {

    private static final Logger logger = LoggerFactory.getLogger(DailyTaskScheduler.class);

    private final StockOperationService stockOperationService;

    public DailyTaskScheduler(StockOperationService stockOperationService) {
        this.stockOperationService = stockOperationService;
    }

    /**
     * 每日例行任務，於每天早上 9 點執行
     */
    @Scheduled(cron = "0 0 9 * * ?")
    public void executeDailyTasks() {
//        logger.info("每日例行任務開始執行...");
//
//        try {
//            // 調用服務層方法，執行每日更新邏輯
//            stockOperationService.performDailyUpdates();
//            logger.info("每日例行任務執行完畢！");
//        } catch (Exception e) {
//            logger.error("每日例行任務執行時發生錯誤: {}", e.getMessage(), e);
//        }
    }
}
