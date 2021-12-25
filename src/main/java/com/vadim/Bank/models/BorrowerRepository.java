package com.vadim.Bank.models;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowerRepository extends JpaRepository<Borrower, Long> {
    Borrower findByUsername(String username); //тело метода определяет сам JpaRepository
}
