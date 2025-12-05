package esiih.parser;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import esiih.importer.boundary.PrimaryParserService;
import esiih.importer.boundary.SecondaryParserService;
import esiih.importer.controller.PrimaryDataParser;
import esiih.importer.entity.ParsedPrimaryData;
import esiih.importer.exception.InvalidContentException;
import esiih.importer.exception.InvalidDataException;
import esiih.importer.exception.InvalidFileException;

/**
 * Test class for the {@link PrimaryDataParser} class.
 */
public class TestPrimaryParserService {

    private static final String PRIMARY_BASE = "src/test/resources/primary_data/";
    private PrimaryParserService primaryParserService;

    @BeforeEach
    void setUp() {
        primaryParserService = new PrimaryDataParser();
    }
    
    /**
     * Tests the {@link SecondaryParserService#parsePrimaryData(String)} method.
     * Ensures that a valid CSV file is successfully parsed and returns
     * non-empty Primary Data.
     */
    @Test
    void validFile_whenParsed_thenReturnsParsedPrimaryData() throws InvalidDataException {
        String filePath = PRIMARY_BASE + "PrimaryData_valid.csv";

        ParsedPrimaryData result = primaryParserService.parsePrimaryData(filePath);

        assertNotNull(result);
        assertNotNull(result.getPrimaryDataString());
        assertFalse(result.getPrimaryDataString().isBlank());
    }

    /**
     * Tests the {@link SecondaryParserService#parsePrimaryData(String)} method.
     * Ensures that an CSV file null throws an InvalidDataException.
     */
    @Test
    void nullFilePath_throwsInvalidFileException() {
        assertThrows(InvalidFileException.class,
                () -> primaryParserService.parsePrimaryData(null));
    }

    /**
     * Tests the {@link SecondaryParserService#parsePrimaryData(String)} method.
     * Ensures that reading a non-existing file throws an InvalidDataException
     */
    @Test
    void invalidExtensionFile_throwsInvalidFileException() {
        String filePath = PRIMARY_BASE + "PrimaryData.txt";

        assertThrows(InvalidFileException.class,
                () -> primaryParserService.parsePrimaryData(filePath));
    }

    /**
     * Tests the {@link SecondaryParserService#parsePrimaryData(String)} method.
     * Ensures that a non-existent CSV file throws an InvalidDataException.
     */
    @Test
    void nonExistingCsvFile_throwsInvalidFileException() {
        String filePath = PRIMARY_BASE + "nonExistFile.csv";

        assertThrows(InvalidFileException.class,
                () -> primaryParserService.parsePrimaryData(filePath));
    }

    /**
     * Tests the {@link SecondaryParserService#parsePrimaryData(String)} method.
     * Ensures that an empty CSV file throws an InvalidDataException.
     */
    @Test
    void emptyFile_throwsInvalidContentException() {
        String filePath = PRIMARY_BASE + "PrimaryData_empty.csv";

        assertThrows(InvalidContentException.class,
                () -> primaryParserService.parsePrimaryData(filePath));
    }

    /**
     * Tests the {@link SecondaryParserService#parsePrimaryData(String)} method.
     * Ensures that a CSV file with missing headers throws validation failure.
     */
    @Test
    void fileWithoutHeaders_throwsInvalidContentException() {
        String filePath = PRIMARY_BASE + "PrimaryData_noHeaders.csv";

        assertThrows(InvalidContentException.class,
                () -> primaryParserService.parsePrimaryData(filePath));
    }

    /**
     * Tests the {@link SecondaryParserService#parsePrimaryData(String)} method.
     * Ensures that a data line without a semicolon is detected as invalid.
     */
    @Test
    void fileWithMissingSemicolon_throwsInvalidContentException() {
        String filePath = PRIMARY_BASE + "PrimaryData_missingSemicolon.csv";

        assertThrows(InvalidContentException.class,
                () -> primaryParserService.parsePrimaryData(filePath));
    }

    /**
     * Tests the {@link SecondaryParserService#parsePrimaryData(String)} method.
     * Ensures that an CSV file with invalid Chars throws an
     * InvalidDataException.
     */
    @Test
    void fileWithInvalidCharacters_throwsInvalidContentException() {
        String filePath = PRIMARY_BASE + "PrimaryData_invalidChars.csv";

        assertThrows(InvalidContentException.class,
                () -> primaryParserService.parsePrimaryData(filePath));
    }

    /**
     * Tests the {@link SecondaryParserService#parsePrimaryData(String)} method.
     * Ensures that an CSV file with more than 50 lines throws an
     * InvalidDataException.
     */
    @Test
    void csvFileWithMoreThanMaxLines_throwsInvalidContentException() {
        String filePath = PRIMARY_BASE + "PrimaryData_51Lines.csv";

        assertThrows(InvalidContentException.class,
                () -> primaryParserService.parsePrimaryData(filePath));
    }

    /**
     * Tests the {@link SecondaryParserService#parsePrimaryData(String)} method.
     * Ensures that an CSV file with exactly 50 lines is successfully parsed
     * and returns non-empty Primary Data.
     */
    @Test
    void csvFileWithMaxLinesReturnsParsedPrimaryData() throws InvalidDataException {
        String filePath = PRIMARY_BASE + "PrimaryData_50Lines.csv";

        ParsedPrimaryData result = primaryParserService.parsePrimaryData(filePath);

        assertNotNull(result);
        assertNotNull(result.getPrimaryDataString());
        assertFalse(result.getPrimaryDataString().isBlank());
    }

    /**
     * Tests the {@link SecondaryParserService#parsePrimaryData(String)} method.
     * Ensures that an CSV file with exactly 2 lines is successfully parsed
     * and returns non-empty Primary Data.
     */
    @Test
    void csvFileWithMinLinesReturnsParsedPrimaryData() throws InvalidDataException {
        String filePath = PRIMARY_BASE + "PrimaryData_2Lines.csv";

        ParsedPrimaryData result = primaryParserService.parsePrimaryData(filePath);

        assertNotNull(result);
        assertNotNull(result.getPrimaryDataString());
        assertFalse(result.getPrimaryDataString().isBlank());
    }

    /**
     * Tests the {@link SecondaryParserService#parsePrimaryData(String)} method.
     * Ensures that an CSV file with less than 2 lines throws an
     * InvalidDataException.
     */
    @Test
    void csvFileWithLessThanMinLines_throwsInvalidContentException() {
        String filePath = PRIMARY_BASE + "PrimaryData_1Line.csv";

        assertThrows(InvalidContentException.class,
                () -> primaryParserService.parsePrimaryData(filePath));
    }
}