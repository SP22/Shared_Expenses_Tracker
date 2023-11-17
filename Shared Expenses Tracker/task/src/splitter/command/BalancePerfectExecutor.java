package splitter.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import splitter.entity.Transaction;

import java.util.List;

@Service
public class BalancePerfectExecutor implements CommandExecutor {
    @Autowired private BalanceExecutor balanceExecutor;
    @Override
    public void execute(List<String> params) {
        List<Transaction> balance = balanceExecutor.getBalance(params);
    }
}
