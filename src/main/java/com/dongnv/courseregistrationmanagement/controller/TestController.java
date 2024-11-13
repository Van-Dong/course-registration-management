package com.dongnv.courseregistrationmanagement.controller;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dongnv.courseregistrationmanagement.service.ReportService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TestController {
    JobLauncher customJobLauncher;
    ReportService reportService;

    Job sendSuggestedEmailJob;

    @GetMapping("/runBatchJob")
    public String runBatchJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            JobExecution jobExecution = customJobLauncher.run(sendSuggestedEmailJob, jobParameters);
            return "Job completed successfully!";
        } catch (JobExecutionException e) {
            log.info("Exception batch: ", e);
            return "Job Failed!";
        }
    }

    @GetMapping("/test")
    public String test() {
        log.info("TEST TEST TEST: {}", reportService.getWeeks());

        return "Test Successfully";
    }
}
