package com.telerikacademy.web.photocontest.controllers.mvc;

import com.telerikacademy.web.photocontest.exceptions.AuthorizationException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.models.dto.PermissionsDto;
import com.telerikacademy.web.photocontest.models.dto.UserDto;
import com.telerikacademy.web.photocontest.models.validations.UpdateValidationGroup;
import com.telerikacademy.web.photocontest.services.ModelMapper;
import com.telerikacademy.web.photocontest.services.contracts.UserServices;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
@AllArgsConstructor
public class UserMvcController extends BaseMvcController{
    private final AuthenticationHelper authenticationHelper;
    private final UserServices userServices;
    private final ModelMapper modelMapper;

    @GetMapping
    private String showAllUsers(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetOrganizer(session);
            Pageable pageable = PageRequest.of(page, size);
            Page<User> userPage = userServices.findAll(pageable);

            model.addAttribute("users", userPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("sizePage", size);
            model.addAttribute("totalPages", userPage.getTotalPages());
            return "UsersView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
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
    @GetMapping({"/{id}/update",
                "/{id}/permissions"})
    private String updateUser(@PathVariable Long id, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
            User user = userServices.getById(id);
            UserDto userDto = modelMapper.objectToDto(user);
            model.addAttribute("user", userDto);
            session.setAttribute("updatedUserIsOrganizer", user.isOrganizer());
            session.setAttribute("updatedUserUsername", user.getUsername());
            return "UserUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }
    /*@PostMapping("/{id}/permissions")
    private String updateUser(@PathVariable Long id, Model model,
                              BindingResult bindingResult, HttpSession session,
                              @ModelAttribute("user") UserDto userDto) {
        if (bindingResult.hasErrors()) return "UserUpdateView";
        try {
            User userFromSession = authenticationHelper.tryGetUser(session);
            User userToUpdate = userServices.getById(id);
            PermissionsDto permissionsDto = new PermissionsDto(userDto.isOrganizer());
            userServices.updatePermissions(userToUpdate, userFromSession, permissionsDto);
            return "redirect:/" + userToUpdate.getId();
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }*/
    @PostMapping({"/{id}/update",
            "/{id}/permissions"})
    private String updateUser(@PathVariable Long id, Model model,
                              @Validated(UpdateValidationGroup.class) @ModelAttribute("user") UserDto userDto,
                              BindingResult bindingResult, HttpSession session,
                              HttpServletRequest request) {
        if (bindingResult.hasGlobalErrors()) {
            bindingResult.rejectValue("passwordConfirm", "password_confirm",
                    bindingResult.getGlobalError().getDefaultMessage());
        }
        if (bindingResult.hasErrors()) return "UserUpdateView";
        try {
            User userFromSession = authenticationHelper.tryGetUser(session);
            if (!request.getRequestURI().contains("permissions")) {
                User user = modelMapper.dtoToObject(id, userDto);
                userServices.update(user, userFromSession);
            } else {
                User userToUpdate = userServices.getById(id);
                PermissionsDto permissionsDto = new PermissionsDto(userDto.isOrganizer());
                userServices.updatePermissions(userToUpdate, userFromSession, permissionsDto);
            }
            return "redirect:/users/" + id;
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
