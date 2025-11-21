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
}
