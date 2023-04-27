package com.telerikacademy.web.photocontest.controllers.rest;

import com.telerikacademy.web.photocontest.exceptions.AuthorizationException;
import com.telerikacademy.web.photocontest.exceptions.EntityDuplicateException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.helpers.FilterAndSortingHelper;
import com.telerikacademy.web.photocontest.models.*;
import com.telerikacademy.web.photocontest.models.dto.PhotoDto;
import com.telerikacademy.web.photocontest.models.dto.PhotoResponseDto;
import com.telerikacademy.web.photocontest.models.dto.PhotoReviewDto;
import com.telerikacademy.web.photocontest.models.dto.PhotoReviewResponseDto;
import com.telerikacademy.web.photocontest.models.validations.CreatePhotoGroup;
import com.telerikacademy.web.photocontest.services.ModelMapper;
import com.telerikacademy.web.photocontest.services.contracts.ContestServices;
import com.telerikacademy.web.photocontest.services.contracts.PhotoServices;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import static com.telerikacademy.web.photocontest.helpers.FilterAndSortingHelper.getResult;

@Api(tags = "Photo Rest Controller")
@RestController
@RequestMapping("/api/photos")
@AllArgsConstructor
public class PhotoRestController {
    private final PhotoServices photoServices;
    private final ContestServices contestServices;
    private final AuthenticationHelper authenticationHelper;
    private final ModelMapper modelMapper;
    @Operation(
            summary = "Get All Photo",
            description = "Get a list of all PhotoResponseDto objects."
    )
    @ApiResponse(responseCode = "200", description = "All photos",
            content = {@Content(schema = @Schema(implementation = PhotoResponseDto.class, type = "array"),
                    mediaType = "application/json")})
    @GetMapping
    public List<PhotoResponseDto> getAll() {
        return photoServices.getAll().stream()
                .map(modelMapper::objectToDto).collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<PhotoResponseDto> search(@RequestParam Map<String, String> parameters,
                                         Pageable pageable) {
        FilterAndSortingHelper.Result result = getResult(parameters, pageable);
        return photoServices.search(result.title(), null, null, result.pageable())
                .stream()
                .map(modelMapper::objectToDto)
                .collect(Collectors.toList());
    }
    @Operation(
            summary = "Retrieve a Photo by Id",
            description = "Get a Photo object by specifying its id. The response is PhotoResponseDto object"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Photo found",
                    content = {@Content(schema = @Schema(implementation = PhotoResponseDto.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Photo not found",
                    content = {@Content(schema = @Schema())})
    })
    @GetMapping("/{id}")
    public PhotoResponseDto getById(@PathVariable Long id) {
        try {
            return modelMapper.objectToDto(photoServices.getById(id));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @Operation(
            summary = "Create a new photo",
            description = "Create a Photo object"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Photo created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PhotoResponseDto.class))}),
            @ApiResponse(responseCode = "409", description = "User has already uploaded a photo in this contest.",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Contest not found",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "Unauthorized operation",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "The validation of the body has failed",
                    content = {@Content(schema = @Schema())})
    })
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
    @Operation(
            summary = "Create a new photo review",
            description = "Create a PhotoReviewResponseDto object"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Photo review created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PhotoReviewResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Photo not found",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "Unauthorized operation",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "409", description = "Jury has already uploaded a photo review for this photo.",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "The validation of the body has failed",
                    content = {@Content(schema = @Schema())})
    })
    @PostMapping("/{id}/reviews")
    public PhotoReviewResponseDto postReview(@PathVariable Long id,
                                             @RequestHeader(required = false) Optional<String> authorization,
                                             @Valid @RequestBody PhotoReviewDto photoReviewDto) {
        try {
            User user = authenticationHelper.tryGetUser(authorization);
            Photo photo = photoServices.getById(id);
            PhotoScore photoScore = modelMapper.dtoToObject(photoReviewDto);
            PhotoReviewDetails photoReviewDetails = modelMapper.dtoToReviewDetails(photoReviewDto);
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
    @Operation(
            summary = "Delete a Photo by Id",
            description = "Delete a Photo object by specifying its id."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Photo successfully deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized operation"),
            @ApiResponse(responseCode = "404", description = "Photo not found")
    })
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
    @Operation(
            summary = "Get All Photo reviews of a photo",
            description = "Get a list of all PhotoResponseDto objects of a photo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Get all photo reviews",
                    content = {@Content(schema = @Schema(implementation = PhotoReviewResponseDto.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Photo not found",
                    content = {@Content(schema = @Schema())})
    })
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
