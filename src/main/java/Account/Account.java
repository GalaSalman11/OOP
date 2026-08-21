package Account;

import customer.Customer;

import java.math.BigDecimal;
import java.time.LocalDate;
import  exception.ErrorCode;
import  exception.BankingException ;
import transaction.TransactionHistory;

public abstract class Account {
    private static final BigDecimal MIN_TXN = new BigDecimal("0.01");
    private static final BigDecimal MAX_TXN = new BigDecimal("500000.00");
     private String accountNumber ;
     protected BigDecimal Balance ;
     private AccountStatus accountStatus ;
     private Customer Owner;
     private LocalDate CreationDate;
     private TransactionHistory transactionHistory;


    public Account(String accountNumber, Customer owner) {
        this.accountNumber = accountNumber;
        Owner = owner;
        accountStatus = AccountStatus.ACTIVE;
    }

    protected abstract void doDeposit(BigDecimal amount);
    protected abstract void doWithdraw(BigDecimal amount);
    public abstract BigDecimal getAvailableBalance();
    public BigDecimal getBalance() { return Balance; }
    public String getAccountNumber() { return accountNumber; }
    public Customer getOwner() { return Owner; }
    public AccountStatus getStatus() { return accountStatus; }
    public LocalDate getCreationDate() { return CreationDate; }
    //protected TransactionHistory getHistory() { return history; }
    public final void deposit(BigDecimal amount) {
        validateAmount(amount);
        requireNotClosed();
        doDeposit(amount);
    }
    public void  withdraw(BigDecimal amount){
        validateAmount(amount);
        requireActiveForOutflow();
        doWithdraw(amount);
    }
    public void validateAmount(BigDecimal amount){
        if (amount == null) {
            throw new BankingException(ErrorCode.ERR_VAL_003);
        }

        if (amount.stripTrailingZeros().scale() > 2) {
            throw new BankingException(ErrorCode.ERR_VAL_007);
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BankingException(ErrorCode.ERR_VAL_004);
        }

        if (amount.compareTo(MIN_TXN) < 0) {
            throw new BankingException(ErrorCode.ERR_VAL_005);
        }

        if (amount.compareTo(MAX_TXN) > 0) {
            throw new BankingException(ErrorCode.ERR_VAL_006);
        }
    }
    private void requireNotClosed() {
        if (accountStatus == AccountStatus.CLOSED) {
            throw new BankingException(ErrorCode.ERR_ACC_002, "account " + accountNumber);
        }
    }

    private void requireActiveForOutflow() {
        if (accountStatus == AccountStatus.CLOSED) {
            throw new BankingException(ErrorCode.ERR_ACC_002, "account " + accountNumber);
        }
        if (accountStatus == AccountStatus.FROZEN) {
            throw new BankingException(ErrorCode.ERR_ACC_003, "account " + accountNumber);
        }
    }



    public void freeze(){
       if (accountStatus.equals(AccountStatus.CLOSED)) {
           throw  new BankingException(ErrorCode.ERR_ACC_004 );
        }
        this.accountStatus = AccountStatus.FROZEN ;
    }
    public void unfreeze(){
        if (accountStatus.equals(AccountStatus.CLOSED)){
            throw  new BankingException(ErrorCode.ERR_ACC_005 );
        }
       accountStatus = AccountStatus.ACTIVE;
    }
    public void chargeFee(BigDecimal fee) {
        Balance = Balance.subtract(fee);
    }
    public void applyInterest(BigDecimal interest) {
        Balance = Balance.add(interest);
    }

}
