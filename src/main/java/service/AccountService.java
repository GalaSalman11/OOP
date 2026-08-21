package service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import Account.Account;
import Account.CreditCardAccount;
import Account.CurrentAccount;
import Account.FeeBearing;
import Account.IntrestBearing ;
import Account.LimitPolicy;
import Account.PremiumSavingsAccount;
import Account.SavingsAccount;
import customer.Customer;
import exception.BankingException;
import exception.ErrorCode;
import repository.AccountRepository;

public class AccountService {

    private static final BigDecimal CURRENT_OVERDRAFT = new BigDecimal("5000.00");
    private static final BigDecimal SAVINGS_RATE = new BigDecimal("0.085");
    private static final BigDecimal SAVINGS_MIN_BALANCE = new BigDecimal("500.00");
    private static final BigDecimal CREDIT_LIMIT = new BigDecimal("20000.00");
    private static final BigDecimal CREDIT_RATE = new BigDecimal("0.24");
    private static final BigDecimal PREMIUM_RATE = new BigDecimal("0.12");
    private static final BigDecimal PREMIUM_MIN_BALANCE = new BigDecimal("50000.00");




    private final AccountRepository accountRepository;
    private final LimitPolicy limitPolicy;

    // THESE TWO FIELDS WERE MISSING — this was the root cause of most errors
    private final List<FeeBearing> monthlyFeeAccounts = new ArrayList<>();
    private final List<IntrestBearing> interestBearingAccounts = new ArrayList<>();

    public AccountService(AccountRepository accountRepository, LimitPolicy limitPolicy) {
        this.accountRepository = accountRepository;
        this.limitPolicy = limitPolicy;
    }

    public Account openAccount(Customer customer, String type) {
        String accountNumber = UUID.randomUUID().toString();
        Account account = switch (type) {
            case "CURRENT" -> {
                CurrentAccount current = new CurrentAccount(accountNumber, customer, CURRENT_OVERDRAFT, limitPolicy);
                monthlyFeeAccounts.add(current);
                yield current;
            }
            case "SAVINGS" -> {
                SavingsAccount savings = new SavingsAccount(accountNumber, customer, SAVINGS_RATE,
                        SAVINGS_MIN_BALANCE, SAVINGS_MIN_BALANCE, limitPolicy);
                interestBearingAccounts.add(savings);
                yield savings;
            }
            case "CREDIT_CARD" -> {
                CreditCardAccount creditCard = new CreditCardAccount(accountNumber, customer, CREDIT_LIMIT, CREDIT_RATE, limitPolicy);
                interestBearingAccounts.add(creditCard);
                yield creditCard;
            }
            case "PREMIUM_SAVINGS" -> {
                PremiumSavingsAccount premium = new PremiumSavingsAccount(accountNumber, customer, PREMIUM_RATE,PREMIUM_MIN_BALANCE, PREMIUM_MIN_BALANCE, limitPolicy);
                interestBearingAccounts.add(premium);
                yield premium;
            }
            default -> throw new BankingException(ErrorCode.ERR_VAL_002, "unknown account type: " + type);
        };

        accountRepository.save(account);
        return account;
    }

    public BigDecimal checkBalance(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber).getBalance();
    }

    public void freezeAccount(String accountNumber) {
        accountRepository.findByAccountNumber(accountNumber).freeze();
    }

    public void unfreezeAccount(String accountNumber) {
        accountRepository.findByAccountNumber(accountNumber).unfreeze();
    }

    public void runMonthlyFees() {
        for (FeeBearing feeBearing : monthlyFeeAccounts) {
            BigDecimal fee = feeBearing.calculateFee(BigDecimal.ZERO);
            ((Account) feeBearing).chargeFee(fee);
        }
    }

    public void runMonthlyInterest() {
        for (IntrestBearing interestBearing : interestBearingAccounts) {
            BigDecimal interest = interestBearing.calculateInterest();
            ((Account) interestBearing).applyInterest(interest);
        }
    }
}