package esiih.calculator.exception;

/**
 * Thrown to indicate that a calculation cannot be performed due to invalid input data.
 */
public class InvalidCalculationException extends Exception {

    /**
     * Constructs a new {@code InvalidCalculationException} with the specified
     * detail message.
     *
     * @param message the detail message
     */
    public InvalidCalculationException(String message) {
        super(message);
    }
}
