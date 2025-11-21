package esiih.importer.entity;

/**
 * Entity class for imported primary data.
 */
public class ParsedPrimaryData {

    private String primaryDataString;

    /**
     * Gets the primary data as a string.
     *
     * @return the primary data string
     */
    public String getPrimaryDataString() {
        return primaryDataString;
    }

    /**
     * Sets the primary data string.
     *
     * @param primaryDataString the primary data string to set
     */
    public void setPrimaryDataString(String primaryDataString) {
        this.primaryDataString = primaryDataString;
    }
}
