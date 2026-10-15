import { useState, useEffect } from 'react';
import { CATEGORY_COLORS } from '../data/mockData';
import './AddExpenseModal.css';

const API_BASE = 'http://localhost:8080';

// The five categories defined in mockData — drives both the select and the badge colours
const CATEGORIES = Object.keys(CATEGORY_COLORS).filter(c => c !== 'Other');

interface Props {
  onClose: () => void;   // close without saving
  onSaved: () => void;   // close AND trigger a data refresh
  editingExpense?: Expense;  // if provided, edit this expense; otherwise create new
}

interface Expense {
  id: number;
  title: string;
  amount: number;
  category: string;
  expenseDate: string;
  description?: string;
  createdAt: string;
}

interface FormFields {
  title: string;
  amount: string;        // kept as string so the input is editable; parsed on submit
  category: string;
  expenseDate: string;   // YYYY-MM-DD
  description: string;
}

interface FieldErrors {
  title?: string;
  amount?: string;
  category?: string;
  expenseDate?: string;
}

const EMPTY_FORM: FormFields = {
  title: '',
  amount: '',
  category: '',
  expenseDate: new Date().toISOString().slice(0, 10), // today as default
  description: '',
};

export default function AddExpenseModal({ onClose, onSaved, editingExpense }: Props) {
  const [form, setForm]           = useState<FormFields>(() => {
    if (editingExpense) {
      return {
        title: editingExpense.title,
        amount: String(editingExpense.amount),
        category: editingExpense.category,
        expenseDate: editingExpense.expenseDate,
        description: editingExpense.description ?? '',
      };
    }
    return EMPTY_FORM;
  });
  const [fieldErrors, setErrors]  = useState<FieldErrors>({});
  const [apiError, setApiError]   = useState<string | null>(null);
  const [saving, setSaving]       = useState(false);

  // Close on Escape key
  useEffect(() => {
    function onKey(e: KeyboardEvent) { if (e.key === 'Escape') onClose(); }
    window.addEventListener('keydown', onKey);
    return () => window.removeEventListener('keydown', onKey);
  }, [onClose]);

  function handleChange(e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
    // Clear field error as user types
    setErrors(prev => ({ ...prev, [name]: undefined }));
    setApiError(null);
  }

  // Client-side validation — mirrors the backend constraints
  function validate(): boolean {
    const errs: FieldErrors = {};
    if (!form.title.trim())          errs.title       = 'Title is required.';
    if (!form.category)              errs.category    = 'Please select a category.';
    if (!form.expenseDate)           errs.expenseDate = 'Date is required.';
    if (!form.amount.trim()) {
      errs.amount = 'Amount is required.';
    } else {
      const n = parseFloat(form.amount);
      if (isNaN(n) || n <= 0)        errs.amount = 'Amount must be greater than zero.';
    }
    setErrors(errs);
    return Object.keys(errs).length === 0;
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setApiError(null);
    if (!validate()) return;

    setSaving(true);
    try {
      const body = {
        title:       form.title.trim(),
        amount:      parseFloat(form.amount),
        category:    form.category,
        expenseDate: form.expenseDate,
        description: form.description.trim() || undefined, // omit if empty
      };

      const isEditing = !!editingExpense;
      const url = isEditing ? `${API_BASE}/api/expenses/${editingExpense!.id}` : `${API_BASE}/api/expenses`;
      const method = isEditing ? 'PUT' : 'POST';

      const res = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body:    JSON.stringify(body),
      });

      if (!res.ok) {
        // Try to parse a readable backend error message
        let msg = `Request failed (${res.status})`;
        try {
          const data = await res.json();
          if (data.message) msg = data.message;
          // Backend returns validation errors as an `errors` map
          if (data.errors) {
            const lines = Object.values(data.errors as Record<string, string>).join(' ');
            msg = lines;
          }
        } catch { /* ignore JSON parse failure */ }
        setApiError(msg);
        return;
      }

      onSaved(); // success — tell parent to refresh and close
    } catch {
      setApiError('Could not reach the server. Is the backend running?');
    } finally {
      setSaving(false);
    }
  }

  return (
    <div className="modal-overlay" onClick={onClose} role="dialog" aria-modal="true">
      {/* Stop click propagation so clicking the panel itself doesn't close it */}
      <div className="modal-panel" onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <h2 className="modal-title">{editingExpense ? 'Edit Expense' : 'Add Expense'}</h2>
          <button className="modal-close" onClick={onClose} aria-label="Close">✕</button>
        </div>

        <form onSubmit={handleSubmit} noValidate>
          {/* Title */}
          <div className="form-group">
            <label className="form-label" htmlFor="title">Title <span className="required">*</span></label>
            <input
              id="title" name="title" type="text"
              className={`form-input${fieldErrors.title ? ' form-input--error' : ''}`}
              placeholder="e.g. Canteen lunch"
              maxLength={100}
              value={form.title}
              onChange={handleChange}
              disabled={saving}
            />
            {fieldErrors.title && <span className="form-error">{fieldErrors.title}</span>}
          </div>

          {/* Amount + Category side by side */}
          <div className="form-row">
            <div className="form-group">
              <label className="form-label" htmlFor="amount">Amount (₹) <span className="required">*</span></label>
              <input
                id="amount" name="amount" type="number"
                className={`form-input${fieldErrors.amount ? ' form-input--error' : ''}`}
                placeholder="0.00"
                min="0.01" step="0.01"
                value={form.amount}
                onChange={handleChange}
                disabled={saving}
              />
              {fieldErrors.amount && <span className="form-error">{fieldErrors.amount}</span>}
            </div>

            <div className="form-group">
              <label className="form-label" htmlFor="category">Category <span className="required">*</span></label>
              <select
                id="category" name="category"
                className={`form-input form-select${fieldErrors.category ? ' form-input--error' : ''}`}
                value={form.category}
                onChange={handleChange}
                disabled={saving}
              >
                <option value="">Select…</option>
                {CATEGORIES.map(cat => (
                  <option key={cat} value={cat}>{cat}</option>
                ))}
              </select>
              {fieldErrors.category && <span className="form-error">{fieldErrors.category}</span>}
            </div>
          </div>

          {/* Date */}
          <div className="form-group">
            <label className="form-label" htmlFor="expenseDate">Date <span className="required">*</span></label>
            <input
              id="expenseDate" name="expenseDate" type="date"
              className={`form-input${fieldErrors.expenseDate ? ' form-input--error' : ''}`}
              value={form.expenseDate}
              onChange={handleChange}
              disabled={saving}
            />
            {fieldErrors.expenseDate && <span className="form-error">{fieldErrors.expenseDate}</span>}
          </div>

          {/* Description (optional) */}
          <div className="form-group">
            <label className="form-label" htmlFor="description">Description <span className="optional">(optional)</span></label>
            <textarea
              id="description" name="description"
              className="form-input form-textarea"
              placeholder="Any extra notes…"
              maxLength={500}
              rows={2}
              value={form.description}
              onChange={handleChange}
              disabled={saving}
            />
          </div>

          {/* API-level error */}
          {apiError && <p className="form-api-error">{apiError}</p>}

          {/* Actions */}
          <div className="modal-actions">
            <button type="button" className="btn-secondary" onClick={onClose} disabled={saving}>
              Cancel
            </button>
            <button type="submit" className="btn-primary" disabled={saving}>
              {saving ? (editingExpense ? 'Saving…' : 'Saving…') : (editingExpense ? 'Update Expense' : 'Save Expense')}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
