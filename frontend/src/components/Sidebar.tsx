import './Sidebar.css';

export type Page = 'overview' | 'transactions' | 'analytics';

interface Props {
  activePage: Page;
  onNavigate: (page: Page) => void;
  isOpen: boolean;
  onClose: () => void;
}

const NAV_ITEMS: { id: Page; label: string; icon: string }[] = [
  { id: 'overview',     label: 'Overview',      icon: '◻' },
  { id: 'transactions', label: 'Transactions',   icon: '↕' },
  { id: 'analytics',   label: 'Analytics',      icon: '∿' },
];

export default function Sidebar({ activePage, onNavigate, isOpen, onClose }: Props) {
  function handleNav(page: Page) {
    onNavigate(page);
    onClose(); // close mobile drawer on selection
  }

  return (
    <>
      {/* Mobile overlay */}
      {isOpen && <div className="sidebar-overlay" onClick={onClose} />}

      <aside className={`sidebar${isOpen ? ' sidebar--open' : ''}`}>
        {/* Brand */}
        <div className="sidebar__brand">
          <span className="sidebar__brand-icon">💰</span>
          <span className="sidebar__brand-name">FinTrack</span>
        </div>

        {/* Navigation */}
        <nav className="sidebar__nav">
          <span className="sidebar__section-label">MENU</span>
          {NAV_ITEMS.map(({ id, label, icon }) => (
            <button
              key={id}
              className={`sidebar__item${activePage === id ? ' sidebar__item--active' : ''}`}
              onClick={() => handleNav(id)}
            >
              <span className="sidebar__item-icon">{icon}</span>
              <span>{label}</span>
            </button>
          ))}
        </nav>

        {/* Footer */}
        <div className="sidebar__footer">
          <span className="sidebar__footer-text">OOPJ Project · 2026</span>
        </div>
      </aside>
    </>
  );
}
