package com.telerikacademy.web.photocontest.models.dto;

import com.telerikacademy.web.photocontest.models.Photo;
import com.telerikacademy.web.photocontest.models.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ContestOutputDto {
    private Long id;
    private String title;
    private String category;
    private LocalDateTime phase1;
    private LocalDateTime phase2;
    private LocalDateTime dateCreated;
    private String coverPhoto;
    private boolean isInvitational;
    private List<String> juries;
    private List<String> participants;
    private List<Photo> photos;
}
