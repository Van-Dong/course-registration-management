package com.dongnv.courseregistrationmanagement.batch;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BatchJobScheduler {
    JobLauncher jobLauncher;
    Job exportJob;

    @Scheduled(cron = "0 0 12 ? * 6")
    public void runBatchJob() {
        try {
            jobLauncher.run(exportJob, new JobParameters());
            log.info("Job Completed");
        } catch (JobExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
