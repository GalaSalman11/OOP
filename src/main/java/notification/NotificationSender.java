package notification;

import customer.Customer;

public interface NotificationSender {
    void send(Customer customer, String message);
}