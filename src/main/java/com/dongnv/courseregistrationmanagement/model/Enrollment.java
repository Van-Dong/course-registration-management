package com.dongnv.courseregistrationmanagement.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "enrollment", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "course_id"}),
        indexes = {@Index(columnList = "user_id"), @Index(columnList = "course_id")})
public class Enrollment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "user_id", nullable = false)
    Long userId;

    @Column(name = "course_id", nullable = false)
    Long courseId;
    LocalDateTime enrollmentDate;
}
