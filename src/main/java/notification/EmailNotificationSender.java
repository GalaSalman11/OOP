package notification;

import customer.Customer;

public class EmailNotificationSender implements NotificationSender {

    private final String smtpHost;

    public EmailNotificationSender(String smtpHost) {
        this.smtpHost = smtpHost;
    }
    @Override
    public void send(Customer customer, String message) {
        System.out.printf("[Email via %s] to %s: %s%n", smtpHost, customer.getFullName(), message);
    }
}