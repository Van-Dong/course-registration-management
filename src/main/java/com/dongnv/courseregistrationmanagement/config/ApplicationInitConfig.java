package com.dongnv.courseregistrationmanagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dongnv.courseregistrationmanagement.constant.Role;
import com.dongnv.courseregistrationmanagement.model.User;
import com.dongnv.courseregistrationmanagement.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ApplicationInitConfig {
    @Value("${app.account-admin.password}")
    private String passwordAdmin;

    @Bean
    ApplicationRunner applicationRunner(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        return args -> {
            log.info("Application Started");

            if (!userRepository.existsByEmail("admin")) {
                User user = User.builder()
                        .email("admin")
                        .password(passwordEncoder.encode(passwordAdmin))
                        .role(Role.ADMIN)
                        .build();
                userRepository.save(user);
                log.info("Account admin default created. Please change default password");
            }
        };
    }
}
