package splitter;

import splitter.command.*;
import splitter.model.Transaction;
import splitter.model.User;

import java.time.LocalDate;
import java.util.*;

public class CommandProcessor {
    Map<Command, CommandExecutor> commandMap = new HashMap<>();

    public CommandProcessor() {
        commandMap.put(Command.HELP, new HelpExecutor());
        commandMap.put(Command.EXIT, new ExitExecutor());
        commandMap.put(Command.BORROW, new TransactionExecutor());
        commandMap.put(Command.GROUP, new GroupExecutor());
        commandMap.put(Command.PURCHASE, new PurchaseExecutor());
        commandMap.put(Command.BALANCE, new BalanceExecutor());
        commandMap.put(Command.REPAY, new TransactionExecutor());
    }
    public void execute(String input) {
        Optional<Command> inputCommand = Command.of(input);
        if (inputCommand.isEmpty()) {
            System.out.println("Unknown command");
        } else {
            try {
                Command command = inputCommand.get();
                List<String> params = command.parse(input);
                commandMap.get(command).execute(params);
            } catch (IllegalArgumentException e) {
                System.out.println("Illegal command arguments");
            }
        }

    }

    private void group(String[] words) {
    }

    private void balance(LocalDate when, String[] words) {

    }

    private void borrow(LocalDate when, String[] words) {
        parseArguments(words);
//        history.add(new Transaction(when, personTwo, personOne, amount));
    }

    private void parseArguments(String[] words) {
        try {
//            personOne = new User(words[0]);
//            personTwo = new User(words[1]);
//            amount = Integer.parseInt(words[2]);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException();
        }

    }
}
