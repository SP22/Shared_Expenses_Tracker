package splitter;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CommandProcessor processor = new CommandProcessor();
        String input = "";
        while (!"exit".equals(input)) {
            input = scanner.nextLine();
            processor.execute(input);
        }
    }
}
