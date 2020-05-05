package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDate;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "home/index";
    }

    @GetMapping("/test")
    public String bootstrap(Model model) {
        String username = null;
        LocalDate datetime = null;
        model.addAttribute("username", username);
        model.addAttribute("datetime", datetime);
        return "home/test";
    }


}
