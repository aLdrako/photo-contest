package com.telerikacademy.web.photocontest.models.dto;

public record PhotoReviewResponseDto(
        Long photoId, Long userId, String comment, int score
) {
}
