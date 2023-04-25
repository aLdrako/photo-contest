package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.exceptions.EntityDuplicateException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.models.*;
import com.telerikacademy.web.photocontest.repositories.contracts.ContestResultsRepository;
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
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.Optional;
import java.util.Set;

import static com.telerikacademy.web.photocontest.helpers.Helpers.*;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class PhotoServicesImplTests {
    @Mock
    PhotoRepository mockRepository;
    @Mock
    ContestResultsRepository mockContestResultsRepository;
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
    @Test
    public void delete_Should_CallRepository_When_UserIsCreator() {
        // Arrange
        Photo mockPhoto = createMockPhoto();
        User user = mockPhoto.getUserCreated();
        mockPhoto.getPostedOn().setPhase2(LocalDateTime.now().plusDays(1L));

        Mockito.doNothing().when(mockContestResultsRepository).deleteContestResultsByResultEmbed_Photo(mockPhoto);
        // Act
        services.delete(mockPhoto, user, mockPhoto.getPostedOn());
        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(mockPhoto);
    }
    @Test
    public void delete_Should_CallRepository_When_UserIsOrganizer() {
        // Arrange
        Photo mockPhoto = createMockPhoto();
        User organizer = createMockOrganizer();
        mockPhoto.getPostedOn().setPhase2(LocalDateTime.now().plusDays(1L));
        Mockito.doNothing().when(mockContestResultsRepository).deleteContestResultsByResultEmbed_Photo(mockPhoto);
        // Act
        services.delete(mockPhoto, organizer, mockPhoto.getPostedOn());
        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(mockPhoto);
    }
    @Test
    public void delete_Should_ThrowException_When_UserNotCreatorOrOrganizer() {
        // Arrange
        Photo mockPhoto = createMockPhoto();
        User user = createMockUser();
        user.setId(2L);
        mockPhoto.getPostedOn().setPhase1(LocalDateTime.now().plusDays(1L));

        // Act, Assert
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> services.delete(mockPhoto, user, mockPhoto.getPostedOn()));
    }
    @Test
    public void delete_Should_ThrowException_When_PhotoIsNotInTheContest() {
        // Arrange
        Photo mockPhoto = createMockPhoto();
        User user = createMockUser();
        mockPhoto.getPostedOn().setPhase1(LocalDateTime.now().plusDays(1L));
        mockPhoto.getPostedOn().setPhotos(Set.of());
        // Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> services.delete(mockPhoto, user, mockPhoto.getPostedOn()));
    }
    @Test
    public void delete_Should_ThrowException_When_PhaseOneIsOver() {
        // Arrange
        Photo mockPhoto = createMockPhoto();
        User user = mockPhoto.getUserCreated();

        // Act, Assert
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> services.delete(mockPhoto, user, mockPhoto.getPostedOn()));
    }
    @Test
    public void getById_Should_CallRepository() {
        // Arrange
        Photo mockPhoto = createMockPhoto();
        Mockito.when(mockRepository.findById(anyLong()))
                .thenReturn(Optional.of(mockPhoto));
        // Act
        Photo photo = services.getById(anyLong());

        // Assert
        Assertions.assertEquals(mockPhoto, photo);
    }

    @Test
    public void getById_Should_ThrowException_When_PhotoNotFound() {
        // Arrange
        Mockito.when(mockRepository.findById(anyLong()))
                .thenThrow(EntityNotFoundException.class);

        // Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> services.getById(anyLong()));
    }
    @Test
    public void getScoreOfPhoto_Should_CallRepository() {
        // Arrange
        Photo mockPhoto = createMockPhoto();
        Mockito.when(mockRepository.findById(anyLong()))
                .thenReturn(Optional.of(mockPhoto));
        Mockito.when(mockRepository.getScoreOfPhoto(anyLong()))
                .thenReturn(0);

        // Act, Assert
        Assertions.assertEquals(0, services.getScoreOfPhoto(anyLong()));
    }
    @Test
    public void getScoreOfPhoto_Should_ThrowException_When_PhotoNotFound() {
        // Arrange
        Mockito.when(mockRepository.findById(anyLong()))
                .thenThrow(EntityNotFoundException.class);

        // Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> services.getScoreOfPhoto(anyLong()));
    }
    @Test
    public void postReview_Should_CallRepository() {
        // Arrange
        Photo mockPhoto = createMockPhoto();
        Contest mockContest = mockPhoto.getPostedOn();
        mockPhoto.getPostedOn().setPhase2(LocalDateTime.now().plusDays(2L));
        User jury = createMockOrganizer();
        mockContest.setJuries(Set.of(jury));

        ReviewId id = new ReviewId(mockPhoto, jury);
        mockContest.setPhotos(Set.of(mockPhoto));
        PhotoReviewDetails photoReviewDetails = new PhotoReviewDetails();
        photoReviewDetails.setReviewId(id);
        PhotoScore photoScore = new PhotoScore();
        photoScore.setReviewId(id);
        // Act
        services.postReview(photoScore, mockPhoto, jury, photoReviewDetails);

        // Assert
        Assertions.assertEquals(1, mockPhoto.getReviewsDetails().size());
    }
    @Test
    public void postReview_Should_ThrowException_When_UserIsNotJury() {
        // Arrange
        Photo mockPhoto = createMockPhoto();
        Contest mockContest = mockPhoto.getPostedOn();
        mockPhoto.getPostedOn().setPhase2(LocalDateTime.now().plusDays(2L));
        User user = createMockUser();

        ReviewId id = new ReviewId(mockPhoto, user);
        mockContest.setPhotos(Set.of(mockPhoto));
        PhotoReviewDetails photoReviewDetails = new PhotoReviewDetails();
        photoReviewDetails.setReviewId(id);
        PhotoScore photoScore = new PhotoScore();
        photoScore.setReviewId(id);

        // Act, Assert
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> services.postReview(photoScore, mockPhoto, user, photoReviewDetails));
    }
    @Test
    public void postReview_Should_ThrowException_When_NotInPhaseTwo() {
        // Arrange
        Photo mockPhoto = createMockPhoto();
        Contest mockContest = mockPhoto.getPostedOn();
        User jury = createMockOrganizer();
        mockContest.setJuries(Set.of(jury));

        ReviewId id = new ReviewId(mockPhoto, jury);
        mockContest.setPhotos(Set.of(mockPhoto));
        PhotoReviewDetails photoReviewDetails = new PhotoReviewDetails();
        photoReviewDetails.setReviewId(id);
        PhotoScore photoScore = new PhotoScore();
        photoScore.setReviewId(id);

        // Act, Assert
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> services.postReview(photoScore, mockPhoto, jury, photoReviewDetails));
    }
    @Test
    public void postReview_Should_ThrowException_When_AlreadyReviewed() {
        // Arrange
        Photo mockPhoto = createMockPhoto();
        Contest mockContest = mockPhoto.getPostedOn();
        mockPhoto.getPostedOn().setPhase2(LocalDateTime.now().plusDays(2L));
        User jury = createMockOrganizer();
        mockContest.setJuries(Set.of(jury));

        ReviewId id = new ReviewId(mockPhoto, jury);
        mockContest.setPhotos(Set.of(mockPhoto));
        PhotoReviewDetails photoReviewDetails = new PhotoReviewDetails();
        photoReviewDetails.setReviewId(id);
        PhotoScore photoScore = new PhotoScore();
        photoScore.setReviewId(id);
        // Act
        services.postReview(photoScore, mockPhoto, jury, photoReviewDetails);

        // Assert
        Assertions.assertThrows(EntityDuplicateException.class,
                () -> services.postReview(photoScore, mockPhoto, jury, photoReviewDetails));
    }
}
