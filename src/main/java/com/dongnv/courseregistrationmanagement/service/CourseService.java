package com.dongnv.courseregistrationmanagement.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dongnv.courseregistrationmanagement.dto.PageResponse;
import com.dongnv.courseregistrationmanagement.dto.mapper.CourseMapper;
import com.dongnv.courseregistrationmanagement.dto.request.CourseCreationRequest;
import com.dongnv.courseregistrationmanagement.dto.request.CourseUpdateRequest;
import com.dongnv.courseregistrationmanagement.dto.response.CourseResponse;
import com.dongnv.courseregistrationmanagement.exception.AppException;
import com.dongnv.courseregistrationmanagement.exception.ErrorCode;
import com.dongnv.courseregistrationmanagement.model.Course;
import com.dongnv.courseregistrationmanagement.repository.CourseRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CourseService {
    CourseRepository courseRepository;
    CourseMapper courseMapper;

    public PageResponse<CourseResponse> getCourses(int page, int size) {
        Page<Course> courses = courseRepository.findAll(PageRequest.of(page - 1, size));
        return PageResponse.<CourseResponse>builder()
                .currentPage(page)
                .totalPages(courses.getTotalPages())
                .pageSize(courses.getSize())
                .totalElements(courses.getTotalElements())
                .data(courses.getContent().stream()
                        .map(courseMapper::toCourseResponse)
                        .toList())
                .build();
    }

    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        return courseMapper.toCourseResponse(course);
    }

    public PageResponse<CourseResponse> getMyCourses(int page, int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        Page<Course> coursePage = courseRepository.findAllByUserId(userId, PageRequest.of(page - 1, size));
        return PageResponse.<CourseResponse>builder()
                .currentPage(page)
                .totalPages(coursePage.getTotalPages())
                .pageSize(coursePage.getSize())
                .totalElements(coursePage.getTotalElements())
                .data(coursePage.getContent().stream()
                        .map(courseMapper::toCourseResponse)
                        .toList())
                .build();
    }

    public CourseResponse createCourse(CourseCreationRequest request) {
        Course course = courseMapper.toCourse(request);
        course = courseRepository.save(course);
        return courseMapper.toCourseResponse(course);
    }

    public CourseResponse updateCourse(CourseUpdateRequest request, Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        courseMapper.updateCourse(course, request);
        course = courseRepository.save(course);
        return courseMapper.toCourseResponse(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
}
