package com.example.socialdrinks.app.controller;

import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/hello")
public class HelloController {

    @GetMapping
    public String hello() {
        return "hello";
    }

    @PostMapping
    public String postHello(String name, Model model) {
        model.addAttribute("name", name);

        return "hello";
    }

}
