package com.telerikacademy.web.photocontest.controllers.rest;

import com.telerikacademy.web.photocontest.exceptions.AuthorizationException;
import com.telerikacademy.web.photocontest.exceptions.EntityDuplicateException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.models.*;
import com.telerikacademy.web.photocontest.models.dto.PhotoDto;
import com.telerikacademy.web.photocontest.models.dto.PhotoResponseDto;
import com.telerikacademy.web.photocontest.models.dto.PhotoReviewDto;
import com.telerikacademy.web.photocontest.models.dto.PhotoReviewResponseDto;
import com.telerikacademy.web.photocontest.models.validations.CreatePhotoGroup;
import com.telerikacademy.web.photocontest.services.ModelMapper;
import com.telerikacademy.web.photocontest.services.contracts.ContestServices;
import com.telerikacademy.web.photocontest.services.contracts.PhotoServices;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.Collectors;

import static com.telerikacademy.web.photocontest.helpers.FileUploadHelper.uploadPhoto;

@RestController
@RequestMapping("/api/photos")
@AllArgsConstructor
public class PhotoRestController {
    private final PhotoServices photoServices;
    private final ContestServices contestServices;
    private final AuthenticationHelper authenticationHelper;
    private final ModelMapper modelMapper;

    @GetMapping
    public List<PhotoResponseDto> getAll() {
        return photoServices.getAll().stream()
                .map(modelMapper::objectToDto).collect(Collectors.toList());
    }
    @GetMapping("/search")
    public List<PhotoResponseDto> search(@RequestParam(required = false) Optional<String> q) {
        return photoServices.search(q, Optional.empty()).stream()
                .map(modelMapper::objectToDto).collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    public PhotoResponseDto getById(@PathVariable Long id) {
        try {
            return modelMapper.objectToDto(photoServices.getById(id));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @PostMapping
    public PhotoResponseDto create(@RequestHeader(required = false) Optional<String> authorization,
                                   @Validated(CreatePhotoGroup.class) @ModelAttribute PhotoDto photoDto) {
        try {
            User user = authenticationHelper.tryGetUser(authorization);
            Photo photo = modelMapper.dtoToObject(photoDto);
            photo.setPostedOn(contestServices.findById(photoDto.getContestId()));
            photo.setUserCreated(user);
            photoServices.create(photo, photoDto.getFile());
            return modelMapper.objectToDto(photo);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (FileUploadException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{id}/review")
    public PhotoReviewResponseDto postReview(@PathVariable Long id,
                                             @RequestHeader(required = false) Optional<String> authorization,
                                             @Valid @RequestBody PhotoReviewDto photoReviewDto) {
        try {
            User user = authenticationHelper.tryGetUser(authorization);
            Photo photo = photoServices.getById(id);
            ReviewId reviewId = new ReviewId(photo, user);

            PhotoScore photoScore = modelMapper.dtoToObject(photoReviewDto);
            PhotoReviewDetails photoReviewDetails = modelMapper.dtoToReviewDetails(photoReviewDto);
            photoScore.setReviewId(reviewId);
            photoReviewDetails.setReviewId(reviewId);
            photoServices.postReview(photoScore, photo, user, photoReviewDetails);
            photoReviewDetails.setPhotoScore(photoScore);
            return modelMapper.objectToDto(photoReviewDetails);
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
            photoServices.delete(photo, user, photo.getPostedOn());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    @GetMapping("/{id}/reviews")
    public List<PhotoReviewResponseDto> getReviewsOfPhoto(@PathVariable Long id) {
        try {
            Photo photo = photoServices.getById(id);
            return photo.getReviewsDetails().stream().map(modelMapper::objectToDto).collect(Collectors.toList());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
