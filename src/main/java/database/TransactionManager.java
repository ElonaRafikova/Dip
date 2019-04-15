package database;

import java.util.ArrayList;
import java.util.List;

public class TransactionManager {

    private List<Transaction> transactionBuffer = new ArrayList<Transaction>();

    public TransactionManager(List<Transaction> transactions) {
        transactionBuffer.addAll(transactions);
    }

    public void addTransaction(Transaction transaction) {
        transactionBuffer.add(transaction);
    }

    public void executeTransactions() {
        for (Transaction transaction : transactionBuffer) {
            transaction.start();
            transaction.execute();
            transaction.commit();
        }
    }


}
