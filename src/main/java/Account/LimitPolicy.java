package Account;

import java.math.BigDecimal;

import java.math.BigDecimal;


public final class LimitPolicy {

    public BigDecimal remainingLimit(BigDecimal currentValue, BigDecimal floor) {
        BigDecimal room = currentValue.subtract(floor);
        return room.max(BigDecimal.ZERO);
    }

    public boolean canDebit(BigDecimal currentValue, BigDecimal amount, BigDecimal floor) {
        if(remainingLimit(currentValue, floor).compareTo(amount) >= 0) {
            return true;
        } else {
            return false;
        }
    }
}