package com.dongnv.courseregistrationmanagement.batch;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BatchJobScheduler {
    JobLauncher customJobLauncher;
    Job exportReportJob;
    Job sendSuggestedEmailJob;

    // 3:00 AM hàng tuần
    @Scheduled(cron = "0 0 3 ? * 1")
    public void runExportReportBatchJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            customJobLauncher.run(exportReportJob, jobParameters);
        } catch (JobExecutionException e) {
            log.info("JobExecutionException: ", e);
        }
    }

    // 3:00 AM hàng tuần
    @Scheduled(cron = "0 0 3 ? * 1")
    public void runSuggestedCourseBatchJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            customJobLauncher.run(sendSuggestedEmailJob, jobParameters);
        } catch (JobExecutionException e) {
            log.info("JobExecutionException: ", e);
        }
    }
}
