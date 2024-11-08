package com.dongnv.courseregistrationmanagement.dto.response;

import com.dongnv.courseregistrationmanagement.model.Course;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

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
