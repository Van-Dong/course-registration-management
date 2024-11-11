package com.dongnv.courseregistrationmanagement.batch;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BatchJobScheduler {
    JobLauncher customJobLauncher;
    Job exportReportJob;

    @Scheduled(cron = "0 0 12 ? * 6")
    public void runBatchJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            customJobLauncher.run(exportReportJob, jobParameters);
            log.info("Job Completed");
        } catch (JobExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
