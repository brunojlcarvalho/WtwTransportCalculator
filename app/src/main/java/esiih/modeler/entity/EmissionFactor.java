package esiih.modeler.entity;

import esiih.modeler.entity.enums.FuelType;
import esiih.modeler.entity.enums.Mode;
import esiih.modeler.entity.enums.Unit;

/**
 * Represents an emission factor for a specific transport mode and fuel type.
 */
public class EmissionFactor {

    private final Mode mode;
    private final FuelType fuelType;
    private final double payloadCategory;
    private final double emptyRunPercent;
    private final double ttwFactor;
    private final double wttFactor;
    private final Unit unit;
    private final double averageConsumption;

    /**
     * Constructor for EmissionFactor.
     *
     * @param mode Transport mode
     * @param fuelType Fuel type
     * @param payloadCategory Payload category in tons
     * @param emptyRunPercent Percentage of empty runs
     * @param ttwFactor Tank-to-Wheel emission factor
     * @param wttFactor Well-to-Tank emission factor
     * @param unit Unit of measurement
     * @param averageConsumption Average consumption value
     */
    public EmissionFactor(Mode mode, FuelType fuelType, double payloadCategory, double emptyRunPercent, double ttwFactor, double wttFactor, Unit unit, double averageConsumption) {
        this.mode = mode;
        this.fuelType = fuelType;
        this.payloadCategory = payloadCategory;
        this.emptyRunPercent = emptyRunPercent;
        this.ttwFactor = ttwFactor;
        this.wttFactor = wttFactor;
        this.unit = unit;
        this.averageConsumption = averageConsumption;
    }

    /**
     * Gets the Well-to-Wheel emission factor by summing WTT and TTW factors.
     *
     * @return Well-to-Wheel emission factor
     */
    public double getWTWFactor() {
        return ttwFactor + wttFactor;
    }

    /**
     * gets the transport mode.
     *
     * @return transport mode
     */
    public Mode getMode() {
        return mode;
    }

    /**
     * gets the fuel type.
     *
     * @return fuel type
     */
    public FuelType getFuelType() {
        return fuelType;
    }

    /**
     * gets the payload category.
     *
     * @return payload category
     */
    public double getPayloadCategory() {
        return payloadCategory;
    }

    /**
     * gets the empty run percent.
     *
     * @return empty run percent
     */
    public double getEmptyRunPercent() {
        return emptyRunPercent;
    }

    /**
     * gets the Tank-to-Wheel emission factor.
     *
     * @return Tank-to-Wheel emission factor
     */
    public double getTtwFactor() {
        return ttwFactor;
    }

    /**
     * gets the Well-to-Tank emission factor.
     *
     * @return Well-to-Tank emission factor
     */
    public double getWttFactor() {
        return wttFactor;
    }

    /**
     * gets the unit of measurement.
     *
     * @return unit of measurement
     */
    public Unit getUnit() {
        return unit;
    }

    /**
     * gets the average consumption.
     *
     * @return average consumption
     */
    public double getAverageConsumption() {
        return averageConsumption;
    }
}
