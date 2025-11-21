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
    KG_CO2E_PER_KWH("kg CO2e / kWh equiv"),
    UNKNOWN("unknown");

    private final String symbol;

    Unit(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static Unit fromString(String text) {
        if (text == null) {
            return UNKNOWN;
        }
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

        return UNKNOWN;
    }
}
