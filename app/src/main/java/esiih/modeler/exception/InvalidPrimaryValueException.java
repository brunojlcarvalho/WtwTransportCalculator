package esiih.modeler.exception;

/**
 * Thrown to indicate that the primary data value is invalid.
 */
public class InvalidPrimaryValueException extends InvalidParsePrimaryDataException {

    /**
     * Constructs a new {@code InvalidPrimaryValueException} with the specified
     * detail message.
     *
     * @param message the detail message
     */
    public InvalidPrimaryValueException(String message) {
        super(message);
    }
}
