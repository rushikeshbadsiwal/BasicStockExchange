package exceptions;


public class LowBalanceException extends RuntimeException {

    public LowBalanceException(String message) {
        super(message);
    }

}