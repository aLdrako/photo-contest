package com.telerikacademy.web.photocontest.controllers.mvc;

import com.fasterxml.jackson.databind.ser.Serializers;
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
import java.io.IOException;
import java.util.Objects;

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
    public String showRegisterPage(Model model, HttpSession session) {
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
            userServices.create(user);
            return "redirect:/";
        } catch (EntityDuplicateException e) {
            if (e.getErrorType().equals("username")) {
                bindingResult.rejectValue("username", "username_exists", e.getMessage());
            } else if (e.getErrorType().equals("email")) {
                bindingResult.rejectValue("email", "email_exists", e.getMessage());
            }
            return "RegisterView";
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
                                          BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) return "ForgottenPasswordView";
        try {
            User user = userServices.getByUsername(userDTO.getUsername());
            emailServices.sendForgottenPasswordEmail(user, session);
            return "EmailSentView";
        } catch (EntityNotFoundException | MessagingException | IOException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }
    @GetMapping("/changepassword/{urlKey}")
    public String showChangePasswordPage(@PathVariable String urlKey, HttpSession session, Model model) {
        try {
            if (Objects.equals(session.getAttribute("urlKey"), urlKey)) {
                UserDto userDto = modelMapper
                        .objectToDto((User) Objects.requireNonNull(session
                                .getAttribute("recipient")));
                model.addAttribute("user", userDto);
                return "ChangePassword";
            } else {
                return "index";
            }
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }
    @PostMapping("/changepassword/{urlKey}")
    public String handleChangePassword(@PathVariable String urlKey, Model model,
                                       HttpSession session,
                                       @Validated(ChangePasswordGroup.class)
                                       @ModelAttribute("user") UserDto userDTO,
                                       BindingResult bindingResult) {
        if (bindingResult.hasGlobalErrors()) {
            bindingResult.rejectValue("passwordConfirm", "password_confirm",
                    bindingResult.getGlobalError().getDefaultMessage());
        }
        if (bindingResult.hasErrors()) return "ChangePassword";
        try {
            if (Objects.equals(session.getAttribute("urlKey"), urlKey)) {
                User user = (User) Objects.requireNonNull(session
                        .getAttribute("recipient"));
                user.setPassword(userDTO.getPassword());
                userServices.update(user, user);
                session.removeAttribute("urlKey");
                session.removeAttribute("recipient");
                return "redirect:/auth/login";
            } else {
                return "index";
            }
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

}
