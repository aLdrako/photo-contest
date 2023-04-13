package com.telerikacademy.web.photocontest.models.dto;

import com.telerikacademy.web.photocontest.models.Photo;

import java.time.LocalDateTime;
import java.util.List;

public record ContestResponseDto(
        Long id, String title, String category, LocalDateTime phase1, LocalDateTime phase2, LocalDateTime dateCreated,
        String coverPhoto, boolean isInvitational, Boolean isFinished, List<String> juries, List<String> participants, List<Photo> photos
) {}
