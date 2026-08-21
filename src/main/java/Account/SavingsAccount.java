package Account;
import java.time.LocalDate;
import  exception.ErrorCode;
import  exception.BankingException ;
import java.math.BigDecimal;
import customer.Customer;
import java.math.BigDecimal;
import java.math.RoundingMode;


public final class SavingsAccount extends Account implements IntrestBearing  {

    private final BigDecimal interestRate;
    private final BigDecimal minimumBalance;
    private final LimitPolicy limitPolicy;

    public SavingsAccount(String accountNumber, Customer owner,
                          BigDecimal interestRate, BigDecimal minimumBalance,
                          BigDecimal openingDeposit, LimitPolicy limitPolicy) {
        super(accountNumber, owner);
        if (openingDeposit.compareTo(minimumBalance) < 0) {
            throw new BankingException(ErrorCode.ERR_ACC_008);
        }
        this.interestRate = interestRate;
        this.minimumBalance = minimumBalance;
        this.limitPolicy = limitPolicy;
        this.Balance = openingDeposit;
    }

    @Override
    protected void doDeposit(BigDecimal amount) {
        Balance = Balance.add(amount);
    }

    @Override
    protected void doWithdraw(BigDecimal amount) {
        if (!limitPolicy.canDebit(Balance, amount, minimumBalance)) {
            throw new BankingException(ErrorCode.ERR_TXN_002,
                    "withdrawal of " + amount + " would breach minimum balance of " + minimumBalance);
        }
        Balance = Balance.subtract(amount);
    }

    @Override
    public BigDecimal calculateInterest() {
        return Balance.multiply(interestRate).divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getAvailableBalance() {
        return Balance.subtract(minimumBalance);
    }

    public BigDecimal getMinimumBalance() { return minimumBalance; }
}