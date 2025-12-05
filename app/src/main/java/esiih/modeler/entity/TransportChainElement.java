package esiih.modeler.entity;

/**
 * Abstract class representing a generic element in a transport chain.
 */
public abstract class TransportChainElement {

    private final String id;
    private double emissionWTW;
    private double emissionWTT;
    private double emissionTTW;

    /**
     * Constructor for TransportChainElement.
     *
     * @param id Unique identifier for the transport chain element.
     */
    public TransportChainElement(String id) {
        this.id = id;
    }

    /**
     * Constructor for TransportChainElement.
     *
     * @param id          Unique identifier for the transport chain element.
     * @param emissionWTW Well-to-Wheel emissions.
     * @param emissionWTT Well-to-Tank emissions.
     * @param emissionTTW Tank-to-Wheel emissions.
     */
    public TransportChainElement(String id, double emissionWTW, double emissionWTT, double emissionTTW) {
        this.id = id;
        this.emissionWTW = emissionWTW;
        this.emissionWTT = emissionWTT;
        this.emissionTTW = emissionTTW;
    }

    /**
     * Gets the unique identifier of the transport chain element.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the Well-to-Wheel emissions of the transport chain element.
     */
    public double getEmissionWTW() {
        return emissionWTW;
    }

    /**
     * Sets the Well-to-Wheel emissions of the transport chain element.
     */
    public void setEmissionWTW(double emissionWTW) {
        this.emissionWTW = emissionWTW;
    }

    /**
     * Gets the Well-to-Tank emissions of the transport chain element.
     */
    public double getEmissionWTT() {
        return emissionWTT;
    }

    /**
     * Sets the Well-to-Tank emissions of the transport chain element.
     */
    public void setEmissionWTT(double emissionWTT) {
        this.emissionWTT = emissionWTT;
    }

    /**
     * Gets the Tank-to-Wheel emissions of the transport chain element.
     */
    public double getEmissionTTW() {
        return emissionTTW;
    }

    /**
     * Sets the Tank-to-Wheel emissions of the transport chain element.
     */
    public void setEmissionTTW(double emissionTTW) {
        this.emissionTTW = emissionTTW;
    }
}
