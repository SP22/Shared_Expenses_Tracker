package splitter.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import splitter.entity.Gift;
import splitter.entity.Group;
import splitter.service.GiftService;
import splitter.service.GroupService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SecretSantaExecutor implements CommandExecutor {

    @Autowired private GroupService groupService;
    @Autowired private GiftService giftService;
    @Override
    public void execute(List<String> params) {
        Group group = groupService.getOrCreate(params.get(1));

        List<Gift> gifts = giftService.createRandomPairs(group);
        Collections.sort(gifts);
        System.out.println(
                gifts.stream()
                        .map(Gift::toString)
                        .collect(Collectors.joining(System.lineSeparator())));
    }
}
