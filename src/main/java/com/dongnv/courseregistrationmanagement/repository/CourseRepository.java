package com.dongnv.courseregistrationmanagement.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dongnv.courseregistrationmanagement.dto.response.CountEnrollmentInWeekResponse;
import com.dongnv.courseregistrationmanagement.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query(
            """
			SELECT new com.dongnv.courseregistrationmanagement.dto.response.CountEnrollmentInWeekResponse(c.id, c.title, c.currentEnrollments, c.maxEnrollments, count(e) AS countEnrollment)
			FROM Course c LEFT JOIN Enrollment e ON e.courseId = c.id AND e.enrollmentDate BETWEEN :startOfWeek AND :endOfWeek
			GROUP BY c.id
			ORDER BY count(e) DESC""")
    Page<CountEnrollmentInWeekResponse> countEnrollmentInWeek(
            @Param("startOfWeek") LocalDateTime startOfWeek,
            @Param("endOfWeek") LocalDateTime endOfWeek,
            Pageable pageable);

    @Query("SELECT c.currentEnrollments < c.maxEnrollments FROM Course c WHERE c.id = :id")
    boolean currentEnrollmentsLessThanMaxEnrollmentsById(@Param("id") Long id);

    @Query(
            "SELECT c FROM Course c JOIN Enrollment e ON c.id = e.courseId WHERE e.userId = :userId ORDER BY e.enrollmentDate DESC")
    Page<Course> findAllByUserId(@Param("userId") Long userId, Pageable pageable);
}
