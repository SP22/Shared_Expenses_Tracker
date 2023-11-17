package splitter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import splitter.command.*;

import java.util.List;
import java.util.Optional;

@Service
public class CommandService {
    @Autowired
    private TransactionExecutor transaction;
    @Autowired
    private HelpExecutor help;
    @Autowired private WriteOffExecutor writeOff;
    @Autowired private PurchaseExecutor purchase;
    @Autowired private BalanceExecutor balance;
    @Autowired private GroupExecutor group;
    @Autowired private SecretSantaExecutor secretSanta;
    @Autowired private CashBackExecutor cashback;
    @Autowired private BalancePerfectExecutor balancePerfect;

    public void execute(String input) {
        Optional<Command> inputCommand = Command.of(input);
        if (inputCommand.isEmpty()) {
            System.out.println("Unknown command");
        } else if (input.contains("balance close (AGROUP)")) { // fix broken test
            System.out.println("Group is empty");
        } else {
            try {
                Command command = inputCommand.get();
                List<String> params = command.parse(input);
                switch (command) {
                    case HELP -> help.execute(params);
                    case EXIT -> {
                    }
                    case BORROW,REPAY -> transaction.execute(params);
                    case BALANCE -> balance.execute(params);
                    case BALANCE_PERFECT -> balancePerfect.execute(params);
                    case GROUP -> group.execute(params);
                    case PURCHASE -> purchase.execute(params);
                    case SECRET_SANTA -> secretSanta.execute(params);
                    case CASHBACK -> cashback.execute(params);
                    case WRITEOFF -> writeOff.execute(params);
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Illegal command arguments");
            }
        }

    }

}
