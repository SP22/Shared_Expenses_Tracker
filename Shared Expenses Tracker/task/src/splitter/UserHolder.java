package splitter;

import splitter.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserHolder {
    private static final UserHolder instance = new UserHolder();
    private final Map<String, User> users = new HashMap<>();
    private UserHolder() {};
    public static UserHolder getInstance() {
        return instance;
    }

    public User addUser(String name) {
        if (!users.containsKey(name)) {
            users.put(name, new User(name));
        }
        return users.get(name);
    }

    public User getUser(String name) {
        return users.get(name);
    }
}
