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

            String[] values = line.split(";", -1);

            String id = values[0];
            String type = values[1];

            if ("TO".equalsIgnoreCase(type)) {
                String vehicleType = values[2];

                if (vehicleType == null || vehicleType.isEmpty())
                    throw new InvalidParsePrimaryDataException("Vehicle type is null or empty");

                if (vehicleType.length() < 0 || vehicleType.length() > 20)
                    throw new InvalidParsePrimaryDataException("Vehicle type length must be between 0 and 20");

                Mode mode;
                try {
                    mode = Mode.fromString(values[3]);
                } catch (IllegalArgumentException e) {
                    throw new InvalidParsePrimaryDataException("It's not possible read the mode.");
                }

                if (mode == Mode.UNKNOWN)
                    throw new InvalidParsePrimaryDataException("Invalid mode in CSV file");

                double distLoaded;
                try {
                    distLoaded = parseDouble(values[4]);
                } catch (NumberFormatException e) {
                    throw new InvalidParsePrimaryDataException("Error parse distance loaded value.");
                }

                if (distLoaded != 0.0 && distLoaded <= 0 || distLoaded > 20000)
                    throw new InvalidParsePrimaryDataException("Invalid distance loaded value.");

                double distEmpty;
                try {
                    distEmpty = parseDouble(values[5]);
                } catch (NumberFormatException e) {
                    throw new InvalidParsePrimaryDataException("Error parse distance empty value.");
                }

                if (distEmpty != 0.0 && distEmpty < 0 || distEmpty > 20000)
                    throw new InvalidParsePrimaryDataException("Invalid empty distance value.");

                double payload;
                try {
                    payload = parseDouble(values[6]);
                } catch (NumberFormatException e) {
                    throw new InvalidParsePrimaryDataException("Error parse payload value.");
                }

                if (payload != 0.0 && payload <= 0 || payload > 28)
                    throw new InvalidParsePrimaryDataException("Invalid payload value.");

                double maxPayload;
                try {
                    maxPayload = parseDouble(values[7]);
                } catch (NumberFormatException e) {
                    throw new InvalidParsePrimaryDataException("Error parse payload value.");
                }

                if (maxPayload != 0.0 && maxPayload <= 0 || maxPayload > 28)
                    throw new InvalidParsePrimaryDataException("Invalid payload value.");

                FuelType fuel;
                try {
                    fuel = FuelType.fromString(values[8]);
                } catch (IllegalArgumentException e) {
                    throw new InvalidParsePrimaryDataException("Error parse fuel value.");
                }

                if (fuel == FuelType.UNKNOWN)
                    throw new InvalidParsePrimaryDataException("Invalid fuel mode in CSV file");

                double litersConsumed;
                try {
                    litersConsumed = parseDouble(values[10]);
                } catch (NumberFormatException e) {
                    throw new InvalidParsePrimaryDataException("Error parse liters Consumed value.");
                }

                if (litersConsumed != 0.0 && litersConsumed < 0 || litersConsumed > 10000000)
                    throw new InvalidParsePrimaryDataException("Invalid liters Consumed value.");

                double consumptionLitTKm;
                try {
                    consumptionLitTKm = parseDouble(values[11]);
                } catch (NumberFormatException e) {
                    throw new InvalidParsePrimaryDataException("Error parse consumption liters value.");
                }

                if (consumptionLitTKm != 0.0 && consumptionLitTKm <= 0 || consumptionLitTKm > 100)
                    throw new InvalidParsePrimaryDataException("Invalid consumption liters value.");

                TransportOperation to = new TransportOperation(id);
                to.setTransportMode(mode);
                to.setVehicleType(vehicleType);
                to.setDistanceLoadedKm(distLoaded);
                to.setDistanceEmptyKm(distEmpty);
                to.setPayloadTon(payload);
                to.setMaxCapacityTon(maxPayload);
                to.setFuelType(fuel);

                transportChain.addElement(to);

            } else if ("HO".equalsIgnoreCase(type)) {

                double duration;
                try {
                    duration = (values.length > 9) ? parseDouble(values[9]) : 0.0;
                } catch (NumberFormatException e) {
                    throw new InvalidParsePrimaryDataException("Error parse duration value.");
                }

                if (duration != 0.0 && duration <= 0 || duration > 2000)
                    throw new InvalidParsePrimaryDataException("Invalid duration value.");

                double energy;
                try {
                    energy = (values.length > 12) ? parseDouble(values[12]) : 0.0;
                } catch (NumberFormatException e) {
                    throw new InvalidParsePrimaryDataException("Error parse energy value.");
                }

                if (energy != 0.0 && energy <= 0 || energy > 100000)
                    throw new InvalidParsePrimaryDataException("Invalid energy value.");

                FuelType energySource;
                try {
                    energySource = FuelType.fromString(values[8]);
                } catch (IllegalArgumentException e) {
                    throw new InvalidParsePrimaryDataException("Error parse hub energy source");
                }

                if (energySource == FuelType.UNKNOWN)
                    throw new InvalidParsePrimaryDataException("Invalid hub energy source");

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
