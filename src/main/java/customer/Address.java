package customer;
import exception.BankingException ;
import exception.ErrorCode ;

public class Address {
    private String city;
    private String street;
    private String governorate;
    private String postalCode;

    public Address(String street, String city, String governorate, String postalCode) {
        if (street == null || street.isBlank()
                || city == null || city.isBlank()
                || governorate == null || governorate.isBlank()) {
            throw new BankingException(ErrorCode.ERR_VAL_014);
        }
        if (postalCode != null && !postalCode.matches("\\d{5}")) {
            throw new BankingException(ErrorCode.ERR_VAL_014);
        }
        this.street = street;
        this.city = city;
        this.governorate = governorate;
        this.postalCode = postalCode;
    }

    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getGovernorate() { return governorate; }
    public String getPostalCode() { return postalCode; }


}
