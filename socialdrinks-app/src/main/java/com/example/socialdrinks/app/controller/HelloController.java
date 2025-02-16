package com.example.socialdrinks.app.controller;

import jakarta.validation.constraints.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.validation.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/hello")
public class HelloController {

    @GetMapping
    public String hello(Model model) {
        model.addAttribute("form", new NameForm());
        return "hello";
    }

    @PostMapping
    public String postHello(@Validated @ModelAttribute("form") NameForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList();

            model.addAttribute("errors", errors);

            return "hello"; // Falls der Name leer ist, bleibt die Seite und zeigt den Fehler an
        }

        model.addAttribute("name", form.getName());
        return "hello";
    }

    public static class NameForm {

        @NotBlank(message = "Name darf nicht leer sein.")
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
