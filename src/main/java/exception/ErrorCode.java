package exception;


public enum ErrorCode {

    // Validation
    ERR_VAL_001("A required argument is null", "Required value is missing"),
    ERR_VAL_002("Text field is empty or whitespace", "Field must not be blank"),
    ERR_VAL_003("Amount is null", "Amount is required"),
    ERR_VAL_004("Amount is zero or negative", "Amount must be greater than zero"),
    ERR_VAL_005("Amount below 0.01", "Amount is below the minimum of 0.01 EGP"),
    ERR_VAL_006("Amount above 500,000.00", "Amount exceeds the maximum of 500,000.00 EGP"),
    ERR_VAL_007("Amount has more than 2 decimal places", "Amount must have at most 2 decimal places"),
    ERR_VAL_008("National ID is not 14 digits", "National ID must be exactly 14 digits"),
    ERR_VAL_009("National ID contains non-digits", "National ID must contain digits only"),
    ERR_VAL_010("Mobile is not 11 digits", "Mobile number must be exactly 11 digits"),
    ERR_VAL_011("Mobile does not start with 01", "Mobile number must start with 01"),
    ERR_VAL_012("Name length out of range", "Name must be between 3 and 100 characters"),
    ERR_VAL_013("Name contains digits or symbols", "Name must contain letters and spaces only"),
    ERR_VAL_014("Address is missing a required part", "Street, city and governorate are required"),
    ERR_VAL_015("Interest rate is negative or above 100%", "Rate must be between 0 and 100"),
    ERR_VAL_016("Overdraft or credit limit is negative", "Limit must not be negative"),

    // Customer
    ERR_CUS_001("Customer ID not found", "Customer not found"),
    ERR_CUS_002("National ID already registered", "A customer with this national ID already exists"),
    ERR_CUS_003("Attempt to change name after registration", "Name cannot be changed"),
    ERR_CUS_004("Attempt to change national ID after registration", "National ID cannot be changed"),

    // Account
    ERR_ACC_001("Account number not found", "Account not found"),
    ERR_ACC_002("Operation not permitted on a closed account", "Account is closed"),
    ERR_ACC_003("Outflow attempted on a frozen account", "Account is frozen"),
    ERR_ACC_004("Freeze/close attempted on a closed account", "A closed account cannot be frozen"),
    ERR_ACC_005("Unfreeze attempted on a closed account", "A closed account cannot be unfrozen"),
    ERR_ACC_006("Close attempted with a non-zero balance", "Account must have a zero balance before closing"),
    ERR_ACC_007("Close attempted with an outstanding debt", "Outstanding balance must be settled before closing"),
    ERR_ACC_008("Savings opened below the minimum balance", "Opening deposit must be at least the minimum balance"),
    ERR_ACC_009("Close attempted on an already-closed account", "Account is already closed"),

    // Transaction
    ERR_TXN_001("Withdrawal or transfer exceeds available funds", "Insufficient funds"),
    ERR_TXN_002("Savings withdrawal would breach the minimum balance", "Withdrawal would breach the minimum balance"),
    ERR_TXN_003("Current withdrawal exceeds the overdraft limit", "Withdrawal exceeds the overdraft limit"),
    ERR_TXN_004("Cash advance exceeds available credit", "Amount exceeds available credit"),
    ERR_TXN_005("Source and destination are the same account", "Source and destination must differ"),
    ERR_TXN_006("Credit card repayment exceeds what is owed", "Repayment exceeds the outstanding balance"),
    ERR_TXN_007("Transaction reference not found", "Transaction not found"),

    // Payment
    ERR_PAY_001("Provider declined the payment", "Payment was declined by the provider"),
    ERR_PAY_002("Provider timed out", "Payment provider did not respond"),
    ERR_PAY_003("Provider threw an unexpected error", "Payment provider error"),
    ERR_PAY_004("Provider is unavailable", "Payment provider is unavailable"),

    // Notification
    ERR_NOT_001("Notification could not be delivered", "Notification delivery failed");

    private final String condition;
    private final String message;

    ErrorCode(String condition, String message) {
        this.condition = condition;
        this.message = message;
    }

    public String condition() { return condition; }
    public String defaultMessage() { return message; }
}