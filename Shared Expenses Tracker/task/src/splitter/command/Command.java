package splitter.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Command {
    HELP("help", "(help)"),
    EXIT("exit", "(exit)"),
    REPAY("repay", "([0-9]{4}\\.[0-9]{2}\\.[0-9]{2})?\\s*(repay)\\s+([a-zA-Z]+)\\s+([a-zA-Z]+)\\s+([0-9.]+)"),
    BORROW("borrow", "([0-9]{4}\\.[0-9]{2}\\.[0-9]{2})?\\s*(borrow)\\s+([a-zA-Z]+)\\s+([a-zA-Z]+)\\s+([0-9.]+)"),
    BALANCE("balance", "([0-9]{4}\\.[0-9]{2}\\.[0-9]{2})?\\s*(balance)\\s*(open|close)?\\s*(\\(([^()]+)\\))?"),
    GROUP("group","(group)\\s+(create|add|remove|show)\\s*([A-Z]+)(.*)?"),
    PURCHASE("purchase", "([0-9]{4}\\.[0-9]{2}\\.[0-9]{2})?\\s*(purchase)\\s+([a-zA-Z]+)\\s+([a-zA-Z]+)\\s+([0-9.]+)\\s+\\(([^()]+)\\)");

    private final String name;
    private final String commandPattern;

    Command(String name, String commandPattern) {
        this.name = name;
        this.commandPattern = commandPattern;
    }

    public String getName() {
        return name;
    }

    public String getCommandPattern() {
        return commandPattern;
    }

    public static Optional<Command> of(String input) {
        Command command = null;
        for (Command cmd : values()) {
            if (input.contains(cmd.name)) {
                command = cmd;
            }
        }
        return Optional.ofNullable(command);
    }

    public List<String> parse(String input) {
            Matcher matcher = Pattern.compile(getCommandPattern()).matcher(input);
            List list = new ArrayList();
        if (matcher.matches()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                list.add(matcher.group(i));
            }
            return list;
        }
        throw new IllegalArgumentException("Illegal command arguments");
    }
}
