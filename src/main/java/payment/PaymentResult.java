package payment;

public record PaymentResult(
        boolean success,
        String providerReference,
        String message
) {}
