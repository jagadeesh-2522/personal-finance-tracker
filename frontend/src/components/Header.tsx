import { Page } from './Sidebar';
import './Header.css';

const PAGE_TITLES: Record<Page, string> = {
  overview:     'Overview',
  transactions: 'Transactions',
  analytics:    'Analytics',
};

interface Props {
  activePage: Page;
  onAddExpense: () => void;
  onMenuToggle: () => void;
}

export default function Header({ activePage, onAddExpense, onMenuToggle }: Props) {
  return (
    <header className="header">
      <div className="header__left">
        {/* Hamburger — visible on mobile only */}
        <button className="header__menu-btn" onClick={onMenuToggle} aria-label="Open menu">
          ☰
        </button>
        <div>
          <h1 className="header__title">Personal Finance</h1>
          <p className="header__subtitle">{PAGE_TITLES[activePage]}</p>
        </div>
      </div>
      <button className="header__add-btn" onClick={onAddExpense}>
        + Add Expense
      </button>
    </header>
  );
}
