package transaction;

import java.math.BigDecimal;

public record TransactionResult(
        boolean success,
        String message,
        String transactionId,
        BigDecimal amount,
        String fromAccountNumber,
        String toAccountNumber
) {}
