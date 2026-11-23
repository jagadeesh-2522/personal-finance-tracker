public class Expense {
    
    // Private fields to store expense details
    private long expenseId;
    private String title;
    private double amount;
    private String category;
    private String date;
    
    // Constructor to initialize all fields (with expenseId)
    public Expense(long expenseId, String title, double amount, String category, String date) {
        this.expenseId = expenseId;
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }
    
    // Constructor to initialize without expenseId (for new expenses before saving to DB)
    public Expense(String title, double amount, String category, String date) {
        this.expenseId = -1;
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }
    
    // Getter for expense ID
    public long getExpenseId() {
        return expenseId;
    }
    
    // Getter for title
    public String getTitle() {
        return title;
    }
    
    // Getter for amount
    public double getAmount() {
        return amount;
    }
    
    // Getter for category
    public String getCategory() {
        return category;
    }
    
    // Getter for date
    public String getDate() {
        return date;
    }
}
