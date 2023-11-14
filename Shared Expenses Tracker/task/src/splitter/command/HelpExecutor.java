package splitter.command;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class HelpExecutor implements CommandExecutor {
    @Override
    public void execute(List<String> params) {
        Arrays.stream(Command.values())
                .map(Command::getName)
                .sorted()
                .forEach(System.out::println);
    }
}
