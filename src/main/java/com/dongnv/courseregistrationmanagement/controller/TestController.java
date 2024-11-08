package com.dongnv.courseregistrationmanagement.controller;

import com.dongnv.courseregistrationmanagement.dto.projection.UserProjection;
import com.dongnv.courseregistrationmanagement.repository.CourseRepository;
import com.dongnv.courseregistrationmanagement.repository.EnrollmentRepository;
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
    CourseRepository courseRepository;
    EnrollmentRepository enrollmentRepository;
    JobLauncher customJobLauncher;
    Job exportReportJob;

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
        LocalDate today = LocalDate.now();
        LocalDateTime startOfWeek = today.with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime endOfWeek = today.with(DayOfWeek.SUNDAY).atTime(23, 59, 59);
        Page<UserProjection> result =
                enrollmentRepository.findUserNotEnrolledInCourse(7L, PageRequest.of(0, 50));
        result.getContent().forEach(user -> {
            log.info("User: {} + {}", user.getFullName(), user.getEmail());
        });

        return "Test Successfully";
    }
}
