package com.dongnv.courseregistrationmanagement.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BatchController {
    JobLauncher jobLauncher;
    Job job;

    @GetMapping("/runBatchJob")
    public String runBatchJob() {
        try {
            JobExecution jobExecution = jobLauncher.run(job, new JobParameters());
            return "Job completed successfully!";
        } catch (JobExecutionException e) {
            log.info("Exception batch: ", e);
            return "Job Failed!";
        }
    }
}
