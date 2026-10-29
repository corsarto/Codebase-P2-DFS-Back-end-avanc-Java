package com.openclassrooms.p2dfsbea.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ExpenseServiceCarTest {

    private ExpenseServiceCar serviceCar;

    @BeforeEach 
    void setUp() {
        serviceCar = new ExpenseServiceCar();
    }

    @Test 
    public void calculateMonthsNeeded_shouldReturnNegativeNumber_whenMonthlySavingsAreEqualToOrLessThanZero() {

        BigDecimal monthlySavingNegative = BigDecimal.valueOf(-23);
        BigDecimal monthlySavingZero = BigDecimal.ZERO;
        BigDecimal price = BigDecimal.valueOf(2345);

        int resultNegative = serviceCar.calculateMonthsNeeded(price, monthlySavingNegative);
        int resultZero = serviceCar.calculateMonthsNeeded(price, monthlySavingZero);

        assertEquals(ExpenseServiceCar.NO_SAVINGS, resultNegative);
        assertEquals(ExpenseServiceCar.NO_SAVINGS, resultZero);
    }

    @Test 
    public void calculateMonthsNeeded_shouldReturnRoundedDivision_whenMonthlySavingAndMonthsExist() {

        BigDecimal monthlySaving = BigDecimal.valueOf(552);
        BigDecimal price = BigDecimal.valueOf(10000);

        int result = serviceCar.calculateMonthsNeeded(price, monthlySaving);

        assertEquals(19, result);
    }
}
