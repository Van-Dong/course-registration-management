package com.dongnv.courseregistrationmanagement.dto.request;

import jakarta.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnrollCourseRequest {
    @NotNull(message = "COURSE_ID_NULL")
    Long courseId;
}
