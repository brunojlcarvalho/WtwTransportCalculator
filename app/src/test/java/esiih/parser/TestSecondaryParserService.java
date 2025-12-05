package esiih.parser;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import esiih.importer.boundary.SecondaryParserService;
import esiih.importer.controller.SecondaryDataParser;
import esiih.importer.entity.ParsedSecondaryData;
import esiih.importer.exception.InvalidContentException;
import esiih.importer.exception.InvalidDataException;
import esiih.importer.exception.InvalidFileException;

/**
 * Test class for the {@link SecondaryDataParser} class.
 */
public class TestSecondaryParserService {

    private static final String SECONDARY_BASE = "src/test/resources/secondary_data/";
    private SecondaryParserService secondaryParserService;

    @BeforeEach
    void setUp() {
        secondaryParserService = new SecondaryDataParser();
    }

    /**
     * Tests the {@link SecondaryParserService#parseSecondaryData(String)}
     * method. Ensures that a valid CSV file is successfully parsed and returns
     * non-empty Secondary Data.
     *
     * @throws InvalidDataException
     */
    @Test
    void testValidCSV() throws InvalidDataException {
        String filePath = "src/main/resources/secondary_data.csv";

        ParsedSecondaryData result = secondaryParserService.parseSecondaryData(filePath);

        assertNotNull(result);
        assertNotNull(result.getSecondaryDataString());
        assertFalse(result.getSecondaryDataString().isEmpty());
    }

    /**
     * Tests the {@link SecondaryParserService#parseSecondaryData(String)}
     * method. Ensures that an empty CSV file throws an InvalidDataException.
     */
    @Test
    void testEmptyCSV() {
        String filePath = SECONDARY_BASE + "empty.csv";

        assertThrows(InvalidContentException.class,
                () -> {
                    secondaryParserService.parseSecondaryData(filePath);
                });
    }

    /**
     * Tests the {@link SecondaryParserService#parseSecondaryData(String)}
     * method. Ensures that a CSV file with invalid headers throws an
     * InvalidDataException.
     *
     */
    @Test
    void TestInvalidHeadersCSV() {
        String filePath = SECONDARY_BASE + "invalid_headers.csv";

        assertThrows(InvalidContentException.class,
                () -> {
                    secondaryParserService.parseSecondaryData(filePath);
                });
    }

    /**
     * Tests the {@link SecondaryParserService#parseSecondaryData(String)}
     * method. Ensures that a CSV file with missing delimiters (semicolons)
     * throws an InvalidDataException.
     */
    @Test
    void TestMissingSemicolonCSV() {
        String filePath = SECONDARY_BASE + "missing_semicolon.csv";

        assertThrows(InvalidDataException.class,
                () -> {
                    secondaryParserService.parseSecondaryData(filePath);
                });
    }

    /**
     * Tests the {@link SecondaryParserService#parseSecondaryData(String)}
     * method. Ensures that a file with an invalid extension (non-CSV) throws an
     * InvalidDataException.
     */
    @Test
    void TestInvalidFormatCSV() {
        String filePath = SECONDARY_BASE + "invalid.txt";

        assertThrows(InvalidFileException.class,
                () -> {
                    secondaryParserService.parseSecondaryData(filePath);
                });
    }

    /**
     * Tests the {@link SecondaryParserService#parseSecondaryData(String)}
     * method. Ensures that a CSV file containing numeric fields with negative
     * values throws an InvalidDataException.
     */
    @Test
    void TestNegativeValuesCSV() {
        String filePath = SECONDARY_BASE + "negative_values.csv";

        assertThrows(InvalidContentException.class,
                () -> {
                    secondaryParserService.parseSecondaryData(filePath);
                });
    }

    /**
     * Tests the {@link SecondaryParserService#parseSecondaryData(String)}
     * method. Ensures that a CSV file with inconsistent spacing throws an
     * InvalidDataException.
     */
    @Test
    void TestDoubleSpaceCSV() {
        String filePath = SECONDARY_BASE + "double_space.csv";

        assertThrows(InvalidDataException.class,
                () -> {
                    secondaryParserService.parseSecondaryData(filePath);
                });
    }

    /**
     * Tests the {@link SecondaryParserService#parseSecondaryData(String)}
     * method. Ensures that a CSV file containing null values throws an
     * InvalidDataException.
     */
    @Test
    void TestNullValueCSV() {
        String filePath = SECONDARY_BASE + "null_values.csv";

        assertThrows(InvalidDataException.class,
                () -> {
                    secondaryParserService.parseSecondaryData(filePath);
                });
    }

    /**
     * Tests the {@link SecondaryParserService#parseSecondaryData(String)}
     * method. Ensures that a CSV file that is too large throws an
     * InvalidDataException.
     */
    @Test
    void TestFileTooLarge() {
        String filePath = SECONDARY_BASE + "1001lines.csv";

        assertThrows(InvalidContentException.class,
                () -> {
                    secondaryParserService.parseSecondaryData(filePath);
                });
    }

    /**
     * Tests the {@link SecondaryParserService#parseSecondaryData(String)}
     * method. Ensures that a CSV file that is too small throws an
     * InvalidDataException.
     */
    @Test
    void TestFileTooSmall() {
        String filePath = SECONDARY_BASE + "1line.csv";

        assertThrows(InvalidContentException.class,
                () -> {
                    secondaryParserService.parseSecondaryData(filePath);
                });
    }

    /**
     * Tests the {@link SecondaryParserService#parseSecondaryData(String)}
     * method. Ensures that a CSV file with the maximum allowed number of lines
     * is successfully parsed.
     *
     * @throws InvalidDataException
     */
    @Test
    void TestMaxLines() throws InvalidDataException {
        String filePath = SECONDARY_BASE + "1000lines.csv";

        ParsedSecondaryData result = secondaryParserService.parseSecondaryData(filePath);

        assertNotNull(result);
        assertNotNull(result.getSecondaryDataString());
    }

    /**
     * Tests the {@link SecondaryParserService#parseSecondaryData(String)}
     * method. Ensures that a CSV file with the minimum allowed number of lines
     * is successfully parsed.
     *
     * @throws InvalidDataException
     */
    @Test
    void TestMinLines() throws InvalidDataException {
        String filePath = SECONDARY_BASE + "2lines.csv";

        ParsedSecondaryData result = secondaryParserService.parseSecondaryData(filePath);

        assertNotNull(result);
        assertNotNull(result.getSecondaryDataString());
        assertFalse(result.getSecondaryDataString().isEmpty());
    }

    /**
     * Tests the {@link SecondaryParserService#parseSecondaryData(String)}
     * method. Ensures that reading a non-existent CSV file throws an
     * InvalidDataException.
     */
    @Test
    void TestFileNotFound() {
        String filePath = SECONDARY_BASE + "non_existent_file.csv";

        assertThrows(InvalidFileException.class,
                () -> {
                    secondaryParserService.parseSecondaryData(filePath);
                });

    }

    /**
     * Tests the {@link SecondaryParserService#parseSecondaryData(String)}
     * method. Ensures that a CSV file containing strings in numeric fields
     * throws an InvalidDataException.
     */
    @Test
    void TestStringInNumericFieldCSV() {
        String filePath = SECONDARY_BASE + "string_in_numeric.csv";

        assertThrows(InvalidContentException.class,
                () -> {
                    secondaryParserService.parseSecondaryData(filePath);
                });
    }

    /**
     * Tests the {@link SecondaryParserService#parseSecondaryData(String)}
     * method. Ensures that a CSV file containing invalid characters throws an
     * InvalidDataException.
     */
    @Test
    void TestInvalidCharactersCSV() {
        String filePath = SECONDARY_BASE + "invalid_characters.csv";

        assertThrows(InvalidDataException.class,
                () -> {
                    secondaryParserService.parseSecondaryData(filePath);
                });
    }

}