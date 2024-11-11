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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/courses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CourseController {
    CourseService courseService;

    @PostMapping("/create")
    @ResponseBody
    ApiResponse<CourseResponse> createCourse(@Valid @RequestBody CourseCreationRequest request) {
        return ApiResponse.<CourseResponse>builder()
                .result(courseService.createCourse(request))
                .build();
    }

    @GetMapping("/all")
    String getCourses(Model model) {
        List<CourseResponse> courses = courseService.getCourses();
        model.addAttribute("courses", courses);
        return "course/list-course";
    }

    @GetMapping("/detail/{id}")
    String getCourse(@PathVariable Long id, Model model) {
        CourseResponse course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        return "course/detail-course";
    }

    @GetMapping("/my-course")
    String getMyCourse(Model model) {
        List<CourseResponse> courses = courseService.getCoursesByUserId();
        model.addAttribute("courses", courses);
        return "course/my-course";
    }

    @GetMapping("/manager")
    String getCourseManager(Model model) {
        List<CourseResponse> courses = courseService.getCourses();
        model.addAttribute("courses", courses);
        return "course/course-manager";
    }

    @PutMapping("/update/{courseId}")
    @ResponseBody
    ApiResponse<CourseResponse> updateCourse(@PathVariable Long courseId, @RequestBody CourseUpdateRequest request) {
        return ApiResponse.<CourseResponse>builder()
                .result(courseService.updateCourse(request, courseId))
                .build();
    }

    @DeleteMapping("/delete/{courseId}")
    @ResponseBody
    ApiResponse<Void> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return ApiResponse.<Void>builder()
                .message("Delete successfully!")
                .build();
    }

}
