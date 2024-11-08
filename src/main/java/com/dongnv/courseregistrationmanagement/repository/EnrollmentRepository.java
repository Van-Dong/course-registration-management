package com.dongnv.courseregistrationmanagement.repository;

import com.dongnv.courseregistrationmanagement.dto.projection.UserProjection;
import com.dongnv.courseregistrationmanagement.dto.response.CountEnrollmentInWeekResponse;
import com.dongnv.courseregistrationmanagement.model.Course;
import com.dongnv.courseregistrationmanagement.model.Enrollment;
import com.dongnv.courseregistrationmanagement.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

//    @Query("SELECT COUNT(e) < c.maxEnrollments " +
//            "FROM Course c LEFT JOIN Enrollment e ON c.id = e.courseId " +
//            "WHERE c.id = :courseId")
//    boolean isCourseNotFull(@Param("courseId") Long courseId);
//
//    @Query("SELECT COUNT(*) FROM Enrollment e WHERE e.courseId = :courseId")
//    Integer countEnrollmentOfCourse(@Param("courseId") Long courseId);

    void deleteEnrollmentByUserIdAndCourseId(Long userId, Long courseId);

    @Query("""
            SELECT u.fullName as fullName, u.email as email FROM User u
            LEFT JOIN Enrollment e ON u.id = e.userId AND e.courseId = :courseId
            WHERE e.id IS NULL AND u.role != 'ADMIN'
            """)
    Page<UserProjection> findUserNotEnrolledInCourse(@Param("courseId") Long courseId, Pageable pageable);

    @Query("SELECT c FROM Course c " +
            "LEFT JOIN Enrollment e ON e.courseId = c.id AND e.userId = :userId " +
            "WHERE c.startDate > CURRENT_TIMESTAMP " +
            "AND c.currentEnrollments < c.maxEnrollments " +
            "AND e.id IS NULL")
    List<Course> findAvailableCoursesForUser(@Param("userId") Long userId);
}
