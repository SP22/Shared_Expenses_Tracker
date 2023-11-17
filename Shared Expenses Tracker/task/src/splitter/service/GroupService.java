package splitter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import splitter.entity.Group;
import splitter.entity.User;
import splitter.repository.GroupRepository;
import splitter.repository.UserRepository;

import java.util.HashMap;
import java.util.Set;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final HashMap<String, Set<User>> groups = new HashMap<>();
    @Autowired
    public GroupService(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    public Group get(String name) {
        if (!groupRepository.existsByName(name)) {
            throw new IllegalArgumentException("Unknown group");
        }
        return groupRepository.getByName(name);
    }

    @Transactional
    public void addUser(String name, User user) {
        Group group = getOrCreate(name);
        if (!group.getMembers().contains(user)) {
            group.addUser(user);
            groupRepository.save(group);
        }
    }

    public void delete(String name) {
        groupRepository.delete(groupRepository.getByName(name));
        groupRepository.flush();
    }

    public boolean exists(String name) {
        return groupRepository.existsByName(name);
    }

    public Group getOrCreate(String name) {
        return groupRepository.findByName(name).orElseGet(() -> groupRepository.save(new Group(name)));
    }

    public void removeUser(String name, User user) {
        groupRepository.getByName(name).removeUser(user);
    }
}
