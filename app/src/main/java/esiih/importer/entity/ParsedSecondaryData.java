package esiih.importer.entity;

/**
 * Entity class for imported secondary data.
 */
public class ParsedSecondaryData {

    private String secondaryDataString;

    /**
     * Gets the secondary data as a string.
     *
     * @return the secondary data string
     */
    public String getSecondaryDataString() {
        return secondaryDataString;
    }

    /**
     * Sets the secondary data string.
     *
     * @param secondaryDataString the secondary data string to set
     */
    public void setSecondaryDataString(String secondaryDataString) {
        this.secondaryDataString = secondaryDataString;
    }
}
