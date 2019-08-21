package exceptions;

public class InsufficientStock extends RuntimeException {
    public InsufficientStock(String message) {
        super(message);
    }
}
