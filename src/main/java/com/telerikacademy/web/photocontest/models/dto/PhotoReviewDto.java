package com.telerikacademy.web.photocontest.models.dto;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class PhotoReviewDto {
    @Min(value = 1, message = "Score must be between 1 and 10!")
    @Max(value = 10, message = "Score must be between 1 and 10!")
    private int score;
    @NotEmpty(message = "A Comment must be present when reviewing a photo!")
    @Size(min = 20, max = 1024, message = "Size of a comment must be between 20 and 1024 characters!")
    private String comment;
    @NotNull(message = "You need to give a true/false if the photo fits the category of the contest")
    private boolean fitsCategory;
}
