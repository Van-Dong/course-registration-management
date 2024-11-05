package com.dongnv.courseregistrationmanagement.controller;

import com.dongnv.courseregistrationmanagement.dto.ApiResponse;
import com.dongnv.courseregistrationmanagement.dto.request.EnrollCourseRequest;
import com.dongnv.courseregistrationmanagement.dto.request.UnenrollCourseRequest;
import com.dongnv.courseregistrationmanagement.dto.response.EnrollCourseResponse;
import com.dongnv.courseregistrationmanagement.model.Enrollment;
import com.dongnv.courseregistrationmanagement.service.EnrollmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EnrollmentController {
    EnrollmentService enrollmentService;

    @PostMapping("/create")
    ApiResponse<EnrollCourseResponse> createEnrollment(@RequestBody EnrollCourseRequest request) {
        return ApiResponse.<EnrollCourseResponse>builder()
                .result(enrollmentService.enrollCourse(request))
                .build();
    }

    @DeleteMapping("/delete")
    ApiResponse<Void> deleteEnrollment(@RequestBody UnenrollCourseRequest request) {
        enrollmentService.unenrollCourse(request);
        return ApiResponse.<Void>builder()
                .message("Unenrollment successfully!")
                .build();
    }
}
