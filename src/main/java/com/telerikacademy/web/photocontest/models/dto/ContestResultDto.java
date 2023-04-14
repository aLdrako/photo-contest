package com.telerikacademy.web.photocontest.models.dto;

import java.util.List;
import java.util.Map;

public record ContestResultDto(Long photoId, String photo, String userCreated, List<Map<String, Object>> juries, int totalScore) {}
