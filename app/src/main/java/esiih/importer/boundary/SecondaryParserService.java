package esiih.importer.boundary;

import esiih.importer.entity.ParsedSecondaryData;
import esiih.importer.exception.InvalidDataException;

/**
 * Importer Secondary CSV service interface.
 */
public interface SecondaryParserService {

    public ParsedSecondaryData parseSecondaryData(String filePath) throws InvalidDataException;

}
