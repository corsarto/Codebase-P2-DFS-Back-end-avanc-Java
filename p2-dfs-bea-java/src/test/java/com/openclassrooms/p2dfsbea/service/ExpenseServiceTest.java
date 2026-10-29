package com.openclassrooms.p2dfsbea.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.openclassrooms.p2dfsbea.model.Expense;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExpenseServiceTest {

    private ExpenseService service;
    private List<Expense> expenses;

    @BeforeEach 
    void setUp() {
        service = new ExpenseService(null);
        expenses = new ArrayList<>();
    }


    @Test 
    public void calculateAmountForCategories_shouldReturnEmptyMap_whenListIsEmpty() {

        Map<String, BigDecimal> result = service.calculateAmountForCategories(expenses);

        assertTrue(result.isEmpty());
    }

    @Test 
    public void calculateAmountForCategories_shouldReturnAddAmount_whenCategoriesExists() {

        Expense groceries = new Expense(LocalDate.of(2026, 3, 24), "food", "grocery-shopping", BigDecimal.valueOf(37));
        Expense foods = new Expense(LocalDate.of(2026, 3, 24), "food", "restaurant", BigDecimal.valueOf(78));
        Expense transports = new Expense(LocalDate.of(2026, 3, 24), "transport", "bus", BigDecimal.valueOf(15));
        expenses.add(groceries);
        expenses.add(foods);
        expenses.add(transports);

        Map<String, BigDecimal> result = service.calculateAmountForCategories(expenses);

        assertEquals(BigDecimal.valueOf(15), result.get("transport"));
        assertEquals(BigDecimal.valueOf(115), result.get("food"));
    }

    @Test 
    public void calculateTotalAmount_shouldReturnZero_whenMapIsEmpty() {

        Map<String, BigDecimal> amounts = new TreeMap<>();

        BigDecimal result = service.calculateTotalAmount(amounts);

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test 
    public void calculateTotalAmount_shouldReturnSumOfTheAmounts_whenCategoriesExists() {

        Map<String, BigDecimal> amounts = new TreeMap<>();
        amounts.put("food", BigDecimal.valueOf(115));
        amounts.put("transport", BigDecimal.valueOf(15));
        
        BigDecimal result = service.calculateTotalAmount(amounts);

        assertEquals(BigDecimal.valueOf(130), result);
    }

    @Test 
    public void countMonths_shouldReturnNumberOfMonthsWithExpenses_whenMonthExists() {

        Expense groceries = new Expense(LocalDate.of(2026, 3, 24), "food", "grocery-shopping", BigDecimal.valueOf(37));
        Expense foods = new Expense(LocalDate.of(2026, 3, 15), "food", "restaurant", BigDecimal.valueOf(78));
        Expense transports = new Expense(LocalDate.of(2026, 5, 18), "transport", "bus", BigDecimal.valueOf(15));
        expenses.add(groceries);
        expenses.add(foods);
        expenses.add(transports);
        
        int result = service.countMonths(expenses);

        assertEquals(2, result);
    }

    @Test 
    public void calculateMonthlyAverageByCategories_shouldReturnMapEmpty_whenThereAreZeroMonths() {

        Map<String, BigDecimal> amounts = new TreeMap<>();
        amounts.put("transport", BigDecimal.valueOf(15));

        Map<String, BigDecimal> result = service.calculateMonthlyAverageByCategories(amounts, 0);
        
        assertTrue(result.isEmpty());
    }

    @Test 
    public void calculateMonthlyAverageByCategories_shouldReturnRoundedDivision_whenAmountPerMultipleMonths() {
        
        Map<String, BigDecimal> amounts = new TreeMap<>();
        amounts.put("food", BigDecimal.valueOf(100));

        Map<String, BigDecimal> result = service.calculateMonthlyAverageByCategories(amounts, 3);

        assertEquals(BigDecimal.valueOf(33.33), result.get("food"));

    }

    @Test 
    public void removeBudget_shouldReturnExpensesOutsideTheBudget_whenBudgetExists() {

        Expense budgets = new Expense(LocalDate.of(2026, 3, 24), "budget", "salary", BigDecimal.valueOf(2000));
        Expense foods = new Expense(LocalDate.of(2026, 3, 15), "food", "restaurant", BigDecimal.valueOf(78));
        Expense transports = new Expense(LocalDate.of(2026, 5, 18), "transport", "bus", BigDecimal.valueOf(15));
        expenses.add(budgets);
        expenses.add(foods);
        expenses.add(transports);

        List<Expense> result = service.removeBudget(expenses);

        assertEquals(2, result.size());
        assertFalse(result.contains(budgets));
    }

    @Test 
    public void removeBudget_shouldReturnAllExpenses_whenBudgetDoesNotExist() {

        Expense groceries = new Expense(LocalDate.of(2026, 3, 24), "grocerie", "grocery shopping", BigDecimal.valueOf(43));
        Expense foods = new Expense(LocalDate.of(2026, 3, 15), "food", "restaurant", BigDecimal.valueOf(78));
        Expense transports = new Expense(LocalDate.of(2026, 5, 18), "transport", "bus", BigDecimal.valueOf(15));
        expenses.add(groceries);
        expenses.add(foods);
        expenses.add(transports);

        List<Expense> result = service.removeBudget(expenses);

        assertEquals(3, result.size());
        assertTrue(result.contains(foods));
    }

    @Test 
    public void findBudget_shouldReturnAmountOfBudget_whenBudgetExists() {

        Expense foods = new Expense(LocalDate.of(2026, 3, 15), "food", "restaurant", BigDecimal.valueOf(78));
        Expense budgets = new Expense(LocalDate.of(2026, 3, 24), "budget", "salary", BigDecimal.valueOf(2000));
        Expense transports = new Expense(LocalDate.of(2026, 5, 18), "transport", "bus", BigDecimal.valueOf(15));
        expenses.add(foods);
        expenses.add(budgets);
        expenses.add(transports);

        BigDecimal result = service.findBudget(expenses);

        assertEquals(BigDecimal.valueOf(2000), result);
    }

    @Test 
    public void findBudget_shouldReturnZero_whenBudgetDoesNotExist() {

        Expense foods = new Expense(LocalDate.of(2026, 3, 15), "food", "restaurant", BigDecimal.valueOf(78));
        Expense transports = new Expense(LocalDate.of(2026, 5, 18), "transport", "bus", BigDecimal.valueOf(15));
        expenses.add(foods);
        expenses.add(transports);

        BigDecimal result = service.findBudget(expenses);

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test 
    public void calculateMonthlySaving_shouldReturnZero_whenThereIsNoMonths() {

        BigDecimal globalSaving = BigDecimal.valueOf(478);
        int numberOfMonths = 0;

        BigDecimal result = service.calculateMonthlySaving(globalSaving, numberOfMonths);

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test 
    public void calculateMonthlySaving_shouldReturnRoundedDivision_whenMonthsExists() {

        BigDecimal globalSaving = BigDecimal.valueOf(478);
        int numberOfMonths = 6;

        BigDecimal result = service.calculateMonthlySaving(globalSaving, numberOfMonths);

        assertEquals(BigDecimal.valueOf(79.67), result);
    }

    @Test 
    public void calculateAvailableMonthlySaving_shouldReturnAvailableMonthlySaving_whenBudgetAndExpensesExist() {

        Expense foods = new Expense(LocalDate.of(2026, 3, 15), "food", "restaurant", BigDecimal.valueOf(78));
        Expense housings = new Expense(LocalDate.of(2026, 3, 24), "housing", "rent", BigDecimal.valueOf(850));
        Expense budgets = new Expense(LocalDate.of(2026, 3, 29), "budget", "salary", BigDecimal.valueOf(2000));
        Expense transports = new Expense(LocalDate.of(2026, 5, 18), "transport", "bus", BigDecimal.valueOf(15));
        expenses.add(foods);
        expenses.add(housings);
        expenses.add(budgets);
        expenses.add(transports);

        BigDecimal result = service.calculateAvailableMonthlySaving(expenses);

        assertEquals(new BigDecimal("1528.50"), result);
    }
}
