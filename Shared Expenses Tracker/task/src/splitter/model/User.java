package splitter.model;

public class User implements Comparable<User> {
    private String name;
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

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(User from) {
        return this.name.compareTo(from.name);
    }
}
