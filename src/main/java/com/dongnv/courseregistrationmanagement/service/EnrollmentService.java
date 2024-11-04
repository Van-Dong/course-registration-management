package com.dongnv.courseregistrationmanagement.service;

import com.dongnv.courseregistrationmanagement.dto.request.EnrollCourseRequest;
import com.dongnv.courseregistrationmanagement.dto.request.UnenrollCourseRequest;
import com.dongnv.courseregistrationmanagement.dto.response.EnrollCourseResponse;
import com.dongnv.courseregistrationmanagement.dto.response.UnenrollCourseResponse;
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

    public EnrollCourseResponse enrollCourse(EnrollCourseRequest request) {
        return null;
    }

    public UnenrollCourseResponse unenrollCourse(UnenrollCourseRequest request) {
        return null;
    }
}
