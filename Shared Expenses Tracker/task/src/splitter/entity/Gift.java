package splitter.entity;

import javax.persistence.*;

@Entity
@Table(name = "gifts")
public class Gift implements Comparable<Gift> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gift_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "giver_id")
    private User giver;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    public Gift() {
    }

    public Gift(User giver, User receiver) {
        this.giver = giver;
        this.receiver = receiver;
    }

    public User getGiver() {
        return giver;
    }

    public void setGiver(User giver) {
        this.giver = giver;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    @Override
    public int compareTo(Gift gift) {
        int result = this.giver.compareTo(gift.giver);
        if (result == 0) {
            result = this.receiver.compareTo(gift.receiver);
        }
        return result;
    }

    @Override
    public String toString() {
        return giver + " gift to " + receiver;
    }
}
