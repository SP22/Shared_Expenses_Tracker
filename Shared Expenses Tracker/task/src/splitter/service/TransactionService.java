package splitter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import splitter.entity.Transaction;
import splitter.repository.TransactionRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    private final List<Transaction> history = new ArrayList<>();

    public void addTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Transactional
    public void deleteBeforeDate(LocalDate date) {
        transactionRepository.deleteByWhenLessThanEqual(date);
    }

    public List<Transaction> getHistory() {
        return transactionRepository.findAll();
    }
}
