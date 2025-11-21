package esiih.importer.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import esiih.importer.boundary.ParserService;
import esiih.importer.entity.ParsedPrimaryData;
import esiih.importer.entity.ParsedSecondaryData;
import esiih.importer.exception.InvalidDataException;

/**
 * Parser class to parse CSV files.
 */
public class Parser implements ParserService {

    private static final int MAX_LINES = 1000;
    private static final int MAX_LINES_PRIMARY = 50;
    private ParsedSecondaryData parsedSecondaryData;
    private ParsedPrimaryData parsedPrimaryData;

    /**
     * Parses the Secondary Data CSV file.
     *
     * @param filePath the file path
     * @return the parsed secondary data
     * @throws InvalidDataException if the data is invalid
     */
    @Override
    public ParsedSecondaryData parseSecondaryData(String filePath) throws InvalidDataException {
        if (!filePath.toLowerCase().endsWith(".csv")) {
            throw new InvalidDataException("Invalid file type. Only .csv files are supported.");
        }

        String data;
        try {
            data = readFile(filePath);
        } catch (Exception e) {
            throw new InvalidDataException("Error reading the CSV file: " + e.getMessage(), e);
        }

        if (data.trim().isEmpty()) {
            throw new InvalidDataException("CSV file is empty.");
        }

        String[] lines = data.split("\n");
        if (lines.length > MAX_LINES) {
            throw new InvalidDataException("CSV file is too large.");
        }

        boolean isFirstLine = true;

        int payloadIndex = -1;
        int emptyKmIndex = -1;
        int wttIndex = -1;
        int ttwIndex = -1;
        int totalIndex = -1;
        int averageIndex = -1;

        for (String line : lines) {
            if (line.contains("  ")) {
                throw new InvalidDataException("CSV file has inconsistent spacing.");
            }

            if (line.contains(",")) {
                throw new InvalidDataException("CSV file has inconsistent delimiters. Expected semicolon (;).");
            }

            if (line.contains("null")) {
                throw new InvalidDataException("CSV file contains null values.");
            }

            String[] fields = line.split(";");

            if (!Pattern.matches("[\\w\\s;./Â·+\\u00C0-\\u00FF-]+", line)) {
                throw new InvalidDataException("CSV file contains invalid characters in line: " + line);
            }

            if (isFirstLine) {
                for (int i = 0; i < fields.length; i++) {
                    String header = fields[i].trim();

                    if (header.equalsIgnoreCase("PayloadT")) {
                        payloadIndex = i;
                    } else if (header.equalsIgnoreCase("EmptyKmPct")) {
                        emptyKmIndex = i;
                    } else if (header.equalsIgnoreCase("WttEmissionsKgPerTkm")) {
                        wttIndex = i;
                    } else if (header.equalsIgnoreCase("TtwEmissionsKgPerTkm")) {
                        ttwIndex = i;
                    } else if (header.equalsIgnoreCase("TotalEmissionsKgPerTkm")) {
                        totalIndex = i;
                    } else if (header.equalsIgnoreCase("AverageLitersPer100Tkm")) {
                        averageIndex = i;
                    }
                }

                if (!isValidSecondaryDataHeaders(fields)) {
                    throw new InvalidDataException("CSV file has invalid headers.");
                }
                isFirstLine = false;
                continue;
            }

            for (int i = 0; i < fields.length; i++) {
                String field = fields[i];

                if (i == payloadIndex || i == emptyKmIndex || i == wttIndex ||
                        i == ttwIndex || i == totalIndex || i == averageIndex) {

                    if (field.trim().isEmpty()) {
                        continue;
                    }

                    if (field.matches(".*[a-zA-Z].*")) {
                        throw new InvalidDataException("CSV file contains letters in numeric fields: " + field);
                    }

                    if (field.matches("-?\\d*\\.?\\d+") && Double.parseDouble(field) < 0) {
                        throw new InvalidDataException("CSV file contains numeric fields with negative values.");
                    }
                }
            }
        }

        parsedSecondaryData = new ParsedSecondaryData();
        parsedSecondaryData.setSecondaryDataString(data);
        return parsedSecondaryData;
    }

    @Override
    public ParsedPrimaryData parsePrimaryData(String filePath) throws InvalidDataException {

        if (filePath == null || filePath.isBlank()) {
            throw new InvalidDataException("The file path is invalid or empty.");
        }

        if (!filePath.toLowerCase().endsWith(".csv")) {
            throw new InvalidDataException("Invalid file type. Only .csv files are accepted.");
        }

        String data;
        try {
            data = readFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidDataException("Error reading the file.", e);
        }

        if (data.trim().isEmpty()) {
            throw new InvalidDataException("The file is empty.");
        }

        String[] lines = data.split("\n");
        if (lines.length > MAX_LINES_PRIMARY) {
            throw new InvalidDataException("CSV file is too large.");
        }

        String header = lines[0];
        String[] headerParts = header.split(";");

        if (!isValidPrimaryDataHeaders(headerParts)) {
            throw new InvalidDataException("Incomplete or invalid headers.");
        }

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];

            if (!Pattern.matches("[\\w\\s;,.%()/·+\\u00C0-\\u00FF-]+", line)) {
                throw new InvalidDataException("The file contains invalid characters.");
            }

            if (!line.contains(";")) {
                throw new InvalidDataException("Missing semicolon ';' between the data.");
            }
        }

        parsedPrimaryData = new ParsedPrimaryData();
        parsedPrimaryData.setPrimaryDataString(data);
        return parsedPrimaryData;
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

    /**
     * Checks if the headers are valid for Secondary Data.
     *
     * @param headers the headers
     * @return true if the headers are valid, false otherwise
     */
    private boolean isValidSecondaryDataHeaders(String[] headers) {
        String[] expectedHeaders = {
                "Mode",
                "FuelType",
                "PayloadT",
                "EmptyKmPct",
                "ActivityUnit",
                "WttEmissionsKgPerTkm",
                "TtwEmissionsKgPerTkm",
                "TotalEmissionsKgPerTkm",
                "AverageLitersPer100Tkm"
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
}