import { useState } from 'react';
import Sidebar, { Page } from './components/Sidebar';
import Header from './components/Header';
import Overview from './pages/Overview';
import Transactions from './pages/Transactions';
import Analytics from './pages/Analytics';
import AddExpenseModal from './components/AddExpenseModal';
import { useExpenses } from './hooks/useExpenses';
import { useAnalytics } from './hooks/useAnalytics';
import { Expense } from './data/mockData';
import './App.css';

function renderPage(
  page: Page,
  expenses: ReturnType<typeof useExpenses>,
  analytics: ReturnType<typeof useAnalytics>,
  onEdit: (e: Expense) => void,
  onDelete: (id: number) => void
) {
  switch (page) {
    case 'overview':     return <Overview expensesHook={expenses} analyticsHook={analytics} />;
    case 'transactions': return <Transactions expensesHook={expenses} onEdit={onEdit} onDelete={onDelete} />;
    case 'analytics':    return <Analytics analyticsHook={analytics} />;
  }
}

export default function App() {
  const [activePage, setActivePage] = useState<Page>('overview');
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [editingExpense, setEditingExpense] = useState<Expense | null>(null);
  const [deleteConfirmId, setDeleteConfirmId] = useState<number | null>(null);

  // Single shared hooks — all pages see the same data
  const expensesHook = useExpenses();
  const analyticsHook = useAnalytics();

  function handleSaved() {
    setShowModal(false);
    setEditingExpense(null);
    expensesHook.refresh();  // refresh transactions
    analyticsHook.refresh(); // refresh analytics
  }

  function handleAddExpense() {
    setEditingExpense(null);
    setShowModal(true);
  }

  function handleEditExpense(expense: Expense) {
    setEditingExpense(expense);
    setShowModal(true);
  }

  function handleDeleteClick(expenseId: number) {
    setDeleteConfirmId(expenseId);
  }

  async function handleConfirmDelete() {
    if (deleteConfirmId === null) return;

    try {
      const res = await fetch(`http://localhost:8080/api/expenses/${deleteConfirmId}`, {
        method: 'DELETE',
      });

      if (!res.ok) {
        alert(`Failed to delete expense (${res.status})`);
        return;
      }

      setDeleteConfirmId(null);
      expensesHook.refresh();
      analyticsHook.refresh(); // refresh analytics after delete
    } catch (err) {
      alert('Could not reach the server. Is the backend running?');
    }
  }

  function handleCancelDelete() {
    setDeleteConfirmId(null);
  }

  return (
    <div className="app-layout">
      <Sidebar
        activePage={activePage}
        onNavigate={setActivePage}
        isOpen={sidebarOpen}
        onClose={() => setSidebarOpen(false)}
      />
      <div className="app-main">
        <Header
          activePage={activePage}
          onAddExpense={handleAddExpense}
          onMenuToggle={() => setSidebarOpen(o => !o)}
        />
        <main className="app-content">
          {renderPage(activePage, expensesHook, analyticsHook, handleEditExpense, handleDeleteClick)}
        </main>
      </div>

      {showModal && (
        <AddExpenseModal
          onClose={() => {
            setShowModal(false);
            setEditingExpense(null);
          }}
          onSaved={handleSaved}
          editingExpense={editingExpense ?? undefined}
        />
      )}

      {deleteConfirmId !== null && (
        <div className="modal-overlay" onClick={handleCancelDelete} role="dialog" aria-modal="true">
          <div className="modal-panel" onClick={e => e.stopPropagation()} style={{ maxWidth: '400px' }}>
            <div className="modal-header">
              <h2 className="modal-title">Delete Expense?</h2>
              <button className="modal-close" onClick={handleCancelDelete} aria-label="Close">✕</button>
            </div>
            <p style={{ margin: '16px 0', color: '#666' }}>
              Are you sure you want to delete this expense? This action cannot be undone.
            </p>
            <div className="modal-actions">
              <button className="btn-secondary" onClick={handleCancelDelete}>
                Cancel
              </button>
              <button className="btn-danger" onClick={handleConfirmDelete}>
                Delete
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
