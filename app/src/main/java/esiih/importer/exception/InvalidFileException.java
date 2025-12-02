package esiih.importer.exception;

/**
 * Thrown to indicate that the file is invalid.
 */
public class InvalidFileException extends InvalidDataException {

    /**
     * Constructs a new {@code InvalidFileException} with the specified detail
     * message.
     *
     * @param message the detail message
     */
    public InvalidFileException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code InvalidFileException} with the specified detail
     * message and cause.
     *
     * @param message the detail message
     * @param cause the cause (which is saved for later retrieval by the
     * {@link #getCause()} method)
     */
    public InvalidFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
