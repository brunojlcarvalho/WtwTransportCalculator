package esiih.exporter.boundary;

import esiih.modeler.entity.TransportChain;
import java.io.IOException;

/**
 * Exporter Service Interface
 */
public interface ExporterService {

    /**
     * Export the transport chain data to a CSV file
     * 
     * @param transportChain
     * @param filePath
     * @throws IOException
     */
    void exportTransportChain(TransportChain transportChain, String filePath) throws IOException;
}