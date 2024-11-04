package com.dongnv.courseregistrationmanagement.service;

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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CourseService {
    CourseRepository courseRepository;
    CourseMapper courseMapper;

    public List<CourseResponse> getCourses() {
        return courseRepository.findAll().stream()
                .map(courseMapper::toCourseResponse)
                .toList();
    }

    public CourseResponse createCourse(CourseCreationRequest request) {
        log.info("CourseRequest: ", request);
        Course course = courseMapper.toCourse(request);
        log.info("Course: ", course);
        course = courseRepository.save(course);
        return courseMapper.toCourseResponse(course);
    }

    public CourseResponse updateCourse(CourseUpdateRequest request, Long id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.COURSE_NOT_FOUND)
        );

        log.info("Before update: ", course);
        log.info("Requets update: ", request);
        courseMapper.updateCourse(course, request);
        course = courseRepository.save(course);

        log.info("After update: ", course);
        return courseMapper.toCourseResponse(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }


}
