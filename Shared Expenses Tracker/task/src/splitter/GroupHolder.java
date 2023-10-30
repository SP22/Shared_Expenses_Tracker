package splitter;

import splitter.model.User;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class GroupHolder {
    private static final GroupHolder instance = new GroupHolder();
    private final HashMap<String, Set<User>> groups = new HashMap<>();
    private GroupHolder() {}
    public static GroupHolder getInstance() {
        return instance;
    }

    public Set<User> getGroup(String name) {
        return groups.get(name);
    }

    public void addUserToGroup(String name, User user) {
        if (!groups.containsKey(name)) {
            create(name);
        }
        groups.get(name).add(user);
    }

    public void create(String name) {
        groups.put(name, new TreeSet<>());
    }

    public void removeUserFromGroup(String name, User user) {
        groups.get(name).remove(user);
    }
}
