package splitter.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import splitter.service.TransactionService;
import splitter.service.UserService;
import splitter.entity.Transaction;
import splitter.entity.User;
import splitter.util.DateUtil;
import splitter.util.GroupMemberFilter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
public class PurchaseExecutor implements CommandExecutor {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;
    @Autowired GroupMemberFilter groupMemberFilter;

    BigDecimal minimumAmount = new BigDecimal("0.01");

    @Transactional
    @Override
    public void execute(List<String> params) {
        try {
            LocalDate date = DateUtil.parseDate(params.get(0));

            User payer = userService.getOrCreate(params.get(2));
            BigDecimal totalPrice = new BigDecimal(params.get(4)).setScale(2, RoundingMode.HALF_UP);
            Set<User> borrowers = new TreeSet<>();
            for (String s : groupMemberFilter
                    .filter(params.get(5))) {
                User user = userService.getOrCreate(s);
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
        boolean positiveAmount = true;

        if (totalPrice.compareTo(BigDecimal.ZERO) < 0) {
            totalPrice = totalPrice.negate();
            positiveAmount = false;
        }

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
                if (positiveAmount) {
                    transactionService.addTransaction(new Transaction(date, payer, b, amount));
                } else {
                    transactionService.addTransaction(new Transaction(date, b, payer, amount));
                }
            }
        }
    }
}
