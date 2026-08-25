package com.openclassrooms.p2dfsbea;

import com.openclassrooms.p2dfsbea.model.Expense;
import com.openclassrooms.p2dfsbea.repository.ExpenseRepository;
import com.openclassrooms.p2dfsbea.service.ExpenseService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ExpenseServiceTest {

    @Test
    public void testTotalPerCategory() {
        Expense e1 = new Expense(LocalDate.of(2024,1,1), "housing", "rent", new BigDecimal("500.00"));
        Expense e2 = new Expense(LocalDate.of(2024,1,5), "food", "groceries", new BigDecimal("45.30"));
        Expense e3 = new Expense(LocalDate.of(2024,1,10), "housing", "utilities", new BigDecimal("75.00"));

        ExpenseRepository repo = new ExpenseRepository();
        ExpenseService service = new ExpenseService(repo);

        Map<String, BigDecimal> totals = service.totalPerCategory(List.of(e1, e2, e3));
        Assertions.assertEquals(new BigDecimal("575.00"), totals.get("housing"));
        Assertions.assertEquals(new BigDecimal("45.30"), totals.get("food"));
    }

    @Test
    public void testBorrowingCapacityZeroRate() {
        ExpenseRepository repo = new ExpenseRepository();
        ExpenseService service = new ExpenseService(repo);

        BigDecimal monthly = new BigDecimal("1000.00");
        BigDecimal rate = BigDecimal.ZERO;
        BigDecimal capacity = service.borrowingCapacity(monthly, rate, 20);
        Assertions.assertEquals(new BigDecimal("240000.00"), capacity);
    }
}
