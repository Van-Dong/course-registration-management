package com.dongnv.courseregistrationmanagement.repository;

import com.dongnv.courseregistrationmanagement.constant.Role;
import com.dongnv.courseregistrationmanagement.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Page<User> findAllUserByRole(Role role, Pageable pageable);
}
