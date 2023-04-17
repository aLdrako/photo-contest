package com.telerikacademy.web.photocontest.controllers.mvc;

import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.services.ModelMapper;
import com.telerikacademy.web.photocontest.services.contracts.ContestServices;
import com.telerikacademy.web.photocontest.services.contracts.PhotoServices;
import com.telerikacademy.web.photocontest.services.contracts.UserServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
@AllArgsConstructor
public class UserMvcController extends BaseMvcController{
    private final AuthenticationHelper authenticationHelper;
    private final UserServices userServices;
    private final ModelMapper modelMapper;

    @GetMapping
    private String showAllUsers(Model model) {
        model.addAttribute("users", userServices.getAll());
        return "UsersView";
    }
    @GetMapping("/{id}")
    private String showUser(@PathVariable Long id, Model model) {
        try {
            User user = userServices.getById(id);
            model.addAttribute("user", user);
            return "UserView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }
}
