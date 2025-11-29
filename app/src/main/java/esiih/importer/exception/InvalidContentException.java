package esiih.importer.exception;

/**
 * Thrown to indicate that the content of the data is invalid.
 */
public class InvalidContentException extends InvalidDataException {

    /**
     * Constructs a new {@code InvalidContentException} with the specified
     * detail message.
     *
     * @param message the detail message
     */
    public InvalidContentException(String message) {
        super(message);
    }
}
