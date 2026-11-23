import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class EditExpenseDialog extends JDialog {
    
    private JTextField titleField;
    private JTextField amountField;
    private JComboBox<String> categoryCombo;
    private JTextField dateField;
    private JButton saveButton;
    private JButton cancelButton;
    private Expense expense;
    private boolean saved;
    
    private static final Color NAVY = new Color(25, 40, 80);
    private static final Color PURPLE_PRIMARY = new Color(108, 68, 156);
    private static final Color BACKGROUND = new Color(248, 247, 255);
    private static final Color CARD_WHITE = Color.WHITE;
    private static final Color TEXT_DARK = new Color(40, 50, 80);
    private static final Color BORDER_SUBTLE = new Color(220, 215, 235);
    private static final Color LAVENDER_LIGHT = new Color(240, 237, 250);
    
    public EditExpenseDialog(JFrame parent, Expense expense, ArrayList<String> categories) {
        super(parent, "Edit Expense", true);
        this.expense = expense;
        this.saved = false;
        
        setupGUI(categories);
        loadExpenseData();
        setLocationRelativeTo(parent);
    }
    
    private void setupGUI(ArrayList<String> categories) {
        setSize(400, 350);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Edit Expense");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(NAVY);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(15));
        
        // Title field
        mainPanel.add(new JLabel("Expense Title:"));
        titleField = new JTextField();
        titleField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        titleField.setBackground(LAVENDER_LIGHT);
        titleField.setForeground(TEXT_DARK);
        titleField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        titleField.setBorder(BorderFactory.createLineBorder(BORDER_SUBTLE, 1));
        mainPanel.add(titleField);
        mainPanel.add(Box.createVerticalStrut(10));
        
        // Amount field
        mainPanel.add(new JLabel("Amount (Rs.):"));
        amountField = new JTextField();
        amountField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        amountField.setBackground(LAVENDER_LIGHT);
        amountField.setForeground(TEXT_DARK);
        amountField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        amountField.setBorder(BorderFactory.createLineBorder(BORDER_SUBTLE, 1));
        mainPanel.add(amountField);
        mainPanel.add(Box.createVerticalStrut(10));
        
        // Category combo
        mainPanel.add(new JLabel("Category:"));
        categoryCombo = new JComboBox<>();
        categoryCombo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        categoryCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        for (String cat : categories) {
            categoryCombo.addItem(cat);
        }
        mainPanel.add(categoryCombo);
        mainPanel.add(Box.createVerticalStrut(10));
        
        // Date field
        mainPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        dateField = new JTextField();
        dateField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        dateField.setBackground(LAVENDER_LIGHT);
        dateField.setForeground(TEXT_DARK);
        dateField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        dateField.setBorder(BorderFactory.createLineBorder(BORDER_SUBTLE, 1));
        mainPanel.add(dateField);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(BACKGROUND);
        
        saveButton = new JButton("Save Changes");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        saveButton.setForeground(Color.WHITE);
        saveButton.setBackground(PURPLE_PRIMARY);
        saveButton.setFocusPainted(false);
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.addActionListener(e -> saveExpense());
        
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBackground(new Color(108, 108, 108));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(buttonPanel);
        
        add(mainPanel);
    }
    
    private void loadExpenseData() {
        titleField.setText(expense.getTitle());
        amountField.setText(String.valueOf(expense.getAmount()));
        categoryCombo.setSelectedItem(expense.getCategory());
        dateField.setText(expense.getDate());
    }
    
    private void saveExpense() {
        String title = titleField.getText().trim();
        String amountStr = amountField.getText().trim();
        String category = (String) categoryCombo.getSelectedItem();
        String date = dateField.getText().trim();
        
        // Validation
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than zero.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Amount must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Date cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            java.sql.Date.valueOf(date);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Update expense object
        expense = new Expense(expense.getExpenseId(), title, Double.parseDouble(amountStr), category, date);
        saved = true;
        dispose();
    }
    
    public Expense getExpense() {
        return expense;
    }
    
    public boolean isSaved() {
        return saved;
    }
}
