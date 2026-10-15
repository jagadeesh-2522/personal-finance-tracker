import MonthlyChart from '../components/MonthlyChart';
import CategoryChart from '../components/CategoryChart';
import { useAnalytics } from '../hooks/useAnalytics';
import { CATEGORY_COLORS } from '../data/mockData';
import './Analytics.css';

interface Props {
  analyticsHook: ReturnType<typeof useAnalytics>;
}

function formatCurrency(n: number): string {
  return '₹' + n.toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

export default function Analytics({ analyticsHook }: Props) {
  const { summary, monthly, categories, loading, error } = analyticsHook;

  if (loading) {
    return <div className="analytics"><p>Loading analytics…</p></div>;
  }

  if (error) {
    return <div className="analytics"><p style={{ color: '#e05c5c' }}>Could not load analytics: {error}</p></div>;
  }

  // Highest category
  const topCategory = categories.length > 0 ? categories[0] : null;

  // Peak month
  const monthsWithData = monthly.filter(m => m.total > 0);
  const peakMonth = monthsWithData.length > 0 
    ? monthsWithData.reduce((a, b) => (a.total > b.total ? a : b))
    : null;

  // Grand total for percentages
  const grandTotal = categories.reduce((s, c) => s + c.total, 0) || 1;

  return (
    <div className="analytics">
      {/* Charts */}
      <MonthlyChart data={monthly} />
      <CategoryChart data={categories} />

      {/* Insights cards */}
      <div className="analytics__insights">
        {/* Top category */}
        <div className="insight-card">
          <span className="insight-card__label">Top Category</span>
          <span className="insight-card__value">{topCategory?.category ?? '—'}</span>
          <span className="insight-card__sub">
            {topCategory ? formatCurrency(topCategory.total) : '—'}
            {topCategory ? ` · ${((topCategory.total / grandTotal) * 100).toFixed(0)}% of total` : ''}
          </span>
          <span
            className="insight-card__bar"
            style={{
              background: topCategory ? (CATEGORY_COLORS[topCategory.category] ?? '#4f8ef7') : '#ccc',
              width: topCategory ? `${(topCategory.total / grandTotal) * 100}%` : '0%',
            }}
          />
        </div>

        {/* Peak month */}
        <div className="insight-card">
          <span className="insight-card__label">Peak Month</span>
          <span className="insight-card__value">
            {peakMonth
              ? new Date(2026, peakMonth.month - 1).toLocaleString('en-IN', { month: 'long' })
              : '—'}
          </span>
          <span className="insight-card__sub">
            {peakMonth ? formatCurrency(peakMonth.total) : '—'}
          </span>
          <span className="insight-card__bar" style={{ background: '#f7894f', width: peakMonth ? '100%' : '0%' }} />
        </div>

        {/* Total spending */}
        <div className="insight-card">
          <span className="insight-card__label">Total Spending</span>
          <span className="insight-card__value">{summary ? formatCurrency(summary.totalSpending) : '—'}</span>
          <span className="insight-card__sub">
            {summary ? `${summary.totalExpenses} transaction${summary.totalExpenses !== 1 ? 's' : ''}` : '—'}
          </span>
          <span className="insight-card__bar" style={{ background: '#bb6bd9', width: '60%' }} />
        </div>

        {/* Active months */}
        <div className="insight-card">
          <span className="insight-card__label">Active Months</span>
          <span className="insight-card__value">{monthsWithData.length} / 12</span>
          <span className="insight-card__sub">months with recorded spending</span>
          <span
            className="insight-card__bar"
            style={{ background: '#6fcf97', width: `${(monthsWithData.length / 12) * 100}%` }}
          />
        </div>
      </div>
    </div>
  );
}
