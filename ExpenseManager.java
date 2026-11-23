import java.util.ArrayList;

public class ExpenseManager {
    
    private ArrayList<Expense> expenses;
    
    public ExpenseManager() {
        expenses = new ArrayList<Expense>();
    }
    
    public void addExpense(Expense expense) {
        expenses.add(expense);
    }
    
    public void displayAllExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
            return;
        }
        
        System.out.println("All Expenses:");
        System.out.println("---------------------------------------------");
        for (int i = 0; i < expenses.size(); i++) {
            Expense exp = expenses.get(i);
            System.out.println("Title: " + exp.getTitle());
            System.out.println("Amount: " + exp.getAmount());
            System.out.println("Category: " + exp.getCategory());
            System.out.println("Date: " + exp.getDate());
            System.out.println("---------------------------------------------");
        }
    }
    
    public double calculateTotalAmount() {
        double total = 0;
        for (int i = 0; i < expenses.size(); i++) {
            total = total + expenses.get(i).getAmount();
        }
        return total;
    }
}
