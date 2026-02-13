package ca.yorku.smartbudget.persistence;

import ca.yorku.smartbudget.domain.Budget;
import ca.yorku.smartbudget.domain.Transaction;

import java.nio.file.Path;
import java.util.List;

public class JsonStorage implements Storage {

    private final Path transactionsPath;
    private final Path budgetsPath;

    public JsonStorage(Path transactionsPath, Path budgetsPath) {
        this.transactionsPath = transactionsPath;
        this.budgetsPath = budgetsPath;
    }

    @Override
    public List<Transaction> loadTransactions() {
        throw new UnsupportedOperationException("Implement in Phase 3 (JSON persistence).");
    }

    @Override
    public void saveTransactions(List<Transaction> txs) {
        throw new UnsupportedOperationException("Implement in Phase 3 (JSON persistence).");
    }

    @Override
    public List<Budget> loadBudgets() {
        return List.of();
    }

    @Override
    public void saveBudgets(List<Budget> budgets) {
        // no-op for now
    }
}
