package splitter.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CashBackExecutor implements CommandExecutor {
    @Autowired private PurchaseExecutor purchaseExecutor;
    @Transactional
    @Override
    public void execute(List<String> params) {
        params.set(4, "-" + params.get(4));
        purchaseExecutor.execute(params);
    }
}
