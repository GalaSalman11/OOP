package service;

import java.math.BigDecimal;

import Account.Account;
import payment.PaymentProvider;
import payment.PaymentResult;

public class PaymentService {

    private final PaymentProvider paymentProvider;

    public PaymentService(PaymentProvider paymentProvider) {
        this.paymentProvider = paymentProvider;
    }

    public PaymentResult charge(Account account, BigDecimal amount) {
        account.validateAmount(amount);
        return paymentProvider.processPayment(account, amount);
    }
}