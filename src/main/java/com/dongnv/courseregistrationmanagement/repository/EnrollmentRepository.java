package com.dongnv.courseregistrationmanagement.repository;

import com.dongnv.courseregistrationmanagement.dto.response.UserEnrollmentResponse;
import com.dongnv.courseregistrationmanagement.model.Course;
import com.dongnv.courseregistrationmanagement.model.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    void deleteEnrollmentByUserIdAndCourseId(Long userId, Long courseId);


    @Query("SELECT c FROM Course c " +
            "LEFT JOIN Enrollment e ON e.courseId = c.id AND e.userId = :userId " +
            "WHERE c.startDate > CURRENT_TIMESTAMP " +
            "AND c.currentEnrollments < c.maxEnrollments " +
            "AND e.id IS NULL")
    List<Course> findAvailableCoursesForUser(@Param("userId") Long userId);

    boolean existsByUserIdAndCourseId(Long userId, Long courseId);

    @Query("SELECT new com.dongnv.courseregistrationmanagement.dto.response.UserEnrollmentResponse(u.fullName, u.email, e.enrollmentDate) " +
            "FROM User u JOIN Enrollment e ON u.id = e.userId WHERE e.courseId = :courseId")
    Page<UserEnrollmentResponse> getUsersByCourseId(@Param("courseId") Long courseId, Pageable pageable);

    @Query("SELECT MIN(e.enrollmentDate) FROM Enrollment e")
    Optional<LocalDateTime> getOldestEnrollmentDate();
}
