package esiih.modeler.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a transport chain consisting of multiple transport chain
 * elements(To and Ho).
 */
public class TransportChain {

    private final List<TransportChainElement> elements;
    private double totalWTW;
    private double intensity;
    private String id;
    private String description;

    /**
     * Constructor for TransportChain.
     * 
     * @param id Unique identifier for the transport chain.
     * @param description Description of the transport chain.
     * @param elements List of transport chain elements.
     * @param totalWTW Total Well-to-Wheel emissions of the transport chain.
     * @param intensity Intensity of the transport chain.
     */
    public TransportChain(String id, String description, List<TransportChainElement> elements, double totalWTW,
            double intensity) {
        this.id = id;
        this.description = description;
        this.elements = elements != null ? elements : new ArrayList<>();
        this.totalWTW = totalWTW;
        this.intensity = intensity;
    }

    /**
     * Default constructor for TransportChain.
     */
    public TransportChain() {
        this.elements = new ArrayList<>();
    }

    /**
     * Adds a transport chain element to the transport chain.
     *
     * @param element The transport chain element to add.
     */
    public void addElement(TransportChainElement element) {
        this.elements.add(element);
    }

    /**
     * Gets the list of transport chain elements.
     * 
     * @return The list of transport chain elements.
     */
    public List<TransportChainElement> getElements() {
        return elements;
    }

    /**
     * Gets the unique identifier of the transport chain.
     * 
     * @return the unique identifier of the transport chain
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the description of the transport chain.
     * 
     * @return the description of the transport chain
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the total Well-to-Wheel emissions of the transport chain.
     * 
     * @return the total Well-to-Wheel emissions of the transport chain
     */
    public double getTotalWTW() {
        return totalWTW;
    }

    /**
     * Sets the total Well-to-Wheel emissions of the transport chain.
     * 
     * @param totalWTW the total Well-to-Wheel emissions to set
     */
    public void setTotalWTW(double totalWTW) {
        this.totalWTW = totalWTW;
    }

    /**
     * Gets the intensity of the transport chain.
     * 
     * @return the intensity of the transport chain
     */
    public double getIntensity() {
        return intensity;
    }

    /**
     * Sets the intensity of the transport chain.
     * 
     * @param intensity the intensity to set
     */
    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }
}