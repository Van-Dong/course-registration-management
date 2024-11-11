package com.dongnv.courseregistrationmanagement.service;

import com.dongnv.courseregistrationmanagement.dto.mapper.UserMapper;
import com.dongnv.courseregistrationmanagement.dto.request.UserCreationRequest;
import com.dongnv.courseregistrationmanagement.dto.response.UserResponse;
import com.dongnv.courseregistrationmanagement.exception.AppException;
import com.dongnv.courseregistrationmanagement.exception.ErrorCode;
import com.dongnv.courseregistrationmanagement.model.User;
import com.dongnv.courseregistrationmanagement.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Transactional(transactionManager = "primaryTransactionManager")
    public UserResponse createUser(UserCreationRequest request) {
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        return userMapper.toUserResponse(user);
    }


}
