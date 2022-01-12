package com.vadim.Bank.models;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CreditCalculator {
    double sum;
    int term; //срок кредитования в месяцах
    double percent; //ставка (в процентах)
    Calendar issueDate;
    double overpayment;

    public CreditCalculator(@NotNull Borrower borrower) {
        this(borrower.getCreditSize(), borrower.getTerm(), borrower.getCreditPercent(), borrower.getCreditIssueDate());
    }

    public CreditCalculator(double sum, int term, double percent, Calendar issueDate) {
        this.sum = sum;
        this.term = term;
        this.percent = percent;
        this.overpayment = 0.0;
        if (issueDate != null) {
            this.issueDate = (Calendar)(issueDate.clone());
        } else {
            issueDate = new GregorianCalendar();
        }
    }

    public ArrayList<Payment> annuityPaymentsTable() {
        ArrayList<Payment> payments = new ArrayList<Payment>(); //список платежей

        /*подгонка даты для первого платежа под 15-е*/
        if (issueDate.get(Calendar.DAY_OF_MONTH) != 15) {
            issueDate.set(Calendar.DAY_OF_MONTH, 15);
        }

        if (sum > 0) {
            double r = percent / 1200.0; //процентная ставка за один период
            double ak = (r * Math.pow((1 + r), term)) / (Math.pow((1 + r), term) - 1);
            double monthlyPayment = sum * ak;
            double total = CreditCalculator.round(monthlyPayment * term, 2); //общая сумма выплаты (итого)
            double overpayment = total - sum; //переплата
            this.overpayment = CreditCalculator.round(overpayment, 2);
            ;

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");  //пример вывода 16.10.2021

            double s = sum;
            for (int i = 1; i <= term; i++) {
                double repaymentPercent = s * r;
                double amortization = monthlyPayment - repaymentPercent;
                issueDate.add(Calendar.MONTH, 1);
                s = s - amortization;
                String dateStr = dateFormat.format(issueDate.getTime());
                Payment payment = new Payment(dateStr, CreditCalculator.round(monthlyPayment, 2),
                        CreditCalculator.round(repaymentPercent, 2),
                        CreditCalculator.round(amortization, 2),
                        CreditCalculator.round(s, 2));
                payments.add(payment);
            }
        }

        return payments;
    }

    public ArrayList<Payment> differentiatedPayments() {
        ArrayList<Payment> payments = new ArrayList<Payment>();

        if (sum > 0) {
            double r = percent / 1200.0; //процентная ставка за один период
            double rest = sum; //остаток долга по кредиту
            double mpReal = sum / term;
            int t = term;
            double loanBalance = sum;
            double overpayment = 0.0;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");  //пример вывода 16.10.2021

            /*подгонка даты для первого платежа под 15-е*/
            if (issueDate.get(Calendar.DAY_OF_MONTH) != 15) {
                issueDate.set(Calendar.DAY_OF_MONTH, 15);
            }

            while (t != 0) {
                double monthlyPayment = mpReal + rest * r;
                issueDate.add(Calendar.MONTH, 1);
                String dateStr = dateFormat.format(issueDate.getTime());
                double repaymentPercent = loanBalance * r;
                double amortization = monthlyPayment - repaymentPercent;
                loanBalance = loanBalance - amortization;
                Payment payment = new Payment(dateStr, CreditCalculator.round(monthlyPayment, 2),
                        CreditCalculator.round(repaymentPercent, 2),
                        CreditCalculator.round(amortization, 2),
                        CreditCalculator.round(loanBalance, 2));
                payments.add(payment);
                overpayment += repaymentPercent;
                rest = rest - mpReal;
                t--;
            }
            this.overpayment = CreditCalculator.round(overpayment, 2);
        }

        return payments;
    }

    /*нормальное округление*/
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }

    public static boolean doubleCompare(double one, double two) {
        BigDecimal bd1 = new BigDecimal(Double.toString(one));
        BigDecimal bd2 = new BigDecimal(Double.toString(two));

        int rez = bd1.compareTo(bd2);

        if (rez >= 0) {
            return true;
        }

        return false;
    }

    /*возвращает полную стоимость кредита (тело + проценты)*/
    public double getFullCost() { return sum + overpayment; }

    public double getSum() { return sum; }

    public double getOverpayment() { return overpayment; }

    public void setSum(double sum) { this.sum = sum; }

    public int getTerm() { return term; }

    public void setTerm(int term) { this.term = term; }

    public double getPercent() { return percent; }

    public void setPercent(double percent) { this.percent = percent; }
}
