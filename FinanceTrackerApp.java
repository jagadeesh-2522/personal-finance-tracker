import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class FinanceTrackerApp extends JFrame {
    
    private JTextField titleField;
    private JTextField amountField;
    private JComboBox<String> categoryCombo;
    private JTextField dateField;
    private JTextField searchField;
    private JButton addButton;
    private JButton searchButton;
    
    // Filter components
    private JComboBox<String> filterCategoryCombo;
    private JButton clearFilterButton;
    
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private JLabel totalExpensesLabel;
    private JLabel transactionCountLabel;
    private JLabel categoryStatsLabel;
    
    // Monthly summary labels
    private JLabel monthlyTotalLabel;
    private JLabel monthlyCountLabel;
    
    private ExpenseManager manager;
    private ArrayList<Expense> expensesList;
    private ArrayList<Expense> filteredList;
    private TransactionChartPanel chartPanel;
    
    // Navigation and panels
    private JPanel mainContentArea;
    private JPanel dashboardPanel;
    private AnalysisPanel analysisPanel;
    private JButton dashboardNavButton;
    private JButton analysisNavButton;
    
    private static final Color NAVY = new Color(25, 40, 80);
    private static final Color PURPLE_PRIMARY = new Color(108, 68, 156);
    private static final Color PURPLE_LIGHT = new Color(142, 103, 197);
    private static final Color BACKGROUND = new Color(248, 247, 255);
    private static final Color LAVENDER_LIGHT = new Color(240, 237, 250);
    private static final Color CARD_WHITE = Color.WHITE;
    private static final Color TEXT_DARK = new Color(40, 50, 80);
    private static final Color BORDER_SUBTLE = new Color(220, 215, 235);
    
    public FinanceTrackerApp() {
        manager = new ExpenseManager();
        expensesList = new ArrayList<Expense>();
        filteredList = new ArrayList<Expense>();
        setupGUI();
        loadInitialData();
    }
    
    private void loadInitialData() {
        try {
            ArrayList<String> dbCategories = DatabaseExpenseManager.getCategories();
            if (!dbCategories.isEmpty()) {
                categoryCombo.removeAllItems();
                categoryCombo.addItem("Select Category");
                for (String cat : dbCategories) {
                    categoryCombo.addItem(cat);
                }
                
                // Also populate filter combo
                filterCategoryCombo.removeAllItems();
                filterCategoryCombo.addItem("All Categories");
                for (String cat : dbCategories) {
                    filterCategoryCombo.addItem(cat);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading categories: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        
        loadExpensesFromDatabase();
        chartPanel.loadChartData();
        analysisPanel.loadAllData();
    }
    
    private void loadExpensesFromDatabase() {
        try {
            expensesList.clear();
            ArrayList<Expense> dbExpenses = DatabaseExpenseManager.getAllExpenses();
            expensesList.addAll(dbExpenses);
            refreshTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading expenses: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void setupGUI() {
        setTitle("Personal Finance Tracker");
        setSize(1400, 1200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        
        chartPanel = new TransactionChartPanel();
        analysisPanel = new AnalysisPanel();
        
        JPanel mainContainer = new JPanel(new BorderLayout(0, 0));
        mainContainer.setBackground(BACKGROUND);
        
        JPanel sidebarPanel = createSidebar();
        JPanel contentPanel = createContentPanel();
        
        mainContainer.add(sidebarPanel, BorderLayout.WEST);
        mainContainer.add(contentPanel, BorderLayout.CENTER);
        
        add(mainContainer);
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(NAVY);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_SUBTLE));
        
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(NAVY);
        logoPanel.setMaximumSize(new Dimension(220, 80));
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        
        JLabel walletIcon = new JLabel("💼");
        walletIcon.setFont(new Font("Arial", Font.PLAIN, 32));
        walletIcon.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel appTitle = new JLabel("MyWallet");
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        appTitle.setForeground(Color.WHITE);
        appTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        logoPanel.add(walletIcon);
        logoPanel.add(Box.createVerticalStrut(8));
        logoPanel.add(appTitle);
        
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(NAVY);
        navPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        
        dashboardNavButton = createNavButton("📊 Dashboard", true);
        dashboardNavButton.addActionListener(e -> showDashboard());
        
        analysisNavButton = createNavButton("📈 Analysis", false);
        analysisNavButton.addActionListener(e -> showAnalysis());
        
        navPanel.add(dashboardNavButton);
        navPanel.add(Box.createVerticalStrut(5));
        navPanel.add(analysisNavButton);
        
        sidebar.add(logoPanel);
        sidebar.add(navPanel);
        sidebar.add(Box.createVerticalGlue());
        
        return sidebar;
    }
    
    private JButton createNavButton(String text, boolean isActive) {
        JButton navItem = new JButton(text);
        navItem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        navItem.setMaximumSize(new Dimension(220, 45));
        navItem.setAlignmentX(Component.LEFT_ALIGNMENT);
        navItem.setForeground(Color.WHITE);
        navItem.setBackground(isActive ? PURPLE_PRIMARY : NAVY);
        navItem.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        navItem.setFocusPainted(false);
        navItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        navItem.setHorizontalAlignment(SwingConstants.LEFT);
        
        if (!isActive) {
            navItem.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    navItem.setBackground(new Color(70, 30, 120));
                }
                public void mouseExited(MouseEvent e) {
                    navItem.setBackground(NAVY);
                }
            });
        }
        
        return navItem;
    }
    
    private void showDashboard() {
        dashboardNavButton.setBackground(PURPLE_PRIMARY);
        analysisNavButton.setBackground(NAVY);
        mainContentArea.removeAll();
        mainContentArea.add(dashboardPanel, BorderLayout.CENTER);
        mainContentArea.revalidate();
        mainContentArea.repaint();
    }
    
    private void showAnalysis() {
        analysisNavButton.setBackground(PURPLE_PRIMARY);
        dashboardNavButton.setBackground(NAVY);
        analysisPanel.loadAllData();
        mainContentArea.removeAll();
        mainContentArea.add(analysisPanel, BorderLayout.CENTER);
        mainContentArea.revalidate();
        mainContentArea.repaint();
    }
    
    private JPanel createContentPanel() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(BACKGROUND);
        
        JPanel headerPanel = createHeader();
        mainContentArea = new JPanel(new BorderLayout());
        mainContentArea.setBackground(BACKGROUND);
        mainContentArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create dashboard panel
        dashboardPanel = createDashboard();
        mainContentArea.add(dashboardPanel, BorderLayout.CENTER);
        
        content.add(headerPanel, BorderLayout.NORTH);
        content.add(mainContentArea, BorderLayout.CENTER);
        
        return content;
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BACKGROUND);
        header.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        
        JLabel welcomeLabel = new JLabel("Welcome to Your Finance Dashboard");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        welcomeLabel.setForeground(NAVY);
        
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controlPanel.setBackground(BACKGROUND);
        
        // Search field
        searchField = new JTextField(15);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        searchField.setBackground(CARD_WHITE);
        searchField.setForeground(TEXT_DARK);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_SUBTLE, 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        
        searchButton = createStyledButton("Search", PURPLE_PRIMARY);
        searchButton.addActionListener(e -> refreshTable());
        
        // Filter by category
        filterCategoryCombo = new JComboBox<>();
        filterCategoryCombo.addItem("All Categories");
        filterCategoryCombo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        filterCategoryCombo.addActionListener(e -> applyFilters());
        
        clearFilterButton = createStyledButton("Clear Filter", PURPLE_PRIMARY);
        clearFilterButton.addActionListener(e -> clearAllFilters());
        
        controlPanel.add(new JLabel("Search:"));
        controlPanel.add(searchField);
        controlPanel.add(searchButton);
        controlPanel.add(new JLabel(" | Filter:"));
        controlPanel.add(filterCategoryCombo);
        controlPanel.add(clearFilterButton);
        
        header.add(welcomeLabel, BorderLayout.WEST);
        header.add(controlPanel, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createDashboard() {
        JPanel dashboard = new JPanel(new BorderLayout(15, 15));
        dashboard.setBackground(BACKGROUND);
        
        JPanel summarySection = createSummarySection();
        JPanel mainContentPanel = createMainContent();
        
        JPanel topSection = new JPanel(new BorderLayout(15, 15));
        topSection.setBackground(BACKGROUND);
        topSection.add(summarySection, BorderLayout.NORTH);
        topSection.add(mainContentPanel, BorderLayout.CENTER);
        
        dashboard.add(topSection, BorderLayout.CENTER);
        
        return dashboard;
    }
    
    private JPanel createSummarySection() {
        JPanel summary = new JPanel(new GridLayout(2, 3, 15, 15));
        summary.setBackground(BACKGROUND);
        
        // Overall totals (top row)
        summary.add(createSummaryCard("Total Expenses", "Rs. 0.00", PURPLE_PRIMARY, totalExpensesLabel = new JLabel("Rs. 0.00")));
        summary.add(createSummaryCard("Total Transactions", "0", PURPLE_LIGHT, transactionCountLabel = new JLabel("0")));
        summary.add(createSummaryCard("Categories Used", "0", new Color(175, 130, 200), categoryStatsLabel = new JLabel("0")));
        
        // Monthly totals (bottom row)
        summary.add(createSummaryCard("This Month's Total", "Rs. 0.00", new Color(108, 156, 68), monthlyTotalLabel = new JLabel("Rs. 0.00")));
        summary.add(createSummaryCard("This Month's Transactions", "0", new Color(156, 103, 68), monthlyCountLabel = new JLabel("0")));
        summary.add(Box.createVerticalGlue());
        
        return summary;
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
    
    private JPanel createMainContent() {
        JPanel mainContent = new JPanel(new GridLayout(1, 2, 15, 0));
        mainContent.setBackground(BACKGROUND);
        
        JPanel formCard = createFormCard();
        JPanel tableCard = createTableCard();
        
        mainContent.add(formCard);
        mainContent.add(tableCard);
        
        return mainContent;
    }
    
    private JPanel createFormCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_SUBTLE, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel formTitle = new JLabel("Add New Expense");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formTitle.setForeground(NAVY);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        card.add(formTitle);
        card.add(Box.createVerticalStrut(12));
        
        titleField = new JTextField();
        addFormField(card, "Expense Title:", titleField);
        
        amountField = new JTextField();
        addFormField(card, "Amount (Rs.):", amountField);
        
        categoryCombo = new JComboBox<>();
        categoryCombo.addItem("Select Category");
        categoryCombo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        categoryCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        card.add(new JLabel("Category:"));
        card.add(Box.createVerticalStrut(3));
        card.add(categoryCombo);
        card.add(Box.createVerticalStrut(10));
        
        dateField = new JTextField();
        addFormField(card, "Date (YYYY-MM-DD):", dateField);
        
        card.add(Box.createVerticalStrut(15));
        
        addButton = createStyledButton("+ Add Expense", PURPLE_PRIMARY);
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.addActionListener(e -> addExpense());
        card.add(addButton);
        
        card.add(Box.createVerticalGlue());
        
        return card;
    }
    
    private void addFormField(JPanel parent, String label, JTextField field) {
        parent.add(new JLabel(label));
        parent.add(Box.createVerticalStrut(3));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        field.setBackground(LAVENDER_LIGHT);
        field.setForeground(TEXT_DARK);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        field.setBorder(BorderFactory.createLineBorder(BORDER_SUBTLE, 1));
        parent.add(field);
        parent.add(Box.createVerticalStrut(10));
    }
    
    private JPanel createTableCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_SUBTLE, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel tableTitle = new JLabel("Transaction History");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableTitle.setForeground(NAVY);
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        
        String[] columnNames = {"Title", "Amount (Rs.)", "Category", "Date", "Edit", "Delete"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 4 || column == 5; // Edit and Delete columns are editable
            }
        };
        
        transactionTable = new JTable(tableModel);
        transactionTable.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        transactionTable.setRowHeight(28);
        transactionTable.setBackground(Color.WHITE);
        transactionTable.setGridColor(LAVENDER_LIGHT);
        transactionTable.getTableHeader().setBackground(PURPLE_PRIMARY);
        transactionTable.getTableHeader().setForeground(Color.WHITE);
        transactionTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        transactionTable.setSelectionBackground(new Color(200, 170, 230));
        transactionTable.setSelectionForeground(NAVY);
        
        DefaultTableCellRenderer amountRenderer = new DefaultTableCellRenderer();
        amountRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        transactionTable.getColumnModel().getColumn(1).setCellRenderer(amountRenderer);
        
        // Set Edit button column
        transactionTable.getColumnModel().getColumn(4).setMaxWidth(70);
        transactionTable.getColumnModel().getColumn(4).setCellRenderer(new EditButtonRenderer());
        transactionTable.getColumnModel().getColumn(4).setCellEditor(new EditButtonEditor(new JCheckBox(), this));
        
        // Set Delete button column
        transactionTable.getColumnModel().getColumn(5).setMaxWidth(80);
        transactionTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        transactionTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox(), this));
        
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBackground(Color.WHITE);
        
        card.add(tableTitle, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);
        
        return card;
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private void addExpense() {
        try {
            String title = titleField.getText().trim();
            String amountStr = amountField.getText().trim();
            String category = (String) categoryCombo.getSelectedItem();
            String date = dateField.getText().trim();
            
            // Validate title
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an expense title.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                titleField.requestFocus();
                return;
            }
            
            if (title.length() > 100) {
                JOptionPane.showMessageDialog(this, "Title must be less than 100 characters.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                titleField.requestFocus();
                return;
            }
            
            // Validate amount
            if (amountStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an amount.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                amountField.requestFocus();
                return;
            }
            
            double amount;
            try {
                amount = Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Amount must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                amountField.requestFocus();
                return;
            }
            
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than zero.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                amountField.requestFocus();
                return;
            }
            
            if (amount > 999999999) {
                JOptionPane.showMessageDialog(this, "Amount is too large.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                amountField.requestFocus();
                return;
            }
            
            // Validate category
            if (category == null || category.equals("Select Category")) {
                JOptionPane.showMessageDialog(this, "Please select a valid category.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                categoryCombo.requestFocus();
                return;
            }
            
            // Validate date
            if (date.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a date.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                dateField.requestFocus();
                return;
            }
            
            // Validate date format
            try {
                java.sql.Date.valueOf(date);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD format.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                dateField.requestFocus();
                return;
            }
            
            Expense expense = new Expense(title, amount, category, date);
            
            try {
                DatabaseExpenseManager.saveExpense(expense);
                manager.addExpense(expense);
                expensesList.add(expense);
                
                clearFields();
                loadExpensesFromDatabase();
                chartPanel.loadChartData();
                analysisPanel.loadAllData();
                
                JOptionPane.showMessageDialog(this, "Expense added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error saving expense: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void deleteExpense(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= filteredList.size()) {
            JOptionPane.showMessageDialog(this, "Invalid transaction selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Expense expense = filteredList.get(rowIndex);
        long expenseId = expense.getExpenseId();
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this transaction?\n\nTitle: " + expense.getTitle() + "\nAmount: Rs. " + String.format("%.2f", expense.getAmount()),
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                DatabaseExpenseManager.deleteExpense(expenseId);
                expensesList.remove(expense);
                filteredList.remove(rowIndex);
                
                loadExpensesFromDatabase();
                chartPanel.loadChartData();
                analysisPanel.loadAllData();
                
                JOptionPane.showMessageDialog(this, "Transaction deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting transaction: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void editExpense(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= filteredList.size()) {
            JOptionPane.showMessageDialog(this, "Invalid transaction selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            ArrayList<String> categories = DatabaseExpenseManager.getCategories();
            Expense expense = filteredList.get(rowIndex);
            
            EditExpenseDialog dialog = new EditExpenseDialog(this, expense, categories);
            dialog.setVisible(true);
            
            if (dialog.isSaved()) {
                Expense updatedExpense = dialog.getExpense();
                
                try {
                    DatabaseExpenseManager.updateExpense(updatedExpense);
                    loadExpensesFromDatabase();
                    chartPanel.loadChartData();
                    analysisPanel.loadAllData();
                    
                    JOptionPane.showMessageDialog(this, "Transaction updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Error updating transaction: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading categories: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearFields() {
        titleField.setText("");
        amountField.setText("");
        categoryCombo.setSelectedIndex(0);
        dateField.setText("");
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        
        String searchTerm = searchField.getText().trim().toLowerCase();
        filteredList.clear();
        
        for (int i = 0; i < expensesList.size(); i++) {
            Expense exp = expensesList.get(i);
            if (searchTerm.isEmpty() || 
                exp.getTitle().toLowerCase().contains(searchTerm) ||
                exp.getCategory().toLowerCase().contains(searchTerm)) {
                filteredList.add(exp);
            }
        }
        
        for (int i = 0; i < filteredList.size(); i++) {
            Expense exp = filteredList.get(i);
            Object[] row = {exp.getTitle(), String.format("%.2f", exp.getAmount()), exp.getCategory(), exp.getDate(), "Edit", "Delete"};
            tableModel.addRow(row);
        }
        
        double total = 0;
        ArrayList<String> categories = new ArrayList<String>();
        for (int i = 0; i < filteredList.size(); i++) {
            total += filteredList.get(i).getAmount();
            String cat = filteredList.get(i).getCategory();
            if (!categories.contains(cat)) {
                categories.add(cat);
            }
        }
        
        totalExpensesLabel.setText("Rs. " + String.format("%.2f", total));
        transactionCountLabel.setText(String.valueOf(filteredList.size()));
        categoryStatsLabel.setText(String.valueOf(categories.size()));
        
        updateMonthlyTotals();
    }
    
    private void updateMonthlyTotals() {
        try {
            java.util.Calendar now = java.util.Calendar.getInstance();
            String currentYear = String.format("%04d", now.get(java.util.Calendar.YEAR));
            String currentMonth = String.format("%02d", now.get(java.util.Calendar.MONTH) + 1);
            
            double monthlyTotal = DatabaseExpenseManager.getMonthlyTotal(currentYear, currentMonth);
            int monthlyCount = DatabaseExpenseManager.getMonthlyTransactionCount(currentYear, currentMonth);
            
            monthlyTotalLabel.setText("Rs. " + String.format("%.2f", monthlyTotal));
            monthlyCountLabel.setText(String.valueOf(monthlyCount));
        } catch (SQLException e) {
            System.err.println("Error updating monthly totals: " + e.getMessage());
        }
    }
    
    private void applyFilters() {
        String selectedCategory = (String) filterCategoryCombo.getSelectedItem();
        
        if (selectedCategory != null && !selectedCategory.equals("All Categories")) {
            try {
                expensesList.clear();
                ArrayList<Expense> filteredByCategory = DatabaseExpenseManager.getExpensesByCategory(selectedCategory);
                expensesList.addAll(filteredByCategory);
                refreshTable();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error filtering by category: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            loadExpensesFromDatabase();
        }
    }
    
    private void clearAllFilters() {
        searchField.setText("");
        filterCategoryCombo.setSelectedIndex(0);
        loadExpensesFromDatabase();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                FinanceTrackerApp app = new FinanceTrackerApp();
                app.setVisible(true);
            }
        });
    }
}
