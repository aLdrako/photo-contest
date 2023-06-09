package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.exceptions.EntityDuplicateException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.models.*;
import com.telerikacademy.web.photocontest.repositories.contracts.ContestRepository;
import com.telerikacademy.web.photocontest.repositories.contracts.PhotoRepository;
import com.telerikacademy.web.photocontest.repositories.contracts.ContestResultsRepository;
import com.telerikacademy.web.photocontest.services.contracts.ContestServices;
import com.telerikacademy.web.photocontest.services.contracts.PhotoServices;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.telerikacademy.web.photocontest.helpers.FileUploadHelper.deletePhoto;
import static com.telerikacademy.web.photocontest.helpers.FileUploadHelper.uploadPhoto;

@Service
@AllArgsConstructor
public class PhotoServicesImpl implements PhotoServices {
    private static final String PHOTO_ALREADY_UPLOADED_BY_USER = "This user has already uploaded a photo in this contest!";
    private static final String NOT_IN_PHASE_ONE_MESSAGE = "Photos can be uploaded during Phase One of each contest!";
    private static final String INVALID_REMOVE_MESSAGE = "Only organizers/juries or creator can remove a photo!";
    private static final String NOT_A_JURY_MESSAGE = "You are not a jury in this contest. " +
            "Therefore you cannot post reviews on photos!";
    private static final String DUPLICATE_REVIEW_MESSAGE = "This photo has already been reviewed by jury '%s'!";
    private static final String NOT_IN_PHASE_TWO_MESSAGE = "Reviews can be posted during Phase Two!";
    public static final String NOT_A_PARTICIPANT_MESSAGE = "Only participants in the contest can upload a photo!";
    public static final String INVALID_PHOTO_DELETION_MESSAGE = "Deleting a photo is only possible during Phase One/Two of a contest!";
    private final PhotoRepository photoRepository;
    private final ContestResultsRepository contestResultsRepository;
    private final ContestServices contestServices;
    @Override
    public List<Photo> getAll() {
        return photoRepository.findAll();
    }

    @Override
    public void create(Photo photo, MultipartFile file) throws FileUploadException {
        checkCreatePermissions(photo.getUserCreated(), photo.getPostedOn());
        checkDuplicatePhoto(photo.getUserCreated(), photo.getPostedOn());
        if (photo.getPostedOn().getPhase1().isBefore(LocalDateTime.now())) {
            throw new UnauthorizedOperationException(NOT_IN_PHASE_ONE_MESSAGE);
        }
        photo.setPhoto(uploadPhoto(file));
        photo.getPostedOn().getJuries().forEach(jury -> {
            PhotoScore photoScore = new PhotoScore();
            photoScore.setReviewId(new ReviewId(photo, jury));
            photoScore.setScore(3);
            photo.addScore(photoScore);
        });
        photoRepository.save(photo);
    }

    private void checkDuplicatePhoto(User userCreated, Contest postedOn) {
        boolean duplicatePhoto = photoRepository.existsByUserCreatedAndPostedOn(userCreated,
                postedOn);
        if (duplicatePhoto) {
            throw new EntityDuplicateException(PHOTO_ALREADY_UPLOADED_BY_USER);
        }
    }

    @Override
    @Transactional
    public void delete(Photo photo, User user) {
        checkDeletePermissions(photo, user);
        if (!photo.getPostedOn().getCoverPhoto().equals(photo.getPhoto())) {
            deletePhoto(photo.getPhoto());
        }
        photo.getPostedOn().setPhotos(null);
        contestResultsRepository.deleteContestResultsByResultEmbed_Photo(photo);
        photoRepository.delete(photo);
    }

    private void checkDeletePermissions(Photo photo, User user) {
        if (!photo.getUserCreated().equals(user) && !user.isOrganizer() &&
            !photo.getPostedOn().getJuries().contains(user)) {
            throw new UnauthorizedOperationException(INVALID_REMOVE_MESSAGE);
        }
        if (photo.getPostedOn().getPhase2().isBefore(LocalDateTime.now())) {
            throw new UnauthorizedOperationException(INVALID_PHOTO_DELETION_MESSAGE);
        }
    }

    @Override
    public Photo getById(Long id) {
        return photoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Photo", id));
    }

    @Override
    public void postReview(PhotoScore photoScore, Photo photo, User user,
                           PhotoReviewDetails photoReviewDetails) {
        ReviewId reviewId = new ReviewId(photo, user);
        photoScore.setReviewId(reviewId);
        photoReviewDetails.setReviewId(reviewId);
        checkReviewPostPermissions(photoReviewDetails, photo, user);
        photo.addReviewDetails(photoReviewDetails);
        photo.updateScore(photoScore);
        photoRepository.save(photo);
    }

    @Override
    public int getScoreOfPhoto(Long id) {
        photoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Photo", id));
        return photoRepository.getScoreOfPhoto(id);
    }

    @Override
    public Photo getPhotoByContestId(Long photoId, Long contestId) {
        Contest contest = contestServices.findById(contestId);
        Photo photo = getById(photoId);
        if (!contest.getPhotos().contains(photo)) {
            throw new EntityNotFoundException("Contest", contest.getId(), "photo", photo.getId());
        }
        return photo;
    }

    @Override
    public List<Photo> getPhotosOfContest(Contest contest) {
        return photoRepository.findAllByPostedOn(contest);
    }

    @Override
    public Page<Photo> search(String title, Long contestId, Long userId, Pageable pageable) {
        return photoRepository.search(title, contestId, userId, pageable);
    }

    @Override
    public void alreadyUploadedPhoto(Contest contest, User user) {
        checkDuplicatePhoto(user, contest);
    }

    private void checkReviewPostPermissions(PhotoReviewDetails photoReviewDetails, Photo photo, User user) {
        if (!photo.getPostedOn().getJuries().contains(user)) {
            throw new UnauthorizedOperationException(NOT_A_JURY_MESSAGE);
        }
        if (photo.getReviewsDetails().contains(photoReviewDetails)) {
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
