package id.wide.demo.exception;

public class ProductAvailabilityException extends Exception {

    public ProductAvailabilityException() {
        super();
    }

    public ProductAvailabilityException(String message) {
        super(message);
    }
}
