package splitter.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import splitter.entity.User;
import splitter.service.TransactionService;
import splitter.service.UserService;
import splitter.entity.Transaction;
import splitter.util.DateUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionExecutor implements CommandExecutor {
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionService transactionService;

    @Override
    public void execute(List<String> params) {
        Transaction transaction;
        User user = userService.getOrCreate(params.get(2));
        User user2 = userService.getOrCreate(params.get(3));
        LocalDate when = DateUtil.parseDate(params.get(0));
        BigDecimal amount = new BigDecimal(params.get(4));
        if ("borrow".equals(params.get(1))) {
            transaction = new Transaction(when, user2, user, amount);
        } else { //repay
            transaction = new Transaction(when, user, user2, amount);
        }
        transactionService.addTransaction(transaction);
    }
}
