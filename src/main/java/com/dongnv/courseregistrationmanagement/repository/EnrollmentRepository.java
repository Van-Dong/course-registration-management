package com.dongnv.courseregistrationmanagement.repository;

import com.dongnv.courseregistrationmanagement.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    @Query("SELECT COUNT(e) < c.maxEnrollments " +
            "FROM Course c LEFT JOIN Enrollment e ON c.id = e.courseId " +
            "WHERE c.id = :courseId")
    boolean isCourseNotFull(@Param("courseId") Long courseId);

    @Query("SELECT COUNT(*) FROM Enrollment e WHERE e.courseId = :courseId")
    Integer countEnrollmentOfCourse(@Param("courseId") Long courseId);
}
