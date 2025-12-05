package esiih.modeler.entity.enums;

/**
 * Enum representing different modes of transportation.
 */
public enum Mode {
    ROAD, RAIL, SEA, AIR, HUB, INLAND_WATERWAY, UNKNOWN;

    /**
     * Parses a string to return the corresponding Mode enum value.
     * @param text The input string representing the mode.
     * @return The corresponding Mode enum value, or UNKNOWN if not recognized.
     */
    public static Mode fromString(String text) {
        if (text == null) {
            return UNKNOWN;
        }
        text = text.trim().toUpperCase();

        if (text.contains("ESTRADA") || text.contains("ROAD")) {
            return ROAD;
        }
        if (text.contains("VIA-FERREA") || text.contains("RAIL")) {
            return RAIL;
        }
        if (text.contains("MAR") || text.contains("SEA")) {
            return SEA;
        }
        if (text.contains("AR") || text.contains("AIR")) {
            return AIR;
        }
        if (text.contains("HUB")) {
            return HUB;
        }
        if (text.contains("INLAND") || text.contains("WATERWAY")) {
            return INLAND_WATERWAY;
        }

        return null;
    }
}
