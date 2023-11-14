package splitter.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import splitter.service.TransactionService;
import splitter.util.DateUtil;

import java.util.List;

@Service
public class WriteOffExecutor implements CommandExecutor {
    @Autowired
    private TransactionService transactionService;
    @Override
    public void execute(List<String> params) {
        transactionService.deleteBeforeDate(DateUtil.parseDate(params.get(0)));
    }
}
