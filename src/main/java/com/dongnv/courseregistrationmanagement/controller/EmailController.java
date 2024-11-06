package com.dongnv.courseregistrationmanagement.controller;

import com.dongnv.courseregistrationmanagement.dto.ApiResponse;
import com.dongnv.courseregistrationmanagement.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EmailController {
    EmailService emailService;

    @GetMapping("/email/send")
    ApiResponse<Void> sendEmail() {
        emailService.sendEmail("nguyendong12a15@gmail.com", "Test send email speed", "Hello Dong");
        return ApiResponse.<Void>builder()
                .message("success")
                .build();
    }
}
