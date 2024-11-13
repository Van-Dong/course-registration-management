package com.dongnv.courseregistrationmanagement.dto.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEnrollmentResponse {
    String fullName;
    String email;
    LocalDateTime enrollmentDate;

    public String getFormatEnrollmentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return enrollmentDate.format(formatter);
    }
}
