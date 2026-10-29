package com.openclassrooms.p2dfsbea.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import com.openclassrooms.p2dfsbea.model.Expense;

public class ExpenseServiceLoan {
    private static final String HOUSING = "housing";
    private static final BigDecimal MAX_DEBT_RATIO = BigDecimal.valueOf(0.35);
    private final ExpenseService service;

    public ExpenseServiceLoan(ExpenseService service) {
        this.service = service;
    }

    public BigDecimal calculateAmountAvailableForLoan(List<Expense> expenses) {
        BigDecimal monthlySaving = service.calculateAvailableMonthlySaving(expenses);
        List<Expense> allCategoryExceptBudget = service.removeBudget(expenses);
        Map<String, BigDecimal> totalsByCategories = service.calculateAmountForCategories(allCategoryExceptBudget); 
        int allMonths = service.countMonths(allCategoryExceptBudget);
        Map<String, BigDecimal> averageByCategory = service.calculateMonthlyAverageByCategories(totalsByCategories, allMonths);
        BigDecimal monthlyRent = averageByCategory.getOrDefault(HOUSING, BigDecimal.ZERO);
        return monthlySaving.add(monthlyRent);
    }

    public BigDecimal calculateMonthlyPayment(BigDecimal amountAvailableForLoan, BigDecimal budget) {
        BigDecimal paymentLimit = budget.multiply(MAX_DEBT_RATIO).setScale(2, RoundingMode.HALF_UP);
        return paymentLimit.min(amountAvailableForLoan);
    }

    public BigDecimal calculateTotalLoanAmount(BigDecimal monthlyPayment, BigDecimal borrowingRate, int loanTermInYears) {
        int loanTermInMonths = loanTermInYears * 12;
        BigDecimal totalRefund = monthlyPayment.multiply(BigDecimal.valueOf(loanTermInMonths));
        BigDecimal rateAsAPercentageInDecimal = borrowingRate.divide(BigDecimal.valueOf(100));
        BigDecimal ratePerYear = rateAsAPercentageInDecimal.multiply(BigDecimal.valueOf(loanTermInYears));
        BigDecimal ratePerYearDivideByHalf = ratePerYear.divide(BigDecimal.valueOf(2));
        BigDecimal ratePerYearDivideByHalfPlusOne = ratePerYearDivideByHalf.add(BigDecimal.valueOf(1));
        BigDecimal totalLoanAmount = totalRefund.divide(ratePerYearDivideByHalfPlusOne, 2, RoundingMode.HALF_UP);
        return totalLoanAmount;
    }
}
