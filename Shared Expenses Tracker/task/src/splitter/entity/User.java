package splitter.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements Comparable<User> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "members")
    private Set<Group> groups;

    public User() {};

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(User from) {
        return this.name.compareTo(from.name);
    }
}
