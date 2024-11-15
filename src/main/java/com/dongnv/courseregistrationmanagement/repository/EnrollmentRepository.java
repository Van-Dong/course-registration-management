package com.dongnv.courseregistrationmanagement.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dongnv.courseregistrationmanagement.dto.response.UserEnrollmentResponse;
import com.dongnv.courseregistrationmanagement.model.Course;
import com.dongnv.courseregistrationmanagement.model.Enrollment;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    void deleteEnrollmentByUserIdAndCourseId(Long userId, Long courseId);

    // Lấy danh sách khóa học còn có thể đăng ký mà người dùng này (userId) chưa đăng ký
    @Query("SELECT c FROM Course c " + "LEFT JOIN Enrollment e ON e.courseId = c.id AND e.userId = :userId "
            + "WHERE c.startDate > CURRENT_TIMESTAMP "
            + "AND c.currentEnrollments < c.maxEnrollments "
            + "AND e.id IS NULL")
    List<Course> findAvailableCoursesForUser(@Param("userId") Long userId);


    // Check xem userId có đăng ký khóa học courseId
    boolean existsByUserIdAndCourseId(Long userId, Long courseId);

    // Lấy danh sách user đã đăng ký khóa học (bằng courseId)
    @Query(
            "SELECT new com.dongnv.courseregistrationmanagement.dto.response.UserEnrollmentResponse(u.fullName, u.email, e.enrollmentDate) "
                    + "FROM User u JOIN Enrollment e ON u.id = e.userId WHERE e.courseId = :courseId")
    Page<UserEnrollmentResponse> getUsersByCourseId(@Param("courseId") Long courseId, Pageable pageable);

    // Lấy bản ghi enrollment cũ nhất để sinh ra danh sách week ở page report
    @Query("SELECT MIN(e.enrollmentDate) FROM Enrollment e")
    Optional<LocalDateTime> getOldestEnrollmentDate();
}
