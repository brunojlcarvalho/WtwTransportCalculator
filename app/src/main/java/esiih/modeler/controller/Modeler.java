package esiih.modeler.controller;

import esiih.importer.entity.ParsedPrimaryData;
import esiih.modeler.boundary.ModelerService;
import esiih.modeler.entity.HubOperation;
import esiih.modeler.entity.TransportChain;
import esiih.modeler.entity.TransportChainElement;
import esiih.modeler.entity.TransportOperation;
import esiih.modeler.entity.enums.FuelType;
import esiih.modeler.entity.enums.Mode;
import esiih.modeler.exception.InvalidParsePrimaryDataException;

/**
 * Implementation of the ModelerService interface for modeling transport chains
 * and emission factors.
 */
public class Modeler implements ModelerService {

    private final ModelTransportOperation modelTransportOperation = new ModelTransportOperation();
    private final ModelHubOperation modelHubOperation = new ModelHubOperation();

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
            if (line.isEmpty())
                continue;

            String[] values = line.split(";", -1);
            String id = values[0];
            String type = values[1];

            TransportChainElement element = createElementFromValues(id, type, values, i + 1);
            if (element != null)
                transportChain.addElement(element);

        }

        return transportChain;
    }

    /**
     * Validates the state of the {@link ParsedPrimaryData} instance before
     * processing it. Ensures the object is not null and that its content is non-empty.
     *
     * @param parsedData
     * @throws InvalidParsePrimaryDataException
     */
    private void validateParsedData(ParsedPrimaryData parsedData)
            throws InvalidParsePrimaryDataException {

        if (parsedData == null)
            throw new InvalidParsePrimaryDataException("ParsedPrimaryData is null");


        String primaryString = parsedData.getPrimaryDataString();

        if (primaryString == null || primaryString.isEmpty())
            throw new InvalidParsePrimaryDataException("ParsedPrimaryData string is null or empty");

    }

    /**
     * Creates the correct {@link TransportChainElement} subtype based on the CSV
     * Operacao type. "TO" produces a {@link TransportOperation} and "HO" produces a {@link HubOperation}.
     *
     * @param id
     * @param type
     * @param values
     * @param lineNumber
     * @return
     * @throws InvalidParsePrimaryDataException
     */
    private TransportChainElement createElementFromValues(String id, String type, String[] values, int lineNumber) throws InvalidParsePrimaryDataException {

        if ("TO".equalsIgnoreCase(type)) {
            return modelTransportOperation.fromCsv(id, values);
        } else if ("HO".equalsIgnoreCase(type)) {
            return modelHubOperation.fromCsv(id, values);
        }
        // se não for nenhum tipo conhecido, neste momento ignoramos
        return null;
    }
}
