package com.telerikacademy.web.photocontest.models.dto;

import javax.validation.constraints.*;

public class UserDto {
    @NotEmpty(message = "First name can't be empty")
    @Size(min = 4, max = 32, message = "First name should be between 4 and 32 symbols")
    private String firstName;
    @NotEmpty(message = "Last name can't be empty")
    @Size(min = 4, max = 32, message = "Last name should be between 4 and 32 symbols")
    private String lastName;
    @NotEmpty(message = "Email can't be empty")
    @Email(message = "Email has invalid format")
    private String email;
    @NotEmpty(message = "Username can't be empty")
    @Size(min = 4, max = 16, message = "Username should be between 4 and 16 symbols")
    private String username;
    @NotEmpty(message = "Password can't be empty")
    private String password;
    @NotEmpty(message = "Password confirmation can't be empty")
    private String passwordConfirm;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
