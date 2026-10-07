package com.openclassrooms.p2dfsbea.service;

import com.openclassrooms.p2dfsbea.model.Expense;
import com.openclassrooms.p2dfsbea.repository.ExpenseRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.*;

public class ExpenseService {
    private final ExpenseRepository repository;

    public ExpenseService(ExpenseRepository repository) {
        this.repository = repository;
    }

    public List<Expense> loadAllExpenses() {
        return repository.readAll();
    }

    public void addExpense(Expense expense) throws IOException {
        repository.append(expense);
    }

    public Map<String, BigDecimal> calculateAmountForCategories(List<Expense> expenses) {
        if (expenses.isEmpty()) {
           return new TreeMap<>();
        }
        TreeMap<String, BigDecimal> totalsByCategories = new TreeMap<>(); 
        for (Expense expense : expenses) {
            if (totalsByCategories.containsKey(expense.getCategory())) {
                BigDecimal oldTotal = totalsByCategories.get(expense.getCategory());
                BigDecimal newTotal = oldTotal.add(expense.getAmount());
                totalsByCategories.put(expense.getCategory(), newTotal);
            } else {
                totalsByCategories.put(expense.getCategory(), expense.getAmount());
            }
        }
        return totalsByCategories;
    }

    public BigDecimal calculateTotalAmount(Map<String, BigDecimal> allAmountByCategories) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (BigDecimal categoryAmount : allAmountByCategories.values()) {
           totalAmount = totalAmount.add(categoryAmount);
        }
        return totalAmount;
    }

    public int countMonths(List<Expense> expenses) {
        HashSet<YearMonth> allMonth = new HashSet<>();
        for (Expense expense : expenses) {
            allMonth.add(YearMonth.from(expense.getDate()));
        }
        return allMonth.size();
    }

    public Map<String, BigDecimal> calculateMonthlyAverageByCategories(Map<String, BigDecimal> allAmountByCategories, int numberOfMonths) {
        if (numberOfMonths == 0) {
            return new TreeMap<>();
        }
        TreeMap<String, BigDecimal> averageByCategory = new TreeMap<>();
        BigDecimal numberOfMonthBigDecimal = BigDecimal.valueOf(numberOfMonths);
        for (Map.Entry<String, BigDecimal> amountByCategoryEntry : allAmountByCategories.entrySet()) {
            BigDecimal average = amountByCategoryEntry.getValue().divide(numberOfMonthBigDecimal, 2, RoundingMode.HALF_UP);
            averageByCategory.put(amountByCategoryEntry.getKey(), average);
        }
        return averageByCategory;
    }

    // public Map<String, BigDecimal> calculatePercentageByCategories(Map<String, BigDecimal> allAmountByCategories, BigDecimal totalAmount) {
    // }
}
