package com.example.book_api.global.annotation;

import com.example.book_api.domain.log.enums.ActivityType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Logging {
    ActivityType activityType();
}
