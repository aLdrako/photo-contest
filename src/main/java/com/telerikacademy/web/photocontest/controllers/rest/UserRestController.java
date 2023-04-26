package com.telerikacademy.web.photocontest.controllers.rest;

import com.telerikacademy.web.photocontest.exceptions.AuthorizationException;
import com.telerikacademy.web.photocontest.exceptions.EntityDuplicateException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.models.dto.PermissionsDto;
import com.telerikacademy.web.photocontest.models.dto.UserDto;
import com.telerikacademy.web.photocontest.models.dto.UserResponseDto;
import com.telerikacademy.web.photocontest.models.validations.CreateValidationGroup;
import com.telerikacademy.web.photocontest.models.validations.EnlistUserValidationGroup;
import com.telerikacademy.web.photocontest.models.validations.UpdateValidationGroup;
import com.telerikacademy.web.photocontest.services.ModelMapper;
import com.telerikacademy.web.photocontest.services.contracts.EmailServices;
import com.telerikacademy.web.photocontest.services.contracts.UserServices;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserRestController {

    private final UserServices userServices;
    private final AuthenticationHelper authenticationHelper;
    private final ModelMapper modelMapper;

    @GetMapping
    public List<UserResponseDto> getAll() {
        return userServices.getAll().stream().map(modelMapper::objectToResponseDto)
                .collect(Collectors.toList());
    }
    @GetMapping("/search")
    public List<UserResponseDto> search(Pageable page, @RequestParam(required = false) Optional<String> q) {
        return userServices.search(q, page).stream().map(modelMapper::objectToResponseDto)
                .collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    public UserResponseDto getById(@PathVariable Long id) {
        try {
            return modelMapper.objectToResponseDto(userServices.getById(id));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public UserResponseDto createUser(@Validated(CreateValidationGroup.class) @RequestBody UserDto userDto) {
        try {
            User user = modelMapper.dtoToObject(userDto);
            userServices.create(user);
            return modelMapper.objectToResponseDto(user);
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public UserResponseDto update(@PathVariable Long id,
                       @RequestHeader(required = false) Optional<String> authorization,
                       @Validated(UpdateValidationGroup.class) @RequestBody UserDto userDto) {
        try {
            User userFromAuthorization = authenticationHelper.tryGetUser(authorization);
            User user = modelMapper.dtoToObject(id, userDto);
            userServices.update(user, userFromAuthorization);
            return modelMapper.objectToResponseDto(user);
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id,
                       @RequestHeader(required = false) Optional<String> authorization) {
        try {
            User userFromAuthorization = authenticationHelper.tryGetUser(authorization);
            userServices.delete(id, userFromAuthorization);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    @PutMapping("/{id}/permissions")
    public UserResponseDto updatePermissions(@PathVariable Long id, @Valid @RequestBody PermissionsDto permissionsDto,
                                  @RequestHeader(required = false) Optional<String> authorization) {
        try {
            User userFromAuthorization = authenticationHelper.tryGetUser(authorization);
            User userFromRepo = userServices.getById(id);
            userServices.updatePermissions(userFromRepo, userFromAuthorization, permissionsDto);
            return modelMapper.objectToResponseDto(userFromRepo);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
