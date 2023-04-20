package com.telerikacademy.web.photocontest.models.validations;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OptionalWithSizeConstraintValidator implements ConstraintValidator<OptionalWithSizeConstraint, String> {
    private int min;
    private int max;

    @Override
    public void initialize(OptionalWithSizeConstraint constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String text, ConstraintValidatorContext context) {
        return text == null || text.isBlank() || text.length() >= min && text.length() <= max;
    }
}

