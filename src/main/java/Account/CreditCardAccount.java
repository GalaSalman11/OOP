package Account;

import java.math.BigDecimal;
import java.math.RoundingMode;

import customer.Customer;
import exception.BankingException;
import exception.ErrorCode;

public final class CreditCardAccount extends Account implements FeeBearing,IntrestBearing {

    private static final BigDecimal FEE_RATE = new BigDecimal("0.03");   // 3%
    private static final BigDecimal FEE_FLOOR = new BigDecimal("50.00");

    private final BigDecimal creditLimit;
    private final BigDecimal interestRate;    // 0.24 for 24%, charged on amount owed
    private final LimitPolicy limitPolicy;

    public CreditCardAccount(String accountNumber, Customer owner,
                             BigDecimal creditLimit, BigDecimal interestRate,
                             LimitPolicy limitPolicy) {
        super(accountNumber, owner);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
        this.limitPolicy = limitPolicy;
        this.Balance = creditLimit;   // balance represents AVAILABLE CREDIT, starts full
    }

    @Override
    protected void doDeposit(BigDecimal amount) {
        // a "deposit" is a repayment — reduces what's owed, restores available credit
        BigDecimal owed = getAmountOwed();
        if (amount.compareTo(owed) > 0) {
            throw new BankingException(ErrorCode.ERR_TXN_006,
                    "repayment of " + amount + " exceeds outstanding balance of " + owed);
        }
        Balance = Balance.add(amount).min(creditLimit);
    }

    @Override
    protected void doWithdraw(BigDecimal amount) {
        // a "withdrawal" is a purchase/cash advance — the fee counts against
        // the limit too, so both are checked together
        BigDecimal fee = calculateFee(amount);
        BigDecimal totalDebit = amount.add(fee);
        if (!limitPolicy.canDebit(Balance, totalDebit, BigDecimal.ZERO)) {
            throw new BankingException(ErrorCode.ERR_TXN_004,
                    "amount " + amount + " + fee " + fee + " exceeds available credit of " + Balance);
        }
        Balance = Balance.subtract(totalDebit);
    }

    @Override
    public BigDecimal calculateFee(BigDecimal amount) {
        BigDecimal percentageFee = amount.multiply(FEE_RATE).setScale(2, RoundingMode.HALF_UP);
        return percentageFee.max(FEE_FLOOR);
    }

    @Override
    public BigDecimal calculateInterest() {
        // interest is CHARGED on the debt, not earned — increases what's owed when applied
        return getAmountOwed().multiply(interestRate)
                .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getAvailableBalance() {
        return creditLimit.subtract(getAmountOwed());
    }

    public BigDecimal getAmountOwed() {
        return creditLimit.subtract(Balance);
    }

    public BigDecimal getCreditLimit() { return creditLimit; }


}