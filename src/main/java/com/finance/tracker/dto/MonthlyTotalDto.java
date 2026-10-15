package com.finance.tracker.dto;

import java.math.BigDecimal;

public class MonthlyTotalDto {
    private Integer month;
    private BigDecimal total;

    public MonthlyTotalDto(Integer month, BigDecimal total) {
        this.month = month;
        this.total = total;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
