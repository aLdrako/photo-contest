package com.telerikacademy.web.photocontest.controllers.rest;

import com.telerikacademy.web.photocontest.exceptions.AuthorizationException;
import com.telerikacademy.web.photocontest.exceptions.EntityDuplicateException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.models.Category;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.models.dto.CategoryDto;
import com.telerikacademy.web.photocontest.services.ModelMapper;
import com.telerikacademy.web.photocontest.services.contracts.CategoryServices;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
@AllArgsConstructor
public class CategoryRestController {

    private final AuthenticationHelper authenticationHelper;
    private final CategoryServices categoryServices;
    private final ModelMapper modelMapper;

    @GetMapping
    public Iterable<Category> findAll() {
        return categoryServices.findAll();
    }

    @GetMapping("/{id}")
    public Category findById(@PathVariable Long id) {
        try {
            return categoryServices.findById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

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
