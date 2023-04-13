package com.telerikacademy.web.photocontest.models.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record ContestResponseDto(
        Long id, String title, String category, LocalDateTime phase1, LocalDateTime phase2, LocalDateTime dateCreated,
        String coverPhoto, boolean isInvitational, Boolean isFinished, List<String> juries, List<String> participants, List<Map<String, Object>> photos, List<ContestResultDto> results
) {}
