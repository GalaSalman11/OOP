package payment;

import java.math.BigDecimal;

import Account.Account;

public interface PaymentProvider {
    PaymentResult processPayment(Account account, BigDecimal amount);
}