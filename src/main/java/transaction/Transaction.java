package transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Transaction(
        String transactionId,
        String fromAccountNumber,
        String toAccountNumber,
        BigDecimal amount,
        LocalDateTime timestamp
) {}