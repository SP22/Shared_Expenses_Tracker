package splitter.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import splitter.service.TransactionService;
import splitter.entity.Transaction;
import splitter.entity.User;
import splitter.util.DateUtil;
import splitter.util.GroupMemberFilter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BalanceExecutor implements CommandExecutor {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    GroupMemberFilter groupMemberFilter;
    @Transactional
    @Override
    public void execute(List<String> params) {
        List<Transaction> output = getBalance(params);

        Set<String> filteredUsers = groupMemberFilter.filter(params.get(3));

        output = output.stream().sorted().collect(Collectors.toList());
//        if (output.isEmpty()) {
//            System.out.println("No repayments");
//        } else {
            boolean printed = false;
            for (Transaction t : output) {
                if (filteredUsers.isEmpty() || filteredUsers.contains(t.from.getName())) {
                    printed = true;
                    System.out.println(t.from.getName() + " owes " + t.to.getName() + " " + t.amount.setScale(2, RoundingMode.HALF_UP));
                }
            }
            if (!printed) {
                System.out.println("No repayments");
            }
//        }
    }

    public List<Transaction> getBalance(List<String> params) {
        boolean open = true;
        LocalDate when = DateUtil.parseDate(params.get(0));
        if ("close".equals(params.get(2))) {
            open = false;
        }
        LocalDate until = open ? when.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()) : when;
        List<Transaction> copy = transactionService.getHistory().stream()
                .filter(x -> !x.when.isAfter(until))
                .sorted()
                .toList();

        Map<User, Map<User, BigDecimal>> balance = new HashMap<>();
        for (Transaction t : copy) {
            User from = t.from;
            User to = t.to;
            BigDecimal amount = t.amount;
            if (from.compareTo(to) > 0) {
                from = t.to;
                to = t.from;
                amount = amount.negate();
            }
            if (!balance.containsKey(from)) {
                balance.put(from, new HashMap<>());
                balance.get(from).put(to, amount);
            } else {
                balance.get(from).put(to, balance.get(from).getOrDefault(to, BigDecimal.ZERO).add(amount));
            }
        }
        List<Transaction> output = new ArrayList<>();
        for (User s : balance.keySet()) {
            for (User k : balance.get(s).keySet()) {
                BigDecimal amount = balance.get(s).get(k);
                if (amount.signum() > 0) {
                    output.add(new Transaction(LocalDate.now(), k, s, amount));
                } else if (amount.signum() < 0) {
                    output.add(new Transaction(LocalDate.now(), s, k , amount.negate()));
                }
            }
        }
        return output;
    }
}
