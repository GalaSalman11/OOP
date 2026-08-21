package Account;
import java.time.LocalDate;
import  exception.ErrorCode;
import  exception.BankingException ;
import java.math.BigDecimal;
import customer.Customer;

public final class CurrentAccount extends Account implements FeeBearing {

    private static final BigDecimal MONTHLY_FEE = new BigDecimal("25.00");

    private final BigDecimal overdraftLimit;
    private final LimitPolicy limitPolicy;

    public CurrentAccount(String accountNumber, Customer owner,
                          BigDecimal overdraftLimit, LimitPolicy limitPolicy) {
        super(accountNumber, owner);
        this.overdraftLimit = overdraftLimit;
        this.limitPolicy = limitPolicy;
        this.Balance = BigDecimal.ZERO;
    }

    @Override
    protected void doDeposit(BigDecimal amount) {
        Balance = Balance.add(amount);
    }

    @Override
    protected void doWithdraw(BigDecimal amount) {
        // floor is -overdraftLimit — balance may go negative, but not past this
        BigDecimal floor = overdraftLimit.negate();
        if (!limitPolicy.canDebit(Balance, amount, floor)) {
            throw new BankingException(ErrorCode.ERR_TXN_003,
                    "withdrawal of " + amount + " exceeds overdraft limit of " + overdraftLimit);
        }
        Balance = Balance.subtract(amount);
    }

    @Override
    public BigDecimal calculateFee(BigDecimal amount) {
        // flat monthly maintenance fee — `amount` is intentionally unused here
        return MONTHLY_FEE;
    }

    @Override
    public BigDecimal getAvailableBalance() {
        return Balance.add(overdraftLimit);
    }

    public BigDecimal getOverdraftLimit() { return overdraftLimit; }


}