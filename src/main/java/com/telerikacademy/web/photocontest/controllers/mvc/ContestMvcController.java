package com.telerikacademy.web.photocontest.controllers.mvc;

import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.models.dto.ContestResponseDto;
import com.telerikacademy.web.photocontest.services.ModelMapper;
import com.telerikacademy.web.photocontest.services.contracts.ContestServices;
import com.telerikacademy.web.photocontest.services.contracts.PhotoServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping("/contests")
@AllArgsConstructor
public class ContestMvcController extends BaseMvcController {

    private final AuthenticationHelper authenticationHelper;
    private final ContestServices contestServices;
    private final PhotoServices photoServices;
    private final ModelMapper modelMapper;

    @GetMapping
    public String showAllContests(@RequestParam(required = false) Map<String, String> parameters, Model model, HttpSession session) {
        Iterable<Contest> contests = contestServices.findAll();
        List<ContestResponseDto> list = StreamSupport.stream(contests.spliterator(), false)
                .map(modelMapper::objectToDto).toList();
        model.addAttribute("contests", list);
        return "ContestsView";
    }
}
