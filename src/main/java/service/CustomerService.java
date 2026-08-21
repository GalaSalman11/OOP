package service;

import customer.Customer;
import customer.Address ;
import exception.BankingException ;
import exception.ErrorCode ;
import  repository.CustomerRepository ;

import java.util.UUID;

public class CustomerService {
 private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    public Customer CreateCustomer( String fullName,String nationalId , String mobilenumber , Address address){
     String CustomerId = UUID.randomUUID().toString() ;
     if (customerRepository.existsByNationalId(nationalId)) {
         throw new BankingException(ErrorCode.ERR_CUS_002);
     }
     Customer customer = new Customer( CustomerId ,fullName,nationalId,mobilenumber,address );
        customerRepository.Save(customer);
        return customer;
    }
    public Customer getCustomer(String NationalId){
        return customerRepository.findById(NationalId);
    }
    public Customer updateContactDetails(String customerId, String attemptedFullName,
                                         String attemptedNationalId, String newMobileNumber,
                                         Address newAddress) {
        Customer customer = customerRepository.findById(customerId);
        customer.updateContactDetails(attemptedFullName, attemptedNationalId, newMobileNumber, newAddress);
        customerRepository.Save(customer);
        return customer;
    }
}
