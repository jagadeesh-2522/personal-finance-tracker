# OOPJ PROJECT CODE DOCUMENTATION
## Personal Finance Tracker - Comprehensive Code Analysis and Viva Preparation Report

---

# 1. PROJECT OVERVIEW

## 1.1 What the Application Does
The Personal Finance Tracker is a desktop application that helps users manage their daily expenses. Users can:
- Add new expense transactions with title, amount, category, and date
- View all their transactions in a table format
- Search and filter transactions by category or keyword
- Edit existing transactions
- Delete transactions from the system
- View expense summaries (total expenses, transaction count, categories used)
- See monthly expense summaries for the current month
- View visual analysis through graphs (transaction-wise and category-wise bar charts)
- Access a separate Analysis section with detailed expense statistics

## 1.2 Technologies Used
- **Frontend**: Java Swing (GUI components)
- **Backend**: Pure Java (No frameworks)
- **Database**: Supabase PostgreSQL (Cloud-hosted)
- **Connectivity**: JDBC (Java Database Connectivity)
- **Build**: Java Compiler (javac)

## 1.3 How Java Swing, JDBC and Supabase PostgreSQL Work Together

### Java Swing (GUI Layer)
- Provides visual components like JFrame, JPanel, JButton, JTable, JComboBox
- Handles user interactions (button clicks, text input, selections)
- Displays data to the user in organized panels and dialogs

### JDBC (Connection Layer)
- Acts as a bridge between Java application and PostgreSQL database
- Uses Connection objects to establish database connections
- Uses PreparedStatement to execute SQL queries safely
- Uses ResultSet to retrieve data from database

### Supabase PostgreSQL (Data Layer)
- Stores all expense records in tables
- Manages categories, users' transactions
- Provides SQL query execution
- Ensures data persistence and integrity

### Data Flow Diagram:
```
User Input (Swing GUI)
    ↓
FinanceTrackerApp processes input
    ↓
DatabaseExpenseManager prepares SQL queries
    ↓
JDBC PreparedStatement
    ↓
Supabase PostgreSQL executes query
    ↓
ResultSet returns data
    ↓
Java objects (Expense, etc.) created
    ↓
Swing components refresh with new data
    ↓
User sees updated information
```

## 1.4 Overall Application Architecture

### Layer 1: Presentation Layer (GUI)
- FinanceTrackerApp.java - Main application window
- EditExpenseDialog.java - Edit expense form
- Various JPanels for organizing components

### Layer 2: Business Logic Layer
- ExpenseManager.java - Manages expense calculations
- Button editors and renderers for table interactions

### Layer 3: Data Access Layer
- DatabaseExpenseManager.java - Handles all database operations
- DatabaseConnection.java - Manages database connectivity

### Layer 4: Data Model Layer
- Expense.java - Represents a single expense record
- CategoryExpenseData.java - Represents category aggregated data
- TransactionData.java - Represents transaction data for charts

### Layer 5: Visualization Layer
- TransactionChartPanel.java - Draws transaction bar chart
- CategoryChartPanel.java - Draws category bar chart
- ExpenseSummaryPanel.java - Displays summary cards
- AnalysisPanel.java - Organizes analysis components

---

# 2. FILE-WISE CODE EXPLANATION

## 2.1 Expense.java

### Purpose
Represents a single expense transaction object. This is a data model class.

### Main Class
```java
public class Expense { }
```

### Important Variables
| Variable | Data Type | Purpose |
|----------|-----------|---------|
| expenseId | long | Unique identifier from database |
| title | String | Expense description |
| amount | double | Amount spent in rupees |
| category | String | Category of expense |
| date | String | Date of expense (YYYY-MM-DD) |

### Important Methods

**Constructor 1 (With ID):**
```java
public Expense(long expenseId, String title, double amount, String category, String date)
```
- Used when retrieving existing expenses from database
- All fields are initialized including the expenseId

**Constructor 2 (Without ID):**
```java
public Expense(String title, double amount, String category, String date)
```
- Used when creating new expense (before saving to database)
- Sets expenseId to -1 (indicating it's not yet in database)

**Getter Methods:**
- `getExpenseId()` - Returns the expense ID
- `getTitle()` - Returns expense description
- `getAmount()` - Returns amount spent
- `getCategory()` - Returns category
- `getDate()` - Returns expense date

### Interaction with Other Files
- Created in `FinanceTrackerApp.java` when user adds expense
- Retrieved by `DatabaseExpenseManager.getAllExpenses()`
- Used in transaction table display
- Modified in `EditExpenseDialog.java`

### OOP Concepts Used
1. **Encapsulation** - All fields are private, accessed through public getters
2. **Constructor Overloading** - Two constructors with different parameters
3. **Classes and Objects** - Expense class represents real-world expense entity

### Java Concepts Used
- Private/public access modifiers
- Primitive data types (long, double)
- Reference data type (String)
- Method definition and invocation
- Constructor concept

### Code Explanation
```java
private double amount;  // Encapsulation: private variable
public double getAmount() {  // Public getter method
    return amount;
}
```
The amount variable is private, so it can only be accessed through the public getter method. This protects the data and ensures controlled access.

---

## 2.2 ExpenseManager.java

### Purpose
Manages in-memory expense calculations. Performs business logic operations on expense objects.

### Main Class
```java
public class ExpenseManager { }
```

### Important Variables
| Variable | Data Type | Purpose |
|----------|-----------|---------|
| expenses | ArrayList<Expense> | Stores all expense objects in memory |

### Important Methods

**Constructor:**
```java
public ExpenseManager()
```
- Initializes empty ArrayList for expenses

**addExpense(Expense expense)**
- Adds an Expense object to the ArrayList
- Purpose: Store expense in memory (Note: Database storage happens separately)

**displayAllExpenses()**
- Prints all expenses to console
- Used for testing and debugging

**calculateTotalAmount()**
- Loops through all expenses and sums their amounts
- Returns total as double
- Used for displaying total expenses

### Interaction with Other Files
- Created in `FinanceTrackerApp` constructor
- `addExpense()` called after database insertion in FinanceTrackerApp
- Used to calculate totals for display

### OOP Concepts Used
1. **Classes and Objects** - ExpenseManager class with Expense objects
2. **Encapsulation** - Private ArrayList managed through public methods
3. **Aggregation** - Contains multiple Expense objects

### Java Concepts Used
- ArrayList collection
- for loop for iteration
- double primitive type
- Method definition

### Code Explanation
```java
public double calculateTotalAmount() {
    double total = 0;
    for (int i = 0; i < expenses.size(); i++) {
        total = total + expenses.get(i).getAmount();
    }
    return total;
}
```
This method loops through all expenses using a traditional for loop, adds each expense's amount to a running total, and returns the final sum.

---

## 2.3 DatabaseConnection.java

### Purpose
Manages connection to Supabase PostgreSQL database using JDBC.

### Main Class
```java
public class DatabaseConnection { }
```

### Important Variables
| Variable | Data Type | Purpose |
|----------|-----------|---------|
| host | String (from env) | Database server address |
| port | String (from env) | Database server port |
| database | String (from env) | Database name |
| user | String (from env) | Database username |
| password | String (from env) | Database password |

### Important Methods

**Static Initializer Block:**
```java
static {
    try {
        Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
        System.err.println("PostgreSQL JDBC Driver not found");
    }
}
```
- Loads PostgreSQL JDBC driver when class is loaded
- Happens automatically, only once per program run

**getConnection() (Static Method):**
```java
public static Connection getConnection() throws SQLException
```
- Reads database connection parameters from environment variables
- Validates all required variables are set
- Creates and returns a Connection object
- Throws SQLException if connection fails

### Interaction with Other Files
- Called by `DatabaseExpenseManager` methods
- Connection used in all database operations

### OOP Concepts Used
1. **Static Methods and Blocks** - getConnection is static, can be called without object
2. **Exception Handling** - throws SQLException for error propagation
3. **Encapsulation** - Connection details hidden in this class

### Java Concepts Used
- static keyword and static initializer block
- try-catch exception handling
- Class.forName() for driver loading
- DriverManager for connection creation
- Environment variables (System.getenv())

### Important Code Snippet
```java
String host = System.getenv("SUPABASE_HOST");
if (host == null || host.isEmpty()) {
    errorMessage.append("SUPABASE_HOST not set. ");
}
```
This checks if the environment variable is set. If not, error message is appended.

---

## 2.4 DatabaseExpenseManager.java

### Purpose
Handles all database operations: INSERT, SELECT, UPDATE, DELETE using JDBC.

### Main Classes
```java
public class DatabaseExpenseManager { }
```

### Important Methods

**saveExpense(Expense expense) - INSERT Operation**
```java
public static void saveExpense(Expense expense) throws SQLException
```
- Inserts new expense into database
- Uses PreparedStatement to prevent SQL injection
- Gets category ID from category name
- Converts date string to SQL Date format
- Throws SQLException if operation fails

SQL Query Used:
```sql
INSERT INTO expenses (title, amount, category_id, expense_date) VALUES (?, ?, ?, ?)
```

**getAllExpenses() - SELECT Operation**
```java
public static ArrayList<Expense> getAllExpenses() throws SQLException
```
- Retrieves all expenses from database
- Joins expenses table with categories table
- Creates Expense objects from ResultSet
- Returns ArrayList of Expense objects

SQL Query Used:
```sql
SELECT e.expense_id, e.title, e.amount, c.category_name, e.expense_date 
FROM expenses e 
JOIN categories c ON e.category_id = c.category_id 
ORDER BY e.expense_id DESC
```

**getCategories() - SELECT Operation**
```java
public static ArrayList<String> getCategories() throws SQLException
```
- Retrieves all available categories
- Returns ArrayList of category names
- Used to populate dropdown menus

SQL Query Used:
```sql
SELECT category_name FROM categories ORDER BY category_name
```

**deleteExpense(long expenseId) - DELETE Operation**
```java
public static void deleteExpense(long expenseId) throws SQLException
```
- Deletes expense with matching ID
- Uses PreparedStatement with parameter binding
- Checks if any row was deleted
- Throws SQLException if no matching record found

SQL Query Used:
```sql
DELETE FROM expenses WHERE expense_id = ?
```

**updateExpense(Expense expense) - UPDATE Operation**
```java
public static void updateExpense(Expense expense) throws SQLException
```
- Updates existing expense record
- Changes title, amount, category, date
- Uses expenseId to identify which record to update

SQL Query Used:
```sql
UPDATE expenses SET title = ?, amount = ?, category_id = ?, expense_date = ? WHERE expense_id = ?
```

**getExpensesByCategory(String categoryName) - Filtered SELECT**
```java
public static ArrayList<Expense> getExpensesByCategory(String categoryName) throws SQLException
```
- Retrieves only expenses matching a specific category
- Used for filtering functionality

**getMonthlyTotal(String year, String month) - Aggregation**
```java
public static double getMonthlyTotal(String year, String month) throws SQLException
```
- Calculates total expenses for a specific month
- Uses PostgreSQL date functions

SQL Query Used:
```sql
SELECT SUM(amount) as total FROM expenses 
WHERE TO_CHAR(expense_date, 'YYYY') = ? AND TO_CHAR(expense_date, 'MM') = ?
```

**getTransactionChartData() - Chart Data Retrieval**
```java
public static ArrayList<TransactionData> getTransactionChartData() throws SQLException
```
- Gets expense IDs and amounts for chart display
- Returns ArrayList of TransactionData objects

### OOP Concepts Used
1. **Static Methods** - All methods are static, no object instantiation needed
2. **Exception Handling** - All methods throw SQLException
3. **Encapsulation** - Database details hidden from other classes
4. **Aggregation** - Returns collections of objects

### Java Concepts Used
- PreparedStatement for parameterized queries
- ResultSet for data retrieval
- try-with-resources (try with statements)
- ArrayList collections
- String formatting
- Date handling (java.sql.Date)
- while loops for ResultSet iteration

### Code Explanation - PreparedStatement
```java
String query = "INSERT INTO expenses (title, amount, category_id, expense_date) VALUES (?, ?, ?, ?)";
try (PreparedStatement insertStmt = connection.prepareStatement(query)) {
    insertStmt.setString(1, title);
    insertStmt.setDouble(2, amount);
    insertStmt.setLong(3, categoryId);
    insertStmt.setDate(4, sqlDate);
    insertStmt.executeUpdate();
}
```
- `?` placeholders represent parameters (safe from SQL injection)
- `setString()`, `setDouble()`, etc. bind values to parameters
- `executeUpdate()` performs the INSERT operation

---

## 2.5 FinanceTrackerApp.java

### Purpose
Main application class. Contains the GUI, event handling, and orchestration of all features.

### Main Class
```java
public class FinanceTrackerApp extends JFrame { }
```
- Extends JFrame (main window)
- Uses Swing components for GUI

### Important Variables
| Variable | Data Type | Purpose |
|----------|-----------|---------|
| titleField | JTextField | Input for expense title |
| amountField | JTextField | Input for amount |
| categoryCombo | JComboBox<String> | Dropdown for category selection |
| dateField | JTextField | Input for date |
| searchField | JTextField | Search input |
| filterCategoryCombo | JComboBox<String> | Category filter dropdown |
| transactionTable | JTable | Displays transactions |
| tableModel | DefaultTableModel | Table data model |
| totalExpensesLabel | JLabel | Shows total |
| monthlyTotalLabel | JLabel | Shows monthly total |
| expensesList | ArrayList<Expense> | All expenses in memory |
| filteredList | ArrayList<Expense> | Filtered expenses for display |
| chartPanel | TransactionChartPanel | Transaction chart |
| analysisPanel | AnalysisPanel | Analysis page |

### Important Methods

**Constructor: FinanceTrackerApp()**
- Initializes manager and ArrayList
- Calls setupGUI() and loadInitialData()

**setupGUI()**
- Creates main window (JFrame)
- Sets window size: 1400x1200
- Creates sidebar and content panels
- Adds to main container

**createSidebar()**
- Creates navigation panel on left side
- Adds "Dashboard" and "Analysis" buttons
- Styling with colors and fonts

**createHeader()**
- Creates top panel with welcome message
- Adds search field and filter controls
- Buttons: Search, Clear Filter

**createDashboard()**
- Creates main content area
- Combines summary section and main content

**createSummarySection()**
- Creates 2x3 grid of summary cards
- Cards: Total Expenses, Total Transactions, Categories, Monthly Total, Monthly Count

**createFormCard()**
- Creates form panel on left of dashboard
- Contains input fields for new expense
- Add button triggers addExpense()

**createTableCard()**
- Creates transaction history table
- Columns: Title, Amount, Category, Date, Edit, Delete
- Custom renderers and editors for buttons

**addExpense()**
- Validates all input fields
- Creates Expense object
- Calls DatabaseExpenseManager.saveExpense()
- Refreshes all displays

**editExpense(int rowIndex)**
- Opens EditExpenseDialog
- Gets updated Expense from dialog
- Calls DatabaseExpenseManager.updateExpense()
- Refreshes all displays

**deleteExpense(int rowIndex)**
- Shows confirmation dialog
- Calls DatabaseExpenseManager.deleteExpense()
- Refreshes all displays

**applyFilters()**
- Gets selected category from dropdown
- Calls DatabaseExpenseManager.getExpensesByCategory()
- Refreshes table with filtered results

**clearAllFilters()**
- Resets search field and filter dropdown
- Loads all expenses from database
- Refreshes table

**updateMonthlyTotals()**
- Gets current month/year
- Calls database methods to get monthly totals
- Updates monthly summary labels

**refreshTable()**
- Applies search filtering
- Adds rows to table
- Calculates and displays summary totals
- Calls updateMonthlyTotals()

**loadExpensesFromDatabase()**
- Calls DatabaseExpenseManager.getAllExpenses()
- Populates expensesList ArrayList
- Calls refreshTable()

**loadInitialData()**
- Loads categories for dropdown
- Loads initial expenses
- Loads chart data
- Loads analysis data

### Interaction with Other Files
- Uses all DatabaseExpenseManager methods
- Creates Expense objects
- Uses EditExpenseDialog for editing
- Uses ButtonEditor and EditButtonEditor for table buttons
- Uses TransactionChartPanel and AnalysisPanel for visualization

### OOP Concepts Used
1. **Inheritance** - extends JFrame
2. **Encapsulation** - private fields with methods to access them
3. **Aggregation** - contains multiple Swing components and ArrayList
4. **Method Overloading** - Multiple createSummaryCard() calls with different parameters
5. **Polymorphism** - Different panel types added to container

### Java Concepts Used
- Swing components (JFrame, JPanel, JButton, JTable, JTextField, JComboBox, JLabel)
- ActionListener for button events
- BorderLayout, GridLayout, BoxLayout
- ArrayList for data storage
- try-catch for exception handling
- for loops for iteration
- if-else conditionals
- String methods (trim(), toLowerCase(), isEmpty())
- Double.parseDouble() for conversion
- java.util.Calendar for date operations

### Important Code Snippet - Event Handling
```java
addButton = createStyledButton("+ Add Expense", PURPLE_PRIMARY);
addButton.addActionListener(e -> addExpense());
```
- ActionListener listens for button click
- Lambda expression (e -> addExpense()) handles the event
- Calls addExpense() method when clicked

---

## 2.6 EditExpenseDialog.java

### Purpose
Modal dialog window for editing existing expenses.

### Main Class
```java
public class EditExpenseDialog extends JDialog { }
```
- Extends JDialog (modal window)
- Parent is FinanceTrackerApp

### Important Variables
| Variable | Data Type | Purpose |
|----------|-----------|---------|
| titleField | JTextField | Edit expense title |
| amountField | JTextField | Edit amount |
| categoryCombo | JComboBox<String> | Select category |
| dateField | JTextField | Edit date |
| expense | Expense | Original expense object |
| saved | boolean | Flag if user saved changes |

### Important Methods

**Constructor:**
```java
public EditExpenseDialog(JFrame parent, Expense expense, ArrayList<String> categories)
```
- Creates modal dialog
- Loads existing expense data
- Centers on parent window

**setupGUI(ArrayList<String> categories)**
- Creates dialog window (400x350)
- Adds text fields for all properties
- Adds category dropdown
- Adds Save and Cancel buttons

**loadExpenseData()**
- Fills fields with existing expense values
- Pre-selects current category

**saveExpense()**
- Validates all fields (same validation as addExpense)
- Creates new Expense object with updated values
- Sets saved flag to true
- Closes dialog

**Validation Rules:**
1. Title: Not empty, max 100 characters
2. Amount: Valid number, > 0, < 999,999,999
3. Date: Valid YYYY-MM-DD format
4. Category: Must be selected

### OOP Concepts Used
1. **Inheritance** - extends JDialog
2. **Encapsulation** - Private fields, public getters
3. **Validation Logic** - Ensures data integrity

### Java Concepts Used
- JDialog for modal windows
- JTextField, JComboBox, JButton
- try-catch for validation
- Double.parseDouble() for number conversion
- java.sql.Date.valueOf() for date validation

---

## 2.7 ButtonEditor.java

### Purpose
Custom cell editor for Delete buttons in transaction table.

### Main Class
```java
public class ButtonEditor extends DefaultCellEditor { }
```
- Extends DefaultCellEditor
- Implements table cell editing

### Important Variables
| Variable | Data Type | Purpose |
|----------|-----------|---------|
| button | JButton | Delete button |
| app | FinanceTrackerApp | Reference to main app |
| row | int | Row index in table |

### Important Methods

**Constructor:**
```java
public ButtonEditor(JCheckBox checkBox, FinanceTrackerApp app)
```
- Creates red Delete button
- Adds ActionListener to button

**Button ActionListener:**
```java
button.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        app.deleteExpense(row);
        fireEditingStopped();
    }
});
```
- When button clicked: calls deleteExpense() on app
- fireEditingStopped() stops cell editing

**getTableCellEditorComponent()**
- Called when cell is in edit mode
- Returns the button component
- Sets row number for deletion

**getCellEditorValue()**
- Returns the button label text

### OOP Concepts Used
1. **Inheritance** - extends DefaultCellEditor
2. **Method Overriding** - Overrides getTableCellEditorComponent()
3. **Polymorphism** - Acts as CellEditor through interface

### Java Concepts Used
- ActionListener for button clicks
- Anonymous inner class for event handling
- Interface implementation (CellEditor)

---

## 2.8 ButtonRenderer.java

### Purpose
Custom cell renderer for Delete button display in table.

### Main Class
```java
public class ButtonRenderer extends JButton implements TableCellRenderer { }
```
- Extends JButton
- Implements TableCellRenderer

### Important Methods

**getTableCellRendererComponent()**
- Returns button component for display
- Sets button styling (red color, white text)
- Called for each cell in Delete column

### OOP Concepts Used
1. **Inheritance** - extends JButton
2. **Interface Implementation** - implements TableCellRenderer
3. **Polymorphism** - Renders differently based on selection state

---

## 2.9 EditButtonEditor.java and EditButtonRenderer.java

### Similar to ButtonEditor and ButtonRenderer
- Edit button styled in blue instead of red
- Calls editExpense() instead of deleteExpense()
- Otherwise functionally similar

---

## 2.10 TransactionData.java and CategoryExpenseData.java

### Purpose
Simple data holder classes (POJOs - Plain Old Java Objects)

### TransactionData.java
```java
public class TransactionData {
    private long transactionId;
    private double amount;
}
```
- Stores transaction ID and amount
- Used for chart data

### CategoryExpenseData.java
```java
public class CategoryExpenseData {
    private String categoryName;
    private double totalAmount;
}
```
- Stores category name and total amount
- Used for category chart

---

## 2.11 TransactionChartPanel.java and CategoryChartPanel.java

### Purpose
Custom panels that draw bar charts using Graphics2D.

### Methods
- `loadChartData()` - Retrieves data from database
- `paintComponent(Graphics g)` - Draws the chart
  - Draws axes, grid lines, title, bars, labels
  - Uses Graphics2D for drawing
  - Calculates bar heights based on data

### OOP Concepts Used
1. **Inheritance** - extends JPanel
2. **Method Overriding** - Overrides paintComponent()
3. **Encapsulation** - Data loading internal to class

### Java Concepts Used
- Graphics2D for drawing
- for loops for bar drawing
- Math calculations for scaling
- Font and Color objects
- drawLine(), fillRect(), drawString() methods

---

## 2.12 AnalysisPanel.java

### Purpose
Container panel that organizes analysis components.

### Components
- ExpenseSummaryPanel (top) - Summary cards
- CategoryChartPanel (bottom left) - Category chart
- TransactionChartPanel (bottom right) - Transaction chart

### Methods
- `loadAllData()` - Calls loadChartData() on all panels
- Automatically refreshes when switched to this panel

---

## 2.13 ExpenseSummaryPanel.java

### Purpose
Displays 3 summary cards: Total Expenses, Total Transactions, Average Expense.

### Method
- `loadSummaryData()` - Calculates totals and updates labels

---

## 2.14 TestDatabaseConnection.java and TestDatabaseExpenseManager.java

### Purpose
Test classes for verifying database functionality (used during development).

### Methods
- Test database connection
- Test data retrieval operations
- Print results to console for verification

---

# 3. OOP CONCEPTS USED IN PROJECT

## 3.1 Classes and Objects

**Definition:** A class is a blueprint for creating objects. Objects are instances of classes.

**Usage in Project:**
- Expense.java - Blueprint for expense objects
- ExpenseManager.java - Manages collection of expense objects
- FinanceTrackerApp.java - Main application object

**Example:**
```java
// Class definition
public class Expense { }

// Object creation
Expense expense = new Expense(1, "Groceries", 500.50, "Food", "2024-01-15");
```

**Viva Explanation:** A class is like a template. In our project, Expense is a class that defines what properties an expense should have (title, amount, category, date). When we create an expense, we're creating an object from this template. For example, the expense "Groceries 500 rupees" is an object of the Expense class.

---

## 3.2 Encapsulation

**Definition:** Bundling data (variables) and methods together, and hiding internal details using access modifiers.

**Usage in Project:**
- Expense.java - All variables are private, accessed through public getters
- DatabaseConnection.java - Connection details hidden
- DatabaseExpenseManager.java - SQL logic hidden from GUI

**Example from Expense.java:**
```java
private double amount;  // Private - can't access directly

public double getAmount() {  // Public - controlled access
    return amount;
}
```

**Viva Explanation:** Encapsulation means hiding internal details. In our Expense class, the amount variable is private, so nobody can directly change it. They must use the getAmount() method. This protects the data and ensures it's always accessed correctly. It's like a bank - you can't directly access the money, but the bank provides methods (withdraw, deposit) to manage it safely.

---

## 3.3 Inheritance

**Definition:** Creating a new class from an existing class, inheriting its properties and methods.

**Usage in Project:**
- FinanceTrackerApp extends JFrame
- EditExpenseDialog extends JDialog
- TransactionChartPanel extends JPanel
- CategoryChartPanel extends JPanel
- ButtonEditor extends DefaultCellEditor
- ButtonRenderer extends JButton

**Example from FinanceTrackerApp.java:**
```java
public class FinanceTrackerApp extends JFrame {
    // Inherits all JFrame methods like setVisible(), setSize(), etc.
}
```

**Viva Explanation:** Inheritance means a class can inherit properties from another class. FinanceTrackerApp inherits from JFrame, which means it automatically gets all the methods of JFrame (like setVisible, setSize, setTitle). We don't have to rewrite these methods; we just extend JFrame and use them. It's like a child inheriting features from a parent.

---

## 3.4 Method Overriding

**Definition:** A subclass providing its own implementation of a method that exists in the parent class.

**Usage in Project:**
- TransactionChartPanel overrides paintComponent() to draw custom chart
- CategoryChartPanel overrides paintComponent() to draw custom chart
- ButtonEditor overrides getTableCellEditorComponent()
- ButtonRenderer overrides getTableCellRendererComponent()

**Example from TransactionChartPanel.java:**
```java
// JPanel has a paintComponent method
// We override it to draw our chart instead
@Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);  // Call parent's implementation first
    Graphics2D g2d = (Graphics2D) g;
    // Draw chart using Graphics2D
}
```

**Viva Explanation:** Method overriding means a subclass rewrites a method from the parent class with its own implementation. In our project, JPanel normally just displays a blank panel. But we override its paintComponent() method to draw a bar chart instead. It's like inheriting a recipe from your parent, but modifying it to suit your taste.

---

## 3.5 Polymorphism

**Definition:** Objects of different classes can be used interchangeably if they share a common parent class or interface.

**Usage in Project:**
- ButtonEditor and EditButtonEditor both extend DefaultCellEditor
- ButtonRenderer extends JButton and implements TableCellRenderer
- Different panel types (FormCard, TableCard) added to container as JPanel

**Example:**
```java
// Polymorphism - Both can be used as CellEditor
TableCellEditor deleteEditor = new ButtonEditor(...);
TableCellEditor editEditor = new EditButtonEditor(...);
```

**Viva Explanation:** Polymorphism means one interface, many implementations. Our ButtonEditor and EditButtonEditor both extend DefaultCellEditor, so they can be used wherever a CellEditor is needed. Even though they're different classes, they can be treated the same way. It's like all vehicles (car, bike, bus) have a common interface (go, stop) but implement them differently.

---

## 3.6 Constructors

**Definition:** Special methods that initialize objects when they are created.

**Usage in Project:**

**Expense.java - Constructor Overloading:**
```java
// Constructor 1 - with ID (for existing expenses from DB)
public Expense(long expenseId, String title, double amount, String category, String date)

// Constructor 2 - without ID (for new expenses)
public Expense(String title, double amount, String category, String date)
```

**EditExpenseDialog.java:**
```java
public EditExpenseDialog(JFrame parent, Expense expense, ArrayList<String> categories) {
    super(parent, "Edit Expense", true);  // Call parent constructor
    this.expense = expense;
    setupGUI(categories);
}
```

**Viva Explanation:** Constructors are methods that run automatically when you create an object. They set up the initial state. In Expense class, we have two constructors - one for new expenses (sets ID to -1) and one for existing expenses (sets actual ID). This is called constructor overloading. It's like having different ways to initialize an object depending on the situation.

---

## 3.7 Method Overloading

**Definition:** Multiple methods with the same name but different parameters.

**Usage in Project:**
- Expense class has two constructors (method overloading)
- createSummaryCard() is called multiple times with different parameters

**Example:**
```java
// Multiple addFormField calls with different field names
addFormField(card, "Expense Title:", titleField);
addFormField(card, "Amount (Rs.):", amountField);
addFormField(card, "Date (YYYY-MM-DD):", dateField);
```

**Viva Explanation:** Method overloading means you can have multiple methods with the same name but different parameters. Java knows which one to call based on the parameters you pass. In our Expense class, we have two constructors named Expense but with different parameters - one with ID and one without. This gives flexibility in how to create objects.

---

## 3.8 Static Members

**Definition:** Members (variables or methods) that belong to the class itself, not to individual objects.

**Usage in Project:**
- DatabaseExpenseManager - All methods are static (no object creation needed)
- DatabaseConnection.getConnection() is static
- Color constants in FinanceTrackerApp are static final

**Example from DatabaseExpenseManager.java:**
```java
public static ArrayList<Expense> getAllExpenses() throws SQLException {
    // Can be called as DatabaseExpenseManager.getAllExpenses()
    // Without creating: new DatabaseExpenseManager().getAllExpenses()
}

public static final Color NAVY = new Color(25, 40, 80);  // Static constant
```

**Viva Explanation:** Static means the method or variable belongs to the class, not to objects. You can call DatabaseExpenseManager.getAllExpenses() without creating an object. Static is useful for utility functions that don't need object state. Color constants are static because they're the same for all windows.

---

## 3.9 Access Modifiers (public, private, protected)

**Definition:** Keywords that control visibility of classes, variables, and methods.

**Usage in Project:**
- private - All data fields in Expense, ButtonEditor, etc.
- public - Methods that other classes need to call
- No protected used in this project

**Example from Expense.java:**
```java
private double amount;  // Only Expense class can access directly

public double getAmount() {  // Anyone can call this
    return amount;
}
```

**Viva Explanation:** Access modifiers control who can access what. Private means only the class itself can access it. Public means anyone can access it. We use private for data and public for methods to create a controlled interface. It's like private keys (only you have them) vs public information (everyone can see).

---

## 3.10 Exception Handling

**Definition:** Mechanism to handle errors that occur during program execution.

**Usage in Project:**
- try-catch blocks in all database operations
- throws SQLException in database methods
- try-catch in addExpense() validation

**Example from DatabaseExpenseManager.java:**
```java
try {
    // Attempt database operation
    ArrayList<Expense> expenses = DatabaseExpenseManager.getAllExpenses();
} catch (SQLException e) {
    // Handle error
    JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
}
```

**Viva Explanation:** Exception handling prevents the program from crashing. When something goes wrong (like database connection fails), we catch the error and handle it gracefully. Try block runs the code, catch block runs if an error occurs. This allows the program to continue running even if one operation fails.

---

## 3.11 Interface Implementation

**Definition:** A class can implement an interface to follow a contract of methods it must define.

**Usage in Project:**
- ButtonRenderer implements TableCellRenderer
- (Not extensively used, but important concept)

**Viva Explanation:** Interfaces define a contract - classes implementing them must provide certain methods. ButtonRenderer implements TableCellRenderer, which means it must provide getTableCellRendererComponent() method. This ensures consistent behavior across different renderers.

---

# 4. JAVA CONCEPTS AND SWING COMPONENTS

## 4.1 Swing Components Used

### JFrame
**Purpose:** Main application window
**Usage:** FinanceTrackerApp extends JFrame
**Code:**
```java
public class FinanceTrackerApp extends JFrame {
    setTitle("Personal Finance Tracker");
    setSize(1400, 1200);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}
```

### JPanel
**Purpose:** Container for organizing components
**Usage:** Multiple JPanels for different sections
**Code:**
```java
JPanel dashboard = new JPanel(new BorderLayout(15, 15));
dashboard.setBackground(BACKGROUND);
```

### JButton
**Purpose:** Clickable buttons for user actions
**Usage:** Add Expense, Search, Delete, Edit, Clear Filter buttons
**Code:**
```java
addButton = createStyledButton("+ Add Expense", PURPLE_PRIMARY);
addButton.addActionListener(e -> addExpense());
```

### JLabel
**Purpose:** Display text or values
**Usage:** Title labels, summary value labels
**Code:**
```java
totalExpensesLabel = new JLabel("Rs. 0.00");
totalExpensesLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
```

### JTextField
**Purpose:** Input field for text
**Usage:** Title, amount, date input fields
**Code:**
```java
titleField = new JTextField();
titleField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
```

### JComboBox
**Purpose:** Dropdown list for selection
**Usage:** Category selection, filter by category
**Code:**
```java
categoryCombo = new JComboBox<>();
categoryCombo.addItem("Select Category");
for (String cat : dbCategories) {
    categoryCombo.addItem(cat);
}
```

### JTable
**Purpose:** Display data in rows and columns
**Usage:** Transaction history display
**Code:**
```java
String[] columnNames = {"Title", "Amount (Rs.)", "Category", "Date", "Edit", "Delete"};
transactionTable = new JTable(tableModel);
transactionTable.setFont(new Font("Segoe UI", Font.PLAIN, 10));
```

### JScrollPane
**Purpose:** Scrollable container
**Usage:** Scroll through transaction table
**Code:**
```java
JScrollPane scrollPane = new JScrollPane(transactionTable);
card.add(scrollPane, BorderLayout.CENTER);
```

### JDialog
**Purpose:** Modal dialog window
**Usage:** Edit expense dialog
**Code:**
```java
public class EditExpenseDialog extends JDialog {
    super(parent, "Edit Expense", true);  // true = modal
}
```

### JOptionPane
**Purpose:** Show message dialogs
**Usage:** Error messages, success messages, confirmations
**Code:**
```java
JOptionPane.showMessageDialog(this, "Expense added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

int confirm = JOptionPane.showConfirmDialog(
    this,
    "Are you sure you want to delete?",
    "Confirm",
    JOptionPane.YES_NO_OPTION
);
```

---

## 4.2 Layout Managers

### BorderLayout
- Arranges components in 5 regions: North, South, East, West, Center
- Used for main frame, header, cards

**Example:**
```java
JPanel content = new JPanel(new BorderLayout());
content.add(headerPanel, BorderLayout.NORTH);
content.add(dashboardPanel, BorderLayout.CENTER);
```

### GridLayout
- Arranges components in grid rows and columns
- Used for summary cards (2x3 grid)

**Example:**
```java
JPanel summary = new JPanel(new GridLayout(2, 3, 15, 15));  // 2 rows, 3 columns
```

### BoxLayout
- Arranges components vertically or horizontally
- Used for sidebar, form cards

**Example:**
```java
JPanel card = new JPanel();
card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));  // Vertical
```

### FlowLayout
- Arranges components in a row, wrapping to next line if needed
- Used for search and filter controls

---

## 4.3 Event Handling - ActionListener

**Purpose:** Respond to user interactions like button clicks

**Code Structure:**
```java
button.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        // Code to execute when button is clicked
    }
});

// Or using lambda expression (Java 8+):
button.addActionListener(e -> methodToExecute());
```

**Examples in Project:**
```java
searchButton.addActionListener(e -> refreshTable());
filterCategoryCombo.addActionListener(e -> applyFilters());
clearFilterButton.addActionListener(e -> clearAllFilters());
addButton.addActionListener(e -> addExpense());
```

---

## 4.4 Graphics2D and paintComponent()

**Purpose:** Custom drawing of graphics

**Usage in TransactionChartPanel and CategoryChartPanel:**

```java
@Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
    // Draw axes
    g2d.drawLine(leftMargin, bottomY, rightX, bottomY);  // X-axis
    g2d.drawLine(leftMargin, topY, leftMargin, bottomY);  // Y-axis
    
    // Draw bars
    for (int i = 0; i < data.size(); i++) {
        g2d.fillRect(barX, barY, barWidth, barHeight);
    }
    
    // Draw text
    g2d.drawString("Title", xPos, yPos);
}
```

**Graphics2D Methods Used:**
- `drawLine()` - Draw lines for axes and grid
- `fillRect()` - Draw filled rectangles for bars
- `drawString()` - Draw text labels and values
- `drawRect()` - Draw rectangle outlines
- `setColor()` - Set drawing color
- `setFont()` - Set text font
- `rotate()` - Rotate for Y-axis label

---

## 4.5 ArrayList Collection

**Purpose:** Dynamic array that can grow or shrink

**Usage in Project:**
- Store expenses list
- Store filtered expenses list
- Store categories list
- Store chart data

**Code Examples:**
```java
// Declaration
ArrayList<Expense> expensesList = new ArrayList<>();

// Adding
expensesList.add(expense);

// Iteration
for (int i = 0; i < expensesList.size(); i++) {
    Expense exp = expensesList.get(i);
}

// Clear
expensesList.clear();

// Check size
if (expensesList.isEmpty()) { }
```

---

## 4.6 Loops and Conditionals

### for Loop
Used to iterate through ArrayList
```java
for (int i = 0; i < expensesList.size(); i++) {
    Expense exp = expensesList.get(i);
    // Process expense
}
```

### while Loop
Used with ResultSet for database iteration
```java
try (ResultSet rs = statement.executeQuery()) {
    while (rs.next()) {  // Loop while there are more rows
        String title = rs.getString("title");
    }
}
```

### if-else Statements
Used for validation and conditionals
```java
if (title.isEmpty()) {
    JOptionPane.showMessageDialog(this, "Title cannot be empty.");
    return;
} else if (amount <= 0) {
    JOptionPane.showMessageDialog(this, "Amount must be positive.");
    return;
}
```

### Ternary Operator
Used for simple conditions
```java
String status = (amount > 0) ? "Valid" : "Invalid";
```

---

# 5. JDBC AND DATABASE EXPLANATION

## 5.1 What is JDBC?

**Definition:** JDBC (Java Database Connectivity) is a Java API that allows Java applications to interact with databases using SQL.

**How it Works:**
1. Load JDBC driver
2. Establish connection to database
3. Create SQL statements
4. Execute queries
5. Process results
6. Close connections

---

## 5.2 Connection to Supabase PostgreSQL

### Step 1: Load Driver
```java
static {
    try {
        Class.forName("org.postgresql.Driver");  // Load PostgreSQL driver
    } catch (ClassNotFoundException e) {
        System.err.println("PostgreSQL JDBC Driver not found");
    }
}
```

### Step 2: Get Connection Parameters from Environment
```java
String host = System.getenv("SUPABASE_HOST");
String port = System.getenv("SUPABASE_PORT");
String database = System.getenv("SUPABASE_DB");
String user = System.getenv("SUPABASE_USER");
String password = System.getenv("SUPABASE_PASSWORD");
```

### Step 3: Create Connection URL
```java
String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
Connection connection = DriverManager.getConnection(url, user, password);
```

---

## 5.3 Connection Class

**Purpose:** Represents a connection to the database

**Methods Used:**
- `prepareStatement(String sql)` - Create PreparedStatement
- `close()` - Close connection (handled by try-with-resources)

**Code Pattern:**
```java
try (Connection connection = DatabaseConnection.getConnection()) {
    // Use connection for database operations
}  // Connection automatically closed
```

---

## 5.4 PreparedStatement Class

**Purpose:** Safely execute SQL queries with parameters

**Why Use PreparedStatement:**
- Prevents SQL Injection attacks
- Parameters are safely bound to their types
- More efficient for repeated queries

**Example - INSERT:**
```java
String query = "INSERT INTO expenses (title, amount, category_id, expense_date) VALUES (?, ?, ?, ?)";
try (PreparedStatement stmt = connection.prepareStatement(query)) {
    stmt.setString(1, title);        // ? placeholder 1
    stmt.setDouble(2, amount);       // ? placeholder 2
    stmt.setLong(3, categoryId);     // ? placeholder 3
    stmt.setDate(4, sqlDate);        // ? placeholder 4
    stmt.executeUpdate();             // Execute INSERT
}
```

**Example - SELECT:**
```java
String query = "SELECT expense_id, title, amount FROM expenses WHERE category_id = ?";
try (PreparedStatement stmt = connection.prepareStatement(query)) {
    stmt.setLong(1, categoryId);
    ResultSet rs = stmt.executeQuery();  // Execute SELECT
    while (rs.next()) {
        // Process results
    }
}
```

**Example - UPDATE:**
```java
String query = "UPDATE expenses SET title = ?, amount = ? WHERE expense_id = ?";
try (PreparedStatement stmt = connection.prepareStatement(query)) {
    stmt.setString(1, newTitle);
    stmt.setDouble(2, newAmount);
    stmt.setLong(3, expenseId);
    stmt.executeUpdate();  // Execute UPDATE
}
```

**Example - DELETE:**
```java
String query = "DELETE FROM expenses WHERE expense_id = ?";
try (PreparedStatement stmt = connection.prepareStatement(query)) {
    stmt.setLong(1, expenseId);
    int rowsAffected = stmt.executeUpdate();
    if (rowsAffected == 0) {
        throw new SQLException("No expense found with ID: " + expenseId);
    }
}
```

---

## 5.5 ResultSet Class

**Purpose:** Holds results from SELECT queries

**Methods:**
- `next()` - Move to next row, returns true if row exists
- `getString(columnName)` - Get String value
- `getDouble(columnName)` - Get double value
- `getLong(columnName)` - Get long value
- `getDate(columnName)` - Get Date value

**Example:**
```java
String query = "SELECT expense_id, title, amount, category_name, expense_date FROM expenses";
try (PreparedStatement stmt = connection.prepareStatement(query)) {
    ResultSet rs = stmt.executeQuery();
    while (rs.next()) {  // Loop through each row
        long id = rs.getLong("expense_id");
        String title = rs.getString("title");
        double amount = rs.getDouble("amount");
        String category = rs.getString("category_name");
        
        Expense expense = new Expense(id, title, amount, category, dateStr);
        expenses.add(expense);
    }
}
```

---

## 5.6 SQL Queries Used in Project

### INSERT Query - Add New Expense
```sql
INSERT INTO expenses (title, amount, category_id, expense_date) 
VALUES (?, ?, ?, ?)
```
- Inserts new expense record
- category_id fetched from categories table first

### SELECT Query - Get All Expenses
```sql
SELECT e.expense_id, e.title, e.amount, c.category_name, e.expense_date 
FROM expenses e 
JOIN categories c ON e.category_id = c.category_id 
ORDER BY e.expense_id DESC
```
- Joins expenses with categories
- Returns expense information with category names
- Orders by ID in descending order (newest first)

### SELECT with WHERE - Filter by Category
```sql
SELECT e.expense_id, e.title, e.amount, c.category_name, e.expense_date 
FROM expenses e 
JOIN categories c ON e.category_id = c.category_id 
WHERE c.category_name = ?
```
- Gets only expenses from specific category

### UPDATE Query - Edit Expense
```sql
UPDATE expenses 
SET title = ?, amount = ?, category_id = ?, expense_date = ? 
WHERE expense_id = ?
```
- Updates specific expense record by ID

### DELETE Query - Remove Expense
```sql
DELETE FROM expenses 
WHERE expense_id = ?
```
- Deletes specific expense by ID

### SELECT with GROUP BY - Get Categories
```sql
SELECT category_name FROM categories ORDER BY category_name
```
- Gets all available categories for dropdown

### SELECT with SUM - Monthly Total
```sql
SELECT SUM(amount) as total 
FROM expenses 
WHERE TO_CHAR(expense_date, 'YYYY') = ? 
  AND TO_CHAR(expense_date, 'MM') = ?
```
- Calculates total expenses for a specific month
- Uses PostgreSQL date functions

### SELECT with COUNT - Transaction Count
```sql
SELECT COUNT(*) as count 
FROM expenses 
WHERE TO_CHAR(expense_date, 'YYYY') = ? 
  AND TO_CHAR(expense_date, 'MM') = ?
```
- Counts transactions in a specific month

---

## 5.7 Database Error Handling

**Pattern Used:**
```java
public static void someMethod() throws SQLException {
    try {
        // Database operation
    } catch (SQLException e) {
        throw new SQLException("Error message: " + e.getMessage(), e);
    }
}

// In GUI code:
try {
    DatabaseExpenseManager.someMethod();
} catch (SQLException e) {
    JOptionPane.showMessageDialog(this, 
        "Database Error: " + e.getMessage(), 
        "Error", 
        JOptionPane.ERROR_MESSAGE);
}
```

**Benefits:**
- SQLException caught at database layer
- Re-thrown with descriptive message
- Caught in GUI layer and shown to user
- Application continues running

---

## 5.8 Data Refresh After Operations

**Pattern:**
```java
// 1. Perform database operation
DatabaseExpenseManager.saveExpense(expense);

// 2. Reload data from database
loadExpensesFromDatabase();  // Fetches fresh data

// 3. Refresh all displays
refreshTable();              // Updates transaction table
chartPanel.loadChartData();  // Updates charts
analysisPanel.loadAllData(); // Updates analysis
```

---

# 6. FEATURE-WISE EXPLANATION

## 6.1 Adding Expenses

**Files Involved:**
- FinanceTrackerApp.java - Form and addExpense() method
- DatabaseExpenseManager.java - saveExpense() method
- Expense.java - Data model

**Process:**
1. User fills form fields (title, amount, category, date)
2. User clicks Add button
3. addExpense() validates all inputs
4. Creates Expense object
5. Calls DatabaseExpenseManager.saveExpense()
6. SQL INSERT executes
7. loadExpensesFromDatabase() refreshes data
8. All charts and tables update
9. Success message shown

**Validation Rules:**
- Title: Not empty, max 100 chars
- Amount: Valid number, >0, <999,999,999
- Category: Must be selected
- Date: Must be YYYY-MM-DD format

**Code Flow:**
```
User Input → Validation → Create Expense → Database Save → 
Reload Data → Update GUI → Show Message
```

---

## 6.2 Viewing Transactions

**Files Involved:**
- FinanceTrackerApp.java - createTableCard(), refreshTable()
- DatabaseExpenseManager.java - getAllExpenses()
- ButtonEditor/ButtonRenderer - Delete/Edit buttons in table

**Process:**
1. Application loads initial data
2. getAllExpenses() fetches all records from database
3. Expense objects stored in ArrayList
4. refreshTable() adds rows to JTable
5. Table displays with columns: Title, Amount, Category, Date, Edit, Delete

**Table Features:**
- Sortable (click column header)
- Selectable rows
- Custom cell editors for Edit/Delete buttons
- Scrollable with JScrollPane

---

## 6.3 Searching Transactions

**Files Involved:**
- FinanceTrackerApp.java - Search field and refreshTable()

**Process:**
1. User types in search field
2. User clicks Search button (or pressing Enter)
3. refreshTable() called
4. Loop through expensesList
5. Check if expense title or category contains search term
6. Add matching expenses to filteredList
7. Display only filtered expenses in table

**Code:**
```java
String searchTerm = searchField.getText().trim().toLowerCase();
for (Expense exp : expensesList) {
    if (searchTerm.isEmpty() || 
        exp.getTitle().toLowerCase().contains(searchTerm) ||
        exp.getCategory().toLowerCase().contains(searchTerm)) {
        filteredList.add(exp);
    }
}
```

---

## 6.4 Deleting Transactions

**Files Involved:**
- FinanceTrackerApp.java - deleteExpense() method
- ButtonEditor.java - Button click handling
- DatabaseExpenseManager.java - deleteExpense() method

**Process:**
1. User clicks Delete button in table row
2. ButtonEditor calls app.deleteExpense(rowIndex)
3. Confirmation dialog shown with expense details
4. If user clicks YES:
   - Calls DatabaseExpenseManager.deleteExpense(expenseId)
   - SQL DELETE executes
   - loadExpensesFromDatabase() refreshes data
   - All displays update
   - Success message shown
5. If user clicks NO: No changes made

**SQL:**
```sql
DELETE FROM expenses WHERE expense_id = ?
```

---

## 6.5 Editing Transactions

**Files Involved:**
- FinanceTrackerApp.java - editExpense() method
- EditExpenseDialog.java - Edit form dialog
- EditButtonEditor.java - Button click handling
- DatabaseExpenseManager.java - updateExpense() method

**Process:**
1. User clicks Edit button in table row
2. EditButtonEditor calls app.editExpense(rowIndex)
3. EditExpenseDialog opens with existing values
4. User modifies fields
5. User clicks Save
6. Dialog validates all fields
7. DatabaseExpenseManager.updateExpense() called
8. SQL UPDATE executes
9. Data reloaded and all displays update

**SQL:**
```sql
UPDATE expenses 
SET title = ?, amount = ?, category_id = ?, expense_date = ? 
WHERE expense_id = ?
```

---

## 6.6 Filtering Transactions

**Files Involved:**
- FinanceTrackerApp.java - applyFilters(), clearAllFilters()
- DatabaseExpenseManager.java - getExpensesByCategory()

**Process - Filter by Category:**
1. User selects category from dropdown
2. applyFilters() called automatically
3. If category != "All Categories":
   - Calls DatabaseExpenseManager.getExpensesByCategory(category)
   - SQL WHERE clause filters by category
   - Filtered results displayed in table
4. If category == "All Categories":
   - loadExpensesFromDatabase() shows all

**Clear Filter:**
1. User clicks Clear Filter button
2. clearAllFilters() called
3. Search field cleared
4. Filter dropdown reset to "All Categories"
5. All expenses loaded and displayed

---

## 6.7 Monthly Expense Summary

**Files Involved:**
- FinanceTrackerApp.java - updateMonthlyTotals()
- DatabaseExpenseManager.java - getMonthlyTotal(), getMonthlyTransactionCount()
- ExpenseSummaryPanel.java - Displays monthly cards

**Process:**
1. updateMonthlyTotals() called
2. Gets current year and month from Calendar
3. Calls getMonthlyTotal(year, month)
4. Calls getMonthlyTransactionCount(year, month)
5. PostgreSQL aggregates data for current month
6. Monthly cards updated with values

**SQL:**
```sql
SELECT SUM(amount) FROM expenses 
WHERE TO_CHAR(expense_date, 'YYYY') = '2024' 
  AND TO_CHAR(expense_date, 'MM') = '01'
```

---

## 6.8 Analysis Section

**Files Involved:**
- FinanceTrackerApp.java - Analysis navigation
- AnalysisPanel.java - Container
- CategoryChartPanel.java - Category chart
- TransactionChartPanel.java - Transaction chart
- ExpenseSummaryPanel.java - Summary cards

**Features:**
1. Summary cards (Total, Monthly)
2. Category-wise expense chart
3. Transaction-wise expense chart

**Process:**
1. User clicks "Analysis" button in sidebar
2. AnalysisPanel displayed
3. loadAllData() called:
   - Summary data calculated
   - Chart data fetched from database
   - Panels redrawn with current data

---

## 6.9 Category-wise Graphs

**Files Involved:**
- CategoryChartPanel.java - Chart drawing
- DatabaseExpenseManager.java - getExpenseByCategory()

**Process:**
1. loadChartData() called
2. getExpenseByCategory() fetches data
3. SQL groups by category and sums amounts
4. CategoryExpenseData objects created
5. paintComponent() draws bars:
   - Each bar represents a category
   - Bar height = total amount for category
   - Labels show category name and total

**SQL:**
```sql
SELECT c.category_name, SUM(e.amount) as total_amount 
FROM expenses e 
JOIN categories c ON e.category_id = c.category_id 
GROUP BY c.category_name 
ORDER BY total_amount DESC
```

---

## 6.10 Transaction-wise Graphs

**Files Involved:**
- TransactionChartPanel.java - Chart drawing
- DatabaseExpenseManager.java - getTransactionChartData()

**Process:**
1. loadChartData() called
2. getTransactionChartData() fetches data
3. SQL retrieves expense_id and amount for each transaction
4. TransactionData objects created
5. paintComponent() draws bars:
   - Each bar represents one transaction
   - Bar height = expense amount
   - Labels show transaction ID and amount

**SQL:**
```sql
SELECT expense_id, amount FROM expenses ORDER BY expense_id ASC
```

---

## 6.11 Input Validation

**Files Involved:**
- FinanceTrackerApp.java - addExpense() method
- EditExpenseDialog.java - saveExpense() method

**Validation Steps:**

1. **Title Validation:**
   - Check if empty
   - Check if length > 100
   - Show specific error message

2. **Amount Validation:**
   - Try to parse as double
   - Check if > 0
   - Check if < 999,999,999
   - Focus field if error

3. **Category Validation:**
   - Check if selected (not "Select Category")

4. **Date Validation:**
   - Check if empty
   - Try java.sql.Date.valueOf() - throws exception if invalid format
   - Must be YYYY-MM-DD

**Code Example:**
```java
if (title.isEmpty()) {
    JOptionPane.showMessageDialog(this, "Title cannot be empty.");
    titleField.requestFocus();
    return;
}

try {
    double amount = Double.parseDouble(amountStr);
    if (amount <= 0) {
        throw new Exception("Amount must be > 0");
    }
} catch (Exception e) {
    JOptionPane.showMessageDialog(this, "Invalid amount: " + e.getMessage());
    amountField.requestFocus();
    return;
}
```

---

# 7. APPLICATION EXECUTION FLOW

## 7.1 Complete Execution Flow

```
┌─────────────────────────────────────────────────────────────┐
│ 1. APPLICATION START                                        │
├─────────────────────────────────────────────────────────────┤
│ main() → new FinanceTrackerApp() → Constructor runs        │
│ Sets up ExpenseManager                                      │
│ Calls setupGUI() and loadInitialData()                     │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│ 2. DATABASE CONNECTION ESTABLISHED                          │
├─────────────────────────────────────────────────────────────┤
│ loadInitialData() calls:                                    │
│ - DatabaseExpenseManager.getCategories()                    │
│ - DatabaseExpenseManager.getAllExpenses()                   │
│ - chartPanel.loadChartData()                                │
│ - analysisPanel.loadAllData()                               │
│                                                             │
│ JDBC Connection created via DatabaseConnection.getConnection() │
│ Environment variables loaded (SUPABASE_HOST, PORT, USER, etc) │
│ PostgreSQL driver connected                                 │
│ SELECT queries fetch initial data                           │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│ 3. MAIN DASHBOARD OPENS                                     │
├─────────────────────────────────────────────────────────────┤
│ setupGUI() creates:                                         │
│ - Sidebar with navigation buttons                           │
│ - Header with search and filter controls                    │
│ - Summary cards with totals                                 │
│ - Form for adding new expense                               │
│ - Transaction history table                                 │
│ - Charts showing expense visualization                      │
│                                                             │
│ setVisible(true) displays the window                        │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│ 4. USER ADDS NEW EXPENSE                                    │
├─────────────────────────────────────────────────────────────┤
│ User fills form:                                            │
│ - Title field → "Groceries"                                 │
│ - Amount field → "500.50"                                   │
│ - Category dropdown → "Food"                                │
│ - Date field → "2024-01-15"                                 │
│                                                             │
│ User clicks "Add Expense" button                            │
│ ActionListener triggers addExpense()                        │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│ 5. VALIDATION AND DATABASE INSERT                           │
├─────────────────────────────────────────────────────────────┤
│ addExpense() method:                                        │
│ 1. Validate all fields                                      │
│ 2. Create: Expense expense = new Expense(title, amount, ...) │
│ 3. Call: DatabaseExpenseManager.saveExpense(expense)        │
│                                                             │
│ saveExpense() method:                                       │
│ 1. Get Connection via DatabaseConnection.getConnection()   │
│ 2. SELECT category_id FROM categories WHERE name = ?       │
│ 3. PreparedStatement INSERT query:                          │
│    INSERT INTO expenses (title, amount, category_id, date) │
│    VALUES ('Groceries', 500.50, 2, '2024-01-15')           │
│ 4. executeUpdate() inserts record                           │
│ 5. Connection closes (try-with-resources)                   │
│                                                             │
│ Database now has new record with auto-generated expense_id  │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│ 6. REFRESH TRANSACTION TABLE AND DASHBOARD                  │
├─────────────────────────────────────────────────────────────┤
│ Back in addExpense():                                       │
│ 1. Call loadExpensesFromDatabase()                          │
│ 2. getAllExpenses() executes SELECT query                   │
│ 3. ResultSet loops and creates Expense objects             │
│ 4. All expenses added to expensesList ArrayList            │
│ 5. Call refreshTable()                                      │
│                                                             │
│ refreshTable():                                             │
│ 1. Clear table (setRowCount(0))                             │
│ 2. Apply any active search filter                           │
│ 3. For each filtered expense, add row to table              │
│ 4. Calculate totals (sum, count, categories)               │
│ 5. Update summary labels                                    │
│ 6. Call updateMonthlyTotals()                              │
│                                                             │
│ Table now shows new expense row                             │
│ Summary cards show updated totals                           │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│ 7. UPDATE CHARTS AND ANALYSIS                               │
├─────────────────────────────────────────────────────────────┤
│ Back in addExpense():                                       │
│ 1. chartPanel.loadChartData()                              │
│    - getTransactionChartData() fetches for chart            │
│    - repaint() redraws bar chart                            │
│                                                             │
│ 2. analysisPanel.loadAllData()                             │
│    - getExpenseByCategory() groups by category              │
│    - getMonthlyTotal() calculates monthly sums              │
│    - All chart panels reload and repaint                    │
│                                                             │
│ Charts now show updated data                                │
│ Analysis page has latest information                        │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│ 8. SHOW SUCCESS MESSAGE                                     │
├─────────────────────────────────────────────────────────────┤
│ JOptionPane.showMessageDialog():                            │
│ "Expense added successfully!"                               │
│                                                             │
│ Form fields cleared by clearFields()                        │
│ User can add next expense                                   │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│ 9. EDIT OR DELETE TRANSACTION (Optional)                    │
├─────────────────────────────────────────────────────────────┤
│ User clicks Edit or Delete in table row:                   │
│                                                             │
│ If Edit:                                                    │
│ → EditExpenseDialog opens with existing values              │
│ → User modifies fields                                      │
│ → updateExpense() updates database                          │
│ → Data reloaded and all displays refresh                    │
│                                                             │
│ If Delete:                                                  │
│ → Confirmation dialog shown                                │
│ → deleteExpense() removes from database                     │
│ → Data reloaded and all displays refresh                    │
└─────────────────────────────────────────────────────────────┘
```

## 7.2 Data Flow Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                    USER INTERFACE LAYER                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │  Add Form    │  │ Search/Filter│  │  Buttons     │      │
│  │  Input Fields│  │  Controls    │  │ (Edit/Delete)│      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
                            ↑ ↓
┌─────────────────────────────────────────────────────────────┐
│                APPLICATION LOGIC LAYER                      │
│           FinanceTrackerApp.java (Event Handlers)           │
│  - Validates input                                          │
│  - Creates Expense objects                                  │
│  - Orchestrates database calls                              │
│  - Refreshes GUI components                                 │
└─────────────────────────────────────────────────────────────┘
                            ↑ ↓
┌─────────────────────────────────────────────────────────────┐
│              DATABASE ACCESS LAYER (JDBC)                   │
│    DatabaseExpenseManager.java & DatabaseConnection.java    │
│  - Prepares SQL queries                                     │
│  - Executes PreparedStatements                              │
│  - Handles ResultSets                                       │
│  - Manages connections                                      │
└─────────────────────────────────────────────────────────────┘
                            ↑ ↓
┌─────────────────────────────────────────────────────────────┐
│        SUPABASE POSTGRESQL DATABASE LAYER                   │
│  - expenses table (expense_id, title, amount, category_id, │
│                    expense_date)                            │
│  - categories table (category_id, category_name)            │
│  - Executes SQL: INSERT, SELECT, UPDATE, DELETE             │
│  - Returns data via ResultSet                               │
└─────────────────────────────────────────────────────────────┘
```

---

# 8. VIVA PREPARATION - 40+ IMPORTANT QUESTIONS AND ANSWERS

## 8.1 Basic Java Questions

### Q1: What is a class in Java?
**A:** A class is a blueprint or template for creating objects. It defines the structure (variables) and behavior (methods) that objects will have. For example, Expense is a class that defines what an expense should contain (title, amount, category, date).

### Q2: What is an object in Java?
**A:** An object is an instance of a class. It represents a real-world entity with specific values. For example, an expense "Groceries 500 rupees" is an object of the Expense class.

### Q3: What is the difference between a class and an object?
**A:** A class is a template, an object is a concrete instance. A class is like a cookie cutter, an object is like the actual cookie made from that cutter.

### Q4: What are the primitive data types in Java?
**A:** byte, short, int, long, float, double, boolean, char. In our project, we use: long (for IDs), double (for amounts), boolean (for flags), String (for text data).

### Q5: What is a variable and what are the different types?
**A:** A variable is a named container for storing a value. Types: local variables (in methods), instance variables (in classes), static variables (shared by all objects).

### Q6: What is an ArrayList and why is it used?
**A:** ArrayList is a resizable array that can grow or shrink. We use it to store collections of expenses, filtered expenses, and categories because we don't know the size in advance.

### Q7: What is the difference between ArrayList and Array?
**A:** Arrays have fixed size declared at creation. ArrayLists are dynamic and can change size. ArrayLists are more flexible for our application where the number of expenses keeps changing.

### Q8: What is a loop and what types are there?
**A:** Loops repeat code. Types: for loop (fixed iterations), while loop (until condition false), for-each loop (iterate collections). We use for loops to iterate through ArrayLists and while loops with ResultSet.

### Q9: What is an if-else statement used for?
**A:** Conditionals execute code based on conditions. if-else makes decisions. Used throughout: validating input (if title is empty), checking database results (if rows affected > 0).

### Q10: What is try-catch used for?
**A:** Exception handling. Try block runs code, catch block handles errors. Used in database operations - if query fails, catch block shows user-friendly error message instead of crashing.

---

## 8.2 OOP Concepts Questions

### Q11: What is encapsulation and why is it important?
**A:** Encapsulation is hiding internal details using access modifiers. Private variables + public methods = controlled access. Important because it protects data integrity and makes code maintainable. Example: Expense's amount field is private with public getAmount() getter.

### Q12: What are access modifiers?
**A:** public (accessible everywhere), private (only within class), protected (within package and subclasses). We use private for data fields and public for methods to create a controlled interface.

### Q13: What is inheritance and give an example from our project?
**A:** Inheritance is creating a class that inherits from another class. FinanceTrackerApp extends JFrame, so it inherits all JFrame methods. EditExpenseDialog extends JDialog to inherit dialog functionality.

### Q14: What is method overriding?
**A:** A subclass provides its own implementation of a parent class method. Example: TransactionChartPanel overrides paintComponent() to draw a chart instead of displaying blank panel.

### Q15: What is polymorphism?
**A:** Objects of different classes can be used interchangeably. ButtonEditor and EditButtonEditor both extend DefaultCellEditor, so they can be used as CellEditor. Same interface, different behavior.

### Q16: What is a constructor and why is it used?
**A:** Constructor is a special method that initializes objects when created. Named same as class, no return type. Expense has two constructors - one for new expenses, one for existing (with ID).

### Q17: What is constructor overloading?
**A:** Multiple constructors with different parameters. Expense class has: Expense(title, amount, category, date) and Expense(expenseId, title, amount, category, date). Flexibility in how to create objects.

### Q18: What is the difference between instance and static members?
**A:** Instance members belong to objects, static members belong to class. Every Expense object has its own amount, but all DatabaseExpenseManager methods are static (don't need object creation).

### Q19: What is abstraction?
**A:** Hiding complexity, showing only essential features. User doesn't need to know how SQL queries work, just calls DatabaseExpenseManager.getAllExpenses(). Implementation details are hidden.

### Q20: What is an interface?
**A:** Contract that classes must follow. ButtonRenderer implements TableCellRenderer interface, so it must provide getTableCellRendererComponent() method. Ensures consistent behavior.

---

## 8.3 Java Swing Questions

### Q21: What is JFrame?
**A:** Main application window. FinanceTrackerApp extends JFrame to create the main window. Contains all other components, has methods like setVisible(), setSize(), setTitle().

### Q22: What is JPanel?
**A:** Container for organizing components. We use multiple JPanels: dashboardPanel, headerPanel, sidebarPanel to organize and layout different sections of the GUI.

### Q23: What is a layout manager?
**A:** Manages positioning and sizing of components. Types: BorderLayout (5 regions), GridLayout (grid), BoxLayout (vertical/horizontal). Main frame uses BorderLayout to position sidebar and content.

### Q24: What is JButton and how does it work?
**A:** Clickable button. When clicked, triggers ActionListener. Add button calls addExpense(), delete button calls deleteExpense(). ActionListener implements event handling.

### Q25: What is ActionListener?
**A:** Interface for handling button clicks and events. button.addActionListener(e -> methodToCall()) handles the click event. Lambda expression (Java 8+) makes it concise.

### Q26: What is JTable and how is data displayed?
**A:** Table component for displaying data in rows and columns. Uses TableModel (DefaultTableModel in our case) to store data. transactionTable shows expenses with Edit/Delete buttons.

### Q27: What is a CellEditor and CellRenderer in JTable?
**A:** CellEditor handles editing a cell, CellRenderer handles displaying a cell. ButtonEditor edits Delete button, ButtonRenderer displays Delete button. Allows custom components in table cells.

### Q28: What is JComboBox?
**A:** Dropdown list component. categoryCombo shows categories for selection. filterCategoryCombo for filtering by category. User clicks to select options.

### Q29: What is JTextField?
**A:** Single-line text input field. titleField, amountField, dateField for user input. getText() gets value, setText() sets value.

### Q30: What is JDialog?
**A:** Modal dialog window. EditExpenseDialog extends JDialog to edit existing expenses. super(parent, title, true) makes it modal (blocks interaction with main window).

### Q31: What is JOptionPane used for?
**A:** Shows message dialogs. showMessageDialog() for messages, showConfirmDialog() for yes/no questions. Used for validation errors, success messages, and confirmations.

### Q32: What is JScrollPane?
**A:** Scrollable container. Wraps transactionTable to allow scrolling when table has many rows. Automatically shows scrollbars when needed.

### Q33: What is a JLabel?
**A:** Non-editable text display. Used for titles, category labels, and summary values. titleLabel, totalExpensesLabel display static or changing text.

---

## 8.4 JDBC Questions

### Q34: What is JDBC and what does it do?
**A:** Java Database Connectivity - API for connecting Java apps to databases. Allows executing SQL queries and retrieving results. Loads driver, creates connection, executes queries.

### Q35: What is a JDBC Driver?
**A:** Software that implements JDBC interface for specific database. PostgreSQL driver (org.postgresql.Driver) in our project. Loaded in static block: Class.forName("org.postgresql.Driver").

### Q36: What is a Connection in JDBC?
**A:** Represents active connection to database. Created via DriverManager.getConnection(url, username, password). Used to create statements and execute queries. Closed after use (try-with-resources handles this).

### Q37: What is PreparedStatement and why use it?
**A:** Safely executes SQL queries with parameters. `?` placeholders for parameters. Prevents SQL injection. Example: setString(1, title) binds value to first parameter.

### Q38: What is a ResultSet?
**A:** Holds results from SELECT query. rs.next() moves to next row, returns true if row exists. getString(), getDouble(), getLong() retrieve column values. Looped to process all results.

### Q39: What is executeUpdate() and executeQuery()?
**A:** executeUpdate() for INSERT/UPDATE/DELETE - returns number of rows affected. executeQuery() for SELECT - returns ResultSet with results. Used based on query type.

### Q40: How do we handle database errors?
**A:** Try-catch blocks catch SQLException. Database methods throw SQLException. GUI layer catches and shows user-friendly error messages via JOptionPane.

---

## 8.5 Project-Specific Questions

### Q41: How does the application connect to Supabase?
**A:** Environment variables (SUPABASE_HOST, PORT, DB, USER, PASSWORD) hold connection details. DatabaseConnection.getConnection() builds URL and connects via JDBC. All database operations use this connection.

### Q42: How are expenses added to the database?
**A:** addExpense() validates input, creates Expense object, calls DatabaseExpenseManager.saveExpense(). saveExpense() uses PreparedStatement to INSERT into expenses table with category lookup.

### Q43: How are expenses displayed in the table?
**A:** getAllExpenses() executes SELECT with JOIN to get category names. Creates Expense objects from ResultSet. refreshTable() loops through ArrayList and adds rows to JTable. Table shows all fields with Edit/Delete buttons.

### Q44: How does filtering by category work?
**A:** getExpensesByCategory() executes SELECT with WHERE clause for category. User selects category from dropdown, applyFilters() called, filtered results displayed. "All Categories" shows all.

### Q45: How does the monthly summary work?
**A:** getMonthlyTotal() and getMonthlyTransactionCount() use SQL with PostgreSQL date functions (TO_CHAR). Calculate totals for current month. updateMonthlyTotals() displays in summary cards.

### Q46: How do charts get data?
**A:** loadChartData() calls database methods (getExpenseByCategory, getTransactionChartData). Returns ArrayList of data objects. paintComponent() loops through data drawing bars with Graphics2D.

### Q47: What happens when user clicks Delete button?
**A:** ButtonEditor calls deleteExpense(). Confirmation dialog shown. If yes: deleteExpense() executes DELETE query, data reloaded, displays updated. If no: no changes.

### Q48: What happens when user edits an expense?
**A:** EditButtonEditor calls editExpense(). EditExpenseDialog opens. User modifies and saves. updateExpense() executes UPDATE query. Data reloaded, all displays refresh.

### Q49: How is input validation performed?
**A:** addExpense() and EditExpenseDialog.saveExpense() validate each field: title (not empty, <100 chars), amount (valid number, >0, <999M), category (selected), date (YYYY-MM-DD format). Shows specific error messages.

### Q50: How are changes reflected in all displays?
**A:** After database operation, loadExpensesFromDatabase() fetches fresh data. Then refreshTable() updates transaction table. chartPanel.loadChartData() refreshes charts. analysisPanel.loadAllData() refreshes analysis. updateMonthlyTotals() updates monthly cards.

---

# 9. FINAL FILE-WISE SUMMARY TABLE

| File Name | Main Responsibility | OOP Concepts | Java Concepts | Important Methods |
|-----------|-------------------|--------------|---------------|-------------------|
| Expense.java | Data model for expense entity | Encapsulation, Constructor Overloading | Private fields, Public getters, Primitive types | Constructor(5 params), Constructor(4 params), getters |
| ExpenseManager.java | Business logic for expense calculations | Classes & Objects, Encapsulation, Aggregation | ArrayList, for loop, double arithmetic | addExpense(), calculateTotalAmount(), displayAllExpenses() |
| DatabaseConnection.java | Database connectivity management | Static methods, Exception handling | Static block, Class.forName(), DriverManager | getConnection() |
| DatabaseExpenseManager.java | JDBC database operations | Static methods, Encapsulation | PreparedStatement, ResultSet, try-with-resources | saveExpense(), getAllExpenses(), updateExpense(), deleteExpense(), get*() methods |
| FinanceTrackerApp.java | Main GUI and orchestration | Inheritance (extends JFrame), Event handling, Encapsulation | Swing components, ActionListener, ArrayLists, exception handling | addExpense(), editExpense(), deleteExpense(), loadExpensesFromDatabase(), createTableCard(), applyFilters() |
| EditExpenseDialog.java | Modal dialog for editing expenses | Inheritance (extends JDialog), Encapsulation | Swing components, try-catch, input validation | setupGUI(), loadExpenseData(), saveExpense() |
| ButtonEditor.java | Custom cell editor for Delete button | Inheritance (extends DefaultCellEditor), Method Overriding | ActionListener, anonymous inner class | getTableCellEditorComponent(), getCellEditorValue() |
| ButtonRenderer.java | Custom cell renderer for Delete button | Inheritance (extends JButton), Interface Implementation | Swing rendering | getTableCellRendererComponent() |
| EditButtonEditor.java | Custom cell editor for Edit button | Inheritance (extends DefaultCellEditor), Method Overriding | ActionListener, anonymous inner class | getTableCellEditorComponent(), getCellEditorValue() |
| EditButtonRenderer.java | Custom cell renderer for Edit button | Inheritance (extends JButton), Interface Implementation | Swing rendering | getTableCellRendererComponent() |
| TransactionData.java | Data holder for chart data | Encapsulation, POJ (Plain Old Java Object) | Private fields, Public getters | Constructors, getters |
| CategoryExpenseData.java | Data holder for category aggregation | Encapsulation, POJ | Private fields, Public getters | Constructors, getters |
| TransactionChartPanel.java | Bar chart for individual transactions | Inheritance (extends JPanel), Method Overriding | Graphics2D, for loops, Math calculations | loadChartData(), paintComponent() |
| CategoryChartPanel.java | Bar chart for category-wise expenses | Inheritance (extends JPanel), Method Overriding | Graphics2D, for loops, Math calculations | loadChartData(), paintComponent() |
| AnalysisPanel.java | Container for analysis components | Aggregation, Encapsulation | BorderLayout, GridLayout | loadAllData(), refreshAllGraphs() |
| ExpenseSummaryPanel.java | Summary cards for analysis | Aggregation, Encapsulation | GridLayout, try-catch | loadSummaryData(), createSummaryCard() |

---

# 10. KEY TAKEAWAYS FOR VIVA

## 10.1 Project Architecture
- **Layered Architecture**: Presentation → Business Logic → Data Access → Database
- **Separation of Concerns**: Each class has single responsibility
- **Reusability**: Database methods reused across features

## 10.2 OOP Implementation
- **Encapsulation**: All data private, accessed through methods
- **Inheritance**: Custom components inherit from Swing classes
- **Polymorphism**: Different button types use common interface
- **Constructor Overloading**: Flexibility in object creation

## 10.3 JDBC Best Practices
- **PreparedStatement**: Prevents SQL injection
- **try-with-resources**: Automatic connection cleanup
- **Exception Handling**: User-friendly error messages
- **Connection Pooling Pattern**: Reusable getConnection() method

## 10.4 GUI Development
- **Event-Driven**: ActionListeners respond to user actions
- **Layout Managers**: BorderLayout, GridLayout for responsive design
- **Custom Components**: Cell editors/renderers for table functionality
- **Modal Dialogs**: EditExpenseDialog for focused user interaction

## 10.5 Data Management
- **ArrayList Collections**: Dynamic expense storage
- **In-Memory Filtering**: Search and category filtering
- **Database Aggregation**: SQL GROUP BY and SUM for analytics
- **Real-time Refresh**: All displays update after operations

---

# CONCLUSION

This Personal Finance Tracker demonstrates essential OOPJ concepts including classes, objects, inheritance, encapsulation, polymorphism, constructors, exception handling, and method overriding. The application effectively integrates Java Swing for GUI, JDBC for database connectivity, and PostgreSQL for data persistence. Each component follows single responsibility principle, making the code maintainable and extensible. The multi-layered architecture separates concerns cleanly: presentation logic in Swing components, business logic in managers, and database operations in JDBC classes.

For viva preparation, focus on understanding:
1. How data flows from UI to database and back
2. Why each OOP concept is used (encapsulation for data protection, inheritance for code reuse, polymorphism for flexibility)
3. How JDBC PreparedStatement prevents SQL injection
4. How exception handling provides graceful error management
5. How event-driven programming handles user interactions

The project is a complete, functional desktop application suitable for a second-year B.Tech OOPJ course, demonstrating practical understanding of object-oriented design and database connectivity.

---

END OF DOCUMENTATION

