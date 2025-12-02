package esiih.exporter.boundary;

import esiih.modeler.entity.TransportChain;
import java.io.IOException;
import java.util.List;

public interface ExporterService {
    void exportTransportChains(List<TransportChain> transportChains, String filePath) throws IOException;
}