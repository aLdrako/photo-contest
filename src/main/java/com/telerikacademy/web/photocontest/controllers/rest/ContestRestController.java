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
@Slf4j
public class ContestRestController {

    private final AuthenticationHelper authenticationHelper;
    private final ContestServices contestServices;
    private final PhotoServices photoServices;
    private final ModelMapper modelMapper;

    @GetMapping
    public Iterable<ContestResponseDto> findAll() {
        Iterable<Contest> contests = contestServices.findAll();
        return StreamSupport.stream(contests.spliterator(), false)
                .map(modelMapper::objectToDto).toList();
    }

    @GetMapping("/{id}")
    public ContestResponseDto findById(@PathVariable Long id) {
        try {
            Contest contest = contestServices.findById(id);
            return modelMapper.objectToDto(contest);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/filter")
    public List<ContestResponseDto> filter(@RequestParam Map<String, String> parameters, Pageable page) {
        if (parameters.isEmpty()) return new ArrayList<>();
        FilterAndSortingHelper.Result result = getResult(parameters, page);
        Page<Contest> contests = contestServices.filter(result.title(), result.categoryName(), result.isInvitational(), result.isFinished(), result.phase1(), result.phase2(), result.pageable());
        return contests.stream().map(modelMapper::objectToDto).toList();
    }

    @PostMapping
    public ContestResponseDto create(@RequestHeader(required = false) Optional<String> authorization,
                                     @Validated(CreateValidationGroup.class) @RequestBody ContestDto contestDto) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(authorization);
            Contest contest = modelMapper.dtoToObject(contestDto);
            Contest updatedContest = contestServices.create(contest, authenticatedUser);
            return modelMapper.objectToDto(updatedContest);
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (DateTimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

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

    @PostMapping({
            "/{id}/add-jury",
            "/{id}/add-participant"
    })
    public ContestResponseDto enlistUser(@PathVariable Long id, @RequestHeader(required = false) Optional<String> authorization,
                                         @Validated(EnlistUserValidationGroup.class) @RequestBody UserDto userDto, HttpServletRequest request) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(authorization);
            Contest contest = contestServices.findById(id);
            String endpoint = request.getRequestURI();
            Contest updatedContest;
            if (endpoint.endsWith("/add-jury")) {
                updatedContest = contestServices.addJury(contest, authenticatedUser, userDto.getUsername());
            } else if (endpoint.endsWith("/add-participant")) {
                updatedContest = contestServices.addParticipant(contest, authenticatedUser, userDto.getUsername());
            } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid endpoint");
            return modelMapper.objectToDto(updatedContest);
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

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

    @PutMapping("/{id}")
    public ContestResponseDto update(@PathVariable Long id, @RequestHeader(required = false) Optional<String> authorization,
                                     @Validated(UpdateValidationGroup.class) @RequestBody ContestDto contestDto) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(authorization);
            Contest contest = modelMapper.dtoToObject(id, contestDto);
            Contest updatedContest = contestServices.update(contest, authenticatedUser);
            return modelMapper.objectToDto(updatedContest);
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (DateTimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

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
                                         @RequestParam(required = false) Optional<String> q) {
        return photoServices.search(q, id.describeConstable()).stream()
                .map(modelMapper::objectToDto).collect(Collectors.toList());
    }
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
