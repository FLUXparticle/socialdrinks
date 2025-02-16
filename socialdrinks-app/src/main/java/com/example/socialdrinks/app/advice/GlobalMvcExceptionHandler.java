package com.example.socialdrinks.app.advice;

import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalMvcExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleMvcException(Exception ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

}
