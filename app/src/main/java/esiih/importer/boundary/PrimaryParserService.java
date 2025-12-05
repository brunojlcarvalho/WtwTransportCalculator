package esiih.importer.boundary;

import esiih.importer.entity.ParsedPrimaryData;
import esiih.importer.exception.InvalidDataException;

/**
 * Importer Primary CSV service interface.
 */
public interface PrimaryParserService {

    /**
     * Parses primary data from a CSV file.
     * 
     * @param filePath The path to the primary data CSV file.
     * @return ParsedPrimaryData object containing the parsed data.
     * @throws InvalidDataException If parsing cannot be performed.
     */
    public ParsedPrimaryData parsePrimaryData(String filePath) throws InvalidDataException;
}
