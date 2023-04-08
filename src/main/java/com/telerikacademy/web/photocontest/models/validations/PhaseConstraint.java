package com.telerikacademy.web.photocontest.models.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

// TODO not working, to implement
public class PhaseConstraint implements ConstraintValidator<PhaseValidator, LocalDateTime> {
    private LocalDateTime hour;
    private LocalDateTime day;
    private LocalDateTime month;
    private String phase;
    @Override
    public void initialize(PhaseValidator constraintAnnotation) {
        phase = constraintAnnotation.phase();
        String hourString = constraintAnnotation.hour();
        String dayString = constraintAnnotation.day();
        String monthString = constraintAnnotation.month();
        hour = hourString.equals("hour") ? LocalDateTime.now().plusHours(1) : LocalDateTime.parse(constraintAnnotation.hour());
        day = dayString.equals("day") ? LocalDateTime.now().plusDays(1) : LocalDateTime.parse(constraintAnnotation.day());
        month = monthString.equals("month") ? LocalDateTime.now().plusMonths(1) : LocalDateTime.parse(constraintAnnotation.month());
    }

    @Override
    public boolean isValid(LocalDateTime localDateTime, ConstraintValidatorContext context) {
        if (localDateTime == null) return true;
        if (phase.equals("phase1")) {
            return !localDateTime.isBefore(day) && localDateTime.isAfter(month);
        } else if (phase.equals("phase2")) {
            return !localDateTime.isBefore(hour) && localDateTime.isAfter(day);
        } else return false;
    }
}
