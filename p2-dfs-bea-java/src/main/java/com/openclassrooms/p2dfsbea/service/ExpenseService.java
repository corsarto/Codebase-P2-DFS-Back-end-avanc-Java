package com.openclassrooms.p2dfsbea.service;

import com.openclassrooms.p2dfsbea.model.Expense;
import com.openclassrooms.p2dfsbea.repository.ExpenseRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

public class ExpenseService {
    public static final String CATEGORY_HOUSING = "housing";
    public static final BigDecimal MAX_DEBT_RATIO = new BigDecimal("0.35"); // 35%

    private final ExpenseRepository repository;

    public ExpenseService(ExpenseRepository repository) {
        this.repository = repository;
    }

    public List<Expense> loadAllExpenses() {
        return repository.readAll();
    }

    public Map<String, BigDecimal> totalPerCategory(List<Expense> list) {
        Map<String, BigDecimal> totals = new HashMap<>();
        for (Expense e : list) {
            totals.merge(e.getCategory(), e.getAmount(), BigDecimal::add);
        }
        return totals;
    }

    public Set<String> distinctMonths(List<Expense> list) {
        return list.stream()
                .map(Expense::getDate)
                .filter(Objects::nonNull)
                .map(d -> YearMonth.from(d).toString())
                .collect(Collectors.toSet());
    }

    public long monthsToBuyCar(BigDecimal price, BigDecimal monthlySaving) {
        if (monthlySaving.compareTo(BigDecimal.ZERO) <= 0) return Long.MAX_VALUE;
        BigDecimal months = price.divide(monthlySaving, 0, RoundingMode.CEILING);
        return months.longValueExact();
    }

    /**
     * Compute borrowing capacity (maximum principal) given a monthly payment, annual rate (percent), and years.
     * Formula: principal = payment * (1 - (1 + r)^-n) / r
     * r = monthlyRate (decimal), n = months
     * Allows zero interest (r == 0) as a special case: principal = payment * n
     */
    public BigDecimal borrowingCapacity(BigDecimal monthlyPayment, BigDecimal annualRatePercent, int years) {
        if (monthlyPayment == null || annualRatePercent == null) return BigDecimal.ZERO;
        BigDecimal months = BigDecimal.valueOf(years * 12L);
        BigDecimal monthlyRate = annualRatePercent.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
            return monthlyPayment.multiply(months).setScale(2, RoundingMode.HALF_UP);
        }
        // (1 + r)^-n
        double factor = Math.pow(1.0 + monthlyRate.doubleValue(), -months.doubleValue());
        BigDecimal denominator = monthlyRate;
        BigDecimal numerator = BigDecimal.ONE.subtract(BigDecimal.valueOf(factor));
        BigDecimal principal = monthlyPayment.multiply(numerator).divide(denominator, 2, RoundingMode.HALF_UP);
        return principal;
    }

    public void addExpense(Expense expense) throws IOException {
        repository.append(expense);
    }
}
