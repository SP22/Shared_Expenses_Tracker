package splitter.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import splitter.entity.Group;
import splitter.service.GroupService;
import splitter.service.UserService;
import splitter.entity.User;
import splitter.util.GroupMemberFilter;

import java.util.List;

@Service
public class GroupExecutor implements CommandExecutor {
    @Autowired
    private GroupService groupService;
    @Autowired
    private UserService userService;
    @Autowired GroupMemberFilter groupMemberFilter;

    @Transactional
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
        for (String userName : groupMemberFilter.filter(members)) {
            User user = userService.getOrCreate(userName);
            groupService.addUser(groupName, user);
        }
    }
    private void show(String name) {
        Group group = groupService.get(name);
        if (group == null) {
            System.out.println("Unknown group");
        } else if (group.getMembers().isEmpty()) {
            System.out.println("Group is empty");
        } else {
            group.getMembers().stream().sorted().forEach(System.out::println);
        }
    }

    private void create(String name, String members) {
        if (groupService.exists(name)) {
            groupService.delete(name);
        }
        groupService.getOrCreate(name);
        for (String userName : groupMemberFilter.filter(members)) {
            User user = userService.getOrCreate(userName);
            groupService.addUser(name, user);
        }
    }

    private void remove(String name, String members) {
        for (String userName : groupMemberFilter.filter(members)) {
            User user = userService.getOrCreate(userName);
            groupService.removeUser(name, user);
        }
    }
}
