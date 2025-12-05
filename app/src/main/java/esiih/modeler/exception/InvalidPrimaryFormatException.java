package esiih.modeler.exception;

/**
 * Thrown to indicate that the primary data format is invalid.
 */
public class InvalidPrimaryFormatException extends InvalidParsePrimaryDataException {

    /**
     * Constructs a new {@code InvalidPrimaryFormatException} with the specified
     * detail message.
     *
     * @param message the detail message
     */
    public InvalidPrimaryFormatException(String message) {
        super(message);
    }
}
