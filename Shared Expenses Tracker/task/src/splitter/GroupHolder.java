package splitter;

import splitter.model.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class GroupHolder {
    private static final GroupHolder instance = new GroupHolder();
    private HashMap<String, Set<User>> groups = new HashMap<>();
    private GroupHolder() {};
    public static GroupHolder getInstance() {
        return instance;
    }

    public Set<User> getGroup(String name) {
        return groups.get(name);
    }

    public boolean addUserToGroup(String name, User user) {
        if (!groups.containsKey(name)) {
            groups.put(name, new TreeSet<>());
        }
        return groups.get(name).add(user);
    }

    public boolean removeUserFromGroup(String name, User user) {
        return groups.get(name).remove(user);
    }
}
