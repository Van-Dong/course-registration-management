package com.dongnv.courseregistrationmanagement.controller;

import com.dongnv.courseregistrationmanagement.dto.ApiResponse;
import com.dongnv.courseregistrationmanagement.dto.PageResponse;
import com.dongnv.courseregistrationmanagement.dto.request.CourseCreationRequest;
import com.dongnv.courseregistrationmanagement.dto.request.CourseUpdateRequest;
import com.dongnv.courseregistrationmanagement.dto.response.CourseResponse;
import com.dongnv.courseregistrationmanagement.service.CourseService;
import com.dongnv.courseregistrationmanagement.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CourseController {
    CourseService courseService;
    EnrollmentService enrollmentService;

    @PostMapping("/courses/create")
    @ResponseBody
    ApiResponse<CourseResponse> createCourse(@Valid @RequestBody CourseCreationRequest request) {
        return ApiResponse.<CourseResponse>builder()
                .result(courseService.createCourse(request))
                .build();
    }

    @GetMapping(value = {"/courses/all", "/"})
    String getCourses(@RequestParam(value = "page", defaultValue = "1") int page,
                      @RequestParam(value = "size", defaultValue = "12") int size,
                      Model model) {
        page = page < 1 ? 1 : page;
        size = size < 1 ? 12 : size;
        PageResponse<CourseResponse> courses = courseService.getCourses(page, size);
        model.addAttribute("courses", courses);
        return "course/list-course";
    }

    @GetMapping("/courses/detail/{id}")
    String getCourse(@PathVariable Long id, Model model) {
        CourseResponse course = courseService.getCourseById(id);
        boolean isEnroll = enrollmentService.isEnroll(id);

        model.addAttribute("course", course);
        model.addAttribute("isEnroll", isEnroll);
        return "course/detail-course";
    }

    @GetMapping("/courses/my-course")
    String getMyCourse(@RequestParam(value = "page", defaultValue = "1") int page,
                       @RequestParam(value = "size", defaultValue = "10") int size, Model model) {
        page = page < 1 ? 1 : page;
        size = size < 1 ? 10 : size;
        PageResponse<CourseResponse> courses = courseService.getMyCourses(page, size);
        model.addAttribute("courses", courses);
        return "course/my-course";
    }

    @GetMapping("/courses/manager")
    String getCourseManager(@RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size,
                            Model model) {
        page = page < 1 ? 1 : page;
        size = size < 1 ? 10 : size;
        PageResponse<CourseResponse> courses = courseService.getCourses(page, size);
        model.addAttribute("courses", courses);
        return "course/course-manager";
    }

    @PutMapping("/courses/update/{courseId}")
    @ResponseBody
    ApiResponse<CourseResponse> updateCourse(@PathVariable Long courseId, @RequestBody CourseUpdateRequest request) {
        return ApiResponse.<CourseResponse>builder()
                .result(courseService.updateCourse(request, courseId))
                .build();
    }

    @DeleteMapping("/courses/delete/{courseId}")
    @ResponseBody
    ApiResponse<Void> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return ApiResponse.<Void>builder()
                .message("Delete successfully!")
                .build();
    }

}
