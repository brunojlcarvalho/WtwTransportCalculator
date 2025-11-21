package esiih.parser;

import esiih.importer.boundary.ParserService;
import esiih.importer.controller.Parser;
import esiih.importer.entity.ParsedPrimaryData;
import esiih.importer.entity.ParsedSecondaryData;
import esiih.importer.exception.InvalidDataException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestParserService {

    private static final String PRIMARY_BASE = "src/test/resources/primary_data/";
    private static final String SECONDARY_BASE = "src/test/resources/secondary_data/";

    private final ParserService parserService = new Parser();

    /**
     * Ensures that a valid CSV file is successfully parsed and returns non-empty
     * Secondary Data.
     */
    @Test
    void TestValidCSV() throws InvalidDataException {
        String filePath = "src/main/resources/secondary_data.csv";

        ParsedSecondaryData result = parserService.parseSecondaryData(filePath);

        assertNotNull(result);
        assertNotNull(result.getSecondaryDataString());
        assertFalse(result.getSecondaryDataString().isEmpty());
    }

    /**
     * Ensures that an empty CSV file throws an InvalidDataException.
     */
    @Test
    void TestEmptyCSV() {
        String filePath = SECONDARY_BASE + "empty.csv";

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            parserService.parseSecondaryData(filePath);
        });

        assertEquals("CSV file is empty.", exception.getMessage());
    }

    /**
     * Ensures that a CSV file with invalid headers throws an InvalidDataException.
     */
    @Test
    void TestNoHeaderCSV() {
        String filePath = SECONDARY_BASE + "no_header.csv";

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            parserService.parseSecondaryData(filePath);
        });

        assertEquals("CSV file has invalid headers.", exception.getMessage());
    }

    /**
     * Ensures that a CSV file with missing delimiters (semicolons) throws an
     * InvalidDataException.
     */
    @Test
    void TestMissingSemicolonCSV() {
        String filePath = SECONDARY_BASE + "missing_semicolon.csv";

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            parserService.parseSecondaryData(filePath);
        });

        assertEquals("CSV file has inconsistent delimiters. Expected semicolon (;).", exception.getMessage());
    }

    /**
     * Ensures that a file with an invalid extension (non-CSV) throws an
     * InvalidDataException.
     */
    @Test
    void TestInvalidFormatCSV() {
        String filePath = SECONDARY_BASE + "invalid.txt";

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            parserService.parseSecondaryData(filePath);
        });

        assertEquals("Invalid file type. Only .csv files are supported.", exception.getMessage());
    }

    /**
     * Ensures that a CSV file containing numeric fields with negative values
     * throws an InvalidDataException.
     */
    @Test
    void TestNegativeValuesCSV() {
        String filePath = SECONDARY_BASE + "negative_values.csv";

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            parserService.parseSecondaryData(filePath);
        });

        assertEquals("CSV file contains numeric fields with negative values.", exception.getMessage());
    }

    /**
     * Ensures that a CSV file with inconsistent spacing throws an
     * InvalidDataException.
     */
    @Test
    void TestDoubleSpaceCSV() {
        String filePath = SECONDARY_BASE + "double_space.csv";

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            parserService.parseSecondaryData(filePath);
        });

        assertEquals("CSV file has inconsistent spacing.", exception.getMessage());
    }

    /**
     * Ensures that a CSV file containing null values throws an
     * InvalidDataException.
     */
    @Test
    void TestNullValueCSV() {
        String filePath = SECONDARY_BASE + "null_values.csv";

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            parserService.parseSecondaryData(filePath);
        });

        assertEquals("CSV file contains null values.", exception.getMessage());
    }

    /**
     * Ensures that a CSV file that is too large throws an InvalidDataException.
     */
    @Test
    void TestFileTooLarge() {
        String filePath = SECONDARY_BASE + "large.csv";

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            parserService.parseSecondaryData(filePath);
        });

        assertEquals("CSV file is too large.", exception.getMessage());
    }

    /**
     * Ensures that reading a non-existent CSV file throws an InvalidDataException.
     */
    @Test
    void TestFileNotFound() {
        String filePath = SECONDARY_BASE + "non_existent_file.csv";

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            parserService.parseSecondaryData(filePath);
        });

        assertTrue(exception.getMessage().startsWith("Error reading the CSV file"));
    }

    /**
     * Ensures that a CSV file containing strings in numeric fields throws an
     * InvalidDataException.
     */
    @Test
    void TestStringInNumericFieldCSV() {
        String filePath = SECONDARY_BASE + "string_in_numeric.csv";

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            parserService.parseSecondaryData(filePath);
        });

        assertTrue(exception.getMessage().startsWith("CSV file contains letters in numeric fields:"));
    }

    /**
     * Ensures that a CSV file containing invalid characters throws an
     * InvalidDataException.
     */
    @Test
    void TestInvalidCharactersCSV() {
        String filePath = SECONDARY_BASE + "invalid_characters.csv";

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            parserService.parseSecondaryData(filePath);
        });

        assertTrue(exception.getMessage().startsWith("CSV file contains invalid characters in line:"));
    }

    /**
     * Ensures that a valid CSV file is successfully parsed and returns non-empty
     * Primary Data.
     */
    @Test
    void validFile_whenParsed_thenReturnsParsedPrimaryData() throws InvalidDataException {
        String filePath = PRIMARY_BASE + "PrimaryData_valid.csv";

        ParsedPrimaryData result = parserService.parsePrimaryData(filePath);

        assertNotNull(result);
        assertNotNull(result.getPrimaryDataString());
        assertFalse(result.getPrimaryDataString().isBlank());
    }

    /**
     * Ensures that an CSV file null throws an InvalidDataException.
     */
    @Test
    void nullFilePath_throwsInvalidDataException() {

        InvalidDataException ex = assertThrows(
                InvalidDataException.class,
                () -> parserService.parsePrimaryData(null));

        assertEquals("The file path is invalid or empty.", ex.getMessage());
    }

    /**
     * Ensures that reading a non-existing file throws an InvalidDataException
     */
    @Test
    void invalidExtensionFile_throwsInvalidDataExceptionWithIoCause() {
        String filePath = PRIMARY_BASE + "PrimaryData.txt";

        InvalidDataException ex = assertThrows(
                InvalidDataException.class,
                () -> parserService.parsePrimaryData(filePath));

        assertEquals("Invalid file type. Only .csv files are accepted.", ex.getMessage());
    }

    /**
     * Ensures that a non-existent CSV file throws an InvalidDataException.
     */
    @Test
    void nonExistingCsvFile_throwsInvalidDataException() {
        String filePath = PRIMARY_BASE + "nonExistFile.csv";

        InvalidDataException ex = assertThrows(
                InvalidDataException.class,
                () -> parserService.parsePrimaryData(filePath));

        assertEquals("Error reading the file.", ex.getMessage());
    }

    /**
     * Ensures that an empty CSV file throws an InvalidDataException.
     */
    @Test
    void emptyFile_throwsInvalidDataException() {
        String filePath = PRIMARY_BASE + "PrimaryData_empty.csv";

        InvalidDataException ex = assertThrows(
                InvalidDataException.class,
                () -> parserService.parsePrimaryData(filePath));

        assertEquals("The file is empty.", ex.getMessage());
    }

    /**
     * Ensures that a CSV file with missing headers throws validation failure.
     */
    @Test
    void fileWithoutHeaders_throwsInvalidDataException() {
        String filePath = PRIMARY_BASE + "PrimaryData_noHeaders.csv";

        InvalidDataException ex = assertThrows(
                InvalidDataException.class,
                () -> parserService.parsePrimaryData(filePath));

        assertEquals("Incomplete or invalid headers.", ex.getMessage());
    }

    /**
     * Ensures that a data line without a semicolon is detected as invalid.
     */
    @Test
    void fileWithMissingSemicolon_throwsInvalidDataException() {
        String filePath = PRIMARY_BASE + "PrimaryData_missingSemicolon.csv";

        InvalidDataException ex = assertThrows(
                InvalidDataException.class,
                () -> parserService.parsePrimaryData(filePath));

        assertEquals("Missing semicolon ';' between the data.", ex.getMessage());
    }

    /**
     * Ensures that an CSV file with invalid Chars throws an InvalidDataException.
     */
    @Test
    void fileWithInvalidCharacters_throwsInvalidDataException() {
        String filePath = PRIMARY_BASE + "PrimaryData_invalidChars.csv";

        InvalidDataException ex = assertThrows(
                InvalidDataException.class,
                () -> parserService.parsePrimaryData(filePath));

        assertEquals("The file contains invalid characters.", ex.getMessage());
    }

    /**
     * Ensures that an CSV file with more than 50 lines throws an
     * InvalidDataException.
     */
    @Test
    void csvFileWithMoreThanMaxLines_throwsInvalidDataException() {
        String filePath = PRIMARY_BASE + "PrimaryData_51Lines.csv";

        InvalidDataException ex = assertThrows(
                InvalidDataException.class,
                () -> parserService.parsePrimaryData(filePath));

        assertEquals("CSV file is too large.", ex.getMessage());
    }

}