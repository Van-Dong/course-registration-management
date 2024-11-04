package com.dongnv.courseregistrationmanagement.service;

import com.dongnv.courseregistrationmanagement.dto.request.EnrollCourseRequest;
import com.dongnv.courseregistrationmanagement.dto.request.UnenrollCourseRequest;
import com.dongnv.courseregistrationmanagement.dto.response.EnrollCourseResponse;
import com.dongnv.courseregistrationmanagement.dto.response.UnenrollCourseResponse;
import com.dongnv.courseregistrationmanagement.exception.AppException;
import com.dongnv.courseregistrationmanagement.exception.ErrorCode;
import com.dongnv.courseregistrationmanagement.model.Course;
import com.dongnv.courseregistrationmanagement.repository.CourseRepository;
import com.dongnv.courseregistrationmanagement.repository.EnrollmentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EnrollmentService {
    EnrollmentRepository enrollmentRepository;
    CourseRepository courseRepository;

    public EnrollCourseResponse enrollCourse(EnrollCourseRequest request) {
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new AppException(ErrorCode.COURSE_NOT_FOUND)
        );
        return null;
    }

    public UnenrollCourseResponse unenrollCourse(UnenrollCourseRequest request) {
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new AppException(ErrorCode.COURSE_NOT_FOUND)
        );
        return null;
    }
}
