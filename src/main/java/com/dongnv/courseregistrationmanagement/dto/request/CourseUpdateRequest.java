package com.dongnv.courseregistrationmanagement.dto.request;

import java.time.LocalDate;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
