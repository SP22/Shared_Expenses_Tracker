package splitter.command;

import splitter.GroupHolder;
import splitter.UserHolder;
import splitter.model.User;
import splitter.util.GroupMemberFilter;

import java.util.List;
import java.util.Set;

public class GroupExecutor implements CommandExecutor {
    private final GroupHolder groups = GroupHolder.getInstance();
    private final UserHolder users = UserHolder.getInstance();

    @Override
    public void execute(List<String> params) {
        String command = params.get(1);
        String groupName = params.get(2);
        String members = params.get(3);
        switch (command) {
            case "show":
                show(groupName);
                break;
            case "create":
                create(groupName, members);
                break;
            case "add":
                add(groupName, members);
                break;
            case "remove":
                remove(groupName, members);
                break;
        }
    }

    private void add(String groupName, String members) {
        for (String userName : GroupMemberFilter.filter(members)) {
            User user = users.addUser(userName);
            groups.addUserToGroup(groupName, user);
        }
    }

    private void show(String name) {
        Set<User> group = groups.getGroup(name);
        if (group == null) {
            System.out.println("Unknown group");
        } else if (group.isEmpty()) {
            System.out.println("Group is empty");
        } else {
            group.forEach(System.out::println);
        }

    }

    private void create(String name, String members) {
        groups.create(name);
        for (String userName : GroupMemberFilter.filter(members)) {
            User user = users.addUser(userName);
            groups.addUserToGroup(name, user);
        }
    }

    private void remove(String name, String members) {
        for (String userName : GroupMemberFilter.filter(members)) {
            User user = users.addUser(userName);
            groups.removeUserFromGroup(name, user);
        }
    }
}
