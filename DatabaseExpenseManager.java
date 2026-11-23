import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;

public class DatabaseExpenseManager {
    
    public static void saveExpense(Expense expense) throws SQLException {
        String categoryName = expense.getCategory();
        String title = expense.getTitle();
        double amount = expense.getAmount();
        String date = expense.getDate();
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            
            String getCategoryIdQuery = "SELECT category_id FROM categories WHERE category_name = ?";
            long categoryId = -1;
            
            try (PreparedStatement getCategoryStmt = connection.prepareStatement(getCategoryIdQuery)) {
                getCategoryStmt.setString(1, categoryName);
                
                try (ResultSet rs = getCategoryStmt.executeQuery()) {
                    if (rs.next()) {
                        categoryId = rs.getLong("category_id");
                    }
                }
            }
            
            if (categoryId == -1) {
                throw new SQLException("Category '" + categoryName + "' not found in database.");
            }
            
            Date sqlDate;
            try {
                sqlDate = Date.valueOf(date);
            } catch (IllegalArgumentException e) {
                throw new SQLException("Invalid date format. Use YYYY-MM-DD format. Received: " + date, e);
            }
            
            String insertExpenseQuery = "INSERT INTO expenses (title, amount, category_id, expense_date) VALUES (?, ?, ?, ?)";
            
            try (PreparedStatement insertStmt = connection.prepareStatement(insertExpenseQuery)) {
                insertStmt.setString(1, title);
                insertStmt.setDouble(2, amount);
                insertStmt.setLong(3, categoryId);
                insertStmt.setDate(4, sqlDate);
                
                insertStmt.executeUpdate();
            }
            
        } catch (SQLException e) {
            throw new SQLException("Error saving expense: " + e.getMessage(), e);
        }
    }
    
    public static ArrayList<Expense> getAllExpenses() throws SQLException {
        ArrayList<Expense> expenses = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            
            String query = "SELECT e.expense_id, e.title, e.amount, c.category_name, e.expense_date " +
                          "FROM expenses e " +
                          "JOIN categories c ON e.category_id = c.category_id " +
                          "ORDER BY e.expense_id DESC";
            
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        long expenseId = rs.getLong("expense_id");
                        String title = rs.getString("title");
                        double amount = rs.getDouble("amount");
                        String categoryName = rs.getString("category_name");
                        String date = rs.getString("expense_date");
                        
                        Expense expense = new Expense(expenseId, title, amount, categoryName, date);
                        expenses.add(expense);
                    }
                }
            }
            
        } catch (SQLException e) {
            throw new SQLException("Error retrieving expenses: " + e.getMessage(), e);
        }
        
        return expenses;
    }
    
    public static ArrayList<String> getCategories() throws SQLException {
        ArrayList<String> categories = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            
            String query = "SELECT category_name FROM categories ORDER BY category_name";
            
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        categories.add(rs.getString("category_name"));
                    }
                }
            }
            
        } catch (SQLException e) {
            throw new SQLException("Error retrieving categories: " + e.getMessage(), e);
        }
        
        return categories;
    }
    
    public static ArrayList<TransactionData> getTransactionChartData() throws SQLException {
        ArrayList<TransactionData> transactionData = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            
            String query = "SELECT expense_id, amount FROM expenses ORDER BY expense_id ASC";
            
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        long transactionId = rs.getLong("expense_id");
                        double amount = rs.getDouble("amount");
                        
                        TransactionData data = new TransactionData(transactionId, amount);
                        transactionData.add(data);
                    }
                }
            }
            
        } catch (SQLException e) {
            throw new SQLException("Error retrieving transaction chart data: " + e.getMessage(), e);
        }
        
        return transactionData;
    }
    
    public static ArrayList<CategoryExpenseData> getExpenseByCategory() throws SQLException {
        ArrayList<CategoryExpenseData> categoryData = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            
            String query = "SELECT c.category_name, SUM(e.amount) as total_amount " +
                          "FROM expenses e " +
                          "JOIN categories c ON e.category_id = c.category_id " +
                          "GROUP BY c.category_name " +
                          "ORDER BY total_amount DESC";
            
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        String categoryName = rs.getString("category_name");
                        double totalAmount = rs.getDouble("total_amount");
                        
                        CategoryExpenseData data = new CategoryExpenseData(categoryName, totalAmount);
                        categoryData.add(data);
                    }
                }
            }
            
        } catch (SQLException e) {
            throw new SQLException("Error retrieving expense by category data: " + e.getMessage(), e);
        }
        
        return categoryData;
    }
    
    public static void deleteExpense(long expenseId) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            
            String query = "DELETE FROM expenses WHERE expense_id = ?";
            
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setLong(1, expenseId);
                int rowsAffected = statement.executeUpdate();
                
                if (rowsAffected == 0) {
                    throw new SQLException("No expense found with ID: " + expenseId);
                }
            }
            
        } catch (SQLException e) {
            throw new SQLException("Error deleting expense: " + e.getMessage(), e);
        }
    }
    
    public static ArrayList<Expense> getExpensesByCategory(String categoryName) throws SQLException {
        ArrayList<Expense> expenses = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            
            String query = "SELECT e.expense_id, e.title, e.amount, c.category_name, e.expense_date " +
                          "FROM expenses e " +
                          "JOIN categories c ON e.category_id = c.category_id " +
                          "WHERE c.category_name = ? " +
                          "ORDER BY e.expense_id DESC";
            
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, categoryName);
                
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        long expenseId = rs.getLong("expense_id");
                        String title = rs.getString("title");
                        double amount = rs.getDouble("amount");
                        String category = rs.getString("category_name");
                        String date = rs.getString("expense_date");
                        
                        Expense expense = new Expense(expenseId, title, amount, category, date);
                        expenses.add(expense);
                    }
                }
            }
            
        } catch (SQLException e) {
            throw new SQLException("Error retrieving expenses by category: " + e.getMessage(), e);
        }
        
        return expenses;
    }
    
    public static ArrayList<Expense> getExpensesByDate(String startDate, String endDate) throws SQLException {
        ArrayList<Expense> expenses = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            
            String query = "SELECT e.expense_id, e.title, e.amount, c.category_name, e.expense_date " +
                          "FROM expenses e " +
                          "JOIN categories c ON e.category_id = c.category_id " +
                          "WHERE e.expense_date BETWEEN ? AND ? " +
                          "ORDER BY e.expense_id DESC";
            
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, startDate);
                statement.setString(2, endDate);
                
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        long expenseId = rs.getLong("expense_id");
                        String title = rs.getString("title");
                        double amount = rs.getDouble("amount");
                        String category = rs.getString("category_name");
                        String date = rs.getString("expense_date");
                        
                        Expense expense = new Expense(expenseId, title, amount, category, date);
                        expenses.add(expense);
                    }
                }
            }
            
        } catch (SQLException e) {
            throw new SQLException("Error retrieving expenses by date: " + e.getMessage(), e);
        }
        
        return expenses;
    }
    
    public static void updateExpense(Expense expense) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            
            // Get category ID
            String getCategoryIdQuery = "SELECT category_id FROM categories WHERE category_name = ?";
            long categoryId = -1;
            
            try (PreparedStatement getCategoryStmt = connection.prepareStatement(getCategoryIdQuery)) {
                getCategoryStmt.setString(1, expense.getCategory());
                
                try (ResultSet rs = getCategoryStmt.executeQuery()) {
                    if (rs.next()) {
                        categoryId = rs.getLong("category_id");
                    }
                }
            }
            
            if (categoryId == -1) {
                throw new SQLException("Category '" + expense.getCategory() + "' not found in database.");
            }
            
            // Update expense
            String updateQuery = "UPDATE expenses SET title = ?, amount = ?, category_id = ?, expense_date = ? WHERE expense_id = ?";
            
            try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                updateStmt.setString(1, expense.getTitle());
                updateStmt.setDouble(2, expense.getAmount());
                updateStmt.setLong(3, categoryId);
                updateStmt.setString(4, expense.getDate());
                updateStmt.setLong(5, expense.getExpenseId());
                
                int rowsAffected = updateStmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("No expense found with ID: " + expense.getExpenseId());
                }
            }
            
        } catch (SQLException e) {
            throw new SQLException("Error updating expense: " + e.getMessage(), e);
        }
    }
    
    public static double getMonthlyTotal(String year, String month) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            
            String query = "SELECT SUM(amount) as total FROM expenses " +
                          "WHERE TO_CHAR(expense_date, 'YYYY') = ? AND TO_CHAR(expense_date, 'MM') = ?";
            
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, year);
                statement.setString(2, month);
                
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        return rs.getDouble("total");
                    }
                }
            }
            
        } catch (SQLException e) {
            throw new SQLException("Error retrieving monthly total: " + e.getMessage(), e);
        }
        
        return 0.0;
    }
    
    public static int getMonthlyTransactionCount(String year, String month) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            
            String query = "SELECT COUNT(*) as count FROM expenses " +
                          "WHERE TO_CHAR(expense_date, 'YYYY') = ? AND TO_CHAR(expense_date, 'MM') = ?";
            
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, year);
                statement.setString(2, month);
                
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("count");
                    }
                }
            }
            
        } catch (SQLException e) {
            throw new SQLException("Error retrieving monthly transaction count: " + e.getMessage(), e);
        }
        
        return 0;
    }
}
