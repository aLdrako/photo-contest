package com.telerikacademy.web.photocontest.models.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class PermissionsDto {
    @NotNull(message = "Organizer field cannot be empty!")
    private Boolean organiser;

    public PermissionsDto() {
    }
    public PermissionsDto(Boolean organiser) {
        this.organiser = organiser;
    }

}
