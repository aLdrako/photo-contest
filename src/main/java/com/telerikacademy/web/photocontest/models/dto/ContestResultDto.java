package com.telerikacademy.web.photocontest.models.dto;

import java.util.List;

public record ContestResultDto(Long photoId, String photo, String userCreated, List<String> juries, List<String> comments, List<Integer> scores, int totalScore) {}
