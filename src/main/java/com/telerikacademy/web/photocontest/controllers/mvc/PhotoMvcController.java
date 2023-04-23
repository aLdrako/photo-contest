package com.telerikacademy.web.photocontest.controllers.mvc;

import com.telerikacademy.web.photocontest.exceptions.AuthorizationException;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.models.Photo;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.services.contracts.PhotoServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/photos")
@AllArgsConstructor
public class PhotoMvcController extends BaseMvcController{
    private final PhotoServices photoServices;
    private final AuthenticationHelper authenticationHelper;

    @ModelAttribute("photos")
    private List<Photo> getAllPhotos() {
        return photoServices.getAll();
    }

    @GetMapping
    public String showAllPhotos(HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
            return "PhotosView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }
}
