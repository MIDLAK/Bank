package com.vadim.Bank.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Set;

@Entity
public class Borrower implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // автоматическая работа с индентификаторами
    private Long id;

    private String name, firstName, surname;
    int age;
    private Calendar creditIssueDate; // дата выдачи кредита
    private double creditSize; // размер кредита в рублях
    private double fullCost; // полная стоимость кредита
    private double creditPercent; // процентная ставка
    private int term; // срок кредитования в месяцах
    private double bankAccount; // сумма на счету клиента в банке
    private int numberPayments; // кол-во сделанных клиентом выплат
    private String activationCode; // код подтверждения с email
    private String payoutStrategy; // стратегия платежей (ann - аннуитетные, diff - дифференцированные)

    @Size(min = 2, message = "Не меньше 5 знаков")
    private String password;
    @Email(message = "Ваш email")
    private String username;
    @Transient
    private String passwordConfirm; //не имеет отображение в БД
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles; //роли

    public Borrower(){}

    public Borrower(String name, String firstName, String surname, int age,
                    double creditSize, double creditPercent) {
        this.name = name;
        this.firstName = firstName;
        this.surname = surname;
        this.age = age;
        creditIssueDate = new GregorianCalendar();
        this.creditSize = creditSize;
        this.creditPercent = creditPercent;
    }

    public Borrower(String name, String firstName, String surname, int age, double creditSize,
                    double creditPercent, int term) {
        this.name = name;
        this.firstName = firstName;
        this.surname = surname;
        this.age = age;
        creditIssueDate = new GregorianCalendar();
        this.creditSize = creditSize;
        this.creditPercent = creditPercent;
        this.term = term;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return getRoles(); }

    @Override
    public String getPassword() { return password; }

    @Override
    public String getUsername() { return username; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    public void setPassword(String password) { this.password = password; }

    public void setUsername(String username) { this.username = username; }

    public String getPasswordConfirm() { return passwordConfirm; }

    public void setPasswordConfirm(String passwordConfirm) { this.passwordConfirm = passwordConfirm; }

    public Set<Role> getRoles() { return roles; }

    public void setRoles(Set<Role> roles) { this.roles = roles; }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public int getAge() { return age; }

    public void setAge(int age) { this.age = age; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getSurname() { return surname; }

    public void setSurname(String surname) { this.surname = surname; }

    public Calendar getCreditIssueDate() { return creditIssueDate; }

    public void setCreditIssueDate(Calendar creditIssueDate) { this.creditIssueDate = creditIssueDate; }

    public double getCreditSize() { return creditSize; }

    public void setCreditSize(double creditSize) { this.creditSize = creditSize; }

    public double getCreditPercent() { return creditPercent; }

    public void setCreditPercent(double creditPercent) { this.creditPercent = creditPercent; }

    public int getTerm() { return term; }

    public void setTerm(int term) { this.term = term; }

    public String getActivationCode() { return activationCode; }

    public void setActivationCode(String activationCode) { this.activationCode = activationCode; }

    public double getBankAccount() { return bankAccount; }

    public void setBankAccount(double bankAccount) { this.bankAccount = bankAccount; }

    public double getFullCost() { return fullCost; }

    public void setFullCost(double fullCost) { this.fullCost = fullCost; }

    public int getNumberPayments() { return numberPayments; }

    public void setNumberPayments(int numberPayments) { this.numberPayments = numberPayments; }

    public String getPayoutStrategy() { return payoutStrategy; }

    public void setPayoutStrategy(String payoutStrategy) { this.payoutStrategy = payoutStrategy; }
}