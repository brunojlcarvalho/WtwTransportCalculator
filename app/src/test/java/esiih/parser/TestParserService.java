package esiih.parser;

import esiih.importer.boundary.ParserService;
import esiih.importer.controller.Parser;
import esiih.importer.entity.ParsedPrimaryData;
import esiih.importer.entity.ParsedSecondaryData;
import esiih.importer.exception.InvalidContentException;
import esiih.importer.exception.InvalidDataException;
import esiih.importer.exception.InvalidFileException;
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

        assertThrows(InvalidContentException.class,
                () -> {parserService.parseSecondaryData(filePath);}
        );
    }

    /**
     * Ensures that a CSV file with invalid headers throws an InvalidDataException.
     */
    @Test
    void TestNoHeaderCSV() {
        String filePath = SECONDARY_BASE + "no_header.csv";

        assertThrows(InvalidContentException.class,
                () -> {parserService.parseSecondaryData(filePath);}
        );
    }

    /**
     * Ensures that a CSV file with missing delimiters (semicolons) throws an
     * InvalidDataException.
     */
    @Test
    void TestMissingSemicolonCSV() {
        String filePath = SECONDARY_BASE + "missing_semicolon.csv";

        assertThrows(InvalidDataException.class,
                () -> {parserService.parseSecondaryData(filePath);}
        );
    }

    /**
     * Ensures that a file with an invalid extension (non-CSV) throws an
     * InvalidDataException.
     */
    @Test
    void TestInvalidFormatCSV() {
        String filePath = SECONDARY_BASE + "invalid.txt";

        assertThrows(InvalidFileException.class,
                () -> {parserService.parseSecondaryData(filePath);}
        );
    }

    /**
     * Ensures that a CSV file containing numeric fields with negative values
     * throws an InvalidDataException.
     */
    @Test
    void TestNegativeValuesCSV() {
        String filePath = SECONDARY_BASE + "negative_values.csv";

        assertThrows(InvalidContentException.class,
                () -> {parserService.parseSecondaryData(filePath);}
        );
    }

    /**
     * Ensures that a CSV file with inconsistent spacing throws an
     * InvalidDataException.
     */
    @Test
    void TestDoubleSpaceCSV() {
        String filePath = SECONDARY_BASE + "double_space.csv";

        assertThrows(InvalidDataException.class,
                () -> {parserService.parseSecondaryData(filePath);}
        );
    }

    /**
     * Ensures that a CSV file containing null values throws an
     * InvalidDataException.
     */
    @Test
    void TestNullValueCSV() {
        String filePath = SECONDARY_BASE + "null_values.csv";

        assertThrows(InvalidDataException.class,
                () -> {parserService.parseSecondaryData(filePath);}
        );
    }

    /**
     * Ensures that a CSV file that is too large throws an InvalidDataException.
     */
    @Test
    void TestFileTooLarge() {
        String filePath = SECONDARY_BASE + "large.csv";

        assertThrows(InvalidContentException.class,
                () -> { parserService.parseSecondaryData(filePath);}
        );
    }

    /**
     * Ensures that reading a non-existent CSV file throws an InvalidDataException.
     */
    @Test
    void TestFileNotFound() {
        String filePath = SECONDARY_BASE + "non_existent_file.csv";

        assertThrows(InvalidFileException.class,
                () -> {parserService.parseSecondaryData(filePath);}
        );

    }

    /**
     * Ensures that a CSV file containing strings in numeric fields throws an
     * InvalidDataException.
     */
    @Test
    void TestStringInNumericFieldCSV() {
        String filePath = SECONDARY_BASE + "string_in_numeric.csv";

        assertThrows(InvalidContentException.class,
                () -> {parserService.parseSecondaryData(filePath);}
        );
    }

    /**
     * Ensures that a CSV file containing invalid characters throws an
     * InvalidDataException.
     */
    @Test
    void TestInvalidCharactersCSV() {
        String filePath = SECONDARY_BASE + "invalid_characters.csv";

        assertThrows(InvalidDataException.class,
                () -> {parserService.parseSecondaryData(filePath);}
        );
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
    void nullFilePath_throwsInvalidFileException() {
        assertThrows(InvalidFileException.class,
                () -> parserService.parsePrimaryData(null));
    }

    /**
     * Ensures that reading a non-existing file throws an InvalidDataException
     */
    @Test
    void invalidExtensionFile_throwsInvalidFileException() {
        String filePath = PRIMARY_BASE + "PrimaryData.txt";

        assertThrows(InvalidFileException.class,
                () -> parserService.parsePrimaryData(filePath));
    }

    /**
     * Ensures that a non-existent CSV file throws an InvalidDataException.
     */
    @Test
    void nonExistingCsvFile_throwsInvalidFileException() {
        String filePath = PRIMARY_BASE + "nonExistFile.csv";

        assertThrows(InvalidFileException.class,
                () -> parserService.parsePrimaryData(filePath));
    }

    /**
     * Ensures that an empty CSV file throws an InvalidDataException.
     */
    @Test
    void emptyFile_throwsInvalidContentException() {
        String filePath = PRIMARY_BASE + "PrimaryData_empty.csv";

        assertThrows(InvalidContentException.class,
                () -> parserService.parsePrimaryData(filePath));
    }

    /**
     * Ensures that a CSV file with missing headers throws validation failure.
     */
    @Test
    void fileWithoutHeaders_throwsInvalidContentException() {
        String filePath = PRIMARY_BASE + "PrimaryData_noHeaders.csv";

        assertThrows(InvalidContentException.class,
                () -> parserService.parsePrimaryData(filePath));
    }

    /**
     * Ensures that a data line without a semicolon is detected as invalid.
     */
    @Test
    void fileWithMissingSemicolon_throwsInvalidContentException() {
        String filePath = PRIMARY_BASE + "PrimaryData_missingSemicolon.csv";

        assertThrows(InvalidContentException.class,
                () -> parserService.parsePrimaryData(filePath));
    }

    /**
     * Ensures that an CSV file with invalid Chars throws an InvalidDataException.
     */
    @Test
    void fileWithInvalidCharacters_throwsInvalidContentException() {
        String filePath = PRIMARY_BASE + "PrimaryData_invalidChars.csv";

        assertThrows(InvalidContentException.class,
                () -> parserService.parsePrimaryData(filePath));
    }

    /**
     * Ensures that an CSV file with more than 50 lines throws an
     * InvalidDataException.
     */
    @Test
    void csvFileWithMoreThanMaxLines_throwsInvalidContentException() {
        String filePath = PRIMARY_BASE + "PrimaryData_51Lines.csv";

        assertThrows(InvalidContentException.class,
                () -> parserService.parsePrimaryData(filePath));
    }
}