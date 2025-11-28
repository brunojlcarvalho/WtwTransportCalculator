package esiih.modeler.exception;

public class InvalidPrimaryFormatException extends InvalidParsePrimaryDataException {
    public InvalidPrimaryFormatException(String message) {
        super(message);
    }

    public InvalidPrimaryFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}