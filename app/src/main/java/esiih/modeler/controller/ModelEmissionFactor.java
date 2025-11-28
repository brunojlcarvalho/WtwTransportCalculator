package esiih.modeler.controller;

import esiih.modeler.entity.EmissionFactor;
import esiih.modeler.entity.enums.FuelType;
import esiih.modeler.entity.enums.Mode;
import esiih.modeler.entity.enums.Unit;
import esiih.modeler.exception.InvalidParsedSecondaryDataException;

public class ModelEmissionFactor {
private static final double MAX_PAYLOAD = 28;
private static final double MAX_EMPTY_PCT = 1;
private static final double MIN_WTT_TTW_AVG = 0;
    /**
     * Converts a row of the secondary CSV dataset into an {@link EmissionFactor} instance.
     * Each field is parsed and validated individually according to the
     * emission factor structure. Invalid or unparsable values result in {@link InvalidParsedSecondaryDataException}.
     *
     * @param values
     * @param rowNumber
     * @return
     * @throws InvalidParsedSecondaryDataException
     */
    public EmissionFactor fromCsv(String[] values, int rowNumber) throws InvalidParsedSecondaryDataException {

        Mode mode = parseMode(values[0], rowNumber);
        FuelType fuel = parseFuel(values[1], rowNumber);

        double payload = validateMax(parseDouble(values[2]), MAX_PAYLOAD, "payload");

        double emptyPct =  validateMax(parseDouble(values[3]), MAX_EMPTY_PCT, "empty pct");

        Unit unit = parseUnit(values[4], rowNumber);

        double wtt = validateMin(parseDouble(values[5]), MIN_WTT_TTW_AVG, "WTT");
        double ttw = validateMin(parseDouble(values[6]), MIN_WTT_TTW_AVG, "TTW");

        double averageConsumption = validateMin(parseDouble(values[8]), MIN_WTT_TTW_AVG, "average consumption");

        validateUnitForMode(unit, mode, rowNumber);

        return new EmissionFactor(mode, fuel, payload, emptyPct, ttw, wtt, unit, averageConsumption);
    }

    /**
     * Parses the CSV value into a {@link Mode} instance.
     *
     * @param raw
     * @param rowNumber
     * @return
     * @throws InvalidParsedSecondaryDataException
     */
    private Mode parseMode(String raw, int rowNumber) throws InvalidParsedSecondaryDataException {
        try {
            Mode mode = Mode.fromString(raw);
            if (mode == null)
                throw new InvalidParsedSecondaryDataException("Invalid mode value in row " + rowNumber + ": " + raw);

            return mode;
        } catch (IllegalArgumentException e) {
            throw new InvalidParsedSecondaryDataException("Invalid mode value in row " + rowNumber + ": " + raw);
        }
    }

    /**
     * Parses the CSV value into a {@link FuelType} instance.
     *
     * @param raw
     * @param rowNumber
     * @return
     * @throws InvalidParsedSecondaryDataException
     */
    private FuelType parseFuel(String raw, int rowNumber)
            throws InvalidParsedSecondaryDataException {
        try {
            FuelType fuel = FuelType.fromString(raw);
            if (fuel == null)
                throw new InvalidParsedSecondaryDataException("Invalid fuel value in row " + rowNumber + ": " + raw);

            return fuel;
        } catch (IllegalArgumentException e) {
            throw new InvalidParsedSecondaryDataException("Invalid fuel value in row " + rowNumber + ": " + raw);
        }
    }

    /**
     * Parses the CSV value into a {@link Unit} instance
     *
     * @param raw
     * @param rowNumber
     * @return
     * @throws InvalidParsedSecondaryDataException
     */
    private Unit parseUnit(String raw, int rowNumber)
            throws InvalidParsedSecondaryDataException {
        try {
            Unit unit = Unit.fromString(raw);
            if (unit == null)
                throw new InvalidParsedSecondaryDataException("Invalid unit value in row " + rowNumber + ": " + raw);

            return unit;
        } catch (IllegalArgumentException e) {
            throw new InvalidParsedSecondaryDataException("Invalid unit value in row " + rowNumber + ": " + raw);
        }
    }

    /**
     * Converts a raw numeric string into a double, supporting commas as decimal separators.
     *
     * @param val
     * @return
     */
    private double parseDouble(String val) {
        if (val == null || val.trim().isEmpty())
            return 0.0;

        return Double.parseDouble(val.replace(",", ".").trim());
    }

    /**
     * Validates that the unit provided in the CSV is compatible with the transport mode.
     *
     * - ROAD/RAIL/SEA/INLAND_WATERWAY require: KG_CO2E_PER_TKM
     * - AIR requires: KG_CO2E_PER_TKM
     * - HUB requires: KG_CO2E_PER_KWH or KG_CO2E_PER_TH
     *
     * @param unit
     * @param mode
     * @param rowNumber
     * @throws InvalidParsedSecondaryDataException
     */
    public void validateUnitForMode(Unit unit, Mode mode, int rowNumber)
            throws InvalidParsedSecondaryDataException {

        switch (mode) {
            case ROAD, RAIL, SEA, INLAND_WATERWAY -> {
                if (unit != Unit.KG_CO2E_PER_TKM) {
                    throw new InvalidParsedSecondaryDataException("Invalid unit " + unit + " for mode " + mode + " in row " + rowNumber);
                }
            }
            case AIR -> {
                if (unit != Unit.KG_CO2E_PER_TKM)
                    throw new InvalidParsedSecondaryDataException("Invalid unit " + unit + " for mode " + mode + " in row " + rowNumber);
            }
            case HUB -> {
                if (unit != Unit.KG_CO2E_PER_KWH && unit != Unit.KG_CO2E_PER_TH)
                    throw new InvalidParsedSecondaryDataException("Invalid unit " + unit + " for mode " + mode + " in row " + rowNumber);
            }
            default -> throw new InvalidParsedSecondaryDataException("Invalid mode " + mode + " with unit " + unit + " in row " + rowNumber);
        }
    }

    /**
     * Validates numeric values with max and min limits.
     *
     * @param value
     * @param maxAllowed
     * @param fieldName
     * @return
     * @throws InvalidParsedSecondaryDataException
     */
    public double validateMax(double value, double maxAllowed, String fieldName)
            throws InvalidParsedSecondaryDataException {
        if (value < 0 || value > maxAllowed) {
            throw new InvalidParsedSecondaryDataException("Invalid " + fieldName + " value.");
        }
        return value;
    }

    /**
     * Validates numeric values with min limits.
     * @param value
     * @param minAllowed
     * @param fieldName
     * @return
     * @throws InvalidParsedSecondaryDataException
     */
    public double validateMin(double value, double minAllowed, String fieldName)
            throws InvalidParsedSecondaryDataException {
        if (value < minAllowed) {
            throw new InvalidParsedSecondaryDataException("Invalid " + fieldName + " value.");
        }
        return value;
    }
}
