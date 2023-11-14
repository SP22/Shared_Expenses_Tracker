package splitter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import splitter.entity.Gift;
import splitter.entity.Group;
import splitter.entity.User;
import splitter.repository.GiftRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GiftService {
    private final GroupService groupService;
    private final GiftRepository giftRepository;

    @Autowired
    public GiftService(GroupService groupService, GiftRepository giftRepository) {
        this.groupService = groupService;
        this.giftRepository = giftRepository;
    }

    public Gift create(User giver, User receiver) {
        Gift gift = new Gift(giver, receiver);
        return giftRepository.save(gift);
    }

    @Transactional
    public List<Gift> createRandomPairs(Group group) {
        List<User> givers = new ArrayList<>(groupService.get(group.getName()).getMembers());
        Collections.shuffle(givers);

        List<User> receivers = new ArrayList<>(givers);
        Collections.rotate(receivers, 2);

        List<Gift> gifts = new ArrayList<>();
        int size = givers.size();

        for (int i = 0; i < size; i++) {
            User giver = givers.get(i);
            User receiver = receivers.get(i);
            Gift gift = create(giver, receiver);
            gifts.add(gift);
        }
        return gifts;
    }
}
