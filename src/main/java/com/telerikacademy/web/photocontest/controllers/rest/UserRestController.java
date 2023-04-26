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
import com.telerikacademy.web.photocontest.models.validations.UpdateValidationGroup;
import com.telerikacademy.web.photocontest.services.ModelMapper;
import com.telerikacademy.web.photocontest.services.contracts.UserServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Tag(name = "User Rest Controller", description = "User management API")
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserRestController {

    private final UserServices userServices;
    private final AuthenticationHelper authenticationHelper;
    private final ModelMapper modelMapper;

    @Operation(
            summary = "Get All Users",
            description = "Get a list of all UserResponseDto objects.",
            tags = {"users", "get all"}
    )
    @ApiResponse(responseCode = "200", description = "All users",
            content = {@Content(schema = @Schema(implementation = User.class, type = "array"),
                    mediaType = "application/json")})
    @GetMapping
    public List<UserResponseDto> getAll() {
        return userServices.getAll().stream().map(modelMapper::objectToResponseDto)
                .collect(Collectors.toList());
    }
    @Operation(
            summary = "Get Users Search Result",
            description = "If there is a 'q' parameter the api will search all users by first name, " +
                    "last name or username and return a list of UserResponseDto objects." +
                    "If there is no 'q' parameter the api will return all users.",
            tags = {"users", "search"}
    )
    @ApiResponse(responseCode = "200", description = "Search Result",
            content = {@Content(schema = @Schema(implementation = User.class, type = "array"),
                    mediaType = "application/json")})
    @GetMapping("/search")
    public List<UserResponseDto> search(Pageable page, @RequestParam(required = false) String q) {
        return userServices.searchAll(q != null && q.isEmpty() ? null : q, page)
                .stream().map(modelMapper::objectToResponseDto)
                .collect(Collectors.toList());
    }
    @Operation(
            summary = "Retrieve a User by Id",
            description = "Get a User object by specifying its id. The response is User object",
            tags = {"user", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found",
                    content = {@Content(schema = @Schema(implementation = User.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = {@Content(schema = @Schema())})
    })
    @GetMapping("/{id}")
    public UserResponseDto getById(@PathVariable Long id) {
        try {
            return modelMapper.objectToResponseDto(userServices.getById(id));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @Operation(
            summary = "Create a new user",
            description = "Create a User object",
            tags = {"users", "create"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "409", description = "User with same email/username already exists",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "The validation of the body has failed",
                    content = {@Content(schema = @Schema())})
    })
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
    @Operation(
            summary = "Update a User by Id",
            description = "Update a User object by specifying its id. The response is the updated User object",
            tags = {"users", "update"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated", content = {@Content(schema = @Schema(implementation = User.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "User not found", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "409", description = "User with same email/username already exists", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "Unauthorized operation", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "The validation of the body has failed", content = {@Content(schema = @Schema())})
    })
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
    @Operation(
            summary = "Delete a User by Id",
            description = "Delete a User object by specifying its id.",
            tags = {"users", "delete"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User successfully deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized operation"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
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

    @Operation(
            summary = "Update permissions for a User by Id",
            description = "Update permissions for a User object by specifying its id. The response is the updated User object",
            tags = {"users", "update"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User permissions updated", content = {@Content(schema = @Schema(implementation = User.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "User not found", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "Unauthorized operation", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "The validation of the body has failed", content = {@Content(schema = @Schema())})
    })
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
