package com.dongnv.courseregistrationmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CourseRegistrationManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseRegistrationManagementApplication.class, args);
    }
}
