package exception;

public final class BankingException extends RuntimeException {

    private final ErrorCode errorCode;

    public BankingException(ErrorCode errorCode) {
        super(errorCode.defaultMessage());
        this.errorCode = errorCode;
    }

    public BankingException(ErrorCode errorCode, String detail) {
        super(errorCode.defaultMessage() + " — " + detail);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}