package com.telerikacademy.web.photocontest.models.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LongWordsConstraintValidator implements ConstraintValidator<LongWordsConstraint, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        String[] words = value.split("\\s+");
        for (String word : words) {
            if (word.length() > 20) {
                return false;
            }
        }
        return true;
    }
}
