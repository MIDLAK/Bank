package com.vadim.Bank.service;

import com.vadim.Bank.models.Borrower;
import com.vadim.Bank.models.BorrowerRepository;
import com.vadim.Bank.models.CreditCalculator;
import com.vadim.Bank.models.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@EnableScheduling
@Component
public class CreditRepaymentTask {
    @Autowired
    BorrowerService borrowerService;
    @Autowired
    MailSender mailSender;
    @Autowired
    BorrowerRepository borrowerRepository;

    /*снятие средст со счётов должников (15-го числа каждого месяца)
    и их оповещение об этом.*/
    @Scheduled(cron = "0 0/1 * * * ?")
    public void debtorWithdrawal() {
        //cron = "0 0 12 * * ?" - каждый день в 12:00
        //cron ="0 15 10 15 * ?" - в 10:15 пятнадцатого числа каждого месяца
        //cron = "0 0/5 * * * ?" - каждые 5 минут
        List<Borrower> borrowers = borrowerService.allBorrower();
        CreditCalculator creditCalculator;
        for (Borrower borrower : borrowers) {
            if (borrower.getCreditSize() > 0) {
                if (borrower.getTerm() > borrower.getNumberPayments()) {
                    creditCalculator = new CreditCalculator(borrower.getCreditSize(), borrower.getTerm(),
                            borrower.getCreditPercent(), borrower.getCreditIssueDate());

                    ArrayList<Payment> differentiatedPayments = creditCalculator.differentiatedPayments();
                    Payment payment = differentiatedPayments.get(borrower.getNumberPayments()); //текущий платёж

                    double account = borrower.getBankAccount();
                    double pay = payment.getPay();
                    double sanctions = 0.0; // штрафные санции, если на счету недостаточно средств
                    String sanctionsMassenge = "";
                    if (account < payment.getPay()) {
                        sanctions = CreditCalculator.round(pay * 0.35, 2);
                        sanctionsMassenge = "ВНИМАНИЕ! На Вашем счету оказалось недостаточно средств" +
                                "для платежа. За это Вам начисляется штраф в размере " + sanctions +
                                " рублей (35% от текущего платежа)." +
                                "Пополните счёт, чтобы \"выйти из минуса\"";
                    }
                    borrower.setBankAccount(CreditCalculator.round(account - pay - sanctions, 2));
                    borrower.setFullCost(CreditCalculator.round(borrower.getFullCost() - pay, 2));
                    borrower.setNumberPayments(borrower.getNumberPayments() + 1);
                    borrowerRepository.save(borrower);
                    mailSender.send(borrower.getUsername(), "Платёж по кредиту", "Здравствуйте!" +
                            " С Вашего счёта было списано " + pay + " рублей в качестве платы за кредит. Осталось "
                            + (borrower.getTerm() - borrower.getNumberPayments()) + " платежей. " + sanctionsMassenge +
                            "\nС уважением, Vabank.");
                } else {
                    borrower.setTerm(0);
                    borrower.setCreditSize(0);
                    borrower.setCreditIssueDate(null);
                    borrower.setNumberPayments(0);
                    borrower.setFullCost(0);
                    borrower.setCreditPercent(0.0);

                    mailSender.send(borrower.getUsername(), "Кредита больше нет!", "Здравствуйте!" +
                            "Ваш кредит полностью выплачен\nС уважением, Vabank.");

                    borrowerRepository.save(borrower);
                }
            }
        }
    }
}
