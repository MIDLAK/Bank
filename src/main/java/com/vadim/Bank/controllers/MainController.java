package com.vadim.Bank.controllers;

import com.vadim.Bank.models.BorrowerRepository;
import com.vadim.Bank.service.BorrowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @Autowired
    private BorrowerRepository borrowerRepository;
    @Autowired
    private BorrowerService borrowerService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Главная страница");
        model.addAttribute("namePr", "Vabank");
        model.addAttribute("heading", "ВОПЛОТИ МЕЧТЫ УЖЕ СЕГОДНЯ");

        return "home";
    }

    @GetMapping("/calculator")
    public String calculator(Model model) {
        model.addAttribute("namePr", "Vabank");

        return "calculator";
    }

    @GetMapping("/about")
    public String about(Model model){
        model.addAttribute("namePr", "Vabank");

        return "about";
    }
}