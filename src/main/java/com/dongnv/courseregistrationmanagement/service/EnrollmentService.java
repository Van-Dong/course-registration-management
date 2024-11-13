package com.dongnv.courseregistrationmanagement.service;

import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dongnv.courseregistrationmanagement.dto.PageResponse;
import com.dongnv.courseregistrationmanagement.dto.mapper.CourseMapper;
import com.dongnv.courseregistrationmanagement.dto.request.EnrollCourseRequest;
import com.dongnv.courseregistrationmanagement.dto.request.UnenrollCourseRequest;
import com.dongnv.courseregistrationmanagement.dto.response.EnrollCourseResponse;
import com.dongnv.courseregistrationmanagement.dto.response.StudentInCourseResponse;
import com.dongnv.courseregistrationmanagement.dto.response.UserEnrollmentResponse;
import com.dongnv.courseregistrationmanagement.exception.AppException;
import com.dongnv.courseregistrationmanagement.exception.ErrorCode;
import com.dongnv.courseregistrationmanagement.model.Course;
import com.dongnv.courseregistrationmanagement.model.Enrollment;
import com.dongnv.courseregistrationmanagement.repository.CourseRepository;
import com.dongnv.courseregistrationmanagement.repository.EnrollmentRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EnrollmentService {
    EnrollmentRepository enrollmentRepository;
    CourseRepository courseRepository;
    ConcurrentHashMap<Long, Lock> locks = new ConcurrentHashMap<>();
    CourseMapper courseMapper;

    @Transactional
    public EnrollCourseResponse enrollCourse(EnrollCourseRequest request) {
        Course course = getCourseAndCheckCondition(request.getCourseId());
        Long id = getCurrentUserId();
        Enrollment enrollment =
                Enrollment.builder().userId(id).courseId(request.getCourseId()).build();

        // Lock block where have race conditional
        Lock lock = locks.computeIfAbsent(course.getId(), key -> new ReentrantLock());
        saveEnrollment(course, enrollment, lock);

        return EnrollCourseResponse.builder()
                .status(true)
                .courseEnrolled(course.getTitle())
                .build();
    }

    @Transactional
    public void unenrollCourse(UnenrollCourseRequest request) {
        Course course = getCourseAndCheckCondition(request.getCourseId());
        Long id = getCurrentUserId();

        if (LocalDate.now().isAfter(course.getStartDate())) throw new AppException(ErrorCode.COURSE_IS_STARTED);
        enrollmentRepository.deleteEnrollmentByUserIdAndCourseId(id, request.getCourseId());

        course.setCurrentEnrollments(course.getCurrentEnrollments() - 1);
        courseRepository.save(course);
    }

    public boolean isEnroll(Long courseId) {
        Long id = getCurrentUserId();
        return enrollmentRepository.existsByUserIdAndCourseId(id, courseId);
    }

    public StudentInCourseResponse getStudentInCourseById(Long courseId, int page, int size) {
        Course course =
                courseRepository.findById(courseId).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        Page<UserEnrollmentResponse> userPage =
                enrollmentRepository.getUsersByCourseId(courseId, PageRequest.of(page - 1, size));

        PageResponse<UserEnrollmentResponse> userPageResponse = PageResponse.<UserEnrollmentResponse>builder()
                .currentPage(page)
                .totalPages(userPage.getTotalPages())
                .pageSize(userPage.getSize())
                .totalElements(userPage.getTotalElements())
                .data(userPage.getContent())
                .build();

        return StudentInCourseResponse.builder()
                .course(courseMapper.toCourseResponse(course))
                .users(userPageResponse)
                .build();
    }

    private static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Long.parseLong(authentication.getName());
    }

    private Course getCourseAndCheckCondition(Long request) {
        Course course =
                courseRepository.findById(request).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        if (course.getStartDate().isBefore(LocalDate.now())) throw new AppException(ErrorCode.COURSE_IS_STARTED);
        return course;
    }

    private void saveEnrollment(Course course, Enrollment enrollment, Lock lock) {
        try {
            lock.lock();
            // Check course is full
            if (!courseRepository.currentEnrollmentsLessThanMaxEnrollmentsById(course.getId()))
                throw new AppException(ErrorCode.COURSE_IS_FULL);
            enrollmentRepository.save(enrollment);

            course.setCurrentEnrollments(course.getCurrentEnrollments() + 1);
            courseRepository.save(course);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.ENROLLMENT_EXISTED);
        } finally {
            lock.unlock();
        }
    }
}
