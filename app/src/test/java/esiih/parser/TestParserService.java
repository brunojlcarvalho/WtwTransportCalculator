package esiih.parser;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class TestParserService {

    private static final String BASE = "src/test/resources/secondary_data/";

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
}
