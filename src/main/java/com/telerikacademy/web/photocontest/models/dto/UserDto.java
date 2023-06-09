package com.telerikacademy.web.photocontest.models.dto;

import com.telerikacademy.web.photocontest.models.validations.*;
import lombok.Data;

import javax.validation.constraints.*;
@Data
@EqualFields(baseField = "password", matchField = "passwordConfirm", message = "Passwords must be equal!",
        groups = {CreateValidationGroup.class, UpdateValidationGroup.class, ChangePasswordGroup.class})
public class UserDto {
    @Pattern(regexp = "^\\S*$", message = "Spaces are not allowed",
    groups = {CreateValidationGroup.class, UpdateValidationGroup.class, ChangePasswordGroup.class,
            LoginValidationGroup.class, EnlistUserValidationGroup.class})
    @NotBlank(message = "First name can't be empty",
            groups = {CreateValidationGroup.class})
    @Size(min = 4, max = 32, message = "First name should be between 4 and 32 symbols",
            groups = {UpdateValidationGroup.class, CreateValidationGroup.class})
    private String firstName;
    @Pattern(regexp = "^\\S*$", message = "Spaces are not allowed",
            groups = {CreateValidationGroup.class, UpdateValidationGroup.class, ChangePasswordGroup.class,
                    LoginValidationGroup.class, EnlistUserValidationGroup.class})
    @NotBlank(message = "Last name can't be empty",
            groups = {CreateValidationGroup.class})
    @Size(min = 4, max = 32, message = "Last name should be between 4 and 32 symbols",
            groups = {UpdateValidationGroup.class, CreateValidationGroup.class})
    private String lastName;
    @Pattern(regexp = "^\\S*$", message = "Spaces are not allowed",
            groups = {CreateValidationGroup.class, UpdateValidationGroup.class, ChangePasswordGroup.class,
                    LoginValidationGroup.class, EnlistUserValidationGroup.class, EmailUpdateValidation.class})
    @NotBlank(message = "Email can't be empty",
            groups = {CreateValidationGroup.class, EmailUpdateValidation.class})
    @Email(message = "Email has invalid format",
            groups = {UpdateValidationGroup.class, CreateValidationGroup.class, EmailUpdateValidation.class})
    private String email;
    @Pattern(regexp = "^\\S*$", message = "Spaces are not allowed",
            groups = {CreateValidationGroup.class, UpdateValidationGroup.class, ChangePasswordGroup.class,
                    LoginValidationGroup.class, EnlistUserValidationGroup.class})
    @NotBlank(message = "Username can't be empty",
            groups = {CreateValidationGroup.class, EnlistUserValidationGroup.class,
                    LoginValidationGroup.class})
    @Size(min = 4, max = 16, message = "Username should be between 4 and 16 symbols",
            groups = {CreateValidationGroup.class, EnlistUserValidationGroup.class,
                    LoginValidationGroup.class})
    private String username;
    @Pattern(regexp = "^\\S*$", message = "Spaces are not allowed",
            groups = {CreateValidationGroup.class, UpdateValidationGroup.class, ChangePasswordGroup.class,
                    LoginValidationGroup.class, EnlistUserValidationGroup.class})
    @NotBlank(message = "Password can't be empty",
            groups = {CreateValidationGroup.class, LoginValidationGroup.class, ChangePasswordGroup.class})
    @OptionalWithSizeConstraint(min = 5, max = 16, message = "Password size must be between 5 and 16 characters!",
            groups = {CreateValidationGroup.class, UpdateValidationGroup.class, LoginValidationGroup.class,
                    ChangePasswordGroup.class})
    private String password;
    @Pattern(regexp = "^\\S*$", message = "Spaces are not allowed",
            groups = {CreateValidationGroup.class, UpdateValidationGroup.class, ChangePasswordGroup.class,
                    LoginValidationGroup.class, EnlistUserValidationGroup.class})
    @NotBlank(message = "Password confirmation can't be empty",
            groups = {CreateValidationGroup.class, ChangePasswordGroup.class})
    private String passwordConfirm;
    private boolean organizer;
    
}
