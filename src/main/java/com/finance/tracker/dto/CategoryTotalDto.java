package com.finance.tracker.dto;

import java.math.BigDecimal;

public class CategoryTotalDto {
    private String category;
    private BigDecimal total;

    public CategoryTotalDto(String category, BigDecimal total) {
        this.category = category;
        this.total = total;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
