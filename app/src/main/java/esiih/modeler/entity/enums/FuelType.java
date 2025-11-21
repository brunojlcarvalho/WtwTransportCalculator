package esiih.modeler.entity.enums;

/**
 * Enumeration representing different types of fuel.
 */
public enum FuelType {
    DIESEL, ELECTRICITY, HFO, LNG, JET_KEROSENE, UNKNOWN;

    public static FuelType fromString(String text) {
        if (text == null) {
            return UNKNOWN;
        }
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

        return UNKNOWN;
    }
}
