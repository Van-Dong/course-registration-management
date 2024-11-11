package com.dongnv.courseregistrationmanagement.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    INVALID_FULLNAME(1002, "Invalid full name", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(1003, "Invalid email", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least 6 character", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1005, "Email already exist", HttpStatus.BAD_REQUEST),
    INVALID_TITLE(1006, "Title cannot be blank", HttpStatus.BAD_REQUEST),
    INVALID_DESCRIPTION(1007, "Description cannot be blank", HttpStatus.BAD_REQUEST),
    INVALID_MAX_ENROLLMENTS(1008, "Max enrollments must be in [1, 100]", HttpStatus.BAD_REQUEST),
    START_DATE_BLANK(1009, "Start cannot left blank", HttpStatus.BAD_REQUEST),
    COURSE_NOT_FOUND(1010, "Course not found", HttpStatus.BAD_REQUEST),
    COURSE_ID_NULL(1011, "Course ID must not be null", HttpStatus.BAD_REQUEST),
    AUTHENTICATION_FAILED(1012, "Email or password is not correct", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1013, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1014, "Unauthorized", HttpStatus.FORBIDDEN),
    ENROLLMENT_EXISTED(1015, "Enrollment already exist", HttpStatus.BAD_REQUEST),
    PARSE_ERROR(1016, "Type cannot be converted, please enter correct value", HttpStatus.BAD_REQUEST),
    COURSE_IS_FULL(1017, "The course is full", HttpStatus.BAD_REQUEST),
    COURSE_IS_STARTED(1018, "Course have already started", HttpStatus.BAD_REQUEST),
    INVALID_TEACHER(1019, "Teacher cannot be blank", HttpStatus.BAD_REQUEST),
    INVALID_DURATION(1020, "DURATION must be in [0.5, 100]", HttpStatus.BAD_REQUEST),
    EMAIL_REQUIRED(1021, "Email is required", HttpStatus.BAD_REQUEST),
    PASSWORD_REQUIRED(1022, "Password is required", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.statusCode = status;
    }
    int code;
    String message;
    HttpStatusCode statusCode;
}
