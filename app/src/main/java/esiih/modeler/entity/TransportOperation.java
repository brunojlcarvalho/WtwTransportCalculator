package esiih.modeler.entity;

import esiih.modeler.entity.enums.FuelType;
import esiih.modeler.entity.enums.Mode;

/**
 * Represents a transport operation within a transport chain.
 */

public class TransportOperation extends TransportChainElement {

    private Mode transportMode;
    private String vehicleType;
    private double distanceLoadedKm;
    private double distanceEmptyKm;
    private double payloadTon;
    private double maxCapacityTon;
    private double litersConsumed;
    private double consumptionLitTKm;
    private FuelType fuelType;

    /**
     * Constructor for TransportOperation.
     *
     * @param id Unique identifier for the transport operation.
     */
    public TransportOperation(String id) {
        super(id);
    }

    /**
     * constructor for TransportOperation.
     *
     * @param id                Unique identifier for the transport operation.
     * @param transportMode     Mode of transport.
     * @param vehicleType       Vehicle type description.
     * @param distanceLoadedKm  Distance traveled loaded (km).
     * @param distanceEmptyKm   Distance traveled empty (km).
     * @param payloadTon        Payload carried (tons).
     * @param maxCapacityTon    Maximum capacity (tons).
     * @param litersConsumed    Liters consumed by this operation.
     * @param consumptionLitTKm Consumption in liters per ton-km.
     * @param fuelType          Fuel type used.
     */
    public TransportOperation(String id,
            Mode transportMode,
            String vehicleType,
            double distanceLoadedKm,
            double distanceEmptyKm,
            double payloadTon,
            double maxCapacityTon,
            double litersConsumed,
            double consumptionLitTKm,
            FuelType fuelType) {
        super(id);
        this.transportMode = transportMode;
        this.vehicleType = vehicleType;
        this.distanceLoadedKm = distanceLoadedKm;
        this.distanceEmptyKm = distanceEmptyKm;
        this.payloadTon = payloadTon;
        this.maxCapacityTon = maxCapacityTon;
        this.litersConsumed = litersConsumed;
        this.consumptionLitTKm = consumptionLitTKm;
        this.fuelType = fuelType;
    }

    /**
     * Gets the transport mode of the operation.
     */
    public Mode getTransportMode() {
        return transportMode;
    }

    /**
     * Sets the transport mode of the operation.
     */
    public void setTransportMode(Mode transportMode) {
        this.transportMode = transportMode;
    }

    /**
     * Gets the vehicle type of the operation.
     */
    public String getVehicleType() {
        return vehicleType;
    }

    /**
     * Sets the vehicle type of the operation.
     */
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    /**
     * Gets the distance loaded in kilometers.
     */
    public double getDistanceLoadedKm() {
        return distanceLoadedKm;
    }

    /**
     * Sets the distance loaded in kilometers.
     */
    public void setDistanceLoadedKm(double distanceLoadedKm) {
        this.distanceLoadedKm = distanceLoadedKm;
    }

    /**
     * Gets the distance empty in kilometers.
     */
    public double getDistanceEmptyKm() {
        return distanceEmptyKm;
    }

    /**
     * Sets the distance empty in kilometers.
     */
    public void setDistanceEmptyKm(double distanceEmptyKm) {
        this.distanceEmptyKm = distanceEmptyKm;
    }

    /**
     * Gets the payload in tons.
     */
    public double getPayloadTon() {
        return payloadTon;
    }

    /**
     * Sets the payload in tons.
     */
    public void setPayloadTon(double payloadTon) {
        this.payloadTon = payloadTon;
    }

    /**
     * Gets the fuel type of the operation.
     */
    public FuelType getFuelType() {
        return fuelType;
    }

    /**
     * Sets the fuel type of the operation.
     */
    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    /**
     * Gets the total distance (loaded + empty) in kilometers.
     */
    public double getTotalDistance() {
        return distanceLoadedKm + distanceEmptyKm;
    }

    public double getMaxCapacityTon() {
        return maxCapacityTon;
    }

    public void setMaxCapacityTon(double maxPayloadTon) {
        this.maxCapacityTon = maxPayloadTon;
    }

    public double getLitersConsumed() {
        return litersConsumed;
    }

    public void setLitersConsumed(double litersConsumed) {
        this.litersConsumed = litersConsumed;
    }

    public double getConsumptionLitTKm() {
        return consumptionLitTKm;
    }

    public void setConsumptionLitTKm(double consumptionLitTonKm) {
        this.consumptionLitTKm = consumptionLitTonKm;
    }
}