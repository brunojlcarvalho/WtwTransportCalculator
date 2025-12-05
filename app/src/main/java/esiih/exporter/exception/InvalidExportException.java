package esiih.exporter.exception;

/**
 * Thrown to indicate that the export data is invalid.
 */
public class InvalidExportException extends Exception {

    /**
     * Constructs a new {@code InvalidExportException} with the specified detail
     * message.
     *
     * @param message the detail message
     */
    public InvalidExportException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code InvalidExportException} with the specified detail
     * message and cause.
     *
     * @param message the detail message
     * @param cause the cause (which is saved for later retrieval by the
     * {@link #getCause()} method)
     */
    public InvalidExportException(String message, Throwable cause) {
        super(message, cause);
    }
}
