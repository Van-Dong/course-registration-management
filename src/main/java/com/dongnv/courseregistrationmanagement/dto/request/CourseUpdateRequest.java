package com.dongnv.courseregistrationmanagement.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseUpdateRequest {
    String title;
    String description;
    Integer maxEnrollments;
    LocalDate startDate;
    String teacher;
    Float duration;
}
