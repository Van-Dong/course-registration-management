package com.dongnv.courseregistrationmanagement.model;

import java.util.List;

import jakarta.persistence.*;

import com.dongnv.courseregistrationmanagement.constant.Role;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String fullName;

    @Column(unique = true)
    String email;

    String password;

    @Enumerated(EnumType.STRING)
    Role role;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    List<Enrollment> enrollments;

    @PrePersist
    private void setDefaultValue() {
        if (role == null) {
            this.role = Role.STUDENT;
        }
    }
}
