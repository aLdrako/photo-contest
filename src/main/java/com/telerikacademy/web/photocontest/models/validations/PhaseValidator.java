package com.telerikacademy.web.photocontest.models.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhaseConstraint.class)
public @interface PhaseValidator {
    String message() default "Phase 1 and 2 should be in the future";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String hour() default "hour";
    String day() default "day";
    String month() default "month";
    String phase() default "";
}
