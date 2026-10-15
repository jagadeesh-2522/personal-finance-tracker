import SummaryCard from '../components/SummaryCard';
import MonthlyChart from '../components/MonthlyChart';
import CategoryChart from '../components/CategoryChart';
import TransactionTable from '../components/TransactionTable';
import { useExpenses } from '../hooks/useExpenses';
import { useAnalytics } from '../hooks/useAnalytics';
import { sortExpensesByDateDesc } from '../utils/sortExpenses';
import './Overview.css';

interface Props {
  expensesHook: ReturnType<typeof useExpenses>;
  analyticsHook: ReturnType<typeof useAnalytics>;
}

function formatCurrency(n: number): string {
  return '₹' + n.toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

export default function Overview({ expensesHook, analyticsHook }: Props) {
  const { expenses, loading: expLoading, error: expError } = expensesHook;
  const { summary, monthly, categories, loading: analyticsLoading, error: analyticsError } = analyticsHook;

  // Sort expenses: most recent first (expenseDate DESC, then createdAt DESC as tie-breaker)
  const sortedExpenses = sortExpensesByDateDesc(expenses);

  return (
    <div className="overview">
      {/* Summary cards — use real analytics data */}
      <section className="overview__cards">
        <SummaryCard
          title="Total Spending"
          value={summary ? formatCurrency(summary.totalSpending) : '—'}
          subtitle="All time"
          accent="#4f8ef7"
        />
        <SummaryCard
          title="Transactions"
          value={summary ? String(summary.totalExpenses) : '—'}
          subtitle="Total recorded"
          accent="#6fcf97"
        />
        <SummaryCard
          title="Average Expense"
          value={summary ? formatCurrency(summary.averageExpense) : '—'}
          subtitle="Per transaction"
          accent="#f7894f"
        />
        <SummaryCard
          title="Monthly Spending"
          value={summary ? formatCurrency(summary.totalSpending) : '—'}
          subtitle={new Date().toLocaleString('en-IN', { month: 'long', year: 'numeric' })}
          accent="#bb6bd9"
        />
      </section>

      {/* Charts row */}
      <section className="overview__charts">
        <div className="overview__chart-wide">
          {analyticsLoading && <p className="overview__status">Loading analytics…</p>}
          {analyticsError && <p className="overview__status overview__status--error">Could not load monthly data</p>}
          {!analyticsLoading && !analyticsError && <MonthlyChart data={monthly} />}
        </div>
        <div className="overview__chart-narrow">
          {analyticsLoading && <p className="overview__status">Loading analytics…</p>}
          {analyticsError && <p className="overview__status overview__status--error">Could not load category data</p>}
          {!analyticsLoading && !analyticsError && <CategoryChart data={categories} />}
        </div>
      </section>

      {/* Recent transactions — live data, sorted by expenseDate DESC */}
      <section>
        {expLoading && <p className="overview__status">Loading transactions…</p>}
        {expError && <p className="overview__status overview__status--error">Could not load transactions: {expError}</p>}
        {!expLoading && !expError && <TransactionTable expenses={sortedExpenses} limit={5} />}
      </section>
    </div>
  );
}
