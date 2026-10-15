// ============================================================
// MOCK DATA — replace these with real API calls later
// API base URL will be: http://localhost:8080/api/expenses
// ============================================================

export interface Expense {
  id: number;
  title: string;
  amount: number;
  category: string;
  expenseDate: string; // YYYY-MM-DD
  description?: string;
  createdAt: string;
}

export interface MonthlyTotal {
  month: number;   // 1–12
  total: number;
}

export interface CategoryTotal {
  category: string;
  total: number;
}

export interface Summary {
  totalSpending: number;
  totalExpenses: number;
  averageExpense: number;
}

// --- Recent transactions (maps to GET /api/expenses) ---
export const mockExpenses: Expense[] = [
  { id: 1,  title: 'Canteen Lunch',        amount: 85.50,  category: 'Food',      expenseDate: '2026-10-14', createdAt: '2026-10-14T12:00:00' },
  { id: 2,  title: 'Metro Card Recharge',  amount: 200.00, category: 'Travel',    expenseDate: '2026-10-13', createdAt: '2026-10-13T09:30:00' },
  { id: 3,  title: 'Python Textbook',      amount: 450.00, category: 'Study',     expenseDate: '2026-10-12', createdAt: '2026-10-12T14:00:00' },
  { id: 4,  title: 'Mobile Data Pack',     amount: 299.00, category: 'Utilities', expenseDate: '2026-10-10', createdAt: '2026-10-10T18:00:00' },
  { id: 5,  title: 'Pizza with friends',   amount: 340.00, category: 'Food',      expenseDate: '2026-10-08', createdAt: '2026-10-08T20:00:00' },
  { id: 6,  title: 'Bus Pass',             amount: 150.00, category: 'Travel',    expenseDate: '2026-10-06', createdAt: '2026-10-06T08:00:00' },
  { id: 7,  title: 'Lab Manual Printing',  amount: 60.00,  category: 'Study',     expenseDate: '2026-10-05', createdAt: '2026-10-05T11:00:00' },
  { id: 8,  title: 'Electricity Bill',     amount: 520.00, category: 'Utilities', expenseDate: '2026-09-30', createdAt: '2026-09-30T10:00:00' },
  { id: 9,  title: 'Breakfast',            amount: 55.00,  category: 'Food',      expenseDate: '2026-09-28', createdAt: '2026-09-28T08:30:00' },
  { id: 10, title: 'Train Ticket Home',    amount: 380.00, category: 'Travel',    expenseDate: '2026-09-25', createdAt: '2026-09-25T07:00:00' },
];

// --- Monthly totals (maps to GET /api/expenses/analytics/monthly) ---
export const mockMonthlyTotals: MonthlyTotal[] = [
  { month: 1,  total: 0 },
  { month: 2,  total: 0 },
  { month: 3,  total: 1200 },
  { month: 4,  total: 980 },
  { month: 5,  total: 1450 },
  { month: 6,  total: 760 },
  { month: 7,  total: 1100 },
  { month: 8,  total: 890 },
  { month: 9,  total: 1640 },
  { month: 10, total: 1584.50 },
  { month: 11, total: 0 },
  { month: 12, total: 0 },
];

// --- Category totals (maps to GET /api/expenses/analytics/categories) ---
export const mockCategoryTotals: CategoryTotal[] = [
  { category: 'Food',      total: 480.50 },
  { category: 'Utilities', total: 819.00 },
  { category: 'Travel',    total: 730.00 },
  { category: 'Study',     total: 510.00 },
];

// --- Overall summary (maps to GET /api/expenses/analytics/summary) ---
export const mockSummary: Summary = {
  totalSpending: 2539.50,
  totalExpenses: 10,
  averageExpense: 253.95,
};

// --- Month abbreviations for chart labels ---
export const MONTH_LABELS = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
                              'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

// --- Category colours (consistent across charts and badges) ---
export const CATEGORY_COLORS: Record<string, string> = {
  Food:      '#4f8ef7',
  Travel:    '#f7894f',
  Study:     '#6fcf97',
  Utilities: '#bb6bd9',
  Other:     '#aaaaaa',
};
