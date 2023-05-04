package com.telerikacademy.web.photocontest.controllers.mvc;

import com.telerikacademy.web.photocontest.exceptions.AuthorizationException;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.helpers.FilterAndSortingHelper;
import com.telerikacademy.web.photocontest.models.Photo;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.services.contracts.PhotoServices;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.telerikacademy.web.photocontest.helpers.FilterAndSortingHelper.getResult;

@Controller
@RequestMapping("/photos")
@AllArgsConstructor
public class PhotoMvcController extends BaseMvcController{
    private final PhotoServices photoServices;
    private final AuthenticationHelper authenticationHelper;


    @GetMapping
    public String showAllPhotos(@PageableDefault(size = 9, sort = "id") Pageable pageable,
                                @RequestParam Map<String, String> parameters, HttpSession session,
                                Model model) {
        try {
            authenticationHelper.tryGetOrganizer(session);
            FilterAndSortingHelper.Result result = getResult(parameters, pageable);
            Page<Photo> photoPage = photoServices.search(result.title(), null, null,
                    result.pageable());
            populateModel(model, result, photoPage);

            return "PhotosView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }
    @GetMapping("/myphotos")
    public String showMyPhotos(@PageableDefault(size = 9, sort = "id") Pageable pageable,
                                @RequestParam Map<String, String> parameters, HttpSession session,
                                Model model) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            FilterAndSortingHelper.Result result = getResult(parameters, pageable);
            Page<Photo> photoPage = photoServices.search(result.title(), null, user.getId(),
                    result.pageable());
            populateModel(model, result, photoPage);
            
            return "MyPhotosView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    private void populateModel(Model model, FilterAndSortingHelper.Result result, Page<Photo> photoPage) {
        model.addAttribute("photos", photoPage.getContent());
        model.addAttribute("sizePage", result.pageable().getPageSize());
        model.addAttribute("currentPage", result.pageable().getPageNumber());
        model.addAttribute("title", result.title());
        model.addAttribute("totalPages", photoPage.getTotalPages());
    }
}
