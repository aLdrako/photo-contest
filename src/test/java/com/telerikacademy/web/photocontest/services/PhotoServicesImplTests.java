package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.exceptions.EntityDuplicateException;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.models.Photo;
import com.telerikacademy.web.photocontest.models.PhotoScore;
import com.telerikacademy.web.photocontest.models.ReviewId;
import com.telerikacademy.web.photocontest.repositories.contracts.PhotoRepository;
import com.telerikacademy.web.photocontest.repositories.contracts.RankingRepository;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.Set;

import static com.telerikacademy.web.photocontest.helpers.Helpers.createMockFile;
import static com.telerikacademy.web.photocontest.helpers.Helpers.createMockPhoto;

@ExtendWith(MockitoExtension.class)
public class PhotoServicesImplTests {
    @Mock
    PhotoRepository mockRepository;
    @InjectMocks
    PhotoServicesImpl services;

    @Test
    public void create_Should_CallRepository() throws FileUploadException {
        // Arrange
        Photo mockPhoto = createMockPhoto();
        mockPhoto.getPostedOn().setParticipants(Set.of(mockPhoto.getUserCreated()));
        mockPhoto.getPostedOn().setPhase1(LocalDateTime.now().plusDays(1L));
        // Act
        services.create(mockPhoto, createMockFile());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .save(mockPhoto);
    }
    @Test
    public void create_Should_ThrowException_When_NotParticipantInContest() throws FileUploadException {
        // Arrange
        Photo mockPhoto = createMockPhoto();
        mockPhoto.getPostedOn().setPhase1(LocalDateTime.now().plusDays(1L));

        // Act, Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () ->
                services.create(mockPhoto, createMockFile()));
    }
    @Test
    public void create_Should_ThrowException_When_NotInPhase1() throws FileUploadException {
        // Arrange
        Photo mockPhoto = createMockPhoto();
        mockPhoto.getPostedOn().setParticipants(Set.of(mockPhoto.getUserCreated()));

        // Act, Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () ->
                services.create(mockPhoto, createMockFile()));
    }
    @Test
    public void create_Should_ThrowException_When_ParticipantAlreadyPostedPhoto() throws FileUploadException {
        // Arrange
        Photo mockPhoto = createMockPhoto();
        mockPhoto.getPostedOn().setParticipants(Set.of(mockPhoto.getUserCreated()));
        mockPhoto.getPostedOn().setPhase1(LocalDateTime.now().plusDays(1L));

        Mockito.when(mockRepository.existsByUserCreatedAndPostedOn(mockPhoto.getUserCreated(),
                mockPhoto.getPostedOn())).thenReturn(true);
        // Act, Assert
        Assertions.assertThrows(EntityDuplicateException.class, () ->
                services.create(mockPhoto, createMockFile()));
    }
    @Test
    public void create_Should_CreateDefaultJuryScores_When_UploadingPhoto() throws FileUploadException {
        // Arrange
        Photo mockPhoto = createMockPhoto();
        mockPhoto.getPostedOn().setParticipants(Set.of(mockPhoto.getUserCreated()));
        mockPhoto.getPostedOn().setPhase1(LocalDateTime.now().plusDays(1L));
        mockPhoto.getPostedOn().setJuries(Set.of(mockPhoto.getUserCreated()));
        ReviewId reviewId = new ReviewId(mockPhoto, mockPhoto.getUserCreated());
        PhotoScore photoScore = new PhotoScore();
        photoScore.setReviewId(reviewId);

        // Act
        services.create(mockPhoto, createMockFile());
        // Assert
        Assertions.assertTrue(mockPhoto.getScores().contains(photoScore));
    }
}
