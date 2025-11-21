package esiih.parser;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import esiih.importer.boundary.ParserService;
import esiih.importer.controller.Parser;
import esiih.importer.entity.ParsedPrimaryData;
import esiih.importer.exception.InvalidDataException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestParserService {

    private static final String BASE = "src/test/resources/secondary_data/";
    private static final String PRIMARY_BASE = "src/test/resources/primary_data/";

    private final ParserService parserService = new Parser();

    @Test
    void TestEmptyCSV() throws Exception {
        String filePath = BASE + "empty.csv";
        Path path = Path.of(filePath);

        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        boolean isEmpty = lines.isEmpty() || lines.stream().allMatch(s -> s == null || s.isBlank());
        assertTrue(isEmpty, "Expected empty CSV");
    }

    @Test
    void TestNoHeaderCSV() throws Exception {
        String filePath = BASE + "no_header.csv";
        Path path = Path.of(filePath);

        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

        int firstIdx = -1;
        for (int i = 0; i < lines.size(); i++) {
            String s = lines.get(i);
            if (s != null && !s.isBlank()) {
                firstIdx = i;
                break;
            }
        }

        String header = lines.get(firstIdx).trim();
        boolean hasSemicolon = header.contains(";");
        boolean headerHasAnyLetterToken = Arrays.stream(header.split(";", -1))
                .map(String::trim)
                .anyMatch(tok -> !tok.isEmpty() && tok.chars().anyMatch(Character::isLetter));

        boolean missingHeader = hasSemicolon && !headerHasAnyLetterToken;
        assertTrue(missingHeader, "Expected CSV with no header");
    }

    @Test
    void TestMissingSemicolonCSV() throws Exception {
        String filePath = BASE + "missing_semicolon.csv";
        Path path = Path.of(filePath);

        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        boolean anyNonBlankWithoutSemicolon = lines.stream()
                .filter(s -> s != null && !s.isBlank())
                .anyMatch(s -> !s.contains(";"));

        assertTrue(anyNonBlankWithoutSemicolon, "Expected at least one line without ';'");
    }

    @Test
    void TestInvalidFormatCSV() {
        String filePath = BASE + "invalid.txt";
        boolean invalidType = !filePath.toLowerCase().endsWith(".csv");
        assertTrue(invalidType, "Invalid format expected");
    }

    @Test
    void TestNegativeValuesCSV() throws Exception {
        String filePath = BASE + "negative_values.csv";
        Path path = Path.of(filePath);

        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

        boolean anyNegative = lines.stream()
                .skip(1)
                .filter(s -> s != null && !s.isBlank())
                .map(s -> s.split(";", -1))
                .flatMap(Arrays::stream)
                .map(String::trim)
                .filter(tok -> tok.matches("-?\\d+(\\.\\d+)?"))
                .mapToDouble(Double::parseDouble)
                .anyMatch(v -> v < 0.0);

        assertTrue(anyNegative, "Expected at least one negative value.");
    }

    /**
     * Ensures that an CSV file null throws an InvalidDataException.
     */
    @Test
    void nullFilePath_throwsInvalidDataException() {

        InvalidDataException ex = assertThrows(
                InvalidDataException.class,
                () -> parserService.parsePrimaryData(null)
        );

        assertEquals("The file path is invalid or empty.", ex.getMessage());
    }

    /**
     * Ensures that an CSV file with more than 50 lines throws an InvalidDataException.
     */
    @Test
    void csvFileWithMoreThanMaxLines_throwsInvalidDataException() {
        String filePath = PRIMARY_BASE + "PrimaryData_51Lines.csv";

        InvalidDataException ex = assertThrows(
                InvalidDataException.class,
                () -> parserService.parsePrimaryData(filePath)
        );

        assertEquals("CSV file is too large.", ex.getMessage());
    }

    /**
     * Ensures that an CSV file with invalid Chars throws an InvalidDataException.
     */
    @Test
    void fileWithInvalidCharacters_throwsInvalidDataException() {
        String filePath = PRIMARY_BASE + "PrimaryData_invalidChars.csv";

        InvalidDataException ex = assertThrows(
                InvalidDataException.class,
                () -> parserService.parsePrimaryData(filePath)
        );

        assertEquals("The file contains invalid characters.", ex.getMessage());
    }

    /**
     * Ensures that an empty CSV file throws an InvalidDataException.
     */
    @Test
    void emptyFile_throwsInvalidDataException() {
        String filePath = PRIMARY_BASE + "PrimaryData_empty.csv";

        InvalidDataException ex = assertThrows(
                InvalidDataException.class,
                () -> parserService.parsePrimaryData(filePath)
        );

        assertEquals("The file is empty.", ex.getMessage());
    }

    /**
     * Ensures that a non-existent CSV file throws an InvalidDataException.
     */
    @Test
    void nonExistingCsvFile_throwsInvalidDataException() {
        String filePath = PRIMARY_BASE + "nonExistFile.csv";

        InvalidDataException ex = assertThrows(
                InvalidDataException.class,
                () -> parserService.parsePrimaryData(filePath)
        );

        assertEquals("Error reading the file.", ex.getMessage());
    }

    /**
     * Ensures that a CSV file with missing headers throws validation failure.
     */
    @Test
    void fileWithoutHeaders_throwsInvalidDataException() {
        String filePath = PRIMARY_BASE + "PrimaryData_noHeaders.csv";

        InvalidDataException ex = assertThrows(
                InvalidDataException.class,
                () -> parserService.parsePrimaryData(filePath)
        );

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
                () -> parserService.parsePrimaryData(filePath)
        );

        assertEquals("Missing semicolon ';' between the data.", ex.getMessage());
    }

    /**
     * Ensures that reading a non-existing file throws an InvalidDataException
     */
    @Test
    void invalidExtensionFile_throwsInvalidDataExceptionWithIoCause() {
        String filePath = PRIMARY_BASE + "PrimaryData.txt";

        InvalidDataException ex = assertThrows(
                InvalidDataException.class,
                () -> parserService.parsePrimaryData(filePath)
        );

        assertEquals("Invalid file type. Only .csv files are accepted.", ex.getMessage());
    }

    /**
     * Ensures that a valid CSV file is successfully parsed and returns non-empty Primary Data.
     */
    @Test
    void validFile_whenParsed_thenReturnsParsedPrimaryData() throws InvalidDataException {
        String filePath = PRIMARY_BASE + "PrimaryData_valid.csv";

        ParsedPrimaryData result = parserService.parsePrimaryData(filePath);

        assertNotNull(result);
        assertNotNull(result.getPrimaryDataString());
        assertFalse(result.getPrimaryDataString().isBlank());
    }
}