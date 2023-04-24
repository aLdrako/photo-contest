package com.telerikacademy.web.photocontest.controllers.mvc;

import com.telerikacademy.web.photocontest.exceptions.AuthorizationException;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.models.Photo;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.services.contracts.PhotoServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/photos")
@AllArgsConstructor
public class PhotoMvcController extends BaseMvcController{
    private final PhotoServices photoServices;
    private final AuthenticationHelper authenticationHelper;


    @GetMapping
    public String showAllPhotos(@RequestParam(required = false) Optional<String> title, HttpSession session,
                                Model model) {
        try {
             List<Photo> photos = title.isPresent() ? photoServices.search(title, Optional.empty()) :
                    photoServices.getAll();
            authenticationHelper.tryGetUser(session);
            model.addAttribute("photos", photos);
            return "PhotosView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }
}
