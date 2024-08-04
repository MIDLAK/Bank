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
import org.springframework.util.StringUtils;
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

        if (!StringUtils.isEmpty(borrower.getActivationCode())){
            return "no_confirmation";
        }

        CreditCalculator creditCalculator = new CreditCalculator(borrower.getCreditSize(), borrower.getTerm(), borrower.getCreditPercent(), borrower.getCreditIssueDate());
        ArrayList<Payment> paytable = null;
        if (borrower.getCreditSize() > 0) {
            if (borrower.getPayoutStrategy().equals("ann")) {
                paytable = creditCalculator.annuityPaymentsTable(); //график платежей (аннуитет)
            } else if(borrower.getPayoutStrategy().equals("diff")) {
                paytable = creditCalculator.differentiatedPayments(); //график платежей (дифференцированный)
            }
        }

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

        if (borrower != null){
            double total = CreditCalculator.round(transferAmount + borrower.getBankAccount(), 2);
            borrower.setBankAccount(total);
            borrowerRepository.save(borrower);

            mailSender.send(borrower.getUsername(), "Пополнение счёта", "Здравствуйте! Ваш счёт в банке \"Vabank\" пополнен на " + transferAmount + " рублей.\n" +
                    "Текущая сумма на счёте: " + total + " рублей");
        }

        return "redirect:/personal-area";
    }

    @PostMapping("/withdraw")
    public String withdraw(@ModelAttribute("transfer_amount") double transferAmount, Model model){
        transferAmount = CreditCalculator.round(transferAmount, 2);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //username авторизированного пользователя
        Borrower borrower = (Borrower) borrowerService.loadUserByUsername(username);

        if (borrower != null && CreditCalculator.doubleCompare(borrower.getBankAccount(), transferAmount)){
            borrower.setBankAccount(CreditCalculator.round(borrower.getBankAccount() - transferAmount,2));
            borrowerRepository.save(borrower);

            mailSender.send(borrower.getUsername(), "Снятие со счёта", "Здравствуйте! С Вашего счёта в банке \"Vabank\" было снято " + transferAmount + " рублей.\n" +
                    "Текущая сумма на счёте: " + borrower.getBankAccount() + " рублей");
        }

        return "redirect:/personal-area";
    }
}