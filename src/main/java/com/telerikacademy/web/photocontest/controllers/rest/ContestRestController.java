package com.telerikacademy.web.photocontest.controllers.rest;

import com.telerikacademy.web.photocontest.exceptions.AuthorizationException;
import com.telerikacademy.web.photocontest.exceptions.EntityDuplicateException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.helpers.FilterAndSortingHelper;
import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.models.dto.ContestDto;
import com.telerikacademy.web.photocontest.models.dto.ContestResponseDto;
import com.telerikacademy.web.photocontest.models.validations.CreateValidationGroup;
import com.telerikacademy.web.photocontest.models.validations.UpdateValidationGroup;
import com.telerikacademy.web.photocontest.services.ModelMapper;
import com.telerikacademy.web.photocontest.services.contracts.ContestServices;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.DateTimeException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.telerikacademy.web.photocontest.helpers.FilterAndSortingHelper.getResult;

@RestController
@RequestMapping("/api/contests")
@AllArgsConstructor
@Slf4j
public class ContestRestController {

    private final AuthenticationHelper authenticationHelper;
    private final ContestServices contestServices;
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
    public List<ContestResponseDto> filter(@RequestParam Map<String, String> parameter) {
        FilterAndSortingHelper.Result result = getResult(parameter);
        List<Contest> contests = contestServices.filter(result.title(), result.categoryName(), result.isInvitational());
        return contests.stream().map(modelMapper::objectToDto).toList();
    }

    @PostMapping
    public ContestResponseDto create(@RequestHeader(required = false) Optional<String> authorization, @Validated(CreateValidationGroup.class) @RequestBody ContestDto contestDto) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(authorization);
            Contest contest = modelMapper.dtoToObject(contestDto);
            Contest savedContest = contestServices.save(contest, authenticatedUser);
            return modelMapper.objectToDto(savedContest);
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (DateTimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ContestResponseDto update(@RequestHeader(required = false) Optional<String> authorization, @PathVariable Long id, @Validated(UpdateValidationGroup.class) @RequestBody ContestDto contestDto) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(authorization);
            Contest contest = modelMapper.dtoToObject(id, contestDto);
            Contest updatedContest = contestServices.save(contest, authenticatedUser);
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
}
