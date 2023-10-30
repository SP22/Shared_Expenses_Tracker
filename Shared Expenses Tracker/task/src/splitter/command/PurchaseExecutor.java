package splitter.command;

import splitter.TransactionHistory;
import splitter.UserHolder;
import splitter.model.Transaction;
import splitter.model.User;
import splitter.util.DateUtil;
import splitter.util.GroupMemberFilter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PurchaseExecutor implements CommandExecutor {

    private final TransactionHistory history = TransactionHistory.getInstance();
    private final UserHolder users = UserHolder.getInstance();

    BigDecimal minimumAmount = new BigDecimal("0.01");

    @Override
    public void execute(List<String> params) {
        try {
            LocalDate date = DateUtil.parseDate(params.get(0));

            User payer = users.addUser(params.get(2));
            BigDecimal totalPrice = new BigDecimal(params.get(4));
            Set<User> borrowers = new TreeSet<>();
            UserHolder userHolder = UserHolder.getInstance();
            for (String s : GroupMemberFilter
                    .filter(params.get(5))) {
                User user = userHolder.addUser(s);
                borrowers.add(user);
            }

            if (borrowers.isEmpty()) {
                System.out.println("Group is empty");
                return;
            }

            processPurchase(date, payer, borrowers, totalPrice);

        } catch (Exception e) {
            System.out.println("Illegal command arguments");
        }
    }

    private void processPurchase(LocalDate date, User payer, Set<User> borrowers, BigDecimal totalPrice) {
        boolean haveRemainder;
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
