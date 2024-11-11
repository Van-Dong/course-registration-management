package com.dongnv.courseregistrationmanagement.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Course {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String title;
    String description;
    Integer maxEnrollments;
    Integer currentEnrollments;
    LocalDate startDate;
    String teacher;
    Float duration; // Hours

    @OneToMany
    @JoinColumn(name = "course_id")
    List<Enrollment> enrollments;

    @PrePersist
    void setDefaultValue() {
        if (this.getCurrentEnrollments() == null) {
            this.setCurrentEnrollments(0);
        }
        if (this.getDescription() == null) {
            this.setDescription("");
        }
    }
}
