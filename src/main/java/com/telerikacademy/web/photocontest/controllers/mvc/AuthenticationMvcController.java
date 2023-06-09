package com.telerikacademy.web.photocontest.controllers.mvc;

import com.telerikacademy.web.photocontest.exceptions.AuthorizationException;
import com.telerikacademy.web.photocontest.exceptions.EntityDuplicateException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.models.dto.UserDto;
import com.telerikacademy.web.photocontest.models.validations.ChangePasswordGroup;
import com.telerikacademy.web.photocontest.models.validations.CreateValidationGroup;
import com.telerikacademy.web.photocontest.models.validations.EnlistUserValidationGroup;
import com.telerikacademy.web.photocontest.models.validations.LoginValidationGroup;
import com.telerikacademy.web.photocontest.services.ModelMapper;
import com.telerikacademy.web.photocontest.services.contracts.EmailServices;
import com.telerikacademy.web.photocontest.services.contracts.UserServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

@Controller
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthenticationMvcController extends BaseMvcController {
    private final UserServices userServices;
    private final ModelMapper modelMapper;
    private final AuthenticationHelper authenticationHelper;
    private final EmailServices emailServices;

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new UserDto());
        return "LoginView";
    }

    @PostMapping("/login")
    public String handleLogin(@Validated(LoginValidationGroup.class) @ModelAttribute("user") UserDto userDto,
                              BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) return "LoginView";

        try {
            User user = authenticationHelper.verifyLogin(userDto.getUsername(), userDto.getPassword());
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("currentUser", user.getUsername());
            return "redirect:/";
        } catch (AuthorizationException e) {
            bindingResult.rejectValue("username", "auth_error", e.getMessage());
            return "LoginView";
        }
    }
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new UserDto());
        return "RegisterView";
    }
    @PostMapping("/register")
    public String handleRegister(@Validated(CreateValidationGroup.class) @ModelAttribute("user") UserDto userDTO,
                                 BindingResult bindingResult) {
        if (bindingResult.hasGlobalErrors()) {
            bindingResult.rejectValue("passwordConfirm", "password_confirm",
                    bindingResult.getGlobalError().getDefaultMessage());
        }
        if (bindingResult.hasErrors()) return "RegisterView";

        try {
            User user = modelMapper.dtoToObject(userDTO);
            user.setDeleted(true);
            userServices.create(user);
            emailServices.sendConfirmationEmail(user);
            return "EmailSentView";
        } catch (EntityDuplicateException e) {
            if (e.getErrorType().equals("username")) {
                bindingResult.rejectValue("username", "username_exists", e.getMessage());
            } else if (e.getErrorType().equals("email")) {
                bindingResult.rejectValue("email", "email_exists", e.getMessage());
            }
            return "RegisterView";
        } catch (MessagingException e) {
            bindingResult.rejectValue("email", "email_bad_request", e.getMessage());
            return "RegisterView";
        }
    }
    @GetMapping("/confirmation/{urlKey}")
    public String handleConfirmation(@PathVariable String urlKey, Model model) {
        if (emailServices.getUrlKeys().containsKey(urlKey)) {
            User user = emailServices.getUrlKeys().get(urlKey);
            user.setDeleted(false);
            userServices.update(user, user);
            emailServices.clearKey(urlKey);
            return "EmailSentView";
        } else {
            model.addAttribute("error", "Page Not Found");
            return "NotFoundView";
        }
    }
    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        if (session.getAttribute("currentUser") != null) {
            session.invalidate();
        }
        return "redirect:/auth/login";
    }
    @GetMapping("/forgottenpassword")
    public String showForgottenPasswordPage(Model model) {
        model.addAttribute("user", new UserDto());
        return "ForgottenPasswordView";
    }
    @PostMapping("/forgottenpassword")
    public String handleForgottenPassword(Model model, @Validated(EnlistUserValidationGroup.class)
                                            @ModelAttribute("user") UserDto userDTO,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "ForgottenPasswordView";
        try {
            User user = userServices.getByUsername(userDTO.getUsername());
            emailServices.sendForgottenPasswordEmail(user);
            return "EmailSentView";
        } catch (EntityNotFoundException | MessagingException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }
    @GetMapping("/password/{urlKey}")
    public String showChangePasswordPage(@PathVariable String urlKey, Model model) {
        try {
            if (emailServices.getUrlKeys().containsKey(urlKey)) {
                model.addAttribute("user", modelMapper
                        .objectToDto(emailServices.getUrlKeys().get(urlKey)));
                return "ChangePassword";
            }
            model.addAttribute("error", "Page Not Found");
            return "NotFoundView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }
    @PostMapping("/password/{urlKey}")
    public String handleChangePassword(@PathVariable String urlKey, Model model,
                                       @Validated(ChangePasswordGroup.class)
                                       @ModelAttribute("user") UserDto userDTO,
                                       BindingResult bindingResult) {
        if (bindingResult.hasGlobalErrors()) {
            bindingResult.rejectValue("passwordConfirm", "password_confirm",
                    bindingResult.getGlobalError().getDefaultMessage());
        }
        if (bindingResult.hasErrors()) return "ChangePassword";
        try {
            if (emailServices.getUrlKeys().containsKey(urlKey)) {
                User user = emailServices.getUrlKeys().get(urlKey);
                User userNew = modelMapper.dtoToObject(user.getId(), userDTO);
                userServices.update(userNew, user);
                emailServices.clearKey(urlKey);
                return "redirect:/auth/login";
            }
            model.addAttribute("error", "Page Not Found");
            return "NotFoundView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

}
