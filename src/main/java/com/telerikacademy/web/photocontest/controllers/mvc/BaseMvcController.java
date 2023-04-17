package com.telerikacademy.web.photocontest.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class BaseMvcController {
    @ModelAttribute
    public void populateModels(HttpSession session, HttpServletRequest request, Model model) {
        boolean isAuthenticated = session.getAttribute("currentUser") != null;
        session.setAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("requestURI", request.getRequestURI());
    }


}
