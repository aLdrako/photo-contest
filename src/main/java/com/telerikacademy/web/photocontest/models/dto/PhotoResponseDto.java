package com.telerikacademy.web.photocontest.models.dto;

public record PhotoResponseDto(
        String title, String story, String photo, Long userId, Long contestId
) {
}
