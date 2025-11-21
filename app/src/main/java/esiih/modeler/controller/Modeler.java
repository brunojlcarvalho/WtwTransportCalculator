package esiih.modeler.controller;

import esiih.importer.entity.ParsedPrimaryData;
import esiih.modeler.boundary.ModelerService;
import esiih.modeler.entity.HubOperation;
import esiih.modeler.entity.TransportChain;
import esiih.modeler.entity.TransportOperation;
import esiih.modeler.entity.enums.FuelType;
import esiih.modeler.entity.enums.Mode;
import esiih.modeler.exception.InvalidParsePrimaryDataException;

/**
 * Implementation of the ModelerService interface for modeling transport chains
 * and emission factors.
 */
public class Modeler implements ModelerService {

    private TransportChain transportChain;

    /**
     * Models the TransportChain from the parsed primary data.
     *
     * @param parsedData the parsed primary data
     * @return the transport chain
     * @throws InvalidParsePrimaryDataException if the parsed data is invalid
     */
    @Override
    public TransportChain modelTransportChain(ParsedPrimaryData parsedData) throws InvalidParsePrimaryDataException {
        if (parsedData == null) {
            throw new InvalidParsePrimaryDataException("ParsedPrimaryData is null");
        }

        String primaryString = parsedData.getPrimaryDataString();
        if (primaryString == null || primaryString.isEmpty()) {
            throw new InvalidParsePrimaryDataException("ParsedPrimaryData string is null or empty");
        }

        String[] lines = primaryString.split("\n");

        transportChain = new TransportChain("TC-1", "Transport Chain from CSV");

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] values = line.split(";");

            String id = values[0];
            String type = values[1];

            if ("TO".equalsIgnoreCase(type)) {
                String vehicleType = values[2];

                Mode mode;
                try {
                    mode = Mode.fromString(values[3]);
                } catch (IllegalArgumentException e) {
                    throw new InvalidParsePrimaryDataException("Invalid mode value in row " + i + ": " + values[3]);
                }

                double distLoaded;
                try {
                    distLoaded = parseDouble(values[4]);
                } catch (NumberFormatException e) {
                    throw new InvalidParsePrimaryDataException("Invalid distance loaded value in row " + i);
                }

                double distEmpty;
                try {
                    distEmpty = parseDouble(values[5]);
                } catch (NumberFormatException e) {
                    throw new InvalidParsePrimaryDataException("Invalid distance empty value in row " + i);
                }

                double payload;
                try {
                    payload = parseDouble(values[6]);
                } catch (NumberFormatException e) {
                    throw new InvalidParsePrimaryDataException("Invalid payload value in row " + i);
                }

                FuelType fuel;
                try {
                    fuel = FuelType.fromString(values[8]);
                } catch (IllegalArgumentException e) {
                    throw new InvalidParsePrimaryDataException("Invalid fuel value in row " + i + ": " + values[8]);
                }

                TransportOperation to = new TransportOperation(id);
                to.setTransportMode(mode);
                to.setVehicleType(vehicleType);
                to.setDistanceLoadedKm(distLoaded);
                to.setDistanceEmptyKm(distEmpty);
                to.setPayloadTon(payload);
                to.setFuelType(fuel);

                transportChain.addElement(to);

            } else if ("HO".equalsIgnoreCase(type)) {

                double duration;
                try {
                    duration = (values.length > 9) ? parseDouble(values[9]) : 0.0;
                } catch (NumberFormatException e) {
                    throw new InvalidParsePrimaryDataException("Invalid duration value in row " + i);
                }

                double energy;
                try {
                    energy = (values.length > 12) ? parseDouble(values[12]) : 0.0;
                } catch (NumberFormatException e) {
                    throw new InvalidParsePrimaryDataException("Invalid energy value in row " + i);
                }

                FuelType energySource;
                try {
                    energySource = FuelType.fromString(values[8]);
                } catch (IllegalArgumentException e) {
                    throw new InvalidParsePrimaryDataException("Invalid hub energy source in row " + i);
                }

                HubOperation ho = new HubOperation(id);
                ho.setDurationHours(duration);
                ho.setEnergyConsumed(energy);
                ho.setEnergySource(energySource);
                ho.setHubMode(Mode.HUB);

                transportChain.addElement(ho);
            }
        }

        return transportChain;
    }

    /**
     * Parses a double value from a string, handling commas and empty strings.
     *
     * @param val the string value
     * @return the parsed double
     * @throws NumberFormatException if the string cannot be parsed to a double
     */
    private double parseDouble(String val) {
        if (val == null || val.trim().isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(val.replace(",", ".").trim());
    }
}
