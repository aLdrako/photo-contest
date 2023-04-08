package com.telerikacademy.web.photocontest.controllers.rest;

import com.telerikacademy.web.photocontest.exceptions.AuthorizationException;
import com.telerikacademy.web.photocontest.exceptions.EntityDuplicateException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.models.Ranking;
import com.telerikacademy.web.photocontest.models.Ranks;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.models.dto.UserDto;
import com.telerikacademy.web.photocontest.models.validations.CreateValidationGroup;
import com.telerikacademy.web.photocontest.models.validations.UpdateValidationGroup;
import com.telerikacademy.web.photocontest.services.ModelMapper;
import com.telerikacademy.web.photocontest.services.contracts.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserServices userServices;
    private final AuthenticationHelper authenticationHelper;
    private final ModelMapper modelMapper;
    @Autowired
    public UserRestController(UserServices userServices, AuthenticationHelper authenticationHelper,
                              ModelMapper modelMapper) {
        this.userServices = userServices;
        this.authenticationHelper = authenticationHelper;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<User> getAll() {
        return userServices.getAll();
    }
    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        try {
            return userServices.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public User createUser(@Validated(CreateValidationGroup.class) @RequestBody UserDto userDto) {
        User user = modelMapper.dtoToObject(userDto);
        try {
            userServices.create(user);
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
        return user;
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id,
                       @RequestHeader(required = false) Optional<String> authorization,
                       @Validated(UpdateValidationGroup.class) @RequestBody UserDto userDto) {
        try {
            User userFromAuthorization = authenticationHelper.tryGetUser(authorization);
            User user = modelMapper.dtoToObject(id, userDto);
            userServices.update(user, userFromAuthorization);
            return user;
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}
