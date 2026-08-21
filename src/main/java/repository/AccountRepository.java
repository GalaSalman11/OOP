package repository;

import java.util.List;

import Account.Account;

public interface AccountRepository {
    Account findByAccountNumber(String accountNumber);
    void save(Account account);
    List<Account> findAll();
}