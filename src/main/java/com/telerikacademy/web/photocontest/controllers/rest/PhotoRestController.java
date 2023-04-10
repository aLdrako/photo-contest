package com.telerikacademy.web.photocontest.controllers.rest;

import com.telerikacademy.web.photocontest.services.contracts.ContestServices;
import com.telerikacademy.web.photocontest.services.contracts.UserServices;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/photos")
@AllArgsConstructor
public class PhotoRestController {
    private final UserServices userServices;
    private final ContestServices contestServices;


}
