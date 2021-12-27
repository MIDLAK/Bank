package com.vadim.Bank.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CreditCalculator {

    double sum;
    int term; //срок кредитования в месяцах
    double percent; //ставка (в процентах)
    Calendar issueDate;

    public CreditCalculator(double sum, int term, double percent, Calendar issueDate) {
        this.sum = sum;
        this.term = term;
        this.percent = percent;
        if (issueDate != null) {
            this.issueDate = issueDate;
        } else {
            issueDate = new GregorianCalendar();
        }
    }

    public ArrayList<Payment> annuityPaymentsTable() {
        double r = percent / 1200.0; //процентная ставка за один период
        double ak = (r * Math.pow((1 + r), term)) / (Math.pow((1 + r), term) - 1);
        double monthlyPayment = sum * ak;
        double total = Math.round(monthlyPayment * term * 100.0) / 100.0; //общая сумма выплаты (итого)
        double overpayment = total - sum; //переплата

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");  //пример вывода 16.10.2021
        ArrayList<Payment> payments = new ArrayList<Payment>(); //список платежей


        double loanBalance = sum;
        double s = sum + overpayment;
        for (int i = 1; i <= term; i++) {
            double repaymentPercent = Math.round(loanBalance * r * 100.0) / 100.0;
            double amortization = Math.round((monthlyPayment - repaymentPercent) * 100.0) / 100.0;
            loanBalance = Math.round((loanBalance - monthlyPayment) * 100.0) / 100.0;
            issueDate.add(Calendar.MONTH, 1);
            s = s - monthlyPayment;
            String dateStr = dateFormat.format(issueDate.getTime());
            Payment payment = new Payment(dateStr, Math.round(monthlyPayment * 100.0) / 100.0, repaymentPercent, amortization, Math.round(s*100.0)/100.0);
            payments.add(payment);
        }
        return payments;
    }

    public ArrayList<Payment> differentiatedPayments() {
        ArrayList<Payment> payments = new ArrayList<Payment>();
        double r = percent / 1200.0; //процентная ставка за один период
        double rest = sum; //остаток долга по кредиту
        double mpReal = sum / term;
        int t = term;
        double loanBalance = sum;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");  //пример вывода 16.10.2021
        while (t != 0) {
            double monthlyPayment = mpReal + rest * r;
            issueDate.add(Calendar.MONTH, 1);
            String dateStr = dateFormat.format(issueDate.getTime());
            double repaymentPercent = Math.round(loanBalance * r * 100.0) / 100.0;
            double amortization = Math.round((monthlyPayment - repaymentPercent) * 100.0) / 100.0;
            loanBalance = Math.round((loanBalance - monthlyPayment) * 100.0) / 100.0;
            Payment payment = new Payment(dateStr, Math.round(monthlyPayment * 100.0) / 100.0, repaymentPercent, amortization, loanBalance);
            payments.add(payment);
            rest = rest - mpReal;
            t--;
        }
        return payments;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }
}
