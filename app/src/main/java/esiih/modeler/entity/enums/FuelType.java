package esiih.modeler.entity.enums;

/**
 * Enumeration representing different types of fuel with emission factors.
 */
public enum FuelType {
    DIESEL(3.16, 0.38),
    ELECTRICITY(0.0, 0.35),
    HFO(3.08, 0.66),
    LNG(2.77, 1.21),
    JET_KEROSENE(2.54, 0.67);

    private final double ttwFactor; // kgCO2e/Unit (Liter or kWh)
    private final double wttFactor; // kgCO2e/Unit (Liter or kWh)

    /**
     * Constructor for FuelType enum.
     *
     * @param ttwFactor Tank-to-Wheel emission factor in kgCO2e/Unit.
     * @param wttFactor Well-to-Tank emission factor in kgCO2e/Unit.
     */
    FuelType(double ttwFactor, double wttFactor) {
        this.ttwFactor = ttwFactor;
        this.wttFactor = wttFactor;
    }

    /**
     * Gets the Well-to-Wheel emission factor.
     *
     * @return The WTW emission factor in kgCO2e/Unit.
     */
    public double getWtwFactor() {
        return ttwFactor + wttFactor;
    }

    /**
     * Gets the Tank-to-Wheel emission factor.
     *
     * @return The TTW emission factor in kgCO2e/Unit.
     */
    public double getTtwFactor() {
        return ttwFactor;
    }

    /**
     * Gets the Well-to-Tank emission factor.
     *
     * @return The WTT emission factor in kgCO2e/Unit.
     */
    public double getWttFactor() {
        return wttFactor;
    }

    /**
     * Creates a FuelType enum from a string representation.
     *
     * @param text The string representation of the fuel type.
     * @return The corresponding FuelType enum, or null if not recognized.
     */
    public static FuelType fromString(String text) {
        text = text.trim().toUpperCase();

        if (text.contains("DIESEL")) {
            return DIESEL;
        }
        if (text.contains("ELET") || text.contains("ELECT")) {
            return ELECTRICITY;
        }
        if (text.contains("HFO")) {
            return HFO;
        }
        if (text.contains("LNG")) {
            return LNG;
        }
        if (text.contains("JET") || text.contains("KEROSENE")) {
            return JET_KEROSENE;
        }
        return null;
    }
}
