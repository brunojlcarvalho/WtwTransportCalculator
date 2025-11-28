package esiih.modeler.controller;

import esiih.modeler.entity.HubOperation;
import esiih.modeler.entity.enums.FuelType;
import esiih.modeler.entity.enums.Mode;
import esiih.modeler.exception.InvalidParsePrimaryDataException;
import esiih.modeler.exception.InvalidPrimaryFormatException;
import esiih.modeler.exception.InvalidPrimaryValueException;

public class ModelHubOperation {

    /**
     * Builds a {@link HubOperation} instance from a CSV line.
     *
     * This method extracts HubOperation-specific values from the CSV (duration,
     * energy consumption and energy source), validates them individually, and
     * constructs a fully populated {@link HubOperation} objec
     *
     * @param id
     * @param values
     * @return
     * @throws InvalidParsePrimaryDataException
     */
    public HubOperation fromCsv(String id, String[] values)
            throws InvalidParsePrimaryDataException {

        double duration = parseAndValidateDuration(
                values.length > 9 ? values[9] : "0"
        );

        double energy = parseAndValidateEnergy(
                values.length > 12 ? values[12] : "0"
        );

        FuelType energySource = parseEnergySource(values[8]);

        HubOperation ho = new HubOperation(id);
        ho.setDurationHours(duration);
        ho.setEnergyConsumed(energy);
        ho.setEnergySource(energySource);
        ho.setHubMode(Mode.HUB);

        return ho;

    }

    // -----------------------------------------------------------------------------
    // HubOperation
    // -----------------------------------------------------------------------------

    /**
     * Parses and validates the duration field of a HubOperation.
     *
     * @param raw
     * @return
     * @throws InvalidParsePrimaryDataException
     */
    private double parseAndValidateDuration(String raw)
            throws InvalidParsePrimaryDataException {

        double duration;
        try {
            duration = parseDouble(raw);
        } catch (NumberFormatException e) {
            throw new InvalidPrimaryFormatException("Error parse duration value.");
        }

        if (duration <= 0 || duration > 2000)
            throw new InvalidPrimaryValueException("Invalid duration value.");

        return duration;
    }

    /**
     * Parses and validates the energy consumption field of a HubOperation.
     *
     * @param raw
     * @return
     * @throws InvalidParsePrimaryDataException
     */
    private double parseAndValidateEnergy(String raw)
            throws InvalidParsePrimaryDataException {

        double energy;
        try {
            energy = parseDouble(raw);
        } catch (NumberFormatException e) {
            throw new InvalidPrimaryFormatException("Error parse energy value.");
        }

        if (energy <= 0 || energy > 100000)
            throw new InvalidPrimaryValueException("Invalid energy value.");


        return energy;
    }

    /**
     * Parses and validates the energy source string for a HubOperation.
     *
     * @param raw
     * @return
     * @throws InvalidParsePrimaryDataException
     */
    private FuelType parseEnergySource(String raw)
            throws InvalidParsePrimaryDataException {

        try {
            FuelType energySource = FuelType.fromString(raw);

            if (energySource == null)
                throw new InvalidPrimaryValueException("Invalid hub energy source");

            return energySource;
        } catch (IllegalArgumentException e) {
            throw new InvalidPrimaryFormatException("Error parse hub energy source");
        }
    }

    /**
     * Safely parses a numeric string into a double.
     *
     * @param val
     * @return
     */
    private double parseDouble(String val) {
        if (val == null || val.trim().isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(val.replace(",", ".").trim());
    }
}
