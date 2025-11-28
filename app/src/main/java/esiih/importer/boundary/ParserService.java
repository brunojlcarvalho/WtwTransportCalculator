package esiih.importer.boundary;

import esiih.importer.entity.ParsedPrimaryData;
import esiih.importer.entity.ParsedSecondaryData;
import esiih.importer.exception.InvalidDataException;

/**
 * Importer CSV service interface.
 */
public interface ParserService {

    public ParsedSecondaryData parseSecondaryData(String filePath) throws InvalidDataException;

    public ParsedPrimaryData parsePrimaryData(String filePath) throws InvalidDataException;

}
