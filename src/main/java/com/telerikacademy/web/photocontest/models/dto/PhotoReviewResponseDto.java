package com.telerikacademy.web.photocontest.models.dto;

public record PhotoReviewResponseDto(
        Long photoId, String userCreated, String comment, int score
) {
}
