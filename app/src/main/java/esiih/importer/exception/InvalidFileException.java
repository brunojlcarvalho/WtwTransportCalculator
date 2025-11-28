package esiih.importer.exception;

public class InvalidFileException extends InvalidDataException {

  public InvalidFileException(String message) {
    super(message);
  }

  public InvalidFileException(String message, Throwable cause) {
    super(message, cause);
  }
}
