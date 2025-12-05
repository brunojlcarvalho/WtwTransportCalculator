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
     * Constructor for TransportOperation.
     * 
     * @param id                Unique identifier for the transport operation.
     * @param transportMode     Mode of transport.
     * @param vehicleType       Vehicle type description.
     * @param distanceLoadedKm  Distance traveled loaded (km).
     * @param distanceEmptyKm   Distance traveled empty (km).
     * @param payloadTon        Payload carried (tons).
     * @param maxCapacityTon    Maximum capacity (tons).
     * @param litersConsumed    Liters of fuel consumed.
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
     * 
     * @return The transport mode.
     */
    public Mode getTransportMode() {
        return transportMode;
    }

    /**
     * Sets the transport mode of the operation.
     * 
     * @param transportMode The transport mode to set.
     */
    public void setTransportMode(Mode transportMode) {
        this.transportMode = transportMode;
    }

    /**
     * Gets the vehicle type of the operation.
     * 
     * @return The vehicle type.
     */
    public String getVehicleType() {
        return vehicleType;
    }

    /**
     * Sets the vehicle type of the operation.
     * 
     * @param vehicleType The vehicle type to set.
     */
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    /**
     * Gets the distance loaded in kilometers.
     * 
     * @return The distance loaded in kilometers.
     */
    public double getDistanceLoadedKm() {
        return distanceLoadedKm;
    }

    /**
     * Sets the distance loaded in kilometers.
     * 
     * @param distanceLoadedKm The distance loaded to set.
     */
    public void setDistanceLoadedKm(double distanceLoadedKm) {
        this.distanceLoadedKm = distanceLoadedKm;
    }

    /**
     * Gets the distance empty in kilometers.
     * 
     * @return The distance empty in kilometers.
     */
    public double getDistanceEmptyKm() {
        return distanceEmptyKm;
    }

    /**
     * Sets the distance empty in kilometers.
     * 
     * @param distanceEmptyKm The distance empty to set.
     */
    public void setDistanceEmptyKm(double distanceEmptyKm) {
        this.distanceEmptyKm = distanceEmptyKm;
    }

    /**
     * Gets the payload in tons.
     * 
     * @return The payload in tons.
     */
    public double getPayloadTon() {
        return payloadTon;
    }

    /**
     * Sets the payload in tons.
     * 
     * @param payloadTon The payload to set.
     */
    public void setPayloadTon(double payloadTon) {
        this.payloadTon = payloadTon;
    }

    /**
     * Gets the fuel type of the operation.
     * 
     * @return The fuel type.
     */
    public FuelType getFuelType() {
        return fuelType;
    }

    /**
     * Sets the fuel type of the operation.
     * 
     * @param fuelType The fuel type to set.
     */
    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    /**
     * Gets the total distance (loaded + empty) in kilometers.
     * 
     * @return The total distance in kilometers.
     */
    public double getTotalDistance() {
        return distanceLoadedKm + distanceEmptyKm;
    }

    /**
     * Gets the maximum capacity in tons.
     * 
     * @return The maximum capacity in tons.
     */
    public double getMaxCapacityTon() {
        return maxCapacityTon;
    }

    /**
     * Sets the maximum capacity in tons.
     * 
     * @param maxPayloadTon The maximum capacity to set.
     */
    public void setMaxCapacityTon(double maxPayloadTon) {
        this.maxCapacityTon = maxPayloadTon;
    }

    /**
     * Gets the liters of fuel consumed.
     * 
     * @return The liters of fuel consumed.
     */
    public double getLitersConsumed() {
        return litersConsumed;
    }

    /**
     * Sets the liters of fuel consumed.
     * 
     * @param litersConsumed The liters of fuel consumed to set.
     */
    public void setLitersConsumed(double litersConsumed) {
        this.litersConsumed = litersConsumed;
    }

    /**
     * Gets the consumption in liters per ton-km.
     * 
     * @return The consumption in liters per ton-km.
     */
    public double getConsumptionLitTKm() {
        return consumptionLitTKm;
    }

    /**
     * Sets the consumption in liters per ton-km.
     * 
     * @param consumptionLitTonKm The consumption in liters per ton-km to set.
     */
    public void setConsumptionLitTKm(double consumptionLitTonKm) {
        this.consumptionLitTKm = consumptionLitTonKm;
    }
}