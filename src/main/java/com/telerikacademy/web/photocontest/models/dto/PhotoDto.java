package com.telerikacademy.web.photocontest.models.dto;

import com.telerikacademy.web.photocontest.models.validations.CreatePhotoGroup;
import com.telerikacademy.web.photocontest.models.validations.CreatePhotoViaContestGroup;
import com.telerikacademy.web.photocontest.models.validations.CreateValidationGroup;
import com.telerikacademy.web.photocontest.models.validations.UpdateValidationGroup;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
@Data
public class PhotoDto {
    @NotEmpty(message = "Title must not be empty!",
            groups = {CreatePhotoViaContestGroup.class, CreatePhotoGroup.class})
    @Size(min = 16, max = 50, message = "Title should be between 16 and 50 symbols!",
            groups = {CreatePhotoViaContestGroup.class, CreatePhotoGroup.class})
    private String title;
    @NotEmpty(message = "Story must not be empty!",
            groups = {CreatePhotoViaContestGroup.class, CreatePhotoGroup.class})
    @Size(min = 20, max = 8196, message = "Story should be between 20 and 8196 symbols!",
            groups = {CreatePhotoViaContestGroup.class, CreatePhotoGroup.class})
    private String story;
    @NotEmpty(message = "A photo must be uploaded!",
            groups = {CreatePhotoViaContestGroup.class, CreatePhotoGroup.class})
    private String photo;
    @Positive(message = "A contest id must be a positive number!",
            groups = {CreatePhotoGroup.class})
    private Long contestId;
}