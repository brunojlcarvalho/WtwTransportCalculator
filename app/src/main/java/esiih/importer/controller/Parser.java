package esiih.importer.controller;

import esiih.importer.boundary.ParserService;
import esiih.importer.entity.ParsedPrimaryData;
import esiih.importer.entity.ParsedSecondaryData;
import esiih.importer.exception.InvalidDataException;

/**
 * Parser class to parse CSV files.
 */
public class Parser implements ParserService {

    private final PrimaryDataParser primaryDataParser;
    private final SecondaryDataParser secondaryDataParser;

    public Parser() {
        this(new PrimaryDataParser(), new SecondaryDataParser());
    }

    public Parser(PrimaryDataParser primaryDataParser, SecondaryDataParser secondaryDataParser) {
        this.primaryDataParser = primaryDataParser;
        this.secondaryDataParser = secondaryDataParser;
    }

    /**
     * Parses the Secondary Data CSV file.
     *
     * @param filePath the file path
     * @return the parsed secondary data
     * @throws InvalidDataException if the data is invalid
     */
    @Override
    public ParsedSecondaryData parseSecondaryData(String filePath) throws InvalidDataException {
        return secondaryDataParser.parseSecondaryData(filePath);
    }

    /**
     * Parses the Primary Data CSV file.
     *
     * @param filePath the file path
     * @return the parsed secondary data
     * @throws InvalidDataException if the data is invalid
     */
    @Override
    public ParsedPrimaryData parsePrimaryData(String filePath) throws InvalidDataException {
        return primaryDataParser.parsePrimaryData(filePath);
    }
}
