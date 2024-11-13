package com.dongnv.courseregistrationmanagement.dto.response;

import com.dongnv.courseregistrationmanagement.constant.Role;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Long id;
    String fullName;
    String email;
    Role role;
}
