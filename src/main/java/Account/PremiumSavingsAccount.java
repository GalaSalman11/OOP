package Account;
import customer.Customer ;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;

import  exception.ErrorCode;
import  exception.BankingException ;

public class PremiumSavingsAccount extends Account implements FeeBearing,IntrestBearing {

    private static final int FREE_WITHDRAWALS_PER_MONTH = 3;
    private static final BigDecimal WITHDRAWAL_FEE = new BigDecimal("100.00");

    private final BigDecimal interestRate ;
    private final BigDecimal minimumBalance ;
    private final LimitPolicy limitPolicy ;

    private int withdrawalsThisMonth = 0 ;
    private YearMonth currentMonth = YearMonth.now() ;

    public PremiumSavingsAccount(
            String accountNumber,
            Customer owner,
            BigDecimal interestRate,
            BigDecimal minimumBalance,
            BigDecimal initialBalance,
            LimitPolicy limitPolicy) {

        super(accountNumber, owner);

        this.interestRate = interestRate;
        this.minimumBalance = minimumBalance;
        this.limitPolicy = limitPolicy;

        this.Balance = initialBalance;
    }
//    public PremiumSavingsAccount(BigDecimal Balance , BigDecimal interestRate){
//        super(Balance);
//        this.interestRate = interestRate;
//    }

    private void resetCounterIfNewMonth() {
        YearMonth now = YearMonth.now();
        if (!now.equals(currentMonth)) {
            currentMonth = now;
            withdrawalsThisMonth = 0;
        }
    }

    @Override
    protected void doDeposit(BigDecimal amount) {
      Balance = Balance.add(amount) ;
    }

    @Override
    protected void doWithdraw(BigDecimal amount) {
        resetCounterIfNewMonth();
        BigDecimal fee = calculateFee(amount);
        BigDecimal totalDebit = amount.add(fee);
        boolean canWithdraw = limitPolicy.canDebit(Balance, totalDebit, minimumBalance);

        if (!canWithdraw) {
            throw new BankingException(
                    ErrorCode.ERR_TXN_002, "Withdrawal " + amount + " + fee " + fee +
                            " would breach minimum " + minimumBalance);
            }
        Balance = Balance.subtract(amount);
        withdrawalsThisMonth++;


    }



    @Override
    public BigDecimal calculateFee(BigDecimal amount) {
        resetCounterIfNewMonth();
        if ((withdrawalsThisMonth + 1) <= FREE_WITHDRAWALS_PER_MONTH) {
            return BigDecimal.ZERO;
        } else {
            return WITHDRAWAL_FEE;
        }
    }
    @Override
    public BigDecimal getAvailableBalance() {
        return Balance.subtract(minimumBalance);
    }
    @Override
    public BigDecimal calculateInterest() {
        return Balance.multiply(interestRate).divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
    }
    public BigDecimal getMinimumBalance() { return minimumBalance; }


}



