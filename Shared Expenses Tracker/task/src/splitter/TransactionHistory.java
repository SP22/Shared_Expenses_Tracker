package splitter;

import splitter.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistory {
    private static final TransactionHistory instance = new TransactionHistory();
    private final List<Transaction> history = new ArrayList<>();

    private TransactionHistory() {}

    public static TransactionHistory getInstance() {
        return instance;
    }

    public void addTransaction(Transaction transaction) {
        history.add(transaction);
    }

    public List<Transaction> getHistory() {
        return history;
    }
}
