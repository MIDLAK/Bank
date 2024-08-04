package com.vadim.Bank;

import com.vadim.Bank.models.CreditCalculator;
import com.vadim.Bank.models.Payment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

@RunWith(MockitoJUnitRunner.class)
public class CreditCalculatorTest {
    private CreditCalculator calculator;
    private ArrayList<Payment> paytable;

    private static Calendar issueDate;
    private static double sum;
    private static int term;
    private static double percent;

    @BeforeClass
    public static void prepareTestData() {
        issueDate = new GregorianCalendar(2024, Calendar.MARCH, 20);
        sum = 12_000_000.00;
        term = 121;
        percent = 9.3;
    }

    @Before
    public void init() {
        calculator = new CreditCalculator(sum, term, percent, issueDate);
        paytable = null;
    }

    @Test
    public void roundTest() {
        double num = 123456.3456125648487;

        double twoDig = CreditCalculator.round(num, 2);
        double hundredDig = CreditCalculator.round(num, 100);
        double zeroDig = CreditCalculator.round(num, 0);

        Assert.assertEquals(num, twoDig, 0.01);
        Assert.assertEquals(num, hundredDig, 0.001);
        Assert.assertEquals((double)((int)num*1000)/1000, zeroDig, 0.1);
        Exception negDig = Assert.assertThrows(IllegalArgumentException.class,
                () -> CreditCalculator.round(num, -1));
    }

    @Test
    public void doubleCompareTest() {
        Assert.assertTrue(CreditCalculator.doubleCompare(0.0, 0.0));
        Assert.assertTrue(CreditCalculator.doubleCompare(110.0, 110.0));
        Assert.assertTrue(CreditCalculator.doubleCompare(0.000000001, 0.000000001));
        Assert.assertTrue(CreditCalculator.doubleCompare(-0.00001, -0.00001));
        Assert.assertTrue(CreditCalculator.doubleCompare(1856745235.00001, 1856745235.00001));

        Assert.assertFalse(CreditCalculator.doubleCompare(0.0, 0.1));
        Assert.assertFalse(CreditCalculator.doubleCompare(110.0, 110.000000001));
        Assert.assertFalse(CreditCalculator.doubleCompare(0.000000001, 0.0000000011));
        Assert.assertFalse(CreditCalculator.doubleCompare(-0.1, -0.9));
        Assert.assertFalse(CreditCalculator.doubleCompare(1856745235.00001, 1856745235.000011));
    }

    @Test
    public void annuityPaymentsTableTest() throws Exception {
        paytable = calculator.annuityPaymentsTable();
        Assert.assertNotNull(paytable);
        Assert.assertEquals(paytable.size(), term);
        Assert.assertTrue(isEndPayLoanBalanceEqZero(paytable));

        double overpayment = calculator.getOverpayment();
        Assert.assertTrue(overpayment >= 0);
        Assert.assertTrue(isActualAmortizationSumEqCreditSum(paytable, sum));

        Payment endPay = paytable.get(paytable.size() - 1);
        Payment firstPay = paytable.get(0);
        Assert.assertTrue(doubleToBigDecimalCompare(firstPay.getPay(), endPay.getPay()));
    }

    @Test(expected = Exception.class)
    public void annuityPaymentsTableBadSumTest() throws Exception {
        CreditCalculator calculator = new CreditCalculator(0.0, term, percent, issueDate);
        calculator.annuityPaymentsTable();
    }

    @Test(expected = Exception.class)
    public void annuityPaymentsTableBadTermTest() throws Exception {
        CreditCalculator calculator = new CreditCalculator(sum, -10, percent, issueDate);
        calculator.annuityPaymentsTable();
    }

    @Test(expected = Exception.class)
    public void annuityPaymentsTableBadPercentTest() throws Exception {
        CreditCalculator calculator = new CreditCalculator(sum, term, -4.2, issueDate);
        calculator.annuityPaymentsTable();
    }

    @Test
    public void differentiatedPaymentsTest() throws Exception {
        paytable = calculator.differentiatedPayments();
        Assert.assertNotNull(paytable);
        Assert.assertEquals(paytable.size(), term);
        Assert.assertTrue(isEndPayLoanBalanceEqZero(paytable));

        double overpayment = calculator.getOverpayment();
        Assert.assertTrue(overpayment >= 0);
        Assert.assertTrue(isActualAmortizationSumEqCreditSum(paytable, sum));

        Payment endPay = paytable.get(paytable.size() - 1);
        Payment firstPay = paytable.get(0);
        Assert.assertTrue(doubleToBigDecimalCompare(firstPay.getAmortization(), endPay.getAmortization()));
    }

    @Test(expected = Exception.class)
    public void differentiatedPaymentsBadSumTest() throws Exception {
        CreditCalculator calculator = new CreditCalculator(0.0, term, percent, issueDate);
        calculator.differentiatedPayments();
    }

    @Test(expected = Exception.class)
    public void differentiatedPaymentsBadTermTest() throws Exception {
        CreditCalculator calculator = new CreditCalculator(sum, -100, percent, issueDate);
        calculator.differentiatedPayments();
    }

    @Test(expected = Exception.class)
    public void differentiatedPaymentsBadPercentTest() throws Exception {
        CreditCalculator calculator = new CreditCalculator(sum, term, -4.3, issueDate);
        calculator.differentiatedPayments();
    }

    private boolean isEndPayLoanBalanceEqZero(ArrayList<Payment> payments) {
        Payment endPay = paytable.get(paytable.size() - 1);
        return doubleToBigDecimalCompare(endPay.getLoanBalance(), 0.0);
    }

    private boolean isActualAmortizationSumEqCreditSum(ArrayList<Payment> payments, double sum) {
        double actualAmortizationSum = 0.0;
        for (Payment pay: payments)
            actualAmortizationSum += pay.getAmortization();

        return Math.abs(actualAmortizationSum - sum) < 1;
    }

    private boolean doubleToBigDecimalCompare(double one, double two) {
        BigDecimal bd1 = new BigDecimal(Double.toString(one));
        BigDecimal bd2 = new BigDecimal(Double.toString(two));

        return bd1.compareTo(bd2) == 0;
    }

}
