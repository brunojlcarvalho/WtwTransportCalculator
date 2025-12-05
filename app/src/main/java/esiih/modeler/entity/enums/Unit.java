package esiih.modeler.entity.enums;

/**
 * Enumeration representing various units of measurement used in transport
 * chain.
 */
public enum Unit {
    KILOMETERS("km"),
    TONS("t"),
    LITERS("l"),
    KWH("kWh"),
    HOURS("h"),
    KG_CO2E_PER_TKM("kg CO2e / t·km"),
    KG_CO2E_PER_TH("kg CO2e / t·h"),
    KG_CO2E_PER_KWH("kg CO2e / kWh equiv");

    private final String symbol;

    /**
     * Constructor for Unit enum.
     * 
     * @param symbol The symbol representing the unit.
     */
    Unit(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Gets the symbol representing the unit.
     * 
     * @return The symbol of the unit.
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Parses a string to return the corresponding Unit enum value.
     * 
     * @param text The input string representing the unit.
     * @return The corresponding Unit enum value, or null if not recognized.
     */
    public static Unit fromString(String text) {
        String normalized = text.trim();

        for (Unit u : Unit.values()) {
            if (u.symbol.equalsIgnoreCase(normalized)) {
                return u;
            }
        }
        if (normalized.contains("t·km") || normalized.contains("tkm")) {
            return KG_CO2E_PER_TKM;
        }
        if (normalized.contains("t·h")) {
            return KG_CO2E_PER_TH;
        }
        if (normalized.contains("kWh")) {
            return KG_CO2E_PER_KWH;
        }

        return null;
    }
}
