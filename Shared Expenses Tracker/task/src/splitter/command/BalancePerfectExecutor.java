package splitter.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import splitter.entity.Transaction;
import splitter.service.BalanceReducer;
import splitter.util.GroupMemberFilter;

import java.math.RoundingMode;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BalancePerfectExecutor implements CommandExecutor {
    @Autowired private BalanceExecutor balanceExecutor;
    @Autowired private BalanceReducer balanceReducer;
    @Autowired private GroupMemberFilter groupMemberFilter;
    @Override
    public void execute(List<String> params) {
        List<Transaction> output = balanceExecutor.getBalance(balanceExecutor.getTransactions(params));
        output = balanceReducer.simplifyRepayments(output);
        Set<String> filteredUsers = null;
        try {
            filteredUsers = groupMemberFilter.filter(params.get(3));
        } catch (Exception e) {
            throw new IllegalArgumentException("Group does not exist");
        }

        output = output.stream().sorted().collect(Collectors.toList());
        boolean printed = false;
        for (Transaction t : output) {
            if (t.amount.signum() == 0) continue;
            if (filteredUsers.isEmpty() || filteredUsers.contains(t.from.getName())) {
                printed = true;
                System.out.println(t.from.getName() + " owes " + t.to.getName() + " " + t.amount.setScale(2, RoundingMode.HALF_UP));
            }
        }
        if (!printed) {
            System.out.println("No repayments");
        }
    }
}
