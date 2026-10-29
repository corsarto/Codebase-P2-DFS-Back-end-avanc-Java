package com.openclassrooms.p2dfsbea.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExpenseServiceCar {
    
    public static final int NO_SAVINGS = -1;

    public int calculateMonthsNeeded(BigDecimal price, BigDecimal monthlySaving) {
        if (monthlySaving.compareTo(BigDecimal.ZERO) <= 0) {
            return NO_SAVINGS;
        }
       int numberOfMonth = price.divide(monthlySaving, 0, RoundingMode.UP).intValue();
       return numberOfMonth;
    }
}
