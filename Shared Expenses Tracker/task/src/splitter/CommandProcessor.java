package splitter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

public class CommandProcessor {

    private record Transaction(LocalDate when, String from, String to, int amount) {}
    List<Transaction> history = new ArrayList<>();
    String command = "";
    LocalDate when;
    String personOne;
    String personTwo;
    int amount;

    public CommandProcessor() {

    }
    public void parseInput(String input) {
        String[] words = input.split(" ");
        command = "";
        try {
            when = LocalDate.parse(words[0], DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        } catch (Exception e) {
            command = words[0];
            words = Arrays.copyOfRange(words, 1, words.length);
            when = LocalDate.now();
        }
        if ("".equals(command)) {
            command = words[1];
            words = Arrays.copyOfRange(words, 2, words.length);
        }

        try {
            switch (command) {
                case "help":
                    help();
                    break;
                case "repay":
                    repay(when, words);
                    break;
                case "borrow":
                    borrow(when, words);
                    break;
                case "balance":
                    balance(when, words);
                case "exit":
                    break;
                default:
                    System.out.println("Unknown command");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Illegal command arguments");
        }
    }

    private void balance(LocalDate when, String[] words) {
        boolean open = true;
        try {
            if ("close".equals(words[0])) {
                open = false;
            }
        } catch (IndexOutOfBoundsException e) {
            // do nothing, optional argument
        }
        LocalDate until = open ? when.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()) : when;
        List<Transaction> copy = history.stream()
            .filter(x -> !x.when.isAfter(until))
            .collect(Collectors.toList());
        Map<String, Map<String, Integer>> balance = new HashMap<>();
        for (Transaction t : copy) {
            if (!balance.containsKey(t.from) && !balance.containsKey(t.to)) {
                balance.put(t.from, new HashMap<>());
                balance.get(t.from).put(t.to, t.amount);
            } else if (balance.containsKey(t.from)) {
                    balance.get(t.from).put(t.to, balance.get(t.from).getOrDefault(t.to, 0) + t.amount);
            } else {
                balance.get(t.to).put(t.from, balance.get(t.to).getOrDefault(t.from, 0) - t.amount);
            }
        }
        List<Transaction> output = new ArrayList<>();
        for (String s : balance.keySet()) {
            for (String k : balance.get(s).keySet()) {
                int amount = balance.get(s).get(k);
                if (amount > 0) {
                    output.add(new Transaction(LocalDate.now(), k, s, amount));
                } else if (amount < 0) {
                    output.add(new Transaction(LocalDate.now(), s, k , -amount));
                }
            }
        }
        output = output.stream().sorted(
                new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction o1, Transaction o2) {
                        int result = o1.from.compareTo(o2.from);
                        if (result == 0) {
                            result = o1.to.compareTo(o2.to);
                        }
                        if (result == 0) {
                            result = o2.amount - o1.amount;
                        }
                        return result;
                    }
                }
        ).collect(Collectors.toList());
        if (output.size() == 0) {
            System.out.println("No repayments");
        } else {
            for (Transaction t : output) {
                System.out.println(t.from + " owes " + t.to + " " + t.amount);
            }
        }
    }

    private void borrow(LocalDate when, String[] words) {
        parseArguments(words);
        history.add(new Transaction(when, personTwo, personOne, amount));
    }
    private void repay(LocalDate when, String[] words) {
        parseArguments(words);
        history.add(new Transaction(when, personOne, personTwo, amount));
    }

    private void help() {
        System.out.println("""
                balance
                borrow
                exit
                help
                repay
                """);
    }

    private void parseArguments(String[] words) {
        try {
            personOne = words[0];
            personTwo = words[1];
            amount = Integer.parseInt(words[2]);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException();
        }

    }
}
