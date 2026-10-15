import { Expense, CATEGORY_COLORS } from '../data/mockData';
import './TransactionTable.css';

interface Props {
  expenses: Expense[];
  limit?: number;           // show only first N rows when set
  showTitle?: boolean;
  onEdit?: (expense: Expense) => void;    // called when Edit button clicked
  onDelete?: (expenseId: number) => void; // called when Delete button clicked
}

function formatDate(dateStr: string): string {
  const d = new Date(dateStr + 'T00:00:00');
  return d.toLocaleDateString('en-IN', { day: '2-digit', month: 'short', year: 'numeric' });
}

export default function TransactionTable({ expenses, limit, showTitle = true, onEdit, onDelete }: Props) {
  const rows = limit ? expenses.slice(0, limit) : expenses;

  return (
    <div className="tx-card">
      {showTitle && <h3 className="chart-card__title">Recent Transactions</h3>}
      <div className="tx-table-wrap">
        <table className="tx-table">
          <thead>
            <tr>
              <th>Date</th>
              <th>Title</th>
              <th>Category</th>
              <th className="tx-table__amount-col">Amount</th>
              {(onEdit || onDelete) && <th className="tx-table__actions-col">Actions</th>}
            </tr>
          </thead>
          <tbody>
            {rows.map(exp => {
              const color = CATEGORY_COLORS[exp.category] ?? CATEGORY_COLORS['Other'];
              return (
                <tr key={exp.id}>
                  <td className="tx-table__date">{formatDate(exp.expenseDate)}</td>
                  <td className="tx-table__title">{exp.title}</td>
                  <td>
                    <span className="tx-badge" style={{ background: color + '22', color }}>
                      {exp.category}
                    </span>
                  </td>
                  <td className="tx-table__amount">
                    ₹{exp.amount.toLocaleString('en-IN', { minimumFractionDigits: 2 })}
                  </td>
                  {(onEdit || onDelete) && (
                    <td className="tx-table__actions">
                      {onEdit && (
                        <button
                          className="tx-btn-action tx-btn-edit"
                          onClick={() => onEdit(exp)}
                          title="Edit expense"
                          aria-label={`Edit ${exp.title}`}
                        >
                          ✎
                        </button>
                      )}
                      {onDelete && (
                        <button
                          className="tx-btn-action tx-btn-delete"
                          onClick={() => onDelete(exp.id)}
                          title="Delete expense"
                          aria-label={`Delete ${exp.title}`}
                        >
                          ✕
                        </button>
                      )}
                    </td>
                  )}
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </div>
  );
}
