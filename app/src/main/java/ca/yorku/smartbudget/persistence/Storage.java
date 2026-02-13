package ca.yorku.smartbudget.persistence;

import ca.yorku.smartbudget.domain.Budget;
import ca.yorku.smartbudget.domain.Transaction;

import java.util.List;

public interface Storage {
    List<Transaction> loadTransactions();
    void saveTransactions(List<Transaction> txs);

    List<Budget> loadBudgets();
    void saveBudgets(List<Budget> budgets);
}
