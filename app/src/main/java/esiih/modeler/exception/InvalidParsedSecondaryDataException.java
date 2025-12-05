package esiih.modeler.exception;

/**
 * Thrown to indicate that the parsed secondary data is invalid.
 */
public class InvalidParsedSecondaryDataException extends Exception {

    /**
     * Constructs a new {@code InvalidParsedSecondaryDataException} with the
     * specified detail message.
     *
     * @param message the detail message
     */
    public InvalidParsedSecondaryDataException(String message) {
        super(message);
    }
}
