import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class ExpenseSummaryPanel extends JPanel {
    
    private JLabel totalExpensesLabel;
    private JLabel totalTransactionsLabel;
    private JLabel averageExpenseLabel;
    
    private static final Color BACKGROUND = new Color(248, 247, 255);
    private static final Color CARD_WHITE = Color.WHITE;
    private static final Color BORDER_SUBTLE = new Color(220, 215, 235);
    private static final Color NAVY = new Color(25, 40, 80);
    private static final Color PURPLE_PRIMARY = new Color(108, 68, 156);
    private static final Color PURPLE_LIGHT = new Color(142, 103, 197);
    
    public ExpenseSummaryPanel() {
        setLayout(new GridLayout(1, 3, 15, 0));
        setBackground(BACKGROUND);
        
        // Create summary cards
        totalExpensesLabel = new JLabel("Rs. 0.00");
        totalTransactionsLabel = new JLabel("0");
        averageExpenseLabel = new JLabel("Rs. 0.00");
        
        add(createSummaryCard("Total Expenses", "Rs. 0.00", PURPLE_PRIMARY, totalExpensesLabel));
        add(createSummaryCard("Total Transactions", "0", PURPLE_LIGHT, totalTransactionsLabel));
        add(createSummaryCard("Average Expense", "Rs. 0.00", new Color(175, 130, 200), averageExpenseLabel));
    }
    
    private JPanel createSummaryCard(String title, String placeholder, Color accentColor, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(CARD_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_SUBTLE, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(120, 130, 150));
        
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(accentColor);
        valueLabel.setText(placeholder);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    public void loadSummaryData() {
        try {
            ArrayList<Expense> allExpenses = DatabaseExpenseManager.getAllExpenses();
            
            double totalAmount = 0;
            int totalCount = allExpenses.size();
            
            for (Expense expense : allExpenses) {
                totalAmount += expense.getAmount();
            }
            
            double averageAmount = totalCount > 0 ? totalAmount / totalCount : 0;
            
            totalExpensesLabel.setText("Rs. " + String.format("%.2f", totalAmount));
            totalTransactionsLabel.setText(String.valueOf(totalCount));
            averageExpenseLabel.setText("Rs. " + String.format("%.2f", averageAmount));
            
        } catch (SQLException e) {
            System.err.println("Error loading summary data: " + e.getMessage());
            totalExpensesLabel.setText("Rs. 0.00");
            totalTransactionsLabel.setText("0");
            averageExpenseLabel.setText("Rs. 0.00");
        }
    }
}
