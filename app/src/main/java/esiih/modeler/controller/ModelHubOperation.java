package esiih.modeler.controller;

import esiih.modeler.entity.HubOperation;
import esiih.modeler.entity.enums.FuelType;
import esiih.modeler.entity.enums.Mode;
import esiih.modeler.exception.InvalidParsePrimaryDataException;
import esiih.modeler.exception.InvalidPrimaryFormatException;
import esiih.modeler.exception.InvalidPrimaryValueException;

/**
 * ModelHubOperation is responsible for creating HubOperation instances from
 * CSV data.
 */
public class ModelHubOperation {

    /**
     * Creates a HubOperation instance from CSV values.
     * @param id The operation ID.
     * @param values The CSV row values.
     * @return The created HubOperation instance.
     * @throws InvalidParsePrimaryDataException if parsing fails.
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

        HubOperation ho = new HubOperation(id, Mode.HUB, energySource, energy, duration);

        return ho;

    }

    /**
     * Parses and validates the duration field of a HubOperation.
     *
     * @param raw The raw duration string from the CSV.
     * @return The parsed and validated duration.
     * @throws InvalidParsePrimaryDataException if the duration value is invalid or cannot be parsed.
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
     * @param raw The raw energy consumption string from the CSV.
     * @return The parsed and validated energy consumption.
     * @throws InvalidParsePrimaryDataException if the energy consumption value is invalid or cannot be parsed.
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
     * @param raw The raw energy source string from the CSV.
     * @return The parsed and validated FuelType.
     * @throws InvalidParsePrimaryDataException if the energy source value is invalid or cannot be parsed.
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
     * @param val The raw numeric string.
     * @return The parsed double value.
     */
    private double parseDouble(String val) {
        if (val == null || val.trim().isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(val.replace(",", ".").trim());
    }
}
