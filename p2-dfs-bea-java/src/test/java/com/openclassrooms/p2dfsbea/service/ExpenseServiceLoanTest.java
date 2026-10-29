package com.openclassrooms.p2dfsbea.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.openclassrooms.p2dfsbea.model.Expense;

public class ExpenseServiceLoanTest {

    private ExpenseService service;
    private ExpenseServiceLoan serviceLoan;
    private List<Expense> expenses;

    @BeforeEach 
    void setUp() {
        service = new ExpenseService(null);
        serviceLoan = new ExpenseServiceLoan(service);
        expenses = new ArrayList<>();
    }

    @Test 
    public void calculateAmountAvailableForLoan_shouldReturnAllBudgetAvailable_whenHousingExists() {

        Expense foods = new Expense(LocalDate.of(2026, 3, 15), "food", "restaurant", BigDecimal.valueOf(78));
        Expense housings = new Expense(LocalDate.of(2026, 3, 24), "housing", "rent", BigDecimal.valueOf(850));
        Expense budgets = new Expense(LocalDate.of(2026, 3, 29), "budget", "salary", BigDecimal.valueOf(2000));
        Expense transports = new Expense(LocalDate.of(2026, 5, 18), "transport", "bus", BigDecimal.valueOf(15));
        expenses.add(foods);
        expenses.add(housings);
        expenses.add(budgets);
        expenses.add(transports);

        BigDecimal result = serviceLoan.calculateAmountAvailableForLoan(expenses);

        assertEquals(new BigDecimal("1953.50"), result);
    }

    @Test 
    public void calculateAmountAvailableForLoan_shouldReturnAllBudgetAvailable_whenHousingDoesNotExist() {

        Expense foods = new Expense(LocalDate.of(2026, 3, 15), "food", "restaurant", BigDecimal.valueOf(78));        
        Expense budgets = new Expense(LocalDate.of(2026, 3, 29), "budget", "salary", BigDecimal.valueOf(2000));
        Expense transports = new Expense(LocalDate.of(2026, 5, 18), "transport", "bus", BigDecimal.valueOf(15));
        expenses.add(foods);
        expenses.add(budgets);
        expenses.add(transports);

        BigDecimal result = serviceLoan.calculateAmountAvailableForLoan(expenses);

        assertEquals(new BigDecimal("1953.50"), result);
    }

    @Test 
    public void calculateMonthlyPayment_shouldReturnDebtRatioLimit_whenBudgetDebtRatioLimitIsSmallestValue() {

        BigDecimal budget = BigDecimal.valueOf(300);
        BigDecimal amountAvailableForLoan = BigDecimal.valueOf(350);
        
        BigDecimal result = serviceLoan.calculateMonthlyPayment(amountAvailableForLoan, budget);

        assertEquals(new BigDecimal("105.00"), result);
    }

    @Test 
    public void calculateMonthlyPayment_shouldReturnAmountAvailableForLoan_whenAmountForLoanIsSmallestValue() {

        BigDecimal budget = BigDecimal.valueOf(800);
        BigDecimal amountAvailableForLoan = BigDecimal.valueOf(250);
        
        BigDecimal result = serviceLoan.calculateMonthlyPayment(amountAvailableForLoan, budget);

        assertEquals(BigDecimal.valueOf(250), result);
    }

    @Test 
    public void calculateTotalLoanAmount_shouldReturnTotalLoanAmount_whenBorrowingRateIsGreaterThanZero() {

        BigDecimal borrowingRate = BigDecimal.valueOf(3);
        int loanTermInYears = 20;
        BigDecimal monthlyPayment = BigDecimal.valueOf(700);

        BigDecimal result = serviceLoan.calculateTotalLoanAmount(monthlyPayment, borrowingRate, loanTermInYears);

        assertEquals(new BigDecimal("129230.77"), result);
    }

    @Test 
    public void calculateTotalLoanAmount_shouldReturnTotalLoanAmount_whenBorrowingRateIsZero() {

        BigDecimal borrowingRate = BigDecimal.valueOf(0);
        int loanTermInYears = 20;
        BigDecimal monthlyPayment = BigDecimal.valueOf(700);

        BigDecimal result = serviceLoan.calculateTotalLoanAmount(monthlyPayment, borrowingRate, loanTermInYears);

        assertEquals(new BigDecimal("168000.00"), result);
    }
}
