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

    /**
     * Constructor initializing an empty transport chain.
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
     */
    public List<TransportChainElement> getElements() {
        return elements;
    }

    /**
     * Gets the total Well-to-Wheel emissions of the transport chain.
     */
    public double getTotalWTW() {
        return totalWTW;
    }

    /**
     * Sets the total Well-to-Wheel emissions of the transport chain.
     */
    public void setTotalWTW(double totalWTW) {
        this.totalWTW = totalWTW;
    }

    /**
     * Gets the intensity of the transport chain.
     */
    public double getIntensity() {
        return intensity;
    }

    /**
     * Sets the intensity of the transport chain.
     */
    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }
}
