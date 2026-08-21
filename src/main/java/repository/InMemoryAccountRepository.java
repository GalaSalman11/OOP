package repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import Account.Account;
import exception.BankingException;
import exception.ErrorCode;

public final class InMemoryAccountRepository implements AccountRepository {

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public Account findByAccountNumber(String accountNumber) {
        if (accountNumber == null) {
            throw new BankingException(ErrorCode.ERR_VAL_001);
        }
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new BankingException(ErrorCode.ERR_ACC_001, "account " + accountNumber);
        }
        return account;
    }

    @Override
    public void save(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    @Override
    public List<Account> findAll() {
        return List.copyOf(accounts.values());
    }
}