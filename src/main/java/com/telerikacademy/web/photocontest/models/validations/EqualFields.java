package com.telerikacademy.web.photocontest.models.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EqualFieldsValidator.class)
public @interface EqualFields {
    String message() default "Fields are not equal";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String baseField();
    String matchField();
}

