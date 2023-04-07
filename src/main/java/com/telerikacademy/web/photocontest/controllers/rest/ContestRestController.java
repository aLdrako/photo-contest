package com.telerikacademy.web.photocontest.controllers.rest;

import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.models.dto.ContestDto;
import com.telerikacademy.web.photocontest.models.validations.CreateValidationGroup;
import com.telerikacademy.web.photocontest.models.validations.UpdateValidationGroup;
import com.telerikacademy.web.photocontest.services.ModelMapper;
import com.telerikacademy.web.photocontest.services.contracts.ContestServices;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
    public Optional<Contest> findById(@PathVariable Long id) {
        return contestServices.findById(id);
    }

    @PostMapping
    public Contest create(@Validated(CreateValidationGroup.class) @RequestBody ContestDto contestDto) {
        Contest contest = modelMapper.dtoToObject(contestDto);
        return contestServices.save(contest);
    }

    @PutMapping("/{id}")
    public Contest update(@PathVariable Long id, @Validated(UpdateValidationGroup.class) @RequestBody ContestDto contestDto) {
        Contest contest = modelMapper.dtoToObject(id, contestDto);
        return contestServices.save(contest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        contestServices.deleteById(id);
    }
}
