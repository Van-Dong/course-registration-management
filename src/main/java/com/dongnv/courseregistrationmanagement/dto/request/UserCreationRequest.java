package com.dongnv.courseregistrationmanagement.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @NotBlank(message = "INVALID_FULLNAME")
    String fullName;

    @Email(message = "INVALID_EMAIL")
    @NotBlank(message = "EMAIL_REQUIRED")
    String email;

    @Size(min = 6, message = "INVALID_PASSWORD")
    @NotBlank(message = "PASSWORD_REQUIRED")
    String password;
}
