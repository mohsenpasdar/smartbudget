package ca.yorku.smartbudget.service;

import ca.yorku.smartbudget.domain.Category;
import ca.yorku.smartbudget.domain.Transaction;
import ca.yorku.smartbudget.domain.TransactionType;
import ca.yorku.smartbudget.persistence.InMemoryStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class TransactionServiceTest {
    private InMemoryStorage storage;
    private TransactionService service;

    // A few sample transactions for testing
    private Transaction tx1;
    private Transaction tx2;
    private Transaction tx3;
    private Transaction tx4;
    private Transaction tx5;


    @BeforeEach
    void setUp() {
        tx1 = new Transaction(TransactionType.EXPENSE,
                new BigDecimal("12.50"),
                LocalDate.of(2026, 2, 1),
                Category.FOOD_AND_DINING,
                "pizza dinner");

        tx2 = new Transaction(TransactionType.INCOME,
                new BigDecimal("1500.00"),
                LocalDate.of(2026, 2, 3),
                Category.OTHER,
                "co-op paycheck");

        tx3 = new Transaction(
                UUID.randomUUID(),
                TransactionType.EXPENSE,
                new BigDecimal("60.00"),
                LocalDate.of(2026, 2, 5),
                Category.BILLS_AND_UTILITIES,
                "internet bill"
        );

        tx4 = new Transaction(
                UUID.randomUUID(),
                TransactionType.EXPENSE,
                new BigDecimal("3.35"),
                LocalDate.of(2026, 2, 10),
                Category.TRANSPORTATION,
                "bus fare"
        );

        tx5 = new Transaction(
                UUID.randomUUID(),
                TransactionType.INCOME,
                new BigDecimal("200.00"),
                LocalDate.of(2026, 1, 28),
                Category.OTHER,
                "tax refund"
        );

        storage = new InMemoryStorage(List.of(tx1, tx2, tx3, tx4, tx5));

        service = new TransactionServiceImpl(storage);
        service.loadOnStartup();
    }

    // Load / Startup tests

    @Test
    void testLoadOnStartup() {
        List<Transaction> all = service.getAll();
        assert(all.size() == 5);
        assert(all.contains(tx1));
        assert(all.contains(tx2));
        assert(all.contains(tx3));
        assert(all.contains(tx4));
        assert(all.contains(tx5));
    }

    // Add tests

    @Test
    void add_validTransaction() {
        Transaction newTx = new Transaction(
                TransactionType.EXPENSE,
                new BigDecimal("25.00"),
                LocalDate.of(2026, 2, 15),
                Category.ENTERTAINMENT,
                "movie tickets"
        );

        Transaction added = service.add(newTx);
        assert(added.equals(newTx));

        List<Transaction> all = service.getAll();
        assert(all.size() == 6);
        assert(all.contains(newTx));

        // Persistence updated (saveTransactions should have been called)
        List<Transaction> saved = storage.peekSavedTransactions();
        assert(saved.size() == 6);
        assert(saved.contains(newTx));
    }
}
