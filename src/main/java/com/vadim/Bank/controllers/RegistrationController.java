package com.vadim.Bank.controllers;

import com.vadim.Bank.models.Borrower;
import com.vadim.Bank.service.BorrowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    @Autowired
    private BorrowerService borrowerService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userFrom", new Borrower());

        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("username") String username, @ModelAttribute("password") String password/*, BindingResult bindingResult*/, Model model) {
        Borrower borrower = new Borrower();
        borrower.setPassword(password);
        borrower.setUsername(username);

        if (!borrowerService.saveBorrower(borrower)) {
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "registration";
        }

        return "redirect:/";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code){
        boolean isActivated = borrowerService.activateUser(code);

        if (isActivated){
            model.addAttribute("message", "Адрес электронной почты подтверждён");
        } else {
            model.addAttribute("message", "Адрес электронной почты НЕ подтверждён");
        }

        return "redirect:/";
    }
}
