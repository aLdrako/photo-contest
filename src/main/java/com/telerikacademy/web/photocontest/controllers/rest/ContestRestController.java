package com.telerikacademy.web.photocontest.controllers.rest;

import com.telerikacademy.web.photocontest.exceptions.EntityDuplicateException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.helpers.FilterAndSortingHelper;
import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.models.dto.ContestDto;
import com.telerikacademy.web.photocontest.models.validations.CreateValidationGroup;
import com.telerikacademy.web.photocontest.models.validations.UpdateValidationGroup;
import com.telerikacademy.web.photocontest.services.ModelMapper;
import com.telerikacademy.web.photocontest.services.contracts.ContestServices;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

import static com.telerikacademy.web.photocontest.helpers.FilterAndSortingHelper.getResult;

@RestController
@RequestMapping("/api/contests")
@AllArgsConstructor
public class ContestRestController {

    private final ContestServices contestServices;
    private final ModelMapper modelMapper;

    @GetMapping
    public Iterable<Contest> findAll() {
        return contestServices.findAll();
    }

    @GetMapping("/{id}")
    public Contest findById(@PathVariable Long id) {
        return contestServices.findById(id);
    }

    @GetMapping("/filter")
    public List<Contest> filter(@RequestParam Map<String, String> parameter) {
        FilterAndSortingHelper.Result result = getResult(parameter);
        return contestServices.filter(result.title(), result.categoryName(), result.isInvitational());
    }

    @PostMapping
    public Contest create(@Validated(CreateValidationGroup.class) @RequestBody ContestDto contestDto) {
        try {
            Contest contest = modelMapper.dtoToObject(contestDto);
            return contestServices.save(contest);
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Contest update(@PathVariable Long id, @Validated(UpdateValidationGroup.class) @RequestBody ContestDto contestDto) {
        try {
            Contest contest = modelMapper.dtoToObject(id, contestDto);
            return contestServices.save(contest);
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        contestServices.deleteById(id);
    }
}
