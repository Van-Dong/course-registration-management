package com.dongnv.courseregistrationmanagement.controller;

import com.dongnv.courseregistrationmanagement.dto.ApiResponse;
import com.dongnv.courseregistrationmanagement.dto.request.CourseCreationRequest;
import com.dongnv.courseregistrationmanagement.dto.request.CourseUpdateRequest;
import com.dongnv.courseregistrationmanagement.dto.response.CourseResponse;
import com.dongnv.courseregistrationmanagement.service.CourseService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CourseController {
    CourseService courseService;

    @PostMapping("/create")
    ApiResponse<CourseResponse> createCourse(@Valid @RequestBody CourseCreationRequest request) {
        return ApiResponse.<CourseResponse>builder()
                .result(courseService.createCourse(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<CourseResponse>> getCourses() {
        return ApiResponse.<List<CourseResponse>>builder()
                .result(courseService.getCourses())
                .build();
    }

    @PutMapping("/update/{courseId}")
    ApiResponse<CourseResponse> updateCourse(@PathVariable Long courseId, @RequestBody CourseUpdateRequest request) {
        return ApiResponse.<CourseResponse>builder()
                .result(courseService.updateCourse(request, courseId))
                .build();
    }

    @DeleteMapping("/delete/{courseId}")
    ApiResponse<Void> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return ApiResponse.<Void>builder()
                .message("Delete successfully!")
                .build();
    }

}
