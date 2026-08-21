package repository;

import customer.Address;
import customer.Customer;

public interface CustomerRepository {

    Customer findById(String CustomerId);
    public void Save (Customer customer);
    boolean existsByNationalId(String nationalId);


}
