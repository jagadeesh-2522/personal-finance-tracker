import './SummaryCard.css';

interface Props {
  title: string;
  value: string;
  subtitle?: string;
  accent?: string; // border-top color
}

export default function SummaryCard({ title, value, subtitle, accent = '#4f8ef7' }: Props) {
  return (
    <div className="summary-card" style={{ borderTopColor: accent }}>
      <span className="summary-card__title">{title}</span>
      <span className="summary-card__value">{value}</span>
      {subtitle && <span className="summary-card__subtitle">{subtitle}</span>}
    </div>
  );
}
