package ca.yorku.smartbudget.service;

import ca.yorku.smartbudget.domain.Transaction;
import ca.yorku.smartbudget.domain.TransactionFilter;
import ca.yorku.smartbudget.persistence.Storage;
import ca.yorku.smartbudget.util.Validator;

import java.time.LocalDate;
import java.util.*;

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
    public Transaction update(Transaction updatedTx) {
        Objects.requireNonNull(updatedTx, "tx must not be null");
        if (updatedTx.getId() == null) {
            throw new IllegalArgumentException("Transaction ID is required for update.");
        }

        // Validate the transaction (throws if invalid)
        Validator.validateTransaction(updatedTx);

        int idx = indexOfId(updatedTx.getId());
        if (idx == -1) {
            throw new NoSuchElementException("Transaction with ID " + updatedTx.getId() + " not found.");
        }

        cache.set(idx, updatedTx);
        persist();
        return updatedTx;
    }

    @Override
    public void delete(UUID id) {
        Objects.requireNonNull(id, "id must not be null");

        int idx = indexOfId(id);
        if (idx == -1) {
            throw new NoSuchElementException("Transaction with ID " + id + " not found.");
        }

        cache.remove(idx);
        persist();
    }

    @Override
    public Transaction getById(UUID id) {
        Objects.requireNonNull(id, "id must not be null");

        int idx = indexOfId(id);
        if (idx == -1) {
            throw new NoSuchElementException("Transaction with ID " + id + " not found.");
        }

        return cache.get(idx);
    }

    @Override
    public List<Transaction> filter(TransactionFilter criteria) {
        if (criteria == null || criteria.isEmpty()) {
            return getAll(); // no criteria means return all
        }

        final String keyword = normalize(criteria.getKeyword());
        final LocalDate startDate = criteria.getStartDate();
        final LocalDate endDate = criteria.getEndDate();

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be on or before end date.");
        }

        List<Transaction> result = new ArrayList<>();
        for (Transaction tx : cache) {
            if (matchesKeyword(tx, keyword)
                    && matchesCategory(tx, criteria)
                    && matchesType(tx, criteria)
                    && matchesDateRange(tx, startDate, endDate)) {
                result.add(tx);
            }
        }
        return result;
    }

    @Override
    public void persist() {
        storage.saveTransactions(new ArrayList<>(cache)); // defensive copy
    }

    // Helper methods
    private int indexOfId(UUID id) {
        for (int i = 0; i < cache.size(); i++) {
            if (cache.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    private static String normalize(String s) {
        return s == null ? "" : s.trim().toLowerCase();
    }

    private static boolean matchesKeyword(Transaction tx, String keyword) {
        if (keyword.isEmpty()) {
            return true;
        }
        return tx.getNote() != null && tx.getNote().toLowerCase().contains(keyword);
    }

    private static boolean matchesCategory(Transaction tx, TransactionFilter criteria) {
        return criteria.getCategory() == null || criteria.getCategory().equals(tx.getCategory());
    }

    private static boolean matchesType(Transaction tx, TransactionFilter criteria) {
        return criteria.getType() == null || criteria.getType().equals(tx.getType());
    }

    private static boolean matchesDateRange(Transaction tx, LocalDate startDate, LocalDate endDate) {
        LocalDate date = tx.getDate();
        if (date == null) {
            return false; // should not happen if validation is correct
        }
        if (startDate != null && date.isBefore(startDate)) {
            return false;
        }
        if (endDate != null && date.isAfter(endDate)) {
            return false;
        }
        return true;
    }
}