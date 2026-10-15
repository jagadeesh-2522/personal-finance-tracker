import { Expense } from '../data/mockData';

/**
 * Sort expenses by expenseDate descending (newest first),
 * then by createdAt descending as tie-breaker.
 * Does not mutate the original array.
 */
export function sortExpensesByDateDesc(expenses: Expense[]): Expense[] {
  return [...expenses].sort((a, b) => {
    const dateA = new Date(a.expenseDate).getTime();
    const dateB = new Date(b.expenseDate).getTime();
    
    if (dateA !== dateB) {
      return dateB - dateA; // expenseDate DESC
    }
    
    // Tie-breaker: sort by createdAt DESC if available
    const createdA = a.createdAt ? new Date(a.createdAt).getTime() : 0;
    const createdB = b.createdAt ? new Date(b.createdAt).getTime() : 0;
    return createdB - createdA; // createdAt DESC
  });
}
