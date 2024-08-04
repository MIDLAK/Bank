package com.vadim.Bank.service;

import com.vadim.Bank.models.Borrower;
import com.vadim.Bank.models.BorrowerRepository;
import com.vadim.Bank.models.Role;
import com.vadim.Bank.models.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BorrowerService implements UserDetailsService {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private BorrowerRepository borrowerRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private MailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Borrower borrower = borrowerRepository.findByUsername(username);

        if (borrower == null){
            throw new UsernameNotFoundException("User not found!");
        }

        return borrower;
    }

    public Borrower findBorrowerById(Long userId){
        Optional<Borrower> borrowerFromDb = borrowerRepository.findById(userId);

        return  borrowerFromDb.orElse(new Borrower());
    }

    public List<Borrower> allBorrower(){
        return  borrowerRepository.findAll();
    }

    public boolean saveBorrower(Borrower borrower){
        Borrower borrowerFromDb = borrowerRepository.findByUsername(borrower.getUsername());

        if (borrowerFromDb != null){
            return false;
        }

        borrower.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        borrower.setPassword(bCryptPasswordEncoder.encode(borrower.getPassword()));
        borrower.setActivationCode(UUID.randomUUID().toString());
        borrowerRepository.save(borrower);

        if (!StringUtils.isEmpty(borrower.getUsername())){
            String message = String.format(
                    "Здравствуйте!\n" +
                            "Приветствуем Вас в Vabank! Пожалуйста, перейдите по ссылке, для подтверждения вашего почтового ящика: http://localhost:8080/activate/%s",
                    borrower.getActivationCode()
            );
           mailSender.send(borrower.getUsername(), "Код активации", message);
        }

        return true;
    }

    public boolean activateUser(String code) {
        Borrower borrower = borrowerRepository.findByActivationCode(code);

        if (borrower ==  null){
            return false;
        }

        borrower.setActivationCode(null);
        borrowerRepository.save(borrower);

        return true;
    }
}