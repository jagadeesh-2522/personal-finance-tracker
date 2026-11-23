import java.util.ArrayList;
import java.sql.SQLException;

public class TestDatabaseExpenseManager {
    
    public static void main(String[] args) {
        System.out.println("=== Testing DatabaseExpenseManager ===\n");
        
        try {
            System.out.println("Step 1: Retrieving categories from database...");
            ArrayList<String> categories = DatabaseExpenseManager.getCategories();
            System.out.println("Categories found: " + categories.size());
            for (String category : categories) {
                System.out.println("  - " + category);
            }
            System.out.println();
            
            System.out.println("Step 2: Creating and saving a test expense...");
            Expense testExpense = new Expense("TEST_LUNCH_27OCT2026", 350.75, "Food", "2026-10-27");
            DatabaseExpenseManager.saveExpense(testExpense);
            System.out.println("Test expense saved successfully!");
            System.out.println("  Title: " + testExpense.getTitle());
            System.out.println("  Amount: Rs. " + testExpense.getAmount());
            System.out.println("  Category: " + testExpense.getCategory());
            System.out.println("  Date: " + testExpense.getDate());
            System.out.println();
            
            System.out.println("Step 3: Retrieving all expenses from database...");
            ArrayList<Expense> allExpenses = DatabaseExpenseManager.getAllExpenses();
            System.out.println("Total expenses in database: " + allExpenses.size());
            System.out.println();
            
            System.out.println("Recent expenses (showing first 5):");
            int displayCount = Math.min(5, allExpenses.size());
            for (int i = 0; i < displayCount; i++) {
                Expense exp = allExpenses.get(i);
                System.out.println("  " + (i + 1) + ". Title: " + exp.getTitle() + 
                                 " | Amount: Rs. " + exp.getAmount() + 
                                 " | Category: " + exp.getCategory() + 
                                 " | Date: " + exp.getDate());
            }
            System.out.println();
            
            boolean testExpenseFound = false;
            for (Expense exp : allExpenses) {
                if (exp.getTitle().equals("TEST_LUNCH_27OCT2026")) {
                    testExpenseFound = true;
                    break;
                }
            }
            
            if (testExpenseFound) {
                System.out.println("SUCCESS: Test expense was saved and retrieved from database!");
                System.out.println("\nTo delete this test expense from Supabase:");
                System.out.println("  1. Go to Supabase dashboard");
                System.out.println("  2. Open the 'expenses' table");
                System.out.println("  3. Find the row with title 'TEST_LUNCH_27OCT2026'");
                System.out.println("  4. Click the delete button or use SQL:");
                System.out.println("     DELETE FROM expenses WHERE title = 'TEST_LUNCH_27OCT2026';");
            } else {
                System.out.println("WARNING: Test expense not found in retrieved data.");
            }
            
        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
