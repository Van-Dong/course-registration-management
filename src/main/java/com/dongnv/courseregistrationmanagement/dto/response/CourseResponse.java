package com.dongnv.courseregistrationmanagement.dto.response;

import java.time.LocalDate;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseResponse {
    Long id;
    String title;
    String description;
    Integer maxEnrollments;
    Integer currentEnrollments;
    LocalDate startDate;
    String teacher;
    Float duration;
}
