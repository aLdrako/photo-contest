package com.telerikacademy.web.photocontest.models.dto;

public record UserResponseDto(
        String firstName, String lastName, String username, String email, String joinDate,
        String rank, boolean organizer, int points
) {}
