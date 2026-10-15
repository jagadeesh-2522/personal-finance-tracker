package com.finance.tracker.controller;

import com.finance.tracker.dto.CategoryTotalDto;
import com.finance.tracker.dto.MonthlyTotalDto;
import com.finance.tracker.dto.SummaryDto;
import com.finance.tracker.model.Expense;
import com.finance.tracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    // Constructor-based dependency injection
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    // POST /api/expenses — create a new expense, return 201 Created
    @PostMapping
    public ResponseEntity<Expense> createExpense(@Valid @RequestBody Expense expense) {
        Expense saved = expenseService.createExpense(expense);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // GET /api/expenses — return all expenses, return 200 OK
    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses() {
        List<Expense> expenses = expenseService.getAllExpenses();
        return ResponseEntity.ok(expenses);
    }

    // GET /api/expenses/{id} — return one expense by ID, return 200 OK or 404
    // Exceptions are handled by GlobalExceptionHandler
    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
        Expense expense = expenseService.getExpenseById(id);
        return ResponseEntity.ok(expense);
    }

    // PUT /api/expenses/{id} — update an existing expense, return 200 OK or 404
    // Exceptions are handled by GlobalExceptionHandler
    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long id,
                                                  @Valid @RequestBody Expense updatedExpense) {
        Expense expense = expenseService.updateExpense(id, updatedExpense);
        return ResponseEntity.ok(expense);
    }

    // DELETE /api/expenses/{id} — delete an expense, return 204 No Content or 404
    // Exceptions are handled by GlobalExceptionHandler
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

    // Analytics endpoints

    // GET /api/expenses/analytics/monthly — return monthly totals for current year
    @GetMapping("/analytics/monthly")
    public ResponseEntity<List<MonthlyTotalDto>> getMonthlyTotals() {
        List<MonthlyTotalDto> result = expenseService.getMonthlyTotals();
        return ResponseEntity.ok(result);
    }

    // GET /api/expenses/analytics/categories — return totals per category
    @GetMapping("/analytics/categories")
    public ResponseEntity<List<CategoryTotalDto>> getCategoryTotals() {
        List<CategoryTotalDto> result = expenseService.getCategoryTotals();
        return ResponseEntity.ok(result);
    }

    // GET /api/expenses/analytics/summary — return overall spending summary
    @GetMapping("/analytics/summary")
    public ResponseEntity<SummaryDto> getSummary() {
        SummaryDto result = expenseService.getSummary();
        return ResponseEntity.ok(result);
    }
}
