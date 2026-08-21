package service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import Account.Account;
import exception.BankingException;
import exception.ErrorCode;
import notification.NotificationSender;
import repository.AccountRepository;
import transaction.Transaction;
import transaction.TransactionResult;

public class TransferService {

    private final AccountRepository accountRepository;
    private final NotificationSender notificationSender;

    public TransferService(AccountRepository accountRepository, NotificationSender notificationSender) {
        this.accountRepository = accountRepository;
        this.notificationSender = notificationSender;
    }

    public TransactionResult transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        if (fromAccountNumber == null || toAccountNumber == null) {
            throw new BankingException(ErrorCode.ERR_VAL_001);
        }

        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new BankingException(ErrorCode.ERR_VAL_002, "cannot transfer to the same account");
        }

        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber);
        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber);

        fromAccount.withdraw(amount);
        try {
            toAccount.deposit(amount);
        } catch (RuntimeException e) {

            fromAccount.deposit(amount);
            throw e;
        }

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        String transactionId = UUID.randomUUID().toString();
        Transaction transaction = new Transaction(
                transactionId, fromAccountNumber, toAccountNumber, amount, LocalDateTime.now());

        notificationSender.send(fromAccount.getOwner(),
                "Transfer of " + amount + " sent to account " + toAccountNumber);
        notificationSender.send(toAccount.getOwner(),
                "Transfer of " + amount + " received from account " + fromAccountNumber);

        return new TransactionResult(true, "transfer completed", transaction.transactionId(),
                amount, fromAccountNumber, toAccountNumber);
    }
}