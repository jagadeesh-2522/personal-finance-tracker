import { useState, useEffect, useCallback } from 'react';
import { Expense } from '../data/mockData';

const API_BASE = 'http://localhost:8080';

interface UseExpensesResult {
  expenses: Expense[];
  loading: boolean;
  error: string | null;
  refresh: () => void;   // call this after a successful create/update/delete
}

export function useExpenses(): UseExpensesResult {
  const [expenses, setExpenses] = useState<Expense[]>([]);
  const [loading, setLoading]   = useState(true);
  const [error, setError]       = useState<string | null>(null);
  // Incrementing this counter re-runs the useEffect fetch
  const [refreshKey, setRefreshKey] = useState(0);

  const refresh = useCallback(() => setRefreshKey(k => k + 1), []);

  useEffect(() => {
    let cancelled = false;

    setLoading(true);
    setError(null);

    fetch(`${API_BASE}/api/expenses`)
      .then(res => {
        if (!res.ok) throw new Error(`Server returned ${res.status}`);
        return res.json() as Promise<Expense[]>;
      })
      .then(data => {
        if (!cancelled) { setExpenses(data); setLoading(false); }
      })
      .catch(err => {
        if (!cancelled) {
          setError(err instanceof Error ? err.message : 'Failed to fetch expenses');
          setLoading(false);
        }
      });

    return () => { cancelled = true; };
  }, [refreshKey]); // re-fetches whenever refresh() is called

  return { expenses, loading, error, refresh };
}
