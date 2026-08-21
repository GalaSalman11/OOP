package customer;

import exception.BankingException ;
import exception.ErrorCode ;

public class Customer {
//   review
    private final String customerId;
    private final String fullName;      // immutable after registration — ERR-CUS-003
    private final String nationalId;    // immutable after registration — ERR-CUS-004
    private String mobileNumber;
    private Address address;

    public Customer(String customerId, String fullName, String nationalId,
                    String mobileNumber, Address address) {
        validateName(fullName);
        validateNationalId(nationalId);
        validateMobile(mobileNumber);
        this.customerId = customerId;
        this.fullName = fullName;
        this.nationalId = nationalId;
        this.mobileNumber = mobileNumber;
        this.address = address;
    }


    public void updateContactDetails(String attemptedFullName, String attemptedNationalId,
                                     String newMobileNumber, Address newAddress) {
        if (!this.fullName.equals(attemptedFullName)) {
            throw new BankingException(ErrorCode.ERR_CUS_003);
        }
        if (!this.nationalId.equals(attemptedNationalId)) {
            throw new BankingException(ErrorCode.ERR_CUS_004);
        }
        validateMobile(newMobileNumber);
        this.mobileNumber = newMobileNumber;
        this.address = newAddress;
    }

    private static void validateName(String name) {
        if (name == null) {
            throw new BankingException(ErrorCode.ERR_VAL_001);
        }
        if (name.isBlank()) {
            throw new BankingException(ErrorCode.ERR_VAL_002);
        }
        if (name.length() < 3 || name.length() > 100) {
            throw new BankingException(ErrorCode.ERR_VAL_012);
        }
        if (!name.matches("[a-zA-Z ]+")) {
            throw new BankingException(ErrorCode.ERR_VAL_013);
        }
    }

    private static void validateNationalId(String nationalId) {
        if (nationalId == null) {
            throw new BankingException(ErrorCode.ERR_VAL_001);
        }
        if (nationalId.length() != 14) {
            throw new BankingException(ErrorCode.ERR_VAL_008);
        }
        if (!nationalId.matches("\\d{14}")) {
            throw new BankingException(ErrorCode.ERR_VAL_009);
        }
    }

    private static void validateMobile(String mobile) {
        if (mobile == null) {
            throw new BankingException(ErrorCode.ERR_VAL_001);
        }
        if (mobile.length() != 11) {
            throw new BankingException(ErrorCode.ERR_VAL_010);
        }
        if (!mobile.startsWith("01")) {
            throw new BankingException(ErrorCode.ERR_VAL_011);
        }
    }

    public String getCustomerId() { return customerId; }
    public String getFullName() { return fullName; }
    public String getNationalId() { return nationalId; }
    public String getMobileNumber() { return mobileNumber; }
    public Address getAddress() { return address; }
}