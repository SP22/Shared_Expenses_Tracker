package splitter.command;

import java.util.Arrays;
import java.util.List;

public class HelpExecutor implements CommandExecutor {
    @Override
    public void execute(List<String> params) {
        Arrays.stream(Command.values())
                .map(Command::getName)
                .sorted()
                .forEach(x -> System.out.println(x));
    }
}
