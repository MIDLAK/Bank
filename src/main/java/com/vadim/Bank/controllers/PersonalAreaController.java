package com.vadim.Bank.controllers;

import com.vadim.Bank.models.Borrower;
import com.vadim.Bank.models.BorrowerRepository;
import com.vadim.Bank.models.CreditCalculator;
import com.vadim.Bank.models.Payment;
import com.vadim.Bank.service.BorrowerService;
import com.vadim.Bank.service.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;

@Controller
public class PersonalAreaController {

    @Autowired
    private BorrowerService borrowerService;

    @Autowired
    private BorrowerRepository borrowerRepository;

    @Autowired
    private MailSender mailSender;

    @GetMapping("/personal-area")
    public String personalArea(Model model) {
        model.addAttribute("title", "Личный кабинет");
        model.addAttribute("namePr", "Vabank");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //username авторизированного пользователя
        Borrower borrower = (Borrower) borrowerService.loadUserByUsername(username);

        CreditCalculator creditCalculator = new CreditCalculator(borrower.getCreditSize(), borrower.getTerm(), borrower.getCreditPercent(), borrower.getCreditIssueDate());
        ArrayList<Payment> paytable;
        //paytable = creditCalculator.annuityPaymentsTable(); //график платежей (аннуитет)
        paytable = creditCalculator.differentiatedPayments(); //график платежей (дифференцированный)
        double overpayment = creditCalculator.getOverpayment();
        model.addAttribute("paytable", paytable);
        model.addAttribute("borrower", borrower);
        model.addAttribute("overpayment", overpayment);

        return "personal_area";
    }

    @PostMapping("/refill")
    public String refill(@ModelAttribute("transfer_amount") double transferAmount, Model model){
        transferAmount = CreditCalculator.round(transferAmount, 2);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //username авторизированного пользователя
        Borrower borrower = (Borrower) borrowerService.loadUserByUsername(username);

        boolean success = false;

        if (borrower != null){
            double total = transferAmount + borrower.getBankAccount();
            borrower.setBankAccount(total);
            borrowerRepository.save(borrower);

            mailSender.send(borrower.getUsername(), "Пополнение счёта", "Здравствуйте! Ваш счёт в банке \"Vabank\" пополнен на " + transferAmount + " рублей.\n" +
                    "Общая сумма: " + total + " рублей");
        }

        return "redirect:/personal-area";
    }


}
