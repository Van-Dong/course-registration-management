package com.dongnv.courseregistrationmanagement.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseCreationRequest {
    @NotBlank(message = "INVALID_TITLE")
    String title;

    @NotBlank(message = "INVALID_DESCRIPTION")
    String description;

    @NotNull(message = "INVALID_MAX_ENROLLMENTS")
    @Min(value = 1, message = "INVALID_MAX_ENROLLMENTS")
    @Max(value = 100, message = "INVALID_MAX_ENROLLMENTS")
    Integer maxEnrollments;

    @NotNull(message = "START_DATE_BLANK")
    LocalDate startDate;

    @NotBlank(message = "INVALID_TEACHER")
    String teacher;

    @NotNull(message = "INVALID_DURATION")
    @DecimalMin(value = "0.5", message = "INVALID_DURATION")
    @DecimalMax(value = "100.0", message = "INVALID_DURATION")
    Float duration;
}
