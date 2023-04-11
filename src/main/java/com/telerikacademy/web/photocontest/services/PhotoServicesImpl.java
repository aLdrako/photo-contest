package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.exceptions.EntityDuplicateException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.models.Photo;
import com.telerikacademy.web.photocontest.models.PhotoReview;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.repositories.contracts.PhotoRepository;
import com.telerikacademy.web.photocontest.services.contracts.PhotoServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
@AllArgsConstructor
public class PhotoServicesImpl implements PhotoServices {
    private static final String PHOTO_ALREADY_UPLOADED_BY_USER = "This user has already uploaded a photo in this contest!";
    private static final String NOT_IN_PHASE_ONE_MESSAGE = "Photos can be uploaded during Phase One of each contest!";
    private static final String INVALID_REMOVE_MESSAGE = "Only organizers or creator can remove a photo!";
    private static final String NOT_A_JURY_MESSAGE = "You are not a jury in this contest. " +
            "Therefore you cannot post reviews on photos!";
    private static final String DUPLICATE_REVIEW_MESSAGE = "This photo has already been reviewed by jury '%s'!";
    private static final String NOT_IN_PHASE_TWO_MESSAGE = "Reviews can be posted during Phase Two!";
    public static final String NOT_A_PARTICIPANT_MESSAGE = "Only participants in the contest can upload a photo!";
    private final PhotoRepository photoRepository;
    @Override
    public List<Photo> getAll() {
        return photoRepository.findAll();
    }

    @Override
    public void create(Photo photo) {
        checkCreatePermissions(photo.getUserCreated(), photo.getPostedOn());
        boolean duplicatePhoto = photoRepository.existsByUserCreatedAndPostedOn(photo.getUserCreated(),
                photo.getPostedOn());
        if (duplicatePhoto) {
            throw new EntityDuplicateException(PHOTO_ALREADY_UPLOADED_BY_USER);
        }
        if (photo.getPostedOn().getPhase1().isBefore(LocalDateTime.now())) {
            throw new UnauthorizedOperationException(NOT_IN_PHASE_ONE_MESSAGE);
        }
        photoRepository.save(photo);
    }

    @Override
    public void delete(Photo photo, User user) {
        checkDeletePermissions(photo, user);
        photoRepository.delete(photo);
    }

    private void checkDeletePermissions(Photo photo, User user) {
        if (!photo.getUserCreated().equals(user) && !user.isOrganizer()) {
            throw new UnauthorizedOperationException(INVALID_REMOVE_MESSAGE);
        }
    }

    @Override
    public Photo getById(Long id) {
        return photoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Photo", id));
    }

    @Override
    public void postReview(PhotoReview photoReview, Photo photo, User user) {
        checkReviewPostPermissions(photoReview, photo, user);
        photo.addReview(photoReview);
        photoRepository.save(photo);
    }
    private void checkReviewPostPermissions(PhotoReview photoReview, Photo photo, User user) {
        if (!photo.getPostedOn().getJuries().contains(user)) {
            throw new UnauthorizedOperationException(NOT_A_JURY_MESSAGE);
        }
        if (photo.getReviews().contains(photoReview)) {
            throw new EntityDuplicateException(
                    String.format(DUPLICATE_REVIEW_MESSAGE, user.getUsername()));
        }
        if (photo.getPostedOn().getPhase1().isAfter(LocalDateTime.now()) ||
                photo.getPostedOn().getPhase2().isBefore(LocalDateTime.now())) {
            throw new UnauthorizedOperationException(NOT_IN_PHASE_TWO_MESSAGE);
        }
    }

    private void checkCreatePermissions(User userCreated, Contest postedOn) {
        if (!postedOn.getParticipants().contains(userCreated)) {
            throw new UnauthorizedOperationException(NOT_A_PARTICIPANT_MESSAGE);
        }
    }
}
