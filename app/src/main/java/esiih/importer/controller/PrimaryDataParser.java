package esiih.importer.controller;

import esiih.importer.entity.*;
import esiih.importer.exception.InvalidContentException;
import esiih.importer.exception.InvalidDataException;
import esiih.importer.exception.InvalidFileException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class PrimaryDataParser {
    private static final int MAX_LINES_PRIMARY = 50;

    /**
     * Parses a Primary Data CSV file and returns a {@link ParsedPrimaryData} instance
     * containing the raw content of the file.
     *
     * @param filePath the path to the CSV file to parse
     * @return a {@link ParsedPrimaryData} object containing the raw file data
     * @throws InvalidDataException if the file path, structure, or contents are invalid
     * @throws InvalidFileException if reading the file fails
     */
    public ParsedPrimaryData parsePrimaryData(String filePath) throws InvalidDataException {
        validateFilePath(filePath);

        String data;

        try {
            data = readFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidFileException("Error reading the file.", e);
        }

        String[] lines = data.split("\n");
        validateStructure(lines);

        validateLines(lines);

        ParsedPrimaryData parsedPrimaryData = new ParsedPrimaryData();
        parsedPrimaryData.setPrimaryDataString(data);
        return parsedPrimaryData;
    }

    /**
     * Validates the provided file path.
     *
     * @param filePath the file path to validate
     * @throws InvalidFileException if the file path is empty, null, or not a CSV file
     */
    private void validateFilePath(String filePath) throws InvalidFileException {
        if (filePath == null || filePath.isBlank()) {
            throw new InvalidFileException("The file path is invalid or empty.");
        }

        if (!filePath.toLowerCase().endsWith(".csv")) {
            throw new InvalidFileException("Invalid file type. Only .csv files are accepted.");
        }
    }

    /**
     * Validates the structural integrity of the CSV file.
     *
     * @param lines all lines of the CSV file
     * @throws InvalidContentException if the structure is incomplete, empty,
     *                                 contains too many lines, or has invalid headers
     */
    private void validateStructure(String[] lines) throws InvalidContentException {
        if (lines.length == 0 || lines[0].trim().isEmpty()) {
            throw new InvalidContentException("The file is empty.");
        }

        if (lines.length > MAX_LINES_PRIMARY) {
            throw new InvalidContentException("CSV file is too large.");
        }

        String header = lines[0];
        String[] headerParts = header.split(";");

        if (!isValidPrimaryDataHeaders(headerParts)) {
            throw new InvalidContentException("Incomplete or invalid headers.");
        }
    }

    /**
     * Validates each line of the CSV file after the header.
     *
     * @param lines all lines of the CSV file
     * @throws InvalidContentException if a line contains invalid characters or lacks delimiters
     */
    private void validateLines(String[] lines) throws InvalidContentException {
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];

            if (!Pattern.matches("[\\w\\s;,.%()/·+\\u00C0-\\u00FF-]+", line)) {
                throw new InvalidContentException("The file contains invalid characters.");
            }

            if (!line.contains(";")) {
                throw new InvalidContentException("Missing semicolon ';' between the data.");
            }
        }
    }

    /**
     * Checks if the headers are valid for Primary Data.
     *
     * @param headers the headers
     * @return true if the headers are valid, false otherwise
     */
    private boolean isValidPrimaryDataHeaders(String[] headers) {
        String[] expectedHeaders = {
                "Etapa", "Tipo Operacao", "Veiculo Transporte", "Modo Transaporte",
                "Distancia Carregado (km)", "Distancia Vazio (km)", "Carga Transportada (t)",
                "Carga Maxima (t)", "Tipo Combustivel", "Tempo (h)", "Litros Consumidos (l)",
                "Consumos (l/100tkm)", "Energia(kWh)"
        };

        if (headers.length != expectedHeaders.length) {
            return false;
        }
        for (int i = 0; i < headers.length; i++) {
            if (!headers[i].trim().equalsIgnoreCase(expectedHeaders[i])) {
                return false;
            }
        }
        return true;
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
