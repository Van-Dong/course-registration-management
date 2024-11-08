package com.dongnv.courseregistrationmanagement.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CountEnrollmentInWeekResponse {
    Long id;
    String title;
    Integer currentEnrollments;
    Integer maxEnrollments;
    Long countEnrollmentInWeek;
}
