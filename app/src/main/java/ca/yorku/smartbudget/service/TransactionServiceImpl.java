package ca.yorku.smartbudget.service;

import ca.yorku.smartbudget.domain.Transaction;
import ca.yorku.smartbudget.domain.TransactionFilter;
import ca.yorku.smartbudget.persistence.Storage;
import ca.yorku.smartbudget.util.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TransactionServiceImpl implements TransactionService {
    private Storage storage;
    private List<Transaction> cache;

    public TransactionServiceImpl(Storage storage) {
        this.storage = Objects.requireNonNull(storage, "storage must not be null");
        this.cache = new ArrayList<>();
    }

    @Override
    public void loadOnStartup() {
        List<Transaction> loaded = storage.loadTransactions();
        cache.clear();
        if (loaded != null) {
            cache.addAll(loaded);
        }
    }

    @Override
    public List<Transaction> getAll() {
        return new ArrayList<>(cache); // defensive copy
    }

    @Override
    public Transaction add(Transaction tx) {
        Objects.requireNonNull(tx, "tx must not be null");

        // Validate the transaction (throws if invalid)
        Validator.validateTransaction(tx);

        // Add to the cache then persist
        cache.add(tx);
        persist();

        return tx;
    }

    @Override
    public Transaction update(Transaction tx) {
        throw new UnsupportedOperationException("TODO Step 2.3");
    }

    @Override
    public void delete(UUID id) {
        throw new UnsupportedOperationException("TODO Step 2.3");
    }

    @Override
    public Transaction getById(UUID id) {
        throw new UnsupportedOperationException("TODO Step 2.3");
    }

    @Override
    public List<Transaction> filter(TransactionFilter criteria) {
        throw new UnsupportedOperationException("TODO Step 2.3");
    }

    @Override
    public void persist() {
        throw new UnsupportedOperationException("TODO Step 2.3");
    }
}
