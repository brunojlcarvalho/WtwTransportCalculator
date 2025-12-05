package esiih;

import java.io.File;
import java.io.IOException;
import java.util.List;

import esiih.calculator.boundary.CalculatorService;
import esiih.calculator.controller.Calculator;
import esiih.exporter.boundary.ExporterService;
import esiih.exporter.controller.Exporter;
import esiih.importer.boundary.PrimaryParserService;
import esiih.importer.boundary.SecondaryParserService;
import esiih.importer.controller.PrimaryDataParser;
import esiih.importer.controller.SecondaryDataParser;
import esiih.importer.entity.ParsedPrimaryData;
import esiih.importer.entity.ParsedSecondaryData;
import esiih.importer.exception.InvalidDataException;
import esiih.modeler.boundary.ModelerService;
import esiih.modeler.controller.Modeler;
import esiih.modeler.entity.EmissionFactor;
import esiih.modeler.entity.HubOperation;
import esiih.modeler.entity.TransportChain;
import esiih.modeler.entity.TransportChainElement;
import esiih.modeler.entity.TransportOperation;

public class App {

    private static final String FILE_SECONDARY = "app/src/main/resources/secondary_data.csv";
    private static final String FILE_PRIMARY = "app/src/main/resources/primary_data.csv";
    private static final String FILE_REPORT = "app/src/main/report/report_final.csv";

    /**
     * Main method to execute the application.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        try {
            printInitializationTitle();

            // Initializing Services
            SecondaryParserService secondaryParser = new SecondaryDataParser();
            PrimaryParserService primaryParser = new PrimaryDataParser();
            ModelerService modeler = new Modeler();
            CalculatorService calculator = new Calculator();
            ExporterService exporter = new Exporter();

            // Execution Flow
            ParsedSecondaryData secondaryData = stepImportSecondary(secondaryParser);
            ParsedPrimaryData primaryData = stepImportPrimary(primaryParser);

            List<EmissionFactor> factors = stepModelFactors(modeler, secondaryData);
            TransportChain chain = stepModelChain(modeler, primaryData);

            stepCalculate(calculator, chain, factors);
            stepExport(exporter, chain);

            // Final Result
            printFinalConsoleReport(chain);

        } catch (Exception e) {
            handleError(e);
        }
    }

    /**
     * Prints the initialization title to the console.
     */
    private static void printInitializationTitle() {
        System.out.println(">>> Initiating calculation of GEE emissions <<<");
    }

    /**
     * Steps the import of secondary data.
     * 
     * @param parser The secondary data parser service.
     * @return The parsed secondary data.
     * @throws InvalidDataException If data is invalid.
     */
    private static ParsedSecondaryData stepImportSecondary(SecondaryParserService parser) throws InvalidDataException {
        System.out.println("\n[1] Importing Secondary Data...");
        ParsedSecondaryData data = parser.parseSecondaryData(FILE_SECONDARY);
        System.out.println("    -> Secondary Data loaded successfully.");
        return data;
    }

    /**
     * Steps the import of primary data.
     * 
     * @param parser The primary data parser service.
     * @return The parsed primary data.
     * @throws InvalidDataException If data is invalid.
     */
    private static ParsedPrimaryData stepImportPrimary(PrimaryParserService parser) throws InvalidDataException {
        System.out.println("\n[2] Importing Primary Data...");
        ParsedPrimaryData data = parser.parsePrimaryData(FILE_PRIMARY);
        System.out.println("    -> Primary Data loaded successfully.");
        return data;
    }

    /**
     * Steps the modeling of emission factors.
     * 
     * @param modeler The modeler service.
     * @param data The parsed secondary data.
     * @return A list of modeled emission factors.
     * @throws Exception If an error occurs during modeling.
     */
    private static List<EmissionFactor> stepModelFactors(ModelerService modeler, ParsedSecondaryData data) throws Exception {
        System.out.println("\n[3] Modeling Emission Factors...");
        List<EmissionFactor> factors = modeler.modelEmissionFactors(data);
        System.out.println("    -> " + factors.size() + " emission factors modeled.");
        return factors;
    }

    /**
     * Steps the modeling of the transport chain.
     * 
     * @param modeler The modeler service.
     * @param data The parsed primary data.
     * @return The modeled transport chain.
     * @throws Exception If an error occurs during modeling.
     */
    private static TransportChain stepModelChain(ModelerService modeler, ParsedPrimaryData data) throws Exception {
        System.out.println("\n[4] Modeling Transport Chain...");
        TransportChain chain = modeler.modelTransportChain(data);
        System.out.println("    -> Chain created with " + chain.getElements().size() + " elements.");
        return chain;
    }

    /**
     * Steps the calculation of GEE emissions.
     * 
     * @param calc The calculator service.
     * @param chain The transport chain to calculate.
     * @param factors The list of emission factors.
     * @throws Exception If an error occurs during calculation.
     */
    private static void stepCalculate(CalculatorService calc, TransportChain chain, List<EmissionFactor> factors) throws Exception {
        System.out.println("\n[5] Calculating Emissions (WTW)...");
        calc.calculateGEE(chain, factors);
        System.out.println("    -> Calculations completed.");
    }

    /**
     * Steps the export of the final report.
     * 
     * @param exporter The exporter service.
     * @param chain The transport chain with results to export.
     * @throws Exception If an error occurs during export.
     */
    private static void stepExport(ExporterService exporter, TransportChain chain) throws Exception {
        System.out.println("\n[6] Exporting Results...");

        File file = new File(FILE_REPORT);
        // Garante que a pasta existe antes de exportar
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        exporter.exportReport(chain, FILE_REPORT);
        System.out.println("    -> Success! File created at: " + FILE_REPORT);
    }

    /**
     * Prints the final console report.
     * 
     * @param chain The transport chain with calculated emissions.
     */
    private static void printFinalConsoleReport(TransportChain chain) {
        System.out.println("\n###############################################################################");
        System.out.println("# FINAL EMISSIONS REPORT (Console View)");
        System.out.println("###############################################################################");

        System.out.printf("Total Chain Emissions (WTW): %.2f kgCO2e%n", chain.getTotalWTW());
        System.out.printf("Chain Carbon Intensity:      %.4f kgCO2e/t.km%n", chain.getIntensity());

        System.out.println("\n--- Detail by Element ---");
        System.out.println(String.format("| %-5s | %-18s | %-15s | %-12s |", "ID", "Type", "WTW (kg)", "TTW / WTT"));
        System.out.println("-".repeat(60));

        for (TransportChainElement element : chain.getElements()) {
            String type = element.getClass().getSimpleName();
            
            if (element instanceof TransportOperation) type = "Transport (TO)";
            if (element instanceof HubOperation) type = "Hub (HO)";

            System.out.printf("| %-5s | %-18s | %10.2f kg | %.2f / %.2f%n",
                    element.getId(),
                    type,
                    element.getEmissionWTW(),
                    element.getEmissionTTW(),
                    element.getEmissionWTT());
        }
        System.out.println("-".repeat(60));
    }

    /**
     * Handles errors by printing detailed messages to the console.
     * 
     * @param e The exception to handle.
     */
    private static void handleError(Exception e) {
        System.err.println("\n!!! EXECUTION FAILED !!!");

        if (e instanceof IOException) {
            System.err.println("Error Type: Input/Output Error");
            System.err.println("Hint: Check if the file path exists or if the file is open in Excel.");
        } else if (e instanceof InvalidDataException) {
            System.err.println("Error Type: Invalid Data");
            System.err.println("Hint: Check your CSV content/format.");
        } else {
            System.err.println("Error Type: " + e.getClass().getSimpleName());
        }

        System.err.println("Message: " + e.getMessage());
    }
}