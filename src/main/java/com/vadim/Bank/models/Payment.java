package com.vadim.Bank.models;

public class Payment {
    String date;
    double pay;
    double repaymentPercent; //в погашение процентов
    double amortization; //в погашение долга
    double loanBalance; //остаток

    public Payment(String date, double pay, double repaymentPercent, double amortization, double loanBalance) {
        this.date = date;
        this.pay = pay;
        this.repaymentPercent = repaymentPercent;
        this.amortization = amortization;
        this.loanBalance = loanBalance;
    }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public double getPay() { return pay; }

    public void setPay(double pay) { this.pay = pay; }

    public double getRepaymentPercent() { return repaymentPercent; }

    public void setRepaymentPercent(double repaymentPercent) { this.repaymentPercent = repaymentPercent; }

    public double getAmortization() { return amortization; }

    public void setAmortization(double amortization) { this.amortization = amortization; }

    public double getLoanBalance() { return loanBalance; }

    public void setLoanBalance(double loanBalance) { this.loanBalance = loanBalance; }
}