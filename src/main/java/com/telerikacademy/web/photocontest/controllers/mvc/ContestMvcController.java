package com.telerikacademy.web.photocontest.controllers.mvc;

import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.services.ModelMapper;
import com.telerikacademy.web.photocontest.services.contracts.ContestServices;
import com.telerikacademy.web.photocontest.services.contracts.PhotoServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contests")
@AllArgsConstructor
public class ContestMvcController extends BaseMvcController {

    private final AuthenticationHelper authenticationHelper;
    private final ContestServices contestServices;
    private final PhotoServices photoServices;
    private final ModelMapper modelMapper;

    @GetMapping
    public String showAllContests() {
        return "ContestsView";
    }
}
