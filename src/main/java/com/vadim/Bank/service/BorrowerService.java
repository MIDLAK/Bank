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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowerService implements UserDetailsService {

    @PersistenceContext
    private EntityManager em;
    @Autowired
    BorrowerRepository borrowerRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

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
        borrowerRepository.save(borrower);
        return true;
    }

}
