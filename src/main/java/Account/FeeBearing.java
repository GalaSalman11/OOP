package Account;

import java.math.BigDecimal;

public interface FeeBearing {
    BigDecimal calculateFee(BigDecimal amount);
}
