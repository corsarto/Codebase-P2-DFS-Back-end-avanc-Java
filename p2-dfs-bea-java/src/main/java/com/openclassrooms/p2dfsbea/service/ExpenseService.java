package com.openclassrooms.p2dfsbea.service;

import com.openclassrooms.p2dfsbea.model.Expense;
import com.openclassrooms.p2dfsbea.repository.ExpenseRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.*;

public class ExpenseService {
    private final ExpenseRepository repository;
    private static final BigDecimal MAX_DEBT_RATIO = BigDecimal.valueOf(0.35);
    private static final BigDecimal ANNUAL_PERCENT_TO_MONTHLY_RATE = BigDecimal.valueOf(1200);

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
        Map<String, BigDecimal> totalsByCategories = new TreeMap<>(); 
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
        Set<YearMonth> allMonth = new HashSet<>();
        for (Expense expense : expenses) {
            allMonth.add(YearMonth.from(expense.getDate()));
        }
        return allMonth.size();
    }

    public Map<String, BigDecimal> calculateMonthlyAverageByCategories(Map<String, BigDecimal> allAmountByCategories, int numberOfMonths) {
        if (numberOfMonths == 0) {
            return new TreeMap<>();
        }
        Map<String, BigDecimal> averageByCategory = new TreeMap<>();
        BigDecimal numberOfMonthBigDecimal = BigDecimal.valueOf(numberOfMonths);
        for (Map.Entry<String, BigDecimal> amountByCategoryEntry : allAmountByCategories.entrySet()) {
            BigDecimal average = amountByCategoryEntry.getValue().divide(numberOfMonthBigDecimal, 2, RoundingMode.HALF_UP);
            averageByCategory.put(amountByCategoryEntry.getKey(), average);
        }
        return averageByCategory;
    }

    public List<Expense> removeBudget(List<Expense> expenses) {
        List<Expense> allCategoryExceptBudget = new ArrayList<Expense>();
            for (Expense expense : expenses) {
                if (!expense.getCategory().equals("budget"))  {
                    allCategoryExceptBudget.add(expense);
                }
            }
        return allCategoryExceptBudget;
    }

    public BigDecimal findBudget(List<Expense> expenses) {
        for (Expense expense : expenses) {
            if (expense.getCategory().equals("budget")) {
                return expense.getAmount();
            }
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal calculateGlobalBudget(BigDecimal startBudget, int numberOfMonths) {
        BigDecimal numberOfMonthBigDecimal = BigDecimal.valueOf(numberOfMonths);
        return startBudget.multiply(numberOfMonthBigDecimal);
    }

    public BigDecimal calculateGlobalSaving(BigDecimal globalBudget, BigDecimal totalAmount) {
        return globalBudget.subtract(totalAmount);
    }

    public BigDecimal calculateMonthlySaving(BigDecimal globalSaving, int numberOfMonths) {
        if (numberOfMonths == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal numberOfMonthBigDecimal = BigDecimal.valueOf(numberOfMonths);
        return globalSaving.divide(numberOfMonthBigDecimal, 2, RoundingMode.HALF_UP);
    }

    public int calculateMonthsNeeded(BigDecimal price, BigDecimal monthlySaving) {
        if (monthlySaving.compareTo(BigDecimal.ZERO) <= 0) {
            return -1;
        }
       int numberOfMonth = price.divide(monthlySaving, 0, RoundingMode.UP).intValue();
       return numberOfMonth;
    }

    public BigDecimal calculateAvailableMonthlySaving(List<Expense> expenses) {
        BigDecimal budget = findBudget(expenses);
        List<Expense> onlyExpenses = removeBudget(expenses);
        int months = countMonths(onlyExpenses);
        BigDecimal totalAmount = calculateTotalAmount(calculateAmountForCategories(onlyExpenses));
        BigDecimal globalBudget = calculateGlobalBudget(budget, months);
        BigDecimal globalSaving = calculateGlobalSaving(globalBudget, totalAmount);
        return calculateMonthlySaving(globalSaving, months);
    }

    public boolean isPossibleToBuyWithDebtRatio(BigDecimal monthlyPayment, BigDecimal budget) {
        BigDecimal paymentLimit = budget.multiply(MAX_DEBT_RATIO);
        if (monthlyPayment.compareTo(paymentLimit) <= 0) {
            return true;
        }
        return false;
    }

    public BigDecimal calculateAmountAvailableForLoan(List<Expense> expenses) {
        BigDecimal monthlySaving = calculateAvailableMonthlySaving(expenses);
        List<Expense> allCategoryExceptBudget = removeBudget(expenses);
        Map<String, BigDecimal> totalsByCategories = calculateAmountForCategories(allCategoryExceptBudget); 
        int allMonths = countMonths(allCategoryExceptBudget);
        Map<String, BigDecimal> averageByCategory = calculateMonthlyAverageByCategories(totalsByCategories, allMonths);
        BigDecimal monthlyRent = averageByCategory.getOrDefault("housing", BigDecimal.ZERO);
        return monthlySaving.add(monthlyRent);
    }

    public boolean isPossibleToBuyWithExpenses(BigDecimal monthlyPayment, BigDecimal amountAvailableForLoan) {
        if (monthlyPayment.compareTo(amountAvailableForLoan) <= 0) {
            return true;
        }
        return false;
    }

    public BigDecimal calculateTotalLoanAmount(BigDecimal monthlyPayment, BigDecimal borrowingRate, int loanTermInYears) {
        int loanTermInMonths = loanTermInYears * 12;

        if (borrowingRate.compareTo(BigDecimal.ZERO) == 0) {
            return monthlyPayment.multiply(BigDecimal.valueOf(loanTermInMonths)).setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal monthlyBorrowingRate = borrowingRate.divide(ANNUAL_PERCENT_TO_MONTHLY_RATE, 8, RoundingMode.HALF_UP);
        BigDecimal onePlusMonthlyRate = monthlyBorrowingRate.add(BigDecimal.ONE);
        BigDecimal onePlusRatePowNegativeMonths = onePlusMonthlyRate.pow(-loanTermInMonths, MathContext.DECIMAL64);
        BigDecimal oneMinusRatePowNegativeMonths = BigDecimal.ONE.subtract(onePlusRatePowNegativeMonths);
        BigDecimal annuityFactor = oneMinusRatePowNegativeMonths.divide(monthlyBorrowingRate, 8, RoundingMode.HALF_UP);
        BigDecimal totalLoanAmount = monthlyPayment.multiply(annuityFactor);
        return totalLoanAmount.setScale(2, RoundingMode.HALF_UP);
    }
}
