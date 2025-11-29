package esiih.modeler.exception;

/**
 * Thrown to indicate that the parsed primary data is invalid.
 */
public class InvalidParsePrimaryDataException extends Exception {

    /**
     * Constructs a new {@code InvalidParsePrimaryDataException} with no detail
     * message.
     */
    public InvalidParsePrimaryDataException() {
    }

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

    /**
     * Constructs a new {@code InvalidParsePrimaryDataException} with the
     * specified cause and a detail message of (cause==null ? null :
     * cause.toString()) (which typically contains the class and detail message
     * of cause).
     *
     * @param cause the cause (which is saved for later retrieval by the
     * {@link #getCause()} method)
     */
    public InvalidParsePrimaryDataException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new {@code InvalidParsePrimaryDataException} with the
     * specified detail message, cause, suppression enabled or disabled, and
     * writable stack trace enabled or disabled.
     *
     * @param message the detail message
     * @param cause the cause (which is saved for later retrieval by the
     * {@link #getCause()} method)
     * @param enableSuppression whether or not suppression is enabled or
     * disabled
     * @param writableStackTrace whether or not the stack trace should be
     * writable
     */
    public InvalidParsePrimaryDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
