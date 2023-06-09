package com.telerikacademy.web.photocontest.controllers.mvc;

import com.telerikacademy.web.photocontest.exceptions.AuthorizationException;
import com.telerikacademy.web.photocontest.exceptions.EntityDuplicateException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.helpers.FilterAndSortingHelper;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.models.dto.PermissionsDto;
import com.telerikacademy.web.photocontest.models.dto.UserDto;
import com.telerikacademy.web.photocontest.models.validations.EmailUpdateValidation;
import com.telerikacademy.web.photocontest.models.validations.UpdateValidationGroup;
import com.telerikacademy.web.photocontest.services.ModelMapper;
import com.telerikacademy.web.photocontest.services.contracts.EmailServices;
import com.telerikacademy.web.photocontest.services.contracts.UserServices;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

import static com.telerikacademy.web.photocontest.helpers.FilterAndSortingHelper.getResult;

@Controller
@RequestMapping("/users")
@AllArgsConstructor
public class UserMvcController extends BaseMvcController{
    private final AuthenticationHelper authenticationHelper;
    private final UserServices userServices;
    private final ModelMapper modelMapper;
    private final EmailServices emailServices;

    @GetMapping
    public String showAllUsers(@PageableDefault(size = 9, sort = "id") Pageable pageable,
                                @RequestParam(required = false) Map<String, String> parameters,
                                Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetOrganizer(session);
            FilterAndSortingHelper.Result result = getResult(parameters, pageable);
            Page<User> userPage = userServices.searchAll(result.keyword(), result.pageable());
            model.addAttribute("users", userPage.getContent());
            model.addAttribute("q", result.keyword());
            model.addAttribute("currentPage", result.pageable().getPageNumber());
            model.addAttribute("sizePage", result.pageable().getPageSize());
            model.addAttribute("totalPages", userPage.getTotalPages());
            return "UsersView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }
    @GetMapping("/photojunkies")
    public String showAllPhotoJunkies(@PageableDefault(size = 9, sort = "id") Pageable pageable,
                                       @RequestParam(required = false) Map<String, String> parameters,
                                       Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetOrganizer(session);
            FilterAndSortingHelper.Result result = getResult(parameters, pageable);
            Page<User> userPage = userServices.searchPhotoJunkies(result.keyword(), result.pageable());
            String sort = result.pageable().getSort().toString().contains("id") ? "id" : "rank";
            String order = result.pageable().getSort().toString().contains("ASC") ? "asc" : "desc";

            model.addAttribute("users", userPage.getContent());
            model.addAttribute("currentPage", result.pageable().getPageNumber());
            model.addAttribute("q", result.keyword());
            model.addAttribute("sort", sort);
            model.addAttribute("order", order);
            model.addAttribute("sizePage", result.pageable().getPageSize());
            model.addAttribute("totalPages", userPage.getTotalPages());
            return "PhotoJunkiesView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }
    @GetMapping("/{id}")
    public String showUser(@PathVariable Long id, Model model, HttpSession session) {
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
                "/{id}/permissions",
                "/{id}/email"})
    public String updateUser(@PathVariable Long id, Model model, HttpSession session) {
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
    @PostMapping("/{id}/email")
    public String updateEmail(@PathVariable Long id, Model model,
                              @Validated(EmailUpdateValidation.class) @ModelAttribute("user") UserDto userDto,
                              BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) return "UserUpdateView";

        try {
            User userFromSession = authenticationHelper.tryGetUser(session);
            User user = modelMapper.dtoToObject(id, userDto);
            user.setDeleted(true);
            userServices.update(user, userFromSession);
            emailServices.sendConfirmationEmail(user);
            session.invalidate();
            return "redirect:/";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (MessagingException e) {
            bindingResult.rejectValue("email", "email_bad_request", e.getMessage());
            return "UserUpdateView";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("email", "email_duplicated", e.getMessage());
            return "UserUpdateView";
        }
    }

    @PostMapping({"/{id}/update",
            "/{id}/permissions"})
    public String updateUser(@PathVariable Long id, Model model,
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
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("email", "email_exists", e.getMessage());
            return "UserUpdateView";
        }
    }
    @GetMapping("{id}/delete")
    public String deleteUser(@PathVariable Long id, Model model, HttpSession session) {
        try {
            User currentUser = authenticationHelper.tryGetUser(session);
            userServices.delete(id, currentUser);
            session.invalidate();
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
