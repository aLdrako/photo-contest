package com.telerikacademy.web.photocontest.models.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CategoryDto {
    @NotEmpty(message = "Category name can't be empty")
    @Size(min = 4, max = 16, message = "Category name should be between 4 and 16 symbols")
    private String name;
}
