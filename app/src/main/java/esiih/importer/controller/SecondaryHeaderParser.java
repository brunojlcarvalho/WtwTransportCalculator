package esiih.importer.controller;

import java.util.regex.Pattern;

import esiih.importer.exception.InvalidContentException;
import esiih.importer.exception.InvalidDataException;

/**
 * Responsible for validating the headers of the Secondary Data CSV file.
 */
class SecondaryHeaderParser {

    /**
     * Parses and validates the header line of a Secondary Data CSV file.
     *
     * @param headerLine the header line of the CSV file
     * @return an array of indexes indicating the position of required numeric
     * columns
     * @throws InvalidDataException
     */
    int[] parseAndValidateHeader(String headerLine) throws InvalidDataException {

        validateCommonLineProblems(headerLine);

        String[] headerFields = headerLine.split(";");

        int payloadIndex = -1;
        int emptyKmIndex = -1;
        int wttIndex = -1;
        int ttwIndex = -1;
        int totalIndex = -1;
        int averageIndex = -1;

        for (int i = 0; i < headerFields.length; i++) {
            String header = headerFields[i].trim();

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

        if (!isValidSecondaryDataHeaders(headerFields)) {
            throw new InvalidContentException("CSV file has invalid headers.");
        }

        return new int[]{
            payloadIndex,
            emptyKmIndex,
            wttIndex,
            ttwIndex,
            totalIndex,
            averageIndex
        };
    }

    /**
     * Validates a CSV line for common formatting problems that may compromise
     * the parsing of Secondary Data files.
     *
     * @param line the CSV line to validate
     * @throws InvalidDataException if any formatting issues or invalid
     * characters are detected
     */
    private void validateCommonLineProblems(String line) throws InvalidDataException {
        if (line.contains("  ")) {
            throw new InvalidContentException("CSV file has inconsistent spacing.");
        }

        if (line.contains(",")) {
            throw new InvalidContentException("CSV file has inconsistent delimiters. Expected semicolon (;).");
        }

        if (line.contains("null")) {
            throw new InvalidContentException("CSV file contains null values.");
        }

        if (!Pattern.matches("[\\w\\s;./Â·+\\u00C0-\\u00FF-]+", line)) {
            throw new InvalidContentException("CSV file contains invalid characters in line: " + line);
        }
    }

    /**
     * Checks whether the provided array of header fields matches exactly the
     * expected header structure for Secondary Data CSV files.
     *
     * @param headers the array of header fields extracted from the CSV line
     * @return true if the headers match the expected structure exactly; false
     * otherwise
     */
    private boolean isValidSecondaryDataHeaders(String[] headers) {
        final String[] expectedHeaders = {
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
}
