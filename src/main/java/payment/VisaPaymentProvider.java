package payment;

import java.math.BigDecimal;
import java.util.UUID;

import Account.Account;

public class VisaPaymentProvider implements PaymentProvider {

    private final String apiKey;

    public VisaPaymentProvider(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public PaymentResult processPayment(Account account, BigDecimal amount) {

        String reference = "VISA-" + UUID.randomUUID();
        return new PaymentResult(true, reference,
                "Visa payment of " + amount + " processed for account " + account.getAccountNumber());
    }
}