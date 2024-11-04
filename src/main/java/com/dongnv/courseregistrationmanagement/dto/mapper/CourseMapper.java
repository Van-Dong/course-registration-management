package com.dongnv.courseregistrationmanagement.dto.mapper;

import com.dongnv.courseregistrationmanagement.dto.request.CourseCreationRequest;
import com.dongnv.courseregistrationmanagement.dto.request.CourseUpdateRequest;
import com.dongnv.courseregistrationmanagement.dto.response.CourseResponse;
import com.dongnv.courseregistrationmanagement.model.Course;
import com.dongnv.courseregistrationmanagement.service.CourseService;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    Course toCourse(CourseCreationRequest request);
    CourseResponse toCourseResponse(Course course);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCourse(@MappingTarget Course course, CourseUpdateRequest request);
}
