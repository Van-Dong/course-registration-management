package com.dongnv.courseregistrationmanagement.dto.response;

import com.dongnv.courseregistrationmanagement.dto.PageResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentInCourseResponse {
    CourseResponse course;
    PageResponse<UserEnrollmentResponse> users;
}
