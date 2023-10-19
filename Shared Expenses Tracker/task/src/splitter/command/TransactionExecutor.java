package splitter.command;

import splitter.TransactionHistory;
import splitter.UserHolder;
import splitter.model.Transaction;
import splitter.model.User;
import splitter.util.DateUtil;

import java.math.BigDecimal;
import java.util.List;

public class TransactionExecutor implements CommandExecutor {
    @Override
    public void execute(List<String> params) {
        Transaction transaction;
        UserHolder users = UserHolder.getInstance();
        if ("borrow".equals(params.get(1))) {
            transaction = new Transaction(DateUtil.parseDate(params.get(0)), users.addUser(params.get(3)), users.addUser(params.get(2)), new BigDecimal(params.get(4)));
        } else { //repay
            transaction = new Transaction(DateUtil.parseDate(params.get(0)), users.addUser(params.get(2)), users.addUser(params.get(3)), new BigDecimal(params.get(4)));
        }
        TransactionHistory.getInstance().addTransaction(transaction);
    }
}
