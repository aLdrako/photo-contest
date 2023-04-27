package com.telerikacademy.web.photocontest.controllers.mvc;

import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.models.dto.ContestResponseDto;
import com.telerikacademy.web.photocontest.repositories.contracts.ContestRepository;
import com.telerikacademy.web.photocontest.services.ModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class HomeMvcController extends BaseMvcController {

    private final ContestRepository contestRepository;
    private final ModelMapper modelMapper;

    @ModelAttribute("contests")
    public List<ContestResponseDto> contestTop() {
        List<Contest> top5ByIsFinishedTrue = contestRepository.findTop5ByIsFinishedTrueOrderByPhase2Desc();
        return top5ByIsFinishedTrue.stream().map(modelMapper::objectToDto).toList();
    }

    @GetMapping
    public String showHomePage() {
        return "index";
    }
}
