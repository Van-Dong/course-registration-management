package com.dongnv.courseregistrationmanagement.controller;

import com.dongnv.courseregistrationmanagement.dto.ApiResponse;
import com.dongnv.courseregistrationmanagement.dto.request.EnrollCourseRequest;
import com.dongnv.courseregistrationmanagement.model.Enrollment;
import com.dongnv.courseregistrationmanagement.service.EnrollmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EnrollmentController {
    EnrollmentService enrollmentService;

    @PostMapping("/create")
    ApiResponse<Void> createEnrollment(@RequestBody EnrollCourseRequest request) {
        enrollmentService.test(request);
        return ApiResponse.<Void>builder()
                .message("Success")
                .build();
    }
}
