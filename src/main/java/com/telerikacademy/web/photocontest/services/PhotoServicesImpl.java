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
            throw new EntityDuplicateException("This user has already uploaded a photo in this contest!");
        }
        if (photo.getPostedOn().getPhase1().isBefore(LocalDateTime.now())) {
            throw new UnauthorizedOperationException("Photos can be uploaded during Phase One of each contest!");
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
            throw new UnauthorizedOperationException("Only organizers or creator can remove a photo!");
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
            throw new UnauthorizedOperationException("You are not a jury in this contest. " +
                    "Therefore you cannot post reviews on photos!");
        }
        if (photo.getReviews().contains(photoReview)) {
            throw new EntityDuplicateException("This photo has already been reviewed!");
        }
        if (photo.getPostedOn().getPhase1().isBefore(LocalDateTime.now()) ||
                photo.getPostedOn().getPhase2().isAfter(LocalDateTime.now())) {
            throw new UnauthorizedOperationException("Reviews can be posted during Phase Two!");
        }
    }

    private void checkCreatePermissions(User userCreated, Contest postedOn) {
        if (!postedOn.getParticipants().contains(userCreated)) {
            throw new UnauthorizedOperationException("Only participants in the contest can upload a photo!");
        }
    }
}
