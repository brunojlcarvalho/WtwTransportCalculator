package esiih.importer.boundary;

import esiih.importer.entity.ParsedPrimaryData;
import esiih.importer.exception.InvalidDataException;

/**
 * Importer Primary CSV service interface.
 */
public interface PrimaryParserService {

    public ParsedPrimaryData parsePrimaryData(String filePath) throws InvalidDataException;
}
