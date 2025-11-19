package esiih.importer.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

import esiih.importer.boundary.ParserService;
import esiih.importer.entity.ParsedPrimaryData;
import esiih.importer.entity.ParsedSecondaryData;
import esiih.importer.exception.InvalidDataException;

/**
 * Parser class to parse CSV files for Secondary Data.
 */
public class Parser implements ParserService {

    private static final int MAX_LINES = 1000;
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

        // Variáveis para armazenar os índices dinamicamente
        int payloadIndex = -1;
        int emptyKmIndex = -1;
        int wttIndex = -1;
        int ttwIndex = -1;
        int totalIndex = -1;
        int averageIndex = -1;

        for (String line : lines) {
            // Validações estruturais
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

            if (!Pattern.matches("[\\w\\s;,.%()/·+\\u00C0-\\u00FF-]+", line)) {
                throw new InvalidDataException("CSV file contains invalid characters in line: " + line);
            }

            if (isFirstLine) {
                // Identificação dinâmica das colunas
                for (int i = 0; i < fields.length; i++) {
                    String header = fields[i].trim();

                    if (header.equalsIgnoreCase("Payload_t")) {
                        payloadIndex = i;
                    } else if (header.equalsIgnoreCase("Empty_km_pct")) {
                        emptyKmIndex = i;
                    } else if (header.equalsIgnoreCase("WTT_Emissions_kg_per_tkm")) {
                        wttIndex = i;
                    } else if (header.equalsIgnoreCase("TTW_Emissions_kg_per_tkm")) {
                        ttwIndex = i;
                    } else if (header.equalsIgnoreCase("Total_Emissions_kg_per_tkm")) {
                        totalIndex = i;
                    } else if (header.equalsIgnoreCase("Average (liters / 100 tkm)")) {
                        averageIndex = i;
                    }
                }

                if (!isValidSecondaryDataHeaders(fields)) {
                    throw new InvalidDataException("CSV file has invalid headers.");
                }
                isFirstLine = false;
                continue;
            }

            // Validação numérica
            for (int i = 0; i < fields.length; i++) {
                String field = fields[i];

                if (i == payloadIndex || i == emptyKmIndex || i == wttIndex ||
                        i == ttwIndex || i == totalIndex || i == averageIndex) {

                    // NOVO: Permite campos vazios nas colunas numéricas (ex: Payload vazio para
                    // navios)
                    if (field.trim().isEmpty()) {
                        continue;
                    }

                    if (field.matches(".*[a-zA-Z].*")) {
                        throw new InvalidDataException("CSV file contains letters in numeric fields: " + field);
                    }

                    try {
                        double value = Double.parseDouble(field.replace(",", "."));
                        if (value < 0) {
                            throw new InvalidDataException("CSV file contains numeric fields with negative values.");
                        }
                    } catch (NumberFormatException e) {
                        throw new InvalidDataException("Invalid number format in numeric field: " + field);
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
            //lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.ISO_8859_1);
        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidDataException("Error reading the file.", e);
        }

        // 2) Empty file
        if (data.trim().isEmpty()) {
            throw new InvalidDataException("The file is empty.");
        }

        // 3) Validate headers
        String[] lines = data.split("\n");
        String header = lines[0];
        String[] headerParts = header.split(";");

        if (!isValidPrimaryDataHeaders(headerParts)) {
            throw new InvalidDataException("Incomplete or invalid headers.");
        }

        // 4) Validate data lines
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
                "Mode", "Fuel_Type", "Payload_t", "Empty_km_pct", "Activity_Unit",
                "WTT_Emissions_kg_per_tkm", "TTW_Emissions_kg_per_tkm",
                "Total_Emissions_kg_per_tkm", "Average (liters / 100 tkm)"
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
                "Etapa","Tipo Operacao","Veiculo Transporte","Modo Transaporte",
                "Distancia Carregado (km)","Distancia Vazio (km)","Carga Transportada (t)",
                "Carga Maxima (t)","Tipo Combustivel","Tempo (h)","Litros Consumidos (l)",
                "Consumos (l/100tkm)","Energia(kWh)"
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