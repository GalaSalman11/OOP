import java.math.BigDecimal;

import Account.Account;
import Account.LimitPolicy;
import customer.Address;
import customer.Customer;
import exception.BankingException;
import notification.NotificationSender;
import notification.SmsNotificationSender;
import repository.AccountRepository;
import repository.CustomerRepository;
import repository.InMemoryAccountRepository;
import service.AccountService;
import service.CustomerService;
import service.TransferService;
import transaction.TransactionResult;

public class Main {

    public static void main(String[] args) {

        // ---- Wiring ----
        AccountRepository accountRepository = new InMemoryAccountRepository();
        LimitPolicy limitPolicy = new LimitPolicy();
        NotificationSender notificationSender = new SmsNotificationSender("gateway.local");

        AccountService accountService = new AccountService(accountRepository, limitPolicy);
        TransferService transferService = new TransferService(accountRepository, notificationSender);

        // Note: InMemoryCustomerRepository doesn't implement CustomerRepository yet,
        // so customers are built directly here instead of going through CustomerService.
        Customer alice = new Customer(
                "CUST-001", "Alice Adel", "29901011234567",
                "01012345678", new Address("12 Tahrir St", "Giza", "Giza", "12345"));

        Customer bob = new Customer(
                "CUST-002", "Bob Nabil", "29802022345678",
                "01198765432", new Address("5 Nile Corniche", "Cairo", "Cairo", "11511"));

        System.out.println("=== Opening accounts ===");
        Account aliceCurrent = accountService.openAccount(alice, "CURRENT");
        Account bobSavings = accountService.openAccount(bob, "SAVINGS");
        Account aliceCredit = accountService.openAccount(alice, "CREDIT_CARD");
        Account bobPremium = accountService.openAccount(bob, "PREMIUM_SAVINGS");

        System.out.println("Alice current:  " + aliceCurrent.getAccountNumber() + " balance=" + aliceCurrent.getBalance());
        System.out.println("Bob savings:    " + bobSavings.getAccountNumber() + " balance=" + bobSavings.getBalance());
        System.out.println("Alice credit:   " + aliceCredit.getAccountNumber() + " balance=" + aliceCredit.getBalance());
        System.out.println("Bob premium:    " + bobPremium.getAccountNumber() + " balance=" + bobPremium.getBalance());

        System.out.println("\n=== Deposits & withdrawals ===");
        aliceCurrent.deposit(new BigDecimal("1000.00"));
        System.out.println("Alice current after deposit: " + aliceCurrent.getBalance());

        aliceCurrent.withdraw(new BigDecimal("3000.00")); // dips into overdraft
        System.out.println("Alice current after overdraft withdrawal: " + aliceCurrent.getBalance());

        try {
            bobSavings.withdraw(new BigDecimal("400.00")); // would breach minimum balance
        } catch (BankingException e) {
            System.out.println("Expected failure: " + e.getMessage());
        }

        System.out.println("\n=== Transfer ===");
        accountRepository.save(aliceCurrent);
        accountRepository.save(bobSavings);
        TransactionResult result = transferService.transfer(
                aliceCurrent.getAccountNumber(), bobSavings.getAccountNumber(), new BigDecimal("200.00"));
        System.out.println("Transfer result: " + result);
        System.out.println("Alice current balance: " + accountService.checkBalance(aliceCurrent.getAccountNumber()));
        System.out.println("Bob savings balance:   " + accountService.checkBalance(bobSavings.getAccountNumber()));

        System.out.println("\n=== Monthly fees & interest ===");
        accountService.runMonthlyFees();
        accountService.runMonthlyInterest();
        System.out.println("Alice current after fee:     " + aliceCurrent.getBalance());
        System.out.println("Bob savings after interest:  " + bobSavings.getBalance());
        System.out.println("Bob premium after interest:  " + bobPremium.getBalance());

        System.out.println("\n=== Freeze / unfreeze ===");
        accountService.freezeAccount(aliceCurrent.getAccountNumber());
        try {
            aliceCurrent.withdraw(new BigDecimal("10.00"));
        } catch (BankingException e) {
            System.out.println("Expected failure: " + e.getMessage());
        }
        accountService.unfreezeAccount(aliceCurrent.getAccountNumber());
        System.out.println("Unfrozen — Alice current balance: " + aliceCurrent.getBalance());
    }
}