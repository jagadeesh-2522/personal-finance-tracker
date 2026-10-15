import { useState, useEffect, useCallback } from 'react';
import { MonthlyTotal, CategoryTotal } from '../data/mockData';

const API_BASE = 'http://localhost:8080';

interface Summary {
  totalSpending: number;
  totalExpenses: number;
  averageExpense: number;
}

interface UseAnalyticsResult {
  summary: Summary | null;
  monthly: MonthlyTotal[];
  categories: CategoryTotal[];
  loading: boolean;
  error: string | null;
  refresh: () => void;
}

export function useAnalytics(): UseAnalyticsResult {
  const [summary, setSummary] = useState<Summary | null>(null);
  const [monthly, setMonthly] = useState<MonthlyTotal[]>([]);
  const [categories, setCategories] = useState<CategoryTotal[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [refreshKey, setRefreshKey] = useState(0);

  const refresh = useCallback(() => setRefreshKey(k => k + 1), []);

  useEffect(() => {
    let cancelled = false;

    setLoading(true);
    setError(null);

    // Fetch all three analytics endpoints in parallel
    Promise.all([
      fetch(`${API_BASE}/api/expenses/analytics/summary`).then(res => {
        if (!res.ok) throw new Error(`Summary failed with ${res.status}`);
        return res.json() as Promise<Summary>;
      }),
      fetch(`${API_BASE}/api/expenses/analytics/monthly`).then(res => {
        if (!res.ok) throw new Error(`Monthly failed with ${res.status}`);
        return res.json() as Promise<MonthlyTotal[]>;
      }),
      fetch(`${API_BASE}/api/expenses/analytics/categories`).then(res => {
        if (!res.ok) throw new Error(`Categories failed with ${res.status}`);
        return res.json() as Promise<CategoryTotal[]>;
      }),
    ])
      .then(([summaryData, monthlyData, categoriesData]) => {
        if (!cancelled) {
          setSummary(summaryData);
          setMonthly(monthlyData || []);
          setCategories(categoriesData || []);
          setLoading(false);
        }
      })
      .catch(err => {
        if (!cancelled) {
          setError(err instanceof Error ? err.message : 'Failed to fetch analytics');
          setLoading(false);
        }
      });

    return () => {
      cancelled = true;
    };
  }, [refreshKey]);

  return { summary, monthly, categories, loading, error, refresh };
}
