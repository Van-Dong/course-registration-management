package com.dongnv.courseregistrationmanagement.repository;

import com.dongnv.courseregistrationmanagement.dto.response.CountEnrollmentInWeekResponse;
import com.dongnv.courseregistrationmanagement.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT c FROM Course c WHERE c.startDate > CURRENT_DATE AND c.currentEnrollments < c.maxEnrollments")
    Page<Course> findUpcomingAndNotFullCourses(Pageable pageable);

    @Query("""
            SELECT new com.dongnv.courseregistrationmanagement.dto.response.CountEnrollmentInWeekResponse(c.id, c.title, c.currentEnrollments, c.maxEnrollments, count(e))
            FROM Course c LEFT JOIN Enrollment e ON e.courseId = c.id AND e.enrollmentDate BETWEEN :startOfWeek AND :endOfWeek
            GROUP BY c.id""")
    Page<CountEnrollmentInWeekResponse> countEnrollmentInWeek(@Param("startOfWeek") LocalDateTime startOfWeek,
                                                              @Param("endOfWeek") LocalDateTime endOfWeek,
                                                              Pageable pageable);

    @Query("SELECT c.currentEnrollments < c.maxEnrollments FROM Course c WHERE c.id = :id")
    boolean currentEnrollmentsLessThanMaxEnrollmentsById(@Param("id") Long id);
}
