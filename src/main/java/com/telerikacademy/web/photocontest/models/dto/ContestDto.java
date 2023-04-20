package com.telerikacademy.web.photocontest.models.dto;

import com.telerikacademy.web.photocontest.models.validations.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ContestDto {
    @NotEmpty(message = "Title can't be empty", groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    @Size(min = 10, max = 50, message = "Title should be between 10 and 50 symbols", groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    private String title;
    @Positive(message = "Category Id should be positive number", groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    private Long categoryId;
    @NotNull(message = "Type of contest can't be null", groups = {CreateValidationGroup.class})
    private boolean isInvitational;
    @NotNull(message = "Phase 1 can't be empty", groups = {CreateValidationGroup.class})
    @Future(message = "Phase 1 should be in the future in bounds of one day to one month", groups = {CreateValidationGroup.class})
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime phase1;
    @NotNull(message = "Phase 2 can't be empty", groups = {CreateValidationGroup.class})
    @Future(message = "Phase 2 should be after Phase 1 in bounds of one hour to one day", groups = {CreateValidationGroup.class})
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime phase2;
    private String coverPhoto;
    private List<@Size(min = 4, max = 16, message = "Username should be between 4 and 16 symbols", groups = {CreateValidationGroup.class})
            String> juries;
    private MultipartFile coverPhotoUpload;
}
