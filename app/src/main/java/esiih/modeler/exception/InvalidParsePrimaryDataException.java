package esiih.modeler.exception;

/**
 * Thrown to indicate that the parsed primary data is invalid.
 */
public class InvalidParsePrimaryDataException extends Exception {

    /**
     * Constructs a new {@code InvalidParsePrimaryDataException} with the
     * specified detail message.
     *
     * @param message the detail message
     */
    public InvalidParsePrimaryDataException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code InvalidParsePrimaryDataException} with the
     * specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause (which is saved for later retrieval by the
     * {@link #getCause()} method)
     */
    public InvalidParsePrimaryDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
