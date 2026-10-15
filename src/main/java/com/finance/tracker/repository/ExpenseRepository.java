package com.finance.tracker.repository;

import com.finance.tracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // JpaRepository already provides:
    // save(expense)        → INSERT or UPDATE
    // findById(id)         → SELECT WHERE id = ?
    // findAll()            → SELECT * FROM expenses
    // deleteById(id)       → DELETE WHERE id = ?
    // existsById(id)       → SELECT COUNT(*) WHERE id = ?
    // count()              → SELECT COUNT(*) FROM expenses

    // Analytics queries

    // Get total spending per category
    @Query("SELECT e.category AS category, SUM(e.amount) AS total " +
           "FROM Expense e GROUP BY e.category ORDER BY total DESC")
    List<Object[]> getTotalByCategory();

    // Get total spending for all expenses
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e")
    BigDecimal getTotalSpending();

    // Get average expense amount
    @Query("SELECT COALESCE(AVG(e.amount), 0) FROM Expense e")
    BigDecimal getAverageExpense();

    // Get all expenses grouped by month of the current year
    @Query("SELECT e FROM Expense e WHERE YEAR(e.expenseDate) = YEAR(CURRENT_DATE) " +
           "ORDER BY e.expenseDate ASC")
    List<Expense> getExpensesByCurrentYear();
}

