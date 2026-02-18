package ca.yorku.smartbudget.service;

import ca.yorku.smartbudget.domain.Transaction;
import ca.yorku.smartbudget.domain.TransactionFilter;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    /** Loads transactions from persistence into the in-memory cache. */
    public void loadOnStartup();

    /** Returns all transactions as a defensive copy. */
    List<Transaction> getAll();

    /** Adds a new transaction (validated) then persists. */
    Transaction add(Transaction tx);

    /** Updates an existing transaction by id (validated) then persists. */
    Transaction update(Transaction tx);

    /** Deletes an existing transaction by id then persists. */
    void delete(UUID id);

    /** Returns a transaction by id, or throws if not found. */
    Transaction getById(UUID id);

    /** Returns filtered results as a defensive copy. */
    List<Transaction> filter(TransactionFilter criteria);

    /** Persists the current cache to Storage. */
    void persist();
}
