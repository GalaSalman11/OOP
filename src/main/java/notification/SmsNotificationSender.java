package notification;

import customer.Customer;

public class SmsNotificationSender implements NotificationSender {

    private final String smsGatewayUrl;

    public SmsNotificationSender(String smsGatewayUrl) {
        this.smsGatewayUrl = smsGatewayUrl;
    }

    @Override
    public void send(Customer customer, String message) {
        // In a real integration this would POST to smsGatewayUrl with the
        // customer's mobile number and the message body.
        System.out.printf("[SMS via %s] to %s: %s%n", smsGatewayUrl, customer.getMobileNumber(), message);
    }
}