package com.dongnv.courseregistrationmanagement.service;

import com.dongnv.courseregistrationmanagement.dto.request.EnrollCourseRequest;
import com.dongnv.courseregistrationmanagement.dto.request.UnenrollCourseRequest;
import com.dongnv.courseregistrationmanagement.dto.response.EnrollCourseResponse;
import com.dongnv.courseregistrationmanagement.exception.AppException;
import com.dongnv.courseregistrationmanagement.exception.ErrorCode;
import com.dongnv.courseregistrationmanagement.model.Course;
import com.dongnv.courseregistrationmanagement.model.Enrollment;
import com.dongnv.courseregistrationmanagement.repository.CourseRepository;
import com.dongnv.courseregistrationmanagement.repository.EnrollmentRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EnrollmentService {
    EnrollmentRepository enrollmentRepository;
    CourseRepository courseRepository;
    ConcurrentHashMap<Long, Lock> locks = new ConcurrentHashMap<>();

    @Transactional
    public EnrollCourseResponse enrollCourse(EnrollCourseRequest request) {
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new AppException(ErrorCode.COURSE_NOT_FOUND)
        );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = Long.parseLong(authentication.getName());

        Enrollment enrollment = Enrollment.builder()
                .userId(id)
                .courseId(request.getCourseId())
                .build();
        Lock lock = locks.computeIfAbsent(course.getId(), key -> new ReentrantLock());

        lock.lock();
        saveEnrollment(course, enrollment);
        lock.unlock();

        return EnrollCourseResponse.builder()
                .status(true)
                .courseEnrolled(course.getTitle())
                .build();
    }

    @Transactional
    public void unenrollCourse(UnenrollCourseRequest request) {
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new AppException(ErrorCode.COURSE_NOT_FOUND)
        );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = Long.parseLong(authentication.getName());
        enrollmentRepository.deleteEnrollmentByUserIdAndCourseId(id, request.getCourseId());
    }

    private void saveEnrollment(Course course, Enrollment enrollment) {
        try {
            // Check course is full
            int currentEnrollmentCount = enrollmentRepository.countEnrollmentOfCourse(course.getId());
            if (currentEnrollmentCount >= course.getMaxEnrollments()) {
                throw new AppException(ErrorCode.COURSE_IS_FULL);
            }
            enrollmentRepository.save(enrollment);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.ENROLLMENT_EXSITED);
        }
    }
}
