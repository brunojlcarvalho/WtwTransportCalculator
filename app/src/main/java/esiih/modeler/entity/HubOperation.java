package esiih.modeler.entity;

import esiih.modeler.entity.enums.FuelType;
import esiih.modeler.entity.enums.Mode;

/**
 * Represents a hub operation within a transport chain.
 */
public class HubOperation extends TransportChainElement {

    private Mode hubMode;
    private FuelType energySource;
    private double energyConsumed;
    private double durationHours;

    /**
     * Constructor for HubOperation.
     *
     * @param id Unique identifier for the hub operation.
     * @param hubMode Mode of the hub operation.
     * @param energySource Energy source used during the operation. 
     * @param energyConsumed Amount of energy consumed during the operation.
     * @param durationHours Duration of the operation in hours.
     */
    public HubOperation(String id, Mode hubMode, FuelType energySource, double energyConsumed, double durationHours) {
        super(id);
        this.hubMode = hubMode != null ? hubMode : Mode.HUB;
        this.energySource = energySource;
        this.energyConsumed = energyConsumed;
        this.durationHours = durationHours;
    }

    /**
     * Gets the hub mode of the operation.
     */
    public Mode getHubMode() {
        return hubMode;
    }

    /**
     * Sets the hub mode of the operation.
     */
    public void setHubMode(Mode hubMode) {
        this.hubMode = hubMode;
    }

    /**
     * Gets the energy source of the operation.
     */
    public FuelType getEnergySource() {
        return energySource;
    }

    /**
     * Sets the energy source of the operation.
     */
    public void setEnergySource(FuelType energySource) {
        this.energySource = energySource;
    }

    /**
     * Gets the energy consumed during the operation.
     */
    public double getEnergyConsumed() {
        return energyConsumed;
    }

    /**
     * Sets the energy consumed during the operation.
     */
    public void setEnergyConsumed(double energyConsumed) {
        this.energyConsumed = energyConsumed;
    }

    /**
     * Gets the duration of the operation in hours.
     */
    public double getDurationHours() {
        return durationHours;
    }

    /**
     * Sets the duration of the operation in hours.
     */
    public void setDurationHours(double durationHours) {
        this.durationHours = durationHours;
    }
}