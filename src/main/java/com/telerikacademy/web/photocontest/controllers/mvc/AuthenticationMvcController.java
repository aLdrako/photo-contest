package com.telerikacademy.web.photocontest.controllers.mvc;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.telerikacademy.web.photocontest.exceptions.AuthorizationException;
import com.telerikacademy.web.photocontest.exceptions.EntityDuplicateException;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.models.dto.UserDto;
import com.telerikacademy.web.photocontest.models.validations.CreateValidationGroup;
import com.telerikacademy.web.photocontest.models.validations.LoginValidationGroup;
import com.telerikacademy.web.photocontest.services.ModelMapper;
import com.telerikacademy.web.photocontest.services.contracts.UserServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthenticationMvcController extends BaseMvcController {
    private final UserServices userServices;
    private final ModelMapper modelMapper;
    private final AuthenticationHelper authenticationHelper;

    @GetMapping("/login")
    public String showLoginPage(Model model, HttpSession session) {
        if (!session.isNew()) {
            session.invalidate();
        }
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
            session.setAttribute("currentUser", user.getUsername());
            return "redirect:/";
        } catch (AuthorizationException e) {
            bindingResult.rejectValue("username", "auth_error", e.getMessage());
            return "LoginView";
        }
    }
    @GetMapping("/register")
    public String showRegisterPage(Model model, HttpSession session) {
        if (!session.isNew()) {
            session.invalidate();
        }
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
        session.invalidate();
        return "redirect:/auth/login";
    }

}