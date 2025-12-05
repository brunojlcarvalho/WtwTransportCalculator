package esiih.exporter.boundary;

import java.io.IOException;

import esiih.exporter.exception.InvalidExportException;
import esiih.modeler.entity.TransportChain;

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
    void exportReport(TransportChain transportChain, String filePath) throws IOException, InvalidExportException;
}
