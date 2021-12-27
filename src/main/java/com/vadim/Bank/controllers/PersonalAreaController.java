package com.vadim.Bank.controllers;

import com.vadim.Bank.models.Borrower;
import com.vadim.Bank.models.CreditCalculator;
import com.vadim.Bank.models.Payment;
import com.vadim.Bank.service.BorrowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Controller
public class PersonalAreaController {

    @Autowired
    private BorrowerService borrowerService;

    @GetMapping("/personal-area")
    public String personalArea(Model model) {
        model.addAttribute("title", "Личный кабинет");
        model.addAttribute("namePr", "Vabank");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //username авторизированного пользователя
        Borrower borrower = (Borrower) borrowerService.loadUserByUsername(username);

        CreditCalculator creditCalculator = new CreditCalculator(borrower.getCreditSize(), 12, borrower.getCreditPercent(), borrower.getCreditIssueDate());
        ArrayList<Payment> paytable;
        paytable = creditCalculator.annuityPaymentsTable(); //график платежей (аннуитет)
        //paytable = creditCalculator.differentiatedPayments(); //график платежей (дифференцированный)
        model.addAttribute("paytable", paytable);

        return "personal_area";
    }


}
