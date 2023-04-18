package com.telerikacademy.web.photocontest.controllers.mvc;

import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.helpers.FilterAndSortingHelper;
import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.models.dto.ContestResponseDto;
import com.telerikacademy.web.photocontest.services.ModelMapper;
import com.telerikacademy.web.photocontest.services.contracts.ContestServices;
import com.telerikacademy.web.photocontest.services.contracts.PhotoServices;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

import static com.telerikacademy.web.photocontest.helpers.FilterAndSortingHelper.getResult;

@Controller
@RequestMapping("/contests")
@AllArgsConstructor
@Log4j2
public class ContestMvcController extends BaseMvcController {

    private final AuthenticationHelper authenticationHelper;
    private final ContestServices contestServices;
    private final PhotoServices photoServices;
    private final ModelMapper modelMapper;

    @GetMapping
    public String showAllContests(@PageableDefault(size = 9, sort = "id") Pageable pageable, @RequestParam(required = false) Map<String, String> parameters, Model model, HttpServletRequest request, HttpSession session) {
        FilterAndSortingHelper.Result result = getResult(parameters, pageable);
        Page<Contest> contestPage = contestServices.filter(result.title(), result.categoryName(), result.isInvitational(), result.isFinished(), result.phase(), result.now(), result.pageable());
        List<ContestResponseDto> list = contestPage.stream().map(modelMapper::objectToDto).toList();
        Page<ContestResponseDto> page = new PageImpl<>(list, contestPage.getPageable(), contestPage.getTotalElements());

        String sortParams = pageable.getSort().toString().replace(": ASC", "");
        WebUtils.setSessionAttribute(request, "filterParams", parameters);
        WebUtils.setSessionAttribute(request, "sortParams", sortParams);

        model.addAttribute("contests", page);
        return "ContestsView";
    }

}