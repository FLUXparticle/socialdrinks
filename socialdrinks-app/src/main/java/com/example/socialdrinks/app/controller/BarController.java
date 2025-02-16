package com.example.socialdrinks.app.controller;

import com.example.socialdrinks.app.entity.*;
import com.example.socialdrinks.app.service.*;
import jakarta.validation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/bars")
public class BarController {

    private final BarService barService;

    @Autowired
    public BarController(BarService barService) {
        this.barService = barService;
    }

    @GetMapping("/")
    public String listBars(Model model) {
        model.addAttribute("bars", barService.getAllBars());
        return "bars/list"; // Template: src/main/resources/templates/bars/list.html
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("bar", new Bar());
        return "bars/form"; // Template für das Formular (src/main/resources/templates/bars/form.html)
    }

    @PostMapping("/")
    public String createBar(@Valid @ModelAttribute Bar bar, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "bars/form";
        }
        barService.saveBar(bar);
        return "redirect:/bars/";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Bar bar = barService.getBar(id);
        model.addAttribute("bar", bar);
        return "bars/form";
    }

    @PostMapping("/{id}")
    public String updateBar(@PathVariable Long id, @Valid @ModelAttribute Bar bar, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "bars/form";
        }

        barService.updateBar(id, bar);

        return "redirect:/bars/";
    }

    @GetMapping("/{id}/transactions")
    public String showTransactions(@PathVariable Long id, Model model) {
        Bar bar = barService.getBar(id);

        if (bar == null) {
            // Falls keine Bar gefunden wird
            return "redirect:/bars/";
        }

        List<BarTransaction> transactions = barService.getTransactionsByBarId(id);

        model.addAttribute("bar", bar);
        model.addAttribute("transactions", transactions);

        return "bars/transactions";
    }

}
