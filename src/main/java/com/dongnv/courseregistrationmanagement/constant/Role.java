package com.dongnv.courseregistrationmanagement.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Role {
    ADMIN("ADMIN"),
    STUDENT("STUDENT");

    Role(String name) {
        this.name = name;
    }
    String name;
}
