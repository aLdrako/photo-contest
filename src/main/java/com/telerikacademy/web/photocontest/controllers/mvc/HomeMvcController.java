package com.telerikacademy.web.photocontest.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class HomeMvcController extends BaseMvcController{
    @GetMapping
    public String showHomePage() {
        return "index";
    }

}
