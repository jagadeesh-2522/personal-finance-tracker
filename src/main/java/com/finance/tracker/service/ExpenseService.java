package com.finance.tracker.service;

import com.finance.tracker.dto.CategoryTotalDto;
import com.finance.tracker.dto.MonthlyTotalDto;
import com.finance.tracker.dto.SummaryDto;
import com.finance.tracker.model.Expense;
import com.finance.tracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    // Constructor-based dependency injection — Spring automatically passes
    // the ExpenseRepository implementation when creating this class
    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    // Save a new expense to the database
    public Expense createExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    // Retrieve all expenses from the database
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    // Retrieve a single expense by its ID
    // Throws an exception if the ID does not exist — never returns null
    public Expense getExpenseById(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));
    }

    // Update an existing expense — only modifiable fields are updated
    // createdAt is intentionally left alone (it is a read-only audit field)
    public Expense updateExpense(Long id, Expense updatedExpense) {
        Expense existing = getExpenseById(id); // reuses the not-found check above

        existing.setTitle(updatedExpense.getTitle());
        existing.setAmount(updatedExpense.getAmount());
        existing.setCategory(updatedExpense.getCategory());
        existing.setExpenseDate(updatedExpense.getExpenseDate());
        existing.setDescription(updatedExpense.getDescription());

        return expenseRepository.save(existing);
    }

    // Delete an expense by ID
    // Throws an exception if the ID does not exist instead of silently doing nothing
    public void deleteExpense(Long id) {
        if (!expenseRepository.existsById(id)) {
            throw new RuntimeException("Expense not found with id: " + id);
        }
        expenseRepository.deleteById(id);
    }

    // Analytics methods

    // Get monthly totals for the current calendar year
    // Returns all 12 months, with zero for months that have no expenses
    public List<MonthlyTotalDto> getMonthlyTotals() {
        List<Expense> yearExpenses = expenseRepository.getExpensesByCurrentYear();

        // Group expenses by month
        Map<Integer, BigDecimal> monthlyMap = yearExpenses.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getExpenseDate().getMonthValue(),
                        Collectors.reducing(BigDecimal.ZERO,
                                Expense::getAmount,
                                BigDecimal::add)
                ));

        // Create result list with all 12 months
        List<MonthlyTotalDto> result = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            BigDecimal total = monthlyMap.getOrDefault(month, BigDecimal.ZERO);
            result.add(new MonthlyTotalDto(month, total));
        }

        return result;
    }

    // Get total spending per category
    public List<CategoryTotalDto> getCategoryTotals() {
        List<Object[]> results = expenseRepository.getTotalByCategory();

        return results.stream()
                .map(row -> new CategoryTotalDto(
                        (String) row[0],
                        (BigDecimal) row[1]))
                .collect(Collectors.toList());
    }

    // Get overall spending summary
    public SummaryDto getSummary() {
        BigDecimal totalSpending = expenseRepository.getTotalSpending();
        Long totalExpenses = expenseRepository.count();
        BigDecimal averageExpense = totalExpenses > 0 ? 
                expenseRepository.getAverageExpense() : BigDecimal.ZERO;

        return new SummaryDto(totalSpending, totalExpenses, averageExpense);
    }
}
