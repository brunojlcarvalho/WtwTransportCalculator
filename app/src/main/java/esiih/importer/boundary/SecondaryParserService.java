package esiih.importer.boundary;

import esiih.importer.entity.ParsedSecondaryData;
import esiih.importer.exception.InvalidDataException;

/**
 * Importer Secondary CSV service interface.
 */
public interface SecondaryParserService {

    /**
     * Parses secondary data from a CSV file.
     * 
     * @param filePath The path to the secondary data CSV file.
     * @return ParsedSecondaryData object containing the parsed data.
     * @throws InvalidDataException If parsing cannot be performed.
     */
    public ParsedSecondaryData parseSecondaryData(String filePath) throws InvalidDataException;

}
