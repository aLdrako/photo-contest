package com.telerikacademy.web.photocontest.controllers.mvc;

import com.telerikacademy.web.photocontest.exceptions.AuthorizationException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.models.dto.UserDto;
import com.telerikacademy.web.photocontest.models.validations.UpdateValidationGroup;
import com.telerikacademy.web.photocontest.services.ModelMapper;
import com.telerikacademy.web.photocontest.services.contracts.ContestServices;
import com.telerikacademy.web.photocontest.services.contracts.PhotoServices;
import com.telerikacademy.web.photocontest.services.contracts.UserServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
@AllArgsConstructor
public class UserMvcController extends BaseMvcController{
    private final AuthenticationHelper authenticationHelper;
    private final UserServices userServices;
    private final ModelMapper modelMapper;

    @GetMapping
    private String showAllUsers(Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetOrganizer(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }

        model.addAttribute("users", userServices.getAll());
        return "UsersView";
    }
    @GetMapping("/{id}")
    private String showUser(@PathVariable Long id, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
            User user = userServices.getById(id);
            model.addAttribute("user", user);
            return "UserView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }
    @GetMapping("/{id}/update")
    private String updateUser(@PathVariable Long id, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
            User user = userServices.getById(id);
            UserDto userDto = modelMapper.objectToDto(user);
            model.addAttribute("user", userDto);
            return "UserUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }
    @PostMapping("/{id}/update")
    private String updateUser(@PathVariable Long id, Model model,
                              @Validated(UpdateValidationGroup.class) @ModelAttribute("user") UserDto userDto,
                              BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasGlobalErrors()) {
            bindingResult.rejectValue("passwordConfirm", "password_confirm",
                    bindingResult.getGlobalError().getDefaultMessage());
        }
        if (bindingResult.hasErrors()) return "UserUpdateView";
        try {
            User userFromSession = authenticationHelper.tryGetUser(session);
            User user = modelMapper.dtoToObject(userDto);
            userServices.update(user, userFromSession);
            return "redirect:/" + user.getId();
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }
    @GetMapping("{id}/delete")
    public String deleteUser(@PathVariable Long id, Model model, HttpSession session) {
        try {
            User currentUser = authenticationHelper.tryGetUser(session);
            userServices.delete(id, currentUser);
            if (!currentUser.isOrganizer()) session.invalidate();
            return "redirect:/";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

}
