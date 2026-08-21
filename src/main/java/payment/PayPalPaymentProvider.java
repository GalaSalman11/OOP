package payment;

import java.math.BigDecimal;
import java.util.UUID;

import Account.Account;

public class PayPalPaymentProvider implements PaymentProvider {

    private final String merchantCode;

    public PayPalPaymentProvider(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    @Override
    public PaymentResult processPayment(Account account, BigDecimal amount) {

        String reference = "PAYPAL-" + UUID.randomUUID();
        return new PaymentResult(true, reference,
                "PayPal payment of " + amount + " processed for account " + account.getAccountNumber());
    }
}