package com.telerikacademy.web.photocontest.models.dto;

public record PhotoResponseDto(
        String title, String story, String photo, String userCreated, Long contestId
) {
}
