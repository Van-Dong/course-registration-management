package com.dongnv.courseregistrationmanagement.dto.response;

import java.util.List;

import com.dongnv.courseregistrationmanagement.model.Course;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailContentDto {
    String fullName;
    String email;
    List<Course> courses;
}
