package com.telerikacademy.web.photocontest.models.dto;

import com.telerikacademy.web.photocontest.models.validations.CreateValidationGroup;
import com.telerikacademy.web.photocontest.models.validations.UpdateValidationGroup;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
public class ContestDto {
    @NotEmpty(message = "Title can't be empty", groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    private String title;
    @Positive(message = "Category Id should be positive number", groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    private Long categoryId;
    @NotNull(message = "Type of contest can't be null", groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    private boolean isInvitational;
    @NotNull(message = "Phase 1 can't be empty", groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime phase1;
    @NotNull(message = "Phase 2 can't be empty", groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime phase2;
}
