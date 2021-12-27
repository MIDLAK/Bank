package com.vadim.Bank.controllers;
import com.vadim.Bank.models.Borrower;
import com.vadim.Bank.models.BorrowerRepository;
import com.vadim.Bank.service.BorrowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @PostMapping("/consumer-credit")
    public String consumerAdd(@ModelAttribute("firstname") String firstname, @ModelAttribute("name") String name,
                              @ModelAttribute("surname") String surname, @ModelAttribute("age") double age,
                              @ModelAttribute("credit_size") double creditSize, Model model) {

        model.addAttribute("title", "Главная страница");
        model.addAttribute("namePr", "Vabank");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //username авторизированного пользователя

        Borrower borrower = (Borrower) borrowerService.loadUserByUsername(username);

        if (borrower != null) {
            borrower.setCreditPercent(5.0);
            borrower.setCreditSize(creditSize);
            borrower.setAge((int) age);
            borrower.setCreditIssueDate(new GregorianCalendar());
            borrower.setName(name);
            borrower.setFirstName(firstname);
            borrower.setSurname(surname);

            borrowerRepository.save(borrower);

            return "success";
        }

        return "failure";
    }

    @GetMapping("/consumer-credit")
    public String consumer(Model model) {
        model.addAttribute("title", "Главная страница");
        model.addAttribute("namePr", "Vabank");

        return "consumer_credit";
    }
}
