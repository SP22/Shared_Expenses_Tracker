package splitter.command;

import java.util.List;

@FunctionalInterface
public interface CommandExecutor {
    void execute(List<String> params);
}
