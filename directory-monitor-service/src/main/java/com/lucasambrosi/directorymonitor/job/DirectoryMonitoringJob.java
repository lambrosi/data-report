package com.lucasambrosi.directorymonitor.job;

import com.lucasambrosi.directorymonitor.service.DirectoryMonitorService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DirectoryMonitoringJob {

    private DirectoryMonitorService directoryMonitorService;

    public DirectoryMonitoringJob(DirectoryMonitorService directoryMonitorService) {
        this.directoryMonitorService = directoryMonitorService;
    }

    @Scheduled(cron = "${application.monitoring.cron-job}")
    private void verifyDirectory() {
        directoryMonitorService.verifyInputDirectory();
    }
}
