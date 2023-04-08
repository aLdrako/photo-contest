package com.telerikacademy.web.photocontest.models.validations;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class EqualFieldsValidator implements ConstraintValidator<EqualFields, Object> {

    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(EqualFields constraintAnnotation) {
        firstFieldName = constraintAnnotation.baseField();
        secondFieldName = constraintAnnotation.matchField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Object firstObj = new BeanWrapperImpl(value).getPropertyValue(firstFieldName);
            Object secondObj = new BeanWrapperImpl(value).getPropertyValue(secondFieldName);
            return firstObj == null && secondObj == null || firstObj != null && firstObj.equals(secondObj);
        } catch (Exception e) {
            return false;
        }
    }
}





