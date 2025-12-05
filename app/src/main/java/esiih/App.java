package esiih;

import java.io.IOException;
import java.util.List;

import esiih.calculator.boundary.CalculatorService;
import esiih.calculator.controller.Calculator;
import esiih.calculator.exception.InvalidCalculationException;
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
import esiih.modeler.entity.TransportChain;
import esiih.modeler.entity.TransportChainElement;
import esiih.modeler.exception.InvalidParsePrimaryDataException;
import esiih.modeler.exception.InvalidParsedSecondaryDataException;

public class App {

    /**
     * Main method to run the application
     */
    public static void main(String[] args) {
        // Caminhos de entrada e saída
        String secondaryDataFilePath = "app/src/main/resources/secondary_data.csv";
        String primaryDataFilePath = "app/src/main/resources/primary_data.csv";
        String outputFilePath = "app/src/main/report/report_final.csv";

        try {
            // 1. Instanciar Serviços
            SecondaryParserService secondaryParser = new SecondaryDataParser();
            PrimaryParserService primaryParser = new PrimaryDataParser();
            ModelerService modeler = new Modeler();
            CalculatorService calculator = new Calculator();
            ExporterService exporter = new Exporter();

            System.out.println(">>> Initiating calculation of GEE emissions <<<");

            // 2. Importar
            System.out.println("\n[1] Importing Data...");
            ParsedSecondaryData parsedSecondaryData = secondaryParser.parseSecondaryData(secondaryDataFilePath);
            ParsedPrimaryData parsedPrimaryData = primaryParser.parsePrimaryData(primaryDataFilePath);
            System.out.println("    -> Secondary and Primary Data successfully imported.");

            // 3. Modelar
            System.out.println("\n[2] Modeling Transport Chain...");
            List<EmissionFactor> emissionFactors = modeler.modelEmissionFactors(parsedSecondaryData);
            System.out.println("    -> " + emissionFactors.size() + " emission factors loaded.");

            TransportChain transportChain = modeler.modelTransportChain(parsedPrimaryData);
            System.out.println(
                    "    -> Transport chain created with " + transportChain.getElements().size() + " elements.");

            // 4. Calcular
            System.out.println("\n[3] Calculating Emissions (WTW)...");
            calculator.calculateGEE(transportChain, emissionFactors);
            System.out.println("    -> Calculations completed.");

            // 5. EXPORTAR (NOVO PASSO)
            System.out.println("\n[4] Exporting Results to CSV...");

            exporter.exportReport(transportChain, outputFilePath);
            System.out.println("    -> Success! File created at: " + outputFilePath);

            // 6. Imprimir na consola (opcional, para debug rápido)
            printResults(transportChain);

        } catch (InvalidDataException | InvalidParsePrimaryDataException | InvalidParsedSecondaryDataException
                | InvalidCalculationException e) {
            System.err.println("\n ERROR (Domain/Logic):");
            System.err.println("Message: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("\n ERROR (IO/Export):");
            System.err.println("Could not write to file. Check if it's open in Excel.");
            System.err.println("Message: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("\n UNEXPECTED ERROR:");
            e.printStackTrace();
        }
    }

    private static void printResults(TransportChain chain) {
        System.out.println("\n###############################################################################");
        System.out.println("# FINAL EMISSIONS REPORT (Console View)");
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
