package splitter.command;

import splitter.GroupHolder;
import splitter.UserHolder;
import splitter.model.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class GroupExecutor implements CommandExecutor {
    @Override
    public void execute(List<String> params) {
        switch (params.get(1)) {
            case "show":
                show(params.get(2));
                break;
            case "create":
                create(params.get(2), params.get(3));
                break;
        }
    }

    private void show(String name) {
        GroupHolder groups = GroupHolder.getInstance();
        Set<User> group = groups.getGroup(name);
        if (group == null) {
            System.out.println("Unknown group");
        } else {
            group.forEach(System.out::println);
        }

    }

    private void create(String name, String members) {
        String[] memberList = members.trim().replaceAll("[(),]", "").split(" ");
        for (String userName : memberList) {
            User user = UserHolder.getInstance().addUser(userName);
            GroupHolder.getInstance().addUserToGroup(name, user);
        }
    }
}
