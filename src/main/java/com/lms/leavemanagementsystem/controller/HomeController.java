package com.lms.leavemanagementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Landing page
    @GetMapping("/")
    public String home() {
        return "index";
    }

    // Login page
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}