package com.telerikacademy.web.photocontest.controllers.rest;

import com.telerikacademy.web.photocontest.exceptions.AuthorizationException;
import com.telerikacademy.web.photocontest.exceptions.EntityDuplicateException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.models.Category;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.models.dto.CategoryDto;
import com.telerikacademy.web.photocontest.models.dto.ContestResponseDto;
import com.telerikacademy.web.photocontest.models.dto.UserResponseDto;
import com.telerikacademy.web.photocontest.services.ModelMapper;
import com.telerikacademy.web.photocontest.services.contracts.CategoryServices;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/categories")
@Api(tags = "Category Rest Controller")
@AllArgsConstructor
public class CategoryRestController {

    private final AuthenticationHelper authenticationHelper;
    private final CategoryServices categoryServices;
    private final ModelMapper modelMapper;

    @Operation(
            summary = "Get All Categories",
            description = "Get a list of all Category objects."
    )
    @ApiResponse(responseCode = "200", description = "All categories",
            content = {@Content(schema = @Schema(implementation = Category.class, type = "array"),
                    mediaType = "application/json")})
    @GetMapping
    public List<Category> findAll() {
        Iterable<Category> iterable = categoryServices.findAll();
        return StreamSupport.stream(iterable.spliterator(), false).toList();
    }
    @Operation(
            summary = "Retrieve a Category by Id",
            description = "Get a Category object by specifying its id. The response is Category object"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category found",
                    content = {@Content(schema = @Schema(implementation = Category.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = {@Content(schema = @Schema())})
    })
    @GetMapping("/{id}")
    public Category findById(@PathVariable Long id) {
        try {
            return categoryServices.findById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @Operation(
            summary = "Create a new Category",
            description = "Create a Category object"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized operation",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "409", description = "Category already exists",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "The validation of the body has failed",
                    content = {@Content(schema = @Schema())})
    })
    @PostMapping
    public Category create(@RequestHeader(required = false) Optional<String> authorization, @Valid @RequestBody CategoryDto categoryDto) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(authorization);
            Category category = modelMapper.dtoToObject(categoryDto);
            return categoryServices.save(category, authenticatedUser);
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }  catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
    @Operation(
            summary = "Update a Category by Id",
            description = "Update a Category object by specifying its id. The response is the updated Category object"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category updated",
                    content = {@Content(schema = @Schema(implementation = Category.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "409", description = "Category with same name already exists",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "Unauthorized operation",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "The validation of the body has failed",
                    content = {@Content(schema = @Schema())})
    })
    @PutMapping("/{id}")
    public Category update(@PathVariable Long id, @RequestHeader(required = false) Optional<String> authorization, @Valid @RequestBody CategoryDto categoryDto) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(authorization);
            Category category = modelMapper.dtoToObject(id, categoryDto);
            return categoryServices.save(category, authenticatedUser);
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
    @Operation(
            summary = "Delete a Category by Id",
            description = "Delete a Category object by specifying its id."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category successfully deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized operation"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, @RequestHeader(required = false) Optional<String> authorization) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(authorization);
            categoryServices.deleteById(id, authenticatedUser);
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
