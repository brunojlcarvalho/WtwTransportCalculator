package esiih.modeler.controller;

import java.util.ArrayList;
import java.util.List;

import esiih.importer.entity.ParsedPrimaryData;
import esiih.importer.entity.ParsedSecondaryData;
import esiih.modeler.boundary.ModelerService;
import esiih.modeler.entity.EmissionFactor;
import esiih.modeler.entity.HubOperation;
import esiih.modeler.entity.TransportChain;
import esiih.modeler.entity.TransportChainElement;
import esiih.modeler.entity.TransportOperation;
import esiih.modeler.exception.InvalidParsePrimaryDataException;
import esiih.modeler.exception.InvalidParsedSecondaryDataException;

/**
 * Implementation of the ModelerService interface for modeling transport chains
 * and emission factors.
 */
public class Modeler implements ModelerService {

    private final ModelTransportOperation modelTransportOperation = new ModelTransportOperation();
    private final ModelHubOperation modelHubOperation = new ModelHubOperation();
    private final ModelEmissionFactor modelEmissionFactor = new ModelEmissionFactor();

    /**
     * Models the TransportChain from the parsed primary data.
     *
     * @param parsedData the parsed primary data
     * @return the transport chain
     * @throws InvalidParsePrimaryDataException if the parsed data is invalid
     */
    @Override
    public TransportChain modelTransportChain(ParsedPrimaryData parsedData)
            throws InvalidParsePrimaryDataException {

        validateParsedData(parsedData);

        String[] lines = parsedData.getPrimaryDataString().split("\n");

        TransportChain transportChain = new TransportChain("TC-1", "Transport Chain from CSV");

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] values = line.split(";", -1);
            String id = values[0];
            String type = values[1];

            TransportChainElement element = createElementFromValues(id, type, values, i + 1);
            if (element != null) {
                transportChain.addElement(element);
            }

        }

        return transportChain;
    }

    /**
     * Validates the state of the {@link ParsedPrimaryData} instance before
     * processing it. Ensures the object is not null and that its content is
     * non-empty.
     *
     * @param parsedData the parsed primary data to validate
     * @throws InvalidParsePrimaryDataException if the parsed data is null or
     * its content is empty
     */
    private void validateParsedData(ParsedPrimaryData parsedData)
            throws InvalidParsePrimaryDataException {

        if (parsedData == null) {
            throw new InvalidParsePrimaryDataException("ParsedPrimaryData is null");
        }

        String primaryString = parsedData.getPrimaryDataString();

        if (primaryString == null || primaryString.isEmpty()) {
            throw new InvalidParsePrimaryDataException("ParsedPrimaryData string is null or empty");
        }

    }

    /**
     * Creates the correct {@link TransportChainElement} subtype based on the
     * CSV Operacao type. "TO" produces a {@link TransportOperation} and "HO"
     * produces a {@link HubOperation}.
     *
     * @param id the element ID
     * @param type the element type ("TO" or "HO")
     * @param values the CSV values for the element
     * @param lineNumber the line number in the CSV file (for error reporting)
     * @return the created TransportChainElement
     * @throws InvalidParsePrimaryDataException if parsing fails
     */
    private TransportChainElement createElementFromValues(String id, String type, String[] values, int lineNumber) throws InvalidParsePrimaryDataException {

        if ("TO".equalsIgnoreCase(type)) {
            return modelTransportOperation.fromCsv(id, values);
        } else if ("HO".equalsIgnoreCase(type)) {
            return modelHubOperation.fromCsv(id, values);
        }
        return null;
    }

    /**
     * Models the EmissionFactors from the parsed secondary data.
     *
     * @param parsedData the parsed secondary data
     * @return the list of emission factors
     * @throws InvalidParsedSecondaryDataException if the parsed data is invalid
     */
    @Override
    public List<EmissionFactor> modelEmissionFactors(ParsedSecondaryData parsedData) throws InvalidParsedSecondaryDataException {

        validateParsedSecondaryData(parsedData);

        String[] lines = parsedData.getSecondaryDataString().split("\n");

        List<EmissionFactor> emissionFactors = new ArrayList<>();

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] values = line.split(";", -1);

            EmissionFactor ef = modelEmissionFactor.fromCsv(values, i);
            emissionFactors.add(ef);
        }

        return emissionFactors;
    }

    /**
     * Validates the parsed secondary CSV wrapper to ensure it contains usable
     * data.
     *
     * @param parsedData the parsed secondary data to validate
     * @throws InvalidParsedSecondaryDataException if the parsed data is null or
     * its content is empty
     */
    private void validateParsedSecondaryData(ParsedSecondaryData parsedData)
            throws InvalidParsedSecondaryDataException {

        if (parsedData == null) {
            throw new InvalidParsedSecondaryDataException("ParsedSecondaryData is null");
        }

        String secondaryString = parsedData.getSecondaryDataString();
        if (secondaryString == null || secondaryString.isEmpty()) {
            throw new InvalidParsedSecondaryDataException("ParsedSecondaryData string is null or empty");
        }
    }
}
