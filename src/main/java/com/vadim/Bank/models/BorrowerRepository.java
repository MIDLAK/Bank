package com.vadim.Bank.models;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowerRepository extends JpaRepository<Borrower, Long> {
    /*тела методов определяет сам JpaRepository*/
    Borrower findByUsername(String username);
    Borrower findByActivationCode(String code);
}
