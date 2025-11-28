package esiih;

import esiih.importer.boundary.PrimaryParserService;
import esiih.importer.boundary.SecondaryParserService;
import esiih.importer.controller.PrimaryDataParser;
import esiih.importer.controller.SecondaryDataParser;
import esiih.importer.entity.ParsedPrimaryData;
import esiih.importer.entity.ParsedSecondaryData;
import esiih.importer.exception.InvalidDataException;

public class App {

    /**
     * Main method to run the application
     */
    public static void main(String[] args) {
        String secondaryDataFilePath = "app/src/main/resources/secondary_data.csv";
        String primaryDataFilePath = "app/src/main/resources/primary_data.csv";

        try {
            SecondaryParserService secondaryParserService = new SecondaryDataParser();
            ParsedSecondaryData parsedSecondaryData = secondaryParserService.parseSecondaryData(secondaryDataFilePath);
            PrimaryParserService primaryParserService = new PrimaryDataParser();
            ParsedPrimaryData parsedPrimaryData = primaryParserService.parsePrimaryData(primaryDataFilePath);

            printSecondaryData(parsedSecondaryData);
            System.out.println("###############################################################################");
            printPrimaryData(parsedPrimaryData);

        } catch (InvalidDataException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printSecondaryData(ParsedSecondaryData parsedSecondaryData) {
        System.out.println("Secondary Data String:");
        System.out.println(parsedSecondaryData.getSecondaryDataString());
    }

    private static void printPrimaryData(ParsedPrimaryData parsedPrimaryData) {
        System.out.println("Primary Data String:");
        System.out.println(parsedPrimaryData.getPrimaryDataString());
    }
}
