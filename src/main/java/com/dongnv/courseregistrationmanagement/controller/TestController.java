package com.dongnv.courseregistrationmanagement.controller;

import com.dongnv.courseregistrationmanagement.dto.projection.UserProjection;
import com.dongnv.courseregistrationmanagement.repository.CourseRepository;
import com.dongnv.courseregistrationmanagement.repository.EnrollmentRepository;
import com.dongnv.courseregistrationmanagement.service.ReportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
