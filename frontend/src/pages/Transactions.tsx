import { useState } from 'react';
import TransactionTable from '../components/TransactionTable';
import { CATEGORY_COLORS, Expense } from '../data/mockData';
import { useExpenses } from '../hooks/useExpenses';
import { sortExpensesByDateDesc } from '../utils/sortExpenses';
import './Transactions.css';

interface Props {
  expensesHook: ReturnType<typeof useExpenses>;
  onEdit?: (expense: Expense) => void;
  onDelete?: (expenseId: number) => void;
}

export default function Transactions({ expensesHook, onEdit, onDelete }: Props) {
  const { expenses, loading, error } = expensesHook;
  const [filter, setFilter] = useState('All');
  const [search, setSearch] = useState('');

  // Sort expenses by date first (most recent first)
  const sortedExpenses = sortExpensesByDateDesc(expenses);

  // Derive category list from sorted data
  const allCategories = ['All', ...Array.from(new Set(sortedExpenses.map(e => e.category))).sort()];

  const filtered = sortedExpenses.filter(e => {
    const matchCat = filter === 'All' || e.category === filter;
    const matchSearch = e.title.toLowerCase().includes(search.toLowerCase());
    return matchCat && matchSearch;
  });

  const total = filtered.reduce((sum, e) => sum + e.amount, 0);

  return (
    <div className="transactions">
      {/* Toolbar */}
      <div className="transactions__toolbar">
        <input
          className="transactions__search"
          type="text"
          placeholder="Search transactions…"
          value={search}
          onChange={e => setSearch(e.target.value)}
        />
        <div className="transactions__filters">
          {allCategories.map(cat => {
            const color = cat === 'All' ? '#4f8ef7' : (CATEGORY_COLORS[cat] ?? '#aaa');
            const isActive = filter === cat;
            return (
              <button
                key={cat}
                className={`transactions__filter-btn${isActive ? ' transactions__filter-btn--active' : ''}`}
                style={isActive ? { background: color, borderColor: color, color: '#fff' } : {}}
                onClick={() => setFilter(cat)}
              >
                {cat}
              </button>
            );
          })}
        </div>
      </div>

      {loading && <p className="transactions__status">Loading transactions…</p>}

      {error && (
        <p className="transactions__status transactions__status--error">
          Could not load transactions: {error}
        </p>
      )}

      {!loading && !error && expenses.length === 0 && (
        <p className="transactions__status">No transactions found. Add your first expense!</p>
      )}

      {!loading && !error && expenses.length > 0 && (
        <>
          <div className="transactions__meta">
            <span>{filtered.length} transaction{filtered.length !== 1 ? 's' : ''}</span>
            <span className="transactions__meta-total">
              Total: ₹{total.toLocaleString('en-IN', { minimumFractionDigits: 2 })}
            </span>
          </div>
          <TransactionTable expenses={filtered} showTitle={false} onEdit={onEdit} onDelete={onDelete} />
        </>
      )}
    </div>
  );
}
