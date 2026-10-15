import { CategoryTotal, CATEGORY_COLORS } from '../data/mockData';
import './CategoryChart.css';

interface Props {
  data: CategoryTotal[];
}

export default function CategoryChart({ data }: Props) {
  const grandTotal = data.reduce((sum, d) => sum + d.total, 0) || 1;

  return (
    <div className="chart-card">
      <h3 className="chart-card__title">Spending by Category</h3>
      <div className="category-chart">
        {/* Stacked horizontal bar */}
        <div className="category-chart__bar">
          {data.map(({ category, total }) => (
            <div
              key={category}
              className="category-chart__segment"
              style={{
                width: `${(total / grandTotal) * 100}%`,
                background: CATEGORY_COLORS[category] ?? CATEGORY_COLORS['Other'],
              }}
              title={`${category}: ₹${total.toLocaleString('en-IN')}`}
            />
          ))}
        </div>

        {/* Legend rows */}
        <div className="category-chart__legend">
          {data.map(({ category, total }) => {
            const pct = ((total / grandTotal) * 100).toFixed(1);
            const color = CATEGORY_COLORS[category] ?? CATEGORY_COLORS['Other'];
            return (
              <div key={category} className="category-chart__row">
                <span className="category-chart__dot" style={{ background: color }} />
                <span className="category-chart__name">{category}</span>
                <div className="category-chart__track">
                  <div
                    className="category-chart__fill"
                    style={{ width: `${pct}%`, background: color }}
                  />
                </div>
                <span className="category-chart__pct">{pct}%</span>
                <span className="category-chart__amount">
                  ₹{total.toLocaleString('en-IN')}
                </span>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
}
