package com.telerikacademy.web.photocontest.controllers.rest;

import com.telerikacademy.web.photocontest.exceptions.AuthorizationException;
import com.telerikacademy.web.photocontest.exceptions.EntityDuplicateException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.models.Photo;
import com.telerikacademy.web.photocontest.models.PhotoReview;
import com.telerikacademy.web.photocontest.models.ReviewId;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.models.dto.PhotoDto;
import com.telerikacademy.web.photocontest.models.dto.PhotoReviewDto;
import com.telerikacademy.web.photocontest.models.validations.CreatePhotoGroup;
import com.telerikacademy.web.photocontest.services.ModelMapper;
import com.telerikacademy.web.photocontest.services.contracts.PhotoServices;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/photos")
@AllArgsConstructor
public class PhotoRestController {
    private final PhotoServices photoServices;
    private final AuthenticationHelper authenticationHelper;
    private final ModelMapper modelMapper;

    @GetMapping
    public List<Photo> getAll() {
        return photoServices.getAll();
    }
    @GetMapping("/{id}")
    public Photo getById(@PathVariable Long id) {
        try {
            return photoServices.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @PostMapping
    public Photo create(@RequestHeader(required = false) Optional<String> authorization,
                        @Validated(CreatePhotoGroup.class) @RequestBody PhotoDto photoDto) {
        try {
            User user = authenticationHelper.tryGetUser(authorization);
            Photo photo = modelMapper.dtoToObject(photoDto);
            photo.setUserCreated(user);
            photoServices.create(photo);
            return photo;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping("/{id}/review")
    public PhotoReview postReview(@PathVariable Long id,
                            @RequestHeader(required = false) Optional<String> authorization,
                            @Valid @RequestBody PhotoReviewDto photoReviewDto) {
        try {
            User user = authenticationHelper.tryGetUser(authorization);
            Photo photo = photoServices.getById(id);
            PhotoReview photoReview = modelMapper.dtoToObject(photoReviewDto);
            photoReview.setReviewId(new ReviewId(photo.getId(), user.getId()));
            photoServices.postReview(photoReview, photo, user);
            return photoReview;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id,
                       @RequestHeader(required = false) Optional<String> authorization) {
        try {
            User user = authenticationHelper.tryGetUser(authorization);
            Photo photo = photoServices.getById(id);
            photoServices.delete(photo, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

}
