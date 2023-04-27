package com.telerikacademy.web.photocontest.controllers.rest;

import com.telerikacademy.web.photocontest.exceptions.AuthorizationException;
import com.telerikacademy.web.photocontest.exceptions.EntityDuplicateException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.helpers.FilterAndSortingHelper;
import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.models.Photo;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.models.dto.*;
import com.telerikacademy.web.photocontest.models.validations.*;
import com.telerikacademy.web.photocontest.services.ModelMapper;
import com.telerikacademy.web.photocontest.services.contracts.ContestServices;
import com.telerikacademy.web.photocontest.services.contracts.PhotoServices;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.telerikacademy.web.photocontest.helpers.FileUploadHelper.deletePhoto;
import static com.telerikacademy.web.photocontest.helpers.FileUploadHelper.uploadPhoto;
import static com.telerikacademy.web.photocontest.helpers.FilterAndSortingHelper.getResult;
@RestController
@RequestMapping("/api/contests")
@AllArgsConstructor
@Api(tags = "Contest Rest Controller")
@Slf4j
public class ContestRestController {

    private final AuthenticationHelper authenticationHelper;
    private final ContestServices contestServices;
    private final PhotoServices photoServices;
    private final ModelMapper modelMapper;

    @Operation(
            summary = "Get All Contests",
            description = "Get a list of all ContestResponseDto objects."
    )
    @ApiResponse(responseCode = "200", description = "All Contests",
            content = {@Content(schema = @Schema(implementation = ContestResponseDto.class, type = "array"),
                    mediaType = "application/json")})
    @GetMapping
    public List<ContestResponseDto> findAll() {
        Iterable<Contest> contests = contestServices.findAll();
        return StreamSupport.stream(contests.spliterator(), false)
                .map(modelMapper::objectToDto).toList();
    }
    @Operation(
            summary = "Retrieve a Contest by Id",
            description = "Get a Contest object by specifying its id. The response is ContestResponseDto object"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contest found",
                    content = {@Content(schema = @Schema(implementation = ContestResponseDto.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Contest not found",
                    content = {@Content(schema = @Schema())})
    })
    @GetMapping("/{id}")
    public ContestResponseDto findById(@PathVariable Long id) {
        try {
            Contest contest = contestServices.findById(id);
            return modelMapper.objectToDto(contest);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @Operation(
            summary = "Get Contests Search Result",
            description = "Get a list of all ContestResponseDto objects."
    )
    @ApiResponse(responseCode = "200", description = "All Contests",
            content = {@Content(schema = @Schema(implementation = ContestResponseDto.class, type = "array"),
                    mediaType = "application/json")})
    @Parameters({
            @Parameter(name = "title", example = "Random Title", description = "Filter by title", schema = @Schema(type = "string")),
            @Parameter(name = "category", example = "nature", description = "Filter by category", schema = @Schema(type = "string")),
            @Parameter(name = "type", example = "open", description = "Type is either open or invitational", schema = @Schema(type = "string")),
            @Parameter(name = "phase", example = "finished", description = "Contest phase can be: phase1, phase2, finished", schema = @Schema(type = "string"))
    })
    @GetMapping("/filter")
    public List<ContestResponseDto> filter(@RequestParam(required = false) Map<String, String> parameters, Pageable page) {
        if (parameters.isEmpty()) return new ArrayList<>();
        FilterAndSortingHelper.Result result = getResult(parameters, page);
        Page<Contest> contests = contestServices.filter(result.title(), result.categoryName(), result.isInvitational(), result.isFinished(), result.phase(), result.now(), result.pageable());
        return contests.stream().map(modelMapper::objectToDto).toList();
    }

    @Operation(
            summary = "Create a new contest",
            description = "Create a ContestResponseDto object"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contest created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContestResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Contest not found",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "Unauthorized operation",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "409", description = "A contest with the same title already exists",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "The validation of the body has failed",
                    content = {@Content(schema = @Schema())})
    })
    @PostMapping
    public ContestResponseDto create(@RequestHeader(required = false) Optional<String> authorization,
                                     @Validated(CreateValidationGroup.class) @RequestBody ContestDto contestDto) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(authorization);
            Contest contest = modelMapper.dtoToObject(contestDto);
            Contest updatedContest = contestServices.create(contest, authenticatedUser, contestDto.getCoverPhotoUpload());
            return modelMapper.objectToDto(updatedContest);
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (DateTimeException | FileUploadException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    @Operation(
            summary = "Join a contest",
            description = "Join a contest and return a ContestResponseDto object with new participant"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contest joined",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContestResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Contest not found",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "Unauthorized operation",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "409", description = "User is already participant",
                    content = {@Content(schema = @Schema())})
    })
    @PostMapping("/{id}/join")
    public ContestResponseDto joinContest(@PathVariable Long id, @RequestHeader(required = false) Optional<String> authorization) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(authorization);
            Contest contest = contestServices.findById(id);
            Contest updatedContest = contestServices.join(contest, authenticatedUser);
            return modelMapper.objectToDto(updatedContest);
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
    @Operation(
            summary = "Add jury to contest",
            description = "Add jury and return a ContestResponseDto object with new jury"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Jury joined to contest",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContestResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Contest not found",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "Unauthorized operation",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "409", description = "User is already jury",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "The validation of the body has failed",
                    content = {@Content(schema = @Schema())})
    })
    @PostMapping("/{id}/add-jury")
    public ContestResponseDto addJury(@PathVariable Long id, @RequestHeader(required = false) Optional<String> authorization,
                                         @Validated(EnlistUserValidationGroup.class) @RequestBody UserDto userDto) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(authorization);
            Contest contest = contestServices.findById(id);
            Contest updatedContest = contestServices.addJury(contest, authenticatedUser, userDto.getUsername());
            return modelMapper.objectToDto(updatedContest);
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
    @Operation(
            summary = "Add Participant",
            description = "Add a participant to the contest."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User added to participants",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContestResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Contest/User not found",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "Unauthorized operation",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "409", description = "User is already participant",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "The validation of the body has failed",
                    content = {@Content(schema = @Schema())})
    })
    @PostMapping("/{id}/add-participant")
    public ContestResponseDto addParticipant(@PathVariable Long id, @RequestHeader(required = false) Optional<String> authorization,
                                         @Validated(EnlistUserValidationGroup.class) @RequestBody UserDto userDto) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(authorization);
            Contest contest = contestServices.findById(id);
            Contest updatedContest = contestServices.addParticipant(contest, authenticatedUser, userDto.getUsername());
            return modelMapper.objectToDto(updatedContest);
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
    @Operation(
            summary = "Upload of cover photo",
            description = "Upload of cover photo to a Contest"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cover photo uploaded",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContestResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Contest not found",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "Unauthorized operation",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "The validation of the body has failed",
                    content = {@Content(schema = @Schema())})
    })
    @Parameters({
            @Parameter(name = "file", example = "photo.jpg", description = "Cover photo file", schema = @Schema(type = "string")),
    })
    @PostMapping("/{id}/upload-cover")
    public ContestResponseDto uploadCoverPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file,
                                               @RequestHeader(required = false) Optional<String> authorization) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(authorization);
            Contest contest = contestServices.findById(id);
            if (contest.getCoverPhoto() != null) deletePhoto(contest.getCoverPhoto());
            contest.setCoverPhoto(uploadPhoto(file));
            Contest updatedContest = contestServices.uploadCover(contest, authenticatedUser);
            return modelMapper.objectToDto(updatedContest);
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (FileUploadException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    @Operation(
            summary = "Update a Contest by Id",
            description = "Update a Contest object by specifying its id. The response is the updated Contest object"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contest updated",
                    content = {@Content(schema = @Schema(implementation = ContestResponseDto.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Contest not found",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "409", description = "Contest with same title already exists",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "Unauthorized operation",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "The validation of the body has failed",
                    content = {@Content(schema = @Schema())})
    })
    @PutMapping("/{id}")
    public ContestResponseDto update(@PathVariable Long id, @RequestHeader(required = false) Optional<String> authorization,
                                     @Validated(UpdateValidationGroup.class) @RequestBody ContestDto contestDto) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(authorization);
            Contest contest = modelMapper.dtoToObject(id, contestDto);
            Contest updatedContest = contestServices.update(contest, authenticatedUser, contestDto.getCoverPhotoUpload());
            return modelMapper.objectToDto(updatedContest);
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (DateTimeException | FileUploadException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    @Operation(
            summary = "Delete a Contest by Id",
            description = "Delete a Contest object by specifying its id."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contest successfully deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized operation"),
            @ApiResponse(responseCode = "404", description = "Contest not found")
    })
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, @RequestHeader(required = false) Optional<String> authorization) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(authorization);
            contestServices.deleteById(id, authenticatedUser);
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @Operation(
            summary = "Get All Photo of Contest",
            description = "Get a list of all PhotoResponseDto objects to a contest."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "All contest photos",
                    content = {@Content(schema = @Schema(implementation = PhotoResponseDto.class, type = "array"),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Contest not found",
                    content = {@Content(schema = @Schema())})
    })
    @GetMapping("/{id}/photos")
    public List<PhotoResponseDto> getAllPhotosOfContest(@PathVariable Long id) {
        try {
            Contest contest = contestServices.findById(id);
            return photoServices.getPhotosOfContest(contest).stream()
                    .map(modelMapper::objectToDto).collect(Collectors.toList());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @GetMapping("/{id}/photos/search")
    public List<PhotoResponseDto> search(@PathVariable Long id,
                                         @RequestParam Map<String, String> parameters,
                                         Pageable pageable) {
        FilterAndSortingHelper.Result result = getResult(parameters, pageable);
        return photoServices.search(result.title(), id, result.pageable())
                .stream()
                .map(modelMapper::objectToDto)
                .collect(Collectors.toList());
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
    @PostMapping("/{id}/photos")
    public PhotoResponseDto createPhoto(@PathVariable Long id,
                                        @RequestHeader(required = false) Optional<String> authorization,
                                        @Validated(CreatePhotoViaContestGroup.class)
                                        @ModelAttribute PhotoDto photoDto) {
        try {
            User user = authenticationHelper.tryGetUser(authorization);
            Photo photo = modelMapper.dtoToObject(photoDto);
            photo.setPostedOn(contestServices.findById(id));
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
}
