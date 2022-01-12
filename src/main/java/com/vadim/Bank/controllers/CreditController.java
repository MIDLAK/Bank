package com.vadim.Bank.controllers;

import com.vadim.Bank.models.Borrower;
import com.vadim.Bank.models.BorrowerRepository;
import com.vadim.Bank.models.CreditCalculator;
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

import java.util.GregorianCalendar;

@Controller
public class CreditController {
    @Autowired
    private BorrowerRepository borrowerRepository;
    @Autowired
    private BorrowerService borrowerService;
    @Autowired
    private MailSender mailSender;

    @GetMapping("/consumer-credit")
    public String consumer(Model model) {
        model.addAttribute("title", "Главная страница");
        model.addAttribute("namePr", "Vabank");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //username авторизированного пользователя
        Borrower borrower = (Borrower) borrowerService.loadUserByUsername(username);

        if (!StringUtils.isEmpty(borrower.getActivationCode())){
            return "no_confirmation";
        }

        if (borrower.getCreditSize() > 0.0){
            return "failure";
        }

        return "consumer_credit";
    }

    @PostMapping("/consumer-credit")
    public String consumerAdd(@ModelAttribute("firstname") String firstname, @ModelAttribute("name") String name,
                              @ModelAttribute("surname") String surname, @ModelAttribute("age") double age,
                              @ModelAttribute("credit_size") double creditSize, @ModelAttribute("credit_term") double term, Model model) {

        model.addAttribute("title", "Главная страница");
        model.addAttribute("namePr", "Vabank");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //username авторизированного пользователя
        Borrower borrower = (Borrower) borrowerService.loadUserByUsername(username);

        if (borrower != null) {
            borrower.setCreditPercent(5.9);
            borrower.setCreditSize(creditSize);
            borrower.setAge((int) age);
            borrower.setCreditIssueDate(new GregorianCalendar());
            borrower.setName(name);
            borrower.setFirstName(firstname);
            borrower.setSurname(surname);
            borrower.setTerm((int) term);
            borrower.setBankAccount(borrower.getBankAccount() + creditSize);
            CreditCalculator creditCalculator = new CreditCalculator(borrower);
            borrower.setFullCost(creditCalculator.getFullCost());

            borrowerRepository.save(borrower);

            mailSender.send(borrower.getUsername(), "Новый кредит", "Здравствуйте! Вы оформили кредит на " + term + " месяцев (5.9% годовых)\n" +
                    "Спасибо, что выбрали \"Vabank\"!");

            return "success";
        }

        return "err";
    }

    @GetMapping("/mortgage-credit")
    public String mortgage(Model model){
        model.addAttribute("title", "Главная страница");
        model.addAttribute("namePr", "Vabank");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //username авторизированного пользователя
        Borrower borrower = (Borrower) borrowerService.loadUserByUsername(username);

        if (!StringUtils.isEmpty(borrower.getActivationCode())){
            return "no_confirmation";
        }

        if (borrower.getCreditSize() > 0.0){
            return "failure";
        }

        return "mortgage_credit";
    }

    @PostMapping("/mortgage-credit")
    public String mortgage(@ModelAttribute("firstname") String firstname, @ModelAttribute("name") String name,
                           @ModelAttribute("surname") String surname, @ModelAttribute("age") double age,
                           @ModelAttribute("credit_size") double creditSize, @ModelAttribute("credit_term") double term, Model model){

        model.addAttribute("title", "Главная страница");
        model.addAttribute("namePr", "Vabank");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //username авторизированного пользователя
        Borrower borrower = (Borrower) borrowerService.loadUserByUsername(username);

        if (borrower != null) {
            borrower.setCreditPercent(4.7);
            borrower.setCreditSize(creditSize);
            borrower.setAge((int) age);
            borrower.setCreditIssueDate(new GregorianCalendar());
            borrower.setName(name);
            borrower.setFirstName(firstname);
            borrower.setSurname(surname);
            borrower.setTerm((int) term);
            borrower.setBankAccount(borrower.getBankAccount() + creditSize);
            CreditCalculator creditCalculator = new CreditCalculator(borrower);
            borrower.setFullCost(creditCalculator.getFullCost());

            borrowerRepository.save(borrower);

            mailSender.send(borrower.getUsername(), "Новый кредит", "Здравствуйте! Вы оформили кредит на " + term + " месяцев (4.7% годовых)\n" +
                    "Спасибо, что выбрали \"Vabank\"!");

            return "success";
        }

        return "err";
    }

    @GetMapping("/car-credit")
    public String car(Model model){
        model.addAttribute("title", "Главная страница");
        model.addAttribute("namePr", "Vabank");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //username авторизированного пользователя
        Borrower borrower = (Borrower) borrowerService.loadUserByUsername(username);

        if (!StringUtils.isEmpty(borrower.getActivationCode())){
            return "no_confirmation";
        }

        if (borrower.getCreditSize() > 0.0){
            return "failure";
        }

        return "car_credit";
    }

    @PostMapping("/car-credit")
    public String car(@ModelAttribute("firstname") String firstname, @ModelAttribute("name") String name,
                      @ModelAttribute("surname") String surname, @ModelAttribute("age") double age,
                      @ModelAttribute("credit_size") double creditSize, @ModelAttribute("credit_term") double term, Model model){

        model.addAttribute("title", "Главная страница");
        model.addAttribute("namePr", "Vabank");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //username авторизированного пользователя
        Borrower borrower = (Borrower) borrowerService.loadUserByUsername(username);

        if (borrower != null) {
            borrower.setCreditPercent(3.2);
            borrower.setCreditSize(creditSize);
            borrower.setAge((int) age);
            borrower.setCreditIssueDate(new GregorianCalendar());
            borrower.setName(name);
            borrower.setFirstName(firstname);
            borrower.setSurname(surname);
            borrower.setTerm((int) term);
            borrower.setBankAccount(borrower.getBankAccount() + creditSize);
            CreditCalculator creditCalculator = new CreditCalculator(borrower);
            borrower.setFullCost(creditCalculator.getFullCost());

            borrowerRepository.save(borrower);

            mailSender.send(borrower.getUsername(), "Новый кредит", "Здравствуйте! Вы оформили кредит на " + term + " месяцев (3.2% годовых)\n" +
                    "Спасибо, что выбрали \"Vabank\"!");

            return "success";
        }

        return "err";
    }

    @GetMapping("student-credit")
    public String student(Model model){
        model.addAttribute("title", "Главная страница");
        model.addAttribute("namePr", "Vabank");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //username авторизированного пользователя
        Borrower borrower = (Borrower) borrowerService.loadUserByUsername(username);

        if (!StringUtils.isEmpty(borrower.getActivationCode())){
            return "no_confirmation";
        }

        if (borrower.getCreditSize() > 0.0){
            return "failure";
        }

        return "student_credit";
    }

    @PostMapping("/student-credit")
    public String student(@ModelAttribute("firstname") String firstname, @ModelAttribute("name") String name,
                          @ModelAttribute("surname") String surname, @ModelAttribute("age") double age,
                          @ModelAttribute("credit_size") double creditSize, @ModelAttribute("credit_term") double term, Model model){

        model.addAttribute("title", "Главная страница");
        model.addAttribute("namePr", "Vabank");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //username авторизированного пользователя
        Borrower borrower = (Borrower) borrowerService.loadUserByUsername(username);

        if (borrower != null) {
            borrower.setCreditPercent(4.1);
            borrower.setCreditSize(creditSize);
            borrower.setAge((int) age);
            borrower.setCreditIssueDate(new GregorianCalendar());
            borrower.setName(name);
            borrower.setFirstName(firstname);
            borrower.setSurname(surname);
            borrower.setTerm((int) term);
            borrower.setBankAccount(borrower.getBankAccount() + creditSize);
            CreditCalculator creditCalculator = new CreditCalculator(borrower);
            borrower.setFullCost(creditCalculator.getFullCost());

            borrowerRepository.save(borrower);

            mailSender.send(borrower.getUsername(), "Новый кредит", "Здравствуйте! Вы оформили кредит на " + term + " месяцев (4.1% годовых)\n" +
                    "Спасибо, что выбрали \"Vabank\"!");

            return "success";
        }

        return "err";
    }
}
