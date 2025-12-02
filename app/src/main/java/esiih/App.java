package esiih;

import java.util.List;

import esiih.calculator.boundary.CalculatorService;
import esiih.calculator.controller.Calculator;   // Alterado
import esiih.calculator.exception.InvalidCalculationException;
import esiih.importer.boundary.PrimaryParserService; // Alterado
import esiih.importer.boundary.SecondaryParserService;    // Alterado
import esiih.importer.controller.PrimaryDataParser;  // Alterado
import esiih.importer.controller.SecondaryDataParser;
import esiih.importer.entity.ParsedPrimaryData;
import esiih.importer.entity.ParsedSecondaryData;
import esiih.importer.exception.InvalidDataException;
import esiih.modeler.boundary.ModelerService;
import esiih.modeler.controller.Modeler;
import esiih.modeler.entity.EmissionFactor;
import esiih.modeler.entity.TransportChain;
import esiih.modeler.entity.TransportChainElement;
import esiih.modeler.exception.InvalidParsePrimaryDataException;
import esiih.modeler.exception.InvalidParsedSecondaryDataException;

public class App {

    /**
     * Main method to run the application
     */
    public static void main(String[] args) {
        String secondaryDataFilePath = "app/src/main/resources/secondary_data.csv";
        String primaryDataFilePath = "app/src/main/resources/primary_data.csv";

        try {
            SecondaryParserService secondaryParser = new SecondaryDataParser();
            PrimaryParserService primaryParser = new PrimaryDataParser();

            ModelerService modeler = new Modeler();
            CalculatorService calculator = new Calculator();

            System.out.println(">>> Initiating calculation of GEE emissions <<<");

            System.out.println("\n[1] Importing Data...");

            ParsedSecondaryData parsedSecondaryData = secondaryParser.parseSecondaryData(secondaryDataFilePath);

            ParsedPrimaryData parsedPrimaryData = primaryParser.parsePrimaryData(primaryDataFilePath);

            System.out.println("    -> Secondary and Primary Data successfully imported.");

            System.out.println("\n[2] Modeling Transport Chain...");

            List<EmissionFactor> emissionFactors = modeler.modelEmissionFactors(parsedSecondaryData);
            System.out.println("    -> " + emissionFactors.size() + " emission factors loaded.");

            TransportChain transportChain = modeler.modelTransportChain(parsedPrimaryData);
            System.out.println(
                    "    -> Transport chain created with " + transportChain.getElements().size() + " elements.");
            System.out.println("\n[3] Calculating Emissions (WTW)...");
            calculator.calculateGEE(transportChain, emissionFactors);
            System.out.println("    -> Calculations completed.");

            printResults(transportChain);

        } catch (InvalidDataException | InvalidParsePrimaryDataException | InvalidParsedSecondaryDataException | InvalidCalculationException e) {
            System.err.println("\n ERROR");
            System.err.println("Message: " + e.getMessage());
        }
    }

    private static void printResults(TransportChain chain) {
        System.out.println("\n###############################################################################");
        System.out.println("# FINAL EMISSIONS REPORT");
        System.out.println("###############################################################################");

        System.out.println(String.format("Total Chain Emissions (WTW): %.2f kgCO2e", chain.getTotalWTW()));
        System.out.println(String.format("Chain Carbon Intensity: %.4f kgCO2e/t.km", chain.getIntensity()));
        System.out.println("\n--- Detail by Element ---");
        for (TransportChainElement element : chain.getElements()) {
            System.out.println(String.format(
                    "ID: %-5s | Type: %-20s | WTW: %10.2f kgCO2e | (TTW: %.2f | WTT: %.2f)",
                    element.getId(),
                    element.getClass().getSimpleName(),
                    element.getEmissionWTW(),
                    element.getEmissionTTW(),
                    element.getEmissionWTT()));
        }
        System.out.println("###############################################################################");
    }
}
