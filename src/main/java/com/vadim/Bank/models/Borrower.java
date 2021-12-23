package com.vadim.Bank.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Entity
public class Borrower {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // автоматическая работа с индентификаторами
    private Long id;

    private String name, firstName, surname;
    int age;
    private Calendar creditIssueDate; // дата выдачи кредита
    private double creditSize; // размер кредита в рублях
    private double creditPercent; // процентная ставка

    public Borrower(){}

    //тестовый конструктор (не всё инициализируется)
    public Borrower(String name, String firstName, String surname, int age, double creditSize, double creditPercent) {
        this.name = name;
        this.firstName = firstName;
        this.surname = surname;
        this.age = age;

        creditIssueDate = new GregorianCalendar();
        this.creditSize = creditSize;
        this.creditPercent = creditPercent;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Calendar getCreditIssueDate() {
        return creditIssueDate;
    }

    public void setCreditIssueDate(Calendar creditIssueDate) {
        this.creditIssueDate = creditIssueDate;
    }

    public double getCreditSize() {
        return creditSize;
    }

    public void setCreditSize(double creditSize) {
        this.creditSize = creditSize;
    }

    public double getCreditPercent() {
        return creditPercent;
    }

    public void setCreditPercent(double creditPercent) {
        this.creditPercent = creditPercent;
    }
}
