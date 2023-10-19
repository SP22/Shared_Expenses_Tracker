package splitter.model;

import splitter.TransactionHistory;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction implements Comparable<Transaction> {
    public LocalDate when;
    public User from;
    public User to;
    public BigDecimal amount;

    public Transaction(LocalDate when, User from, User to, BigDecimal amount) {
        this.when = when;
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    @Override
    public int compareTo(Transaction t) {
        int result = this.from.compareTo(t.from);
        if (result == 0) {
            result = this.to.compareTo(t.to);
        }
        if (result == 0) {
            result = this.amount.compareTo(t.amount);
        }
        return result;
    }
}
