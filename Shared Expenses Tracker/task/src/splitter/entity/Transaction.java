package splitter.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class Transaction implements Comparable<Transaction> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public LocalDate when;

    @ManyToOne
    @JoinColumn(name = "from_id")
    public User from;

    @ManyToOne
    @JoinColumn(name = "to_id")
    public User to;
    public BigDecimal amount;

    public Transaction() {}

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
