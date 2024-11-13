package com.dongnv.courseregistrationmanagement.dto.mapper;

import org.mapstruct.Mapper;

import com.dongnv.courseregistrationmanagement.dto.request.UserCreationRequest;
import com.dongnv.courseregistrationmanagement.dto.response.UserResponse;
import com.dongnv.courseregistrationmanagement.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);
}
