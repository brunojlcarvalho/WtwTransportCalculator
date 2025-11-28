package esiih.modeler.exception;

public class InvalidParsedSecondaryDataException extends Exception {

    /**
     * Constructs a new {@code InvalidParsedEmissionsDataException} with no
     * detail message.
     */
    public InvalidParsedSecondaryDataException() {
    }

    /**
     * Constructs a new {@code InvalidParsedEmissionsDataException} with the
     * specified detail message.
     *
     * @param message the detail message
     */
    public InvalidParsedSecondaryDataException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code InvalidParsedEmissionsDataException} with the
     * specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause (which is saved for later retrieval by the
     * {@link #getCause()} method)
     */
    public InvalidParsedSecondaryDataException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new {@code InvalidParsedEmissionsDataException} with the
     * specified cause and a detail message of (cause==null ? null :
     * cause.toString()) (which typically contains the class and detail message
     * of cause).
     *
     * @param cause the cause (which is saved for later retrieval by the
     * {@link #getCause()} method)
     */
    public InvalidParsedSecondaryDataException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new {@code InvalidParsedEmissionsDataException} with the
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
    public InvalidParsedSecondaryDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
