package com.telerikacademy.web.photocontest.models.dto;

import com.telerikacademy.web.photocontest.models.Category;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
public class ContestDto {
    @NotEmpty(message = "Title can't be empty")
    private String title;
    @NotEmpty(message = "Category can't be empty")
    private Category category;
    @NotEmpty(message = "Type of contest can't be empty")
    private boolean isInvitational;
    @NotEmpty(message = "Phase 1 can't be empty")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime phase1;
    @NotEmpty(message = "Phase 2 can't be empty")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime phase2;
}
