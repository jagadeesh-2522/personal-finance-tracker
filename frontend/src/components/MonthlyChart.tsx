import { MonthlyTotal, MONTH_LABELS } from '../data/mockData';
import './MonthlyChart.css';

interface Props {
  data: MonthlyTotal[];
}

export default function MonthlyChart({ data }: Props) {
  const maxTotal = Math.max(...data.map(d => d.total), 1);

  return (
    <div className="chart-card">
      <h3 className="chart-card__title">Monthly Spending — {new Date().getFullYear()}</h3>
      <div className="monthly-chart">
        {data.map(({ month, total }) => {
          const heightPct = (total / maxTotal) * 100;
          const isCurrentMonth = month === new Date().getMonth() + 1;
          return (
            <div key={month} className="monthly-chart__column">
              <span className="monthly-chart__amount">
                {total > 0 ? `₹${total.toLocaleString('en-IN')}` : ''}
              </span>
              <div className="monthly-chart__bar-wrap">
                <div
                  className={`monthly-chart__bar${isCurrentMonth ? ' monthly-chart__bar--active' : ''}`}
                  style={{ height: `${Math.max(heightPct, total > 0 ? 4 : 0)}%` }}
                />
              </div>
              <span className="monthly-chart__label">{MONTH_LABELS[month - 1]}</span>
            </div>
          );
        })}
      </div>
    </div>
  );
}
