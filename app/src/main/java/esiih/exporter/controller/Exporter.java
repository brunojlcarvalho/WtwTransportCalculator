package esiih.exporter.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import esiih.exporter.boundary.ExporterService;
import esiih.exporter.exception.InvalidExportException;
import esiih.modeler.entity.HubOperation;
import esiih.modeler.entity.TransportChain;
import esiih.modeler.entity.TransportChainElement;
import esiih.modeler.entity.TransportOperation;

/**
 * Exporter Implementation.
 */
public class Exporter implements ExporterService {

    private static final String SEP = ";";
    private static final String NEW_LINE = "\n";

    private static final String HEADER = "Element_ID" + SEP
            + "Element_Type" + SEP
            + "Mode_Source" + SEP
            + "Activity_Info" + SEP
            + "WTW_kgCO2e" + SEP
            + "WTT_kgCO2e" + SEP
            + "TTW_kgCO2e" + SEP
            + "Chain_Total_WTW" + SEP
            + "Chain_Intensity";

    /**
     * Export the transport chain data to a CSV file.
     */
    @Override
    public void exportReport(TransportChain chain, String filePath) throws IOException, InvalidExportException {

        if (filePath == null || filePath.trim().isEmpty()) {
            throw new InvalidExportException("Error: invalid file path.");
        }
        if (chain == null) {
            throw new InvalidExportException("Error: NULL transport chain.");
        }

        File file = new File(filePath);
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            writer.write(HEADER);
            writer.write(NEW_LINE);

            List<TransportChainElement> elements = chain.getElements();

            if (elements == null || elements.isEmpty()) {
                throw new InvalidExportException("Error: TransportChain is empty or has null elements list.");
            }
            for (TransportChainElement element : elements) {

                if (element == null) {
                    throw new InvalidExportException("Error: TransportChain contains null elements.");
                }

                StringBuilder sb = new StringBuilder();

                sb.append(element.getId() != null ? element.getId() : "").append(SEP);

                if (element instanceof TransportOperation to) {
                    sb.append("TO").append(SEP);
                    sb.append(to.getTransportMode() != null ? to.getTransportMode().toString() : "").append(SEP);

                    String info = String.format(Locale.ROOT, "%.0f km / %.0f t",
                            to.getDistanceLoadedKm(), to.getPayloadTon());
                    sb.append(info).append(SEP);

                } else if (element instanceof HubOperation ho) {
                    sb.append("HO").append(SEP);
                    sb.append(ho.getEnergySource() != null ? ho.getEnergySource().toString() : "").append(SEP);

                    String info = String.format(Locale.ROOT, "%.0f h / %.0f kWh",
                            ho.getDurationHours(), ho.getEnergyConsumed());
                    sb.append(info).append(SEP);

                } else {
                    throw new InvalidExportException(
                            "Error: Unknown element type encountered: " + element.getClass().getName());
                }

                sb.append(String.format(Locale.ROOT, "%.2f", element.getEmissionWTW())).append(SEP);
                sb.append(String.format(Locale.ROOT, "%.2f", element.getEmissionWTT())).append(SEP);
                sb.append(String.format(Locale.ROOT, "%.2f", element.getEmissionTTW())).append(SEP);

                sb.append(String.format(Locale.ROOT, "%.2f", chain.getTotalWTW())).append(SEP);
                sb.append(String.format(Locale.ROOT, "%.2f", chain.getIntensity()));

                writer.write(sb.toString());
                writer.write(NEW_LINE);
            }

        } catch (IOException e) {
            throw new IOException("Error creating the file: " + filePath, e);
        } catch (Exception e) {
            if (e instanceof InvalidExportException invalidExportException) {
                throw invalidExportException;
            }
            throw new InvalidExportException("Error exporting data: " + e.getMessage(), e);
        }
    }
}
