package ca.yorku.smartbudget.persistence;

import ca.yorku.smartbudget.domain.Budget;
import ca.yorku.smartbudget.domain.Transaction;

import java.util.ArrayList;
import java.util.List;

public class InMemoryStorage implements Storage {
    private List<Transaction> transactions = new ArrayList<>();
    private List<Budget> budgets = new ArrayList<>();

    public InMemoryStorage() {}

    public InMemoryStorage(List<Transaction> seedTransactions) {
        this.transactions = new ArrayList<>(seedTransactions);
    }

    @Override
    public List<Transaction> loadTransactions() {
        return new ArrayList<>(transactions); // defensive copy
    }

    @Override
    public void saveTransactions(List<Transaction> txs) {
        this.transactions = new ArrayList<>(txs); // overwrite with copy
    }

    @Override
    public List<Budget> loadBudgets() {
        return new ArrayList<>(budgets);
    }

    @Override
    public void saveBudgets(List<Budget> budgets) {
        this.budgets = new ArrayList<>(budgets);
    }

    // Optional helper for assertions in tests
    public List<Transaction> peekSavedTransactions() {
        return new ArrayList<>(transactions);
    }
}
