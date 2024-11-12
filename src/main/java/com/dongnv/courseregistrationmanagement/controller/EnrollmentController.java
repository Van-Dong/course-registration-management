package com.dongnv.courseregistrationmanagement.controller;

import com.dongnv.courseregistrationmanagement.dto.ApiResponse;
import com.dongnv.courseregistrationmanagement.dto.request.EnrollCourseRequest;
import com.dongnv.courseregistrationmanagement.dto.request.UnenrollCourseRequest;
import com.dongnv.courseregistrationmanagement.dto.response.EnrollCourseResponse;
import com.dongnv.courseregistrationmanagement.dto.response.StudentInCourseResponse;
import com.dongnv.courseregistrationmanagement.model.Enrollment;
import com.dongnv.courseregistrationmanagement.service.EnrollmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/enrollments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EnrollmentController {
    EnrollmentService enrollmentService;

    @PostMapping("/create")
    @ResponseBody
    ApiResponse<EnrollCourseResponse> createEnrollment(@RequestBody EnrollCourseRequest request) {
        return ApiResponse.<EnrollCourseResponse>builder()
                .result(enrollmentService.enrollCourse(request))
                .build();
    }

    @DeleteMapping("/delete")
    @ResponseBody
    ApiResponse<Void> deleteEnrollment(@RequestBody UnenrollCourseRequest request) {
        enrollmentService.unenrollCourse(request);
        return ApiResponse.<Void>builder()
                .message("Unenrollment successfully!")
                .build();
    }

    @GetMapping("/{courseId}")
    String getCourseManager(@PathVariable Long courseId,
                            @RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size,
                            Model model) {
        page = page < 1 ? 1 : page;
        size = size < 1 ? 10 : size;

        StudentInCourseResponse studentInCourseResponse = enrollmentService.getStudentInCourseById(courseId, page, size);
        model.addAttribute("studentInCourseResponse", studentInCourseResponse);

        return "course/list-student-in-course";
    }
}
