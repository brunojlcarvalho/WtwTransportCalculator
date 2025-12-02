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

    /**
     * Constructs a new {@code InvalidPrimaryFormatException} with the specified
     * detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause (which is saved for later retrieval by the
     * {@link #getCause()} method)
     */
    public InvalidPrimaryFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
