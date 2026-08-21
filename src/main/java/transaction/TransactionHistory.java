package transaction;

import java.util.ArrayList;
import java.util.List;

public final class TransactionHistory {
    private final List<Transaction> transactions = new ArrayList<>();

    public void record(Transaction transaction) { transactions.add(transaction); }
    public List<Transaction> getTransactions() { return transactions; }
}