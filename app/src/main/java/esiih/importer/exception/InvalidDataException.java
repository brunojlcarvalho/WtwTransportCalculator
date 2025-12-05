package esiih.importer.exception;

/**
 * Thrown to indicate that the data is invalid.
 */
public class InvalidDataException extends Exception {

    /**
     * Constructs a new {@code InvalidDataException} with the specified detail
     * message.
     *
     * @param message the detail message
     */
    public InvalidDataException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code InvalidDataException} with the specified detail
     * message and cause.
     *
     * @param message the detail message
     * @param cause the cause (which is saved for later retrieval by the
     * {@link #getCause()} method)
     */
    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
