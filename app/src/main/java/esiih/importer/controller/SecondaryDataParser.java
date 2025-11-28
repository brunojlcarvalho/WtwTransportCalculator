package esiih.importer.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import esiih.importer.entity.ParsedSecondaryData;
import esiih.importer.exception.InvalidContentException;
import esiih.importer.exception.InvalidDataException;
import esiih.importer.exception.InvalidFileException;

/**
 * Parser for Secondary Data CSV files.
 */
public class SecondaryDataParser {

    private static final int MAX_LINES_SECONDARY = 1000;
    private final SecondaryHeaderParser headerParser = new SecondaryHeaderParser();

    /**
     * Parses a Secondary Data CSV file and returns a
     * {@link ParsedSecondaryData} instance containing the raw content of the
     * file.
     *
     * @param filePath the absolute or relative path to the CSV file to parse
     * @return a {@link ParsedSecondaryData} object containing the raw CSV
     * content
     * @throws InvalidDataException if the file containing Exceptions
     */
    public ParsedSecondaryData parseSecondaryData(String filePath) throws InvalidDataException {

        validateFilePath(filePath);

        String data;
        try {
            data = readFile(filePath);
        } catch (IOException e) {
            throw new InvalidFileException("Error reading the CSV file: " + e.getMessage(), e);
        }

        if (data.trim().isEmpty()) {
            throw new InvalidContentException("CSV file is empty.");
        }

        String[] lines = data.split("\n");
        if (lines.length > MAX_LINES_SECONDARY) {
            throw new InvalidContentException("CSV file is too large.");
        }

        String headerLine = lines[0];

        int[] numericIndexes = headerParser.parseAndValidateHeader(headerLine);

        validateDataLines(lines, numericIndexes);

        ParsedSecondaryData parsedSecondaryData = new ParsedSecondaryData();
        parsedSecondaryData.setSecondaryDataString(data);
        return parsedSecondaryData;
    }

    /**
     * Validates the provided file path.
     *
     * @param filePath the file path to validate
     * @throws InvalidDataException if the file path is empty, null, or not a
     * CSV file
     */
    private void validateFilePath(String filePath) throws InvalidDataException {
        if (!filePath.toLowerCase().endsWith(".csv")) {
            throw new InvalidFileException("Invalid file type. Only .csv files are supported.");
        }
    }

    /**
     * Validates the data lines of the CSV file.
     *
     * @param lines the array of CSV lines
     * @param numericIndexes the indexes of fields that should be numeric
     * @throws InvalidDataException if any data line contains invalid data
     */
    private void validateDataLines(String[] lines, int[] numericIndexes) throws InvalidDataException {

        for (int lineNumber = 1; lineNumber < lines.length; lineNumber++) {
            String line = lines[lineNumber];

            validateCommonLineProblems(line);

            String[] fields = line.split(";");

            validateNumericFields(fields, numericIndexes);
        }
    }

    /**
     * Validates a CSV line for common formatting problems that may compromise
     * the parsing of Secondary Data files.
     *
     * @param line the CSV line to validate
     * @throws InvalidDataException if any formatting issues or invalid
     * characters are detected
     */
    private void validateCommonLineProblems(String line)
            throws InvalidDataException {

        if (line.contains("  ")) {
            throw new InvalidDataException("CSV file has inconsistent spacing.");
        }

        if (line.contains(",")) {
            throw new InvalidDataException("CSV file has inconsistent delimiters. Expected semicolon (;).");
        }

        if (line.contains("null")) {
            throw new InvalidDataException("CSV file contains null values.");
        }

        if (!Pattern.matches("[\\w\\s;./Â·+\\u00C0-\\u00FF-]+", line)) {
            throw new InvalidDataException("CSV file contains invalid characters in line: " + line);
        }

    }

    /**
     * Validates numeric fields to ensure they contain valid non-negative
     * numbers.
     *
     * @param fields the array of field strings from a CSV line
     * @param numericIndexes the indexes of fields that should be numeric
     * @throws InvalidDataException if any numeric field contains invalid data
     */
    private void validateNumericFields(String[] fields, int[] numericIndexes) throws InvalidDataException {

        for (int index : numericIndexes) {
            if (index < 0 || index >= fields.length) {
                continue;
            }

            String field = fields[index];

            if (field.trim().isEmpty()) {
                continue;
            }

            if (field.matches(".*[a-zA-Z].*")) {
                throw new InvalidContentException("CSV file contains letters in numeric fields: " + field);
            }

            if (field.matches("-?\\d*\\.?\\d+")) {
                double value = Double.parseDouble(field);
                if (value < 0) {
                    throw new InvalidContentException("CSV file contains numeric fields with negative values.");
                }

            }
        }
    }

    /**
     * Reads the file using UTF-8 encoding.
     *
     * @param filePath the file path
     * @return the file data
     */
    private String readFile(String filePath) throws IOException {
        StringBuilder data = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.append(line).append("\n");
            }
        }
        if (data.length() > 0) {
            data.setLength(data.length() - 1);
        }
        return data.toString();
    }
}
