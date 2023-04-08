package com.telerikacademy.web.photocontest.models.dto;

import com.telerikacademy.web.photocontest.models.validations.CreateValidationGroup;
import com.telerikacademy.web.photocontest.models.validations.EqualFields;
import com.telerikacademy.web.photocontest.models.validations.UpdateValidationGroup;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
@Getter
@Setter
@EqualFields(baseField = "password", matchField = "passwordConfirm", message = "Passwords must be equal!")
public class UserDto {
    @NotEmpty(message = "First name can't be empty",
            groups = {CreateValidationGroup.class})
    @Size(min = 4, max = 32, message = "First name should be between 4 and 32 symbols",
            groups = {UpdateValidationGroup.class, CreateValidationGroup.class})
    private String firstName;
    @NotEmpty(message = "Last name can't be empty",
            groups = {CreateValidationGroup.class})
    @Size(min = 4, max = 32, message = "Last name should be between 4 and 32 symbols",
            groups = {UpdateValidationGroup.class, CreateValidationGroup.class})
    private String lastName;
    @NotEmpty(message = "Email can't be empty",
            groups = {CreateValidationGroup.class})
    @Email(message = "Email has invalid format",
            groups = {UpdateValidationGroup.class, CreateValidationGroup.class})
    private String email;
    @NotEmpty(message = "Username can't be empty",
            groups = {CreateValidationGroup.class})
    @Size(min = 4, max = 16, message = "Username should be between 4 and 16 symbols",
            groups = {CreateValidationGroup.class})
    private String username;
    @NotEmpty(message = "Password can't be empty",
            groups = {CreateValidationGroup.class})
    private String password;
    @NotEmpty(message = "Password confirmation can't be empty",
            groups = {CreateValidationGroup.class})
    private String passwordConfirm;
    
}
