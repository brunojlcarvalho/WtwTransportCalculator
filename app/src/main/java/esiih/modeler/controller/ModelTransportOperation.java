package esiih.modeler.controller;

import esiih.modeler.entity.TransportOperation;
import esiih.modeler.entity.enums.FuelType;
import esiih.modeler.entity.enums.Mode;
import esiih.modeler.exception.InvalidParsePrimaryDataException;
import esiih.modeler.exception.InvalidPrimaryValueException;

public class ModelTransportOperation {

    /**
     * Builds a {@link TransportOperation} instance from a CSV line.
     *
     * This method extracts all TransportOperation-related fields from the
     * provided CSV, validates them individually, and constructs a fully
     * populated TransportOperation domain object.
     *
     * @param id
     * @param values
     * @return
     * @throws InvalidParsePrimaryDataException
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
     * @param vehicleType
     * @return
     * @throws InvalidParsePrimaryDataException
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
     * @param rawMode
     * @return
     * @throws InvalidParsePrimaryDataException
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
     * @param dist
     * @return
     * @throws InvalidParsePrimaryDataException
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
     * @param dist
     * @return
     * @throws InvalidParsePrimaryDataException
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
     * @param value
     * @param maxAllowed
     * @param fieldName
     * @return
     * @throws InvalidParsePrimaryDataException
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
     * @param raw
     * @param min
     * @param max
     * @param fieldName
     * @return
     * @throws InvalidParsePrimaryDataException
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
     * @param raw
     * @param fieldName
     * @return
     * @throws InvalidParsePrimaryDataException
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
