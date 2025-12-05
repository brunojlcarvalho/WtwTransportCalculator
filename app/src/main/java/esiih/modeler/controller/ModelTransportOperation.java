package esiih.modeler.controller;

import esiih.modeler.entity.TransportOperation;
import esiih.modeler.entity.enums.FuelType;
import esiih.modeler.entity.enums.Mode;
import esiih.modeler.exception.InvalidParsePrimaryDataException;
import esiih.modeler.exception.InvalidPrimaryValueException;

/**
 * ModelTransportOperation is responsible for creating TransportOperation
 * instances from CSV data.
 */
public class ModelTransportOperation {

    /**
     * Creates a TransportOperation instance from CSV values.
     * 
     * @param id The operation ID.
     * @param values The CSV row values.
     * @return The created TransportOperation instance.
     * @throws InvalidParsePrimaryDataException if parsing fails.
     */
    public TransportOperation fromCsv(String id, String[] values)
            throws InvalidParsePrimaryDataException {

        String vehicleType = validateVehicleType(values[2]);

        Mode mode = parseMode(values[3]);

        double distLoaded = validateDistanceLoaded(parseDouble(values[4]));

        double distEmpty = validateDistanceEmpty(parseDouble(values[5]));

        double payload = validatePayload(parseDouble(values[6]), 28, "payload");

        double maxPayload = validatePayload(parseDouble(values[7]), 28, "payload");

        FuelType fuel = parseFuel(values[8], "fuel");

        double litersConsumed = parseAndValidateRange(values[10], 0, 10000000, "liters Consumed");

        double consumptionLitTKm = parseAndValidateRange(values[11], 0, 100, "consumption liters");

        TransportOperation to = new TransportOperation(id, mode, vehicleType, distLoaded,
                distEmpty, payload, maxPayload, litersConsumed,
                consumptionLitTKm, fuel);

        return to;
    }

    /**
     * Validates the vehicle type string of a TransportOperation.
     *
     * @param vehicleType The vehicle type string from the CSV.
     * @return The validated vehicle type string.
     * @throws InvalidParsePrimaryDataException if the vehicle type is invalid.
     */
    public String validateVehicleType(String vehicleType) throws InvalidParsePrimaryDataException {
        if (vehicleType == null || vehicleType.isEmpty()) {
            throw new InvalidPrimaryValueException("Vehicle type is null or empty");
        }
        if (vehicleType.length() > 20) {
            throw new InvalidPrimaryValueException("Vehicle type length must be between 0 and 20");
        }
        return vehicleType;
    }

    /**
     * Parses and validates the transport mode (e.g., ROAD, RAIL, AIR).
     *
     * @param rawMode The raw mode string from the CSV.
     * @return The parsed and validated Mode.
     * @throws InvalidParsePrimaryDataException if the mode is invalid or cannot be parsed.
     */
    public Mode parseMode(String rawMode) throws InvalidParsePrimaryDataException {
        try {
            Mode mode = Mode.fromString(rawMode);
            if (mode == null) {
                throw new InvalidPrimaryValueException("Invalid mode in CSV file");
            }
            return mode;
        } catch (IllegalArgumentException e) {
            throw new InvalidPrimaryValueException("It's not possible read the mode.");
        }
    }

    /**
     * Validates the loaded distance (km) value for a TransportOperation.
     *
     * @param dist The loaded distance value.
     * @return The validated loaded distance.
     * @throws InvalidParsePrimaryDataException if the loaded distance is invalid.
     */
    public double validateDistanceLoaded(double dist) throws InvalidParsePrimaryDataException {
        if (dist <= 0 || dist > 20000) {
            throw new InvalidPrimaryValueException("Invalid distance loaded value.");
        }
        return dist;
    }

    /**
     * Validates the empty distance (km) value for a TransportOperation.
     * 
     * @param dist The empty distance value.
     * @return The validated empty distance.
     * @throws InvalidParsePrimaryDataException if the empty distance is invalid.
     */
    public double validateDistanceEmpty(double dist) throws InvalidParsePrimaryDataException {
        if (dist < 0 || dist > 20000) {
            throw new InvalidPrimaryValueException("Invalid empty distance value.");
        }
        return dist;
    }

    /**
     * Validates payload numeric values (payload and max capacity).
     *
     * @param value The payload value.
     * @param maxAllowed The maximum allowed payload value.
     * @param fieldName The name of the field being validated.
     * @return The validated payload value.
     * @throws InvalidParsePrimaryDataException if the payload value is invalid.
     */
    public double validatePayload(double value, double maxAllowed, String fieldName)
            throws InvalidParsePrimaryDataException {
        if (value <= 0 || value > maxAllowed) {
            throw new InvalidPrimaryValueException("Invalid " + fieldName + " value.");
        }
        return value;
    }

    /**
     * Parses and validates a numeric range for fields such as liters consumed and
     * consumption (l/100tkm).
     *
     * @param raw The raw numeric string.
     * @param min The minimum allowed value.
     * @param max The maximum allowed value.
     * @param fieldName The name of the field being validated.
     * @return The parsed and validated numeric value.
     * @throws InvalidParsePrimaryDataException if the value is invalid or cannot be parsed.
     */
    private double parseAndValidateRange(String raw, double min, double max, String fieldName)
            throws InvalidParsePrimaryDataException {
        if (raw == null || raw.trim().isEmpty()) {
            return 0.0;
        }

        double value;
        try {
            value = Double.parseDouble(raw.replace(",", ".").trim());
        } catch (NumberFormatException e) {
            throw new InvalidPrimaryValueException("Error parse " + fieldName + " value.");
        }

        if (value < min || value > max) {
            throw new InvalidPrimaryValueException("Invalid " + fieldName + " value.");
        }

        return value;
    }

    /**
     * Parses and validates the fuel type for a TransportOperation.
     *
     * @param raw The raw fuel type string.
     * @param fieldName The name of the field being validated.
     * @return The parsed and validated FuelType.
     * @throws InvalidParsePrimaryDataException if the fuel type is invalid or cannot be parsed.
     */
    public FuelType parseFuel(String raw, String fieldName) throws InvalidParsePrimaryDataException {
        try {
            FuelType fuel = FuelType.fromString(raw);
            if (fuel == null) {
                throw new InvalidPrimaryValueException("Invalid " + fieldName + " mode in CSV file");
            }
            return fuel;
        } catch (IllegalArgumentException e) {
            throw new InvalidPrimaryValueException("Error parse " + fieldName + " value.");
        }
    }

    /**
     * Safely parses a numeric string into a double, converting comma decimal
     * separators and returning 0.0 for empty values.
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
