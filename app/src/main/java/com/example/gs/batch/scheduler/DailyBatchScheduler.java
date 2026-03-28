package com.example.gs.batch.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.gs.batch.service.DailyBatchService;


@Component
public class DailyBatchScheduler {

    private final DailyBatchService batchService;

    public DailyBatchScheduler(DailyBatchService batchService) {
        this.batchService = batchService;
    }

    @Scheduled(cron = "0 50 23 * * *")
    public void runDailyBatch() {
        batchService.updateMonthlySummary();
    }
}
