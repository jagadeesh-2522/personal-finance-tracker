package com.finance.tracker.dto;

import java.math.BigDecimal;

public class SummaryDto {
    private BigDecimal totalSpending;
    private Long totalExpenses;
    private BigDecimal averageExpense;

    public SummaryDto(BigDecimal totalSpending, Long totalExpenses, BigDecimal averageExpense) {
        this.totalSpending = totalSpending;
        this.totalExpenses = totalExpenses;
        this.averageExpense = averageExpense;
    }

    public BigDecimal getTotalSpending() {
        return totalSpending;
    }

    public void setTotalSpending(BigDecimal totalSpending) {
        this.totalSpending = totalSpending;
    }

    public Long getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(Long totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public BigDecimal getAverageExpense() {
        return averageExpense;
    }

    public void setAverageExpense(BigDecimal averageExpense) {
        this.averageExpense = averageExpense;
    }
}
