package com.vadim.Bank.controllers;

import com.vadim.Bank.models.Borrower;
import com.vadim.Bank.models.BorrowerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

    @Autowired
    private BorrowerRepository borrowerRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Главная страница");
        model.addAttribute("namePr", "Vabank");
        model.addAttribute("heading", "ВОПЛОТИ МЕЧТЫ УЖЕ СЕГОДНЯ");

        return "home";
    }

    @GetMapping("/calculator")
    public String calculator(Model model){
        model.addAttribute("namePr", "Vabank");
        model.addAttribute("title", "Про нас");

        return "calculator";
    }

//    @PostMapping("/consumer-credit")
//    public String consumerAdd(@RequestParam String firstname, @RequestParam String name, @RequestParam String surname, @RequestParam String age, Model model){
//        model.addAttribute("title", "Главная страница");
//        model.addAttribute("namePr", "Vabank");
//
//        Borrower borrower = new Borrower(firstname, name, surname, Integer.parseInt(age));
//        borrowerRepository.save(borrower);
//
//        return "home";
//    }

    @PostMapping("/consumer-credit")
    public String consumerAdd(@ModelAttribute("firstname") String firstname, @ModelAttribute("name") String name,
                              @ModelAttribute("surname") String surname, @ModelAttribute("age") double age,
                              @ModelAttribute("credit_size") double creditSize, Model model){

        model.addAttribute("title", "Главная страница");
        model.addAttribute("namePr", "Vabank");

        double percent = 5.0;

        int ageToInt = (int) age;
        Borrower borrower = new Borrower(firstname, name, surname, ageToInt, creditSize, 5.9);
        borrowerRepository.save(borrower);
        return "success";
    }

    @GetMapping("/consumer-credit")
    public String consumer(Model model) {
        model.addAttribute("title", "Главная страница");
        model.addAttribute("namePr", "Vabank");

        return "consumer_credit";
    }



}
