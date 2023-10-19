package splitter.command;

import splitter.GroupHolder;
import splitter.TransactionHistory;
import splitter.UserHolder;
import splitter.model.Transaction;
import splitter.model.User;
import splitter.util.DateUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class PurchaseExecutor implements CommandExecutor {

    private final TransactionHistory history = TransactionHistory.getInstance();
    private final GroupHolder groups = GroupHolder.getInstance();
    private final UserHolder users = UserHolder.getInstance();
    private final Set<String> temporary = new TreeSet<>();

    BigDecimal minimumAmount = new BigDecimal("0.01");
    boolean haveRemainder = false;

    @Override
    public void execute(List<String> params) {
        try {
            LocalDate date = DateUtil.parseDate(params.get(0));

            User payer = users.addUser(params.get(2));
            BigDecimal totalPrice = new BigDecimal(params.get(4));

            Set<User> borrowers = groups.getGroup(params.get(5));

            haveRemainder = false;
            processPurchase(date, payer, borrowers, totalPrice);

        } catch (Exception e) {
            System.out.println("Illegal command arguments");
        }
    }

    private void processPurchase(LocalDate date, User payer, Set<User> borrowers, BigDecimal totalPrice) {
        BigDecimal quantity = new BigDecimal(borrowers.size());
        BigDecimal sharedAmount = totalPrice.divide(quantity, RoundingMode.FLOOR);
        haveRemainder = (0 != totalPrice.compareTo(sharedAmount.multiply(quantity)));
        BigDecimal remainder = haveRemainder ? totalPrice.subtract(sharedAmount.multiply(quantity)) : BigDecimal.ZERO;
        int extraPayers = haveRemainder ? remainder.divide(minimumAmount, RoundingMode.DOWN).intValue() : 0;
        int count = 0;
        for (User b : borrowers) {
            BigDecimal amount = sharedAmount;
            if (haveRemainder && count++ < extraPayers) {
                amount = amount.add(minimumAmount);
            }
            if (!payer.equals(b)) {
                history.addTransaction(new Transaction(date, payer, b, amount));
            }
        }
    }
}
