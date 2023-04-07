package com.telerikacademy.web.photocontest.controllers.rest;

import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.services.contracts.ContestServices;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/contests")
@AllArgsConstructor
public class ContestRestController {

    private final ContestServices contestServices;

    @GetMapping
    public List<Contest> get() {
        return contestServices.get();
    }
}
