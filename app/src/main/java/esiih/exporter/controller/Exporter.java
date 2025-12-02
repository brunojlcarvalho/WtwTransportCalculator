package esiih.exporter.controller;

import esiih.exporter.boundary.ExporterService;
import esiih.modeler.entity.HubOperation;
import esiih.modeler.entity.TransportChain;
import esiih.modeler.entity.TransportChainElement;
import esiih.modeler.entity.TransportOperation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Exporter implements ExporterService {

    private static final String SEP = ";";
    private static final String NEW_LINE = "\n";

    private static final String HEADER = "Chain_ID" + SEP +
            "Chain_Description" + SEP +
            "TCE_ID" + SEP +
            "TCE_Type" + SEP +
            "Mode_Source" + SEP +
            "Activity_Info" + SEP +
            "WTW_kgCO2e" + SEP +
            "WTT_kgCO2e" + SEP +
            "TTW_kgCO2e" + SEP +
            "Chain_Total_WTW" + SEP +
            "Chain_Intensity";

    @Override
    public void exportTransportChains(List<TransportChain> chains, String filePath) throws IOException {

        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro: O caminho do ficheiro de exportação é inválido.");
        }
        if (chains == null) {
            throw new IllegalArgumentException("Erro: A lista de dados para exportar é nula.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            writer.write(HEADER);
            writer.write(NEW_LINE);

            if (chains.isEmpty()) {
                return;
            }

            for (TransportChain chain : chains) {
                if (chain == null)
                    continue;

                if (chain.getElements() == null || chain.getElements().isEmpty()) {
                    writer.write(buildSummaryLine(chain));
                    writer.write(NEW_LINE);
                    continue;
                }

                for (TransportChainElement element : chain.getElements()) {
                    if (element == null)
                        continue;

                    String line = buildDetailedLine(chain, element);
                    writer.write(line);
                    writer.write(NEW_LINE);
                }
            }
        } catch (IOException e) {
            throw new IOException(
                    "Erro ao escrever no ficheiro: " + filePath, e);
        }
    }

    private String buildDetailedLine(TransportChain chain, TransportChainElement element) {
        StringBuilder sb = new StringBuilder();

        sb.append(safeStr(chain.getId())).append(SEP);
        sb.append(safeStr(chain.getDescription())).append(SEP);
        sb.append(safeStr(element.getId())).append(SEP);

        if (element instanceof TransportOperation to) {
            sb.append("TO").append(SEP);
            sb.append(safeStr(to.getTransportMode())).append(SEP);
            String info = String.format(Locale.GERMAN, "%.0f km / %.0f t",
                    to.getDistanceLoadedKm(), to.getPayloadTon());
            sb.append(info).append(SEP);

        } else if (element instanceof HubOperation ho) {
            sb.append("HO").append(SEP);
            sb.append(safeStr(ho.getEnergySource())).append(SEP);
            String info = String.format(Locale.GERMAN, "%.0f h / %.0f kWh",
                    ho.getDurationHours(), ho.getEnergyConsumed());
            sb.append(info).append(SEP);

        } else {
            sb.append("UNKNOWN").append(SEP).append("N/A").append(SEP).append("N/A").append(SEP);
        }

        sb.append(fmt(element.getEmissionWTW())).append(SEP);
        sb.append(fmt(element.getEmissionWTT())).append(SEP);
        sb.append(fmt(element.getEmissionTTW())).append(SEP);

        sb.append(fmt(chain.getTotalWTW())).append(SEP);
        sb.append(fmt(chain.getIntensity()));

        return sb.toString();
    }

    private String buildSummaryLine(TransportChain chain) {
        return safeStr(chain.getId()) + SEP +
                safeStr(chain.getDescription()) + SEP +
                "N/A" + SEP + "N/A" + SEP + "N/A" + SEP + "N/A" + SEP +
                "0,00" + SEP + "0,00" + SEP + "0,00" + SEP +
                fmt(chain.getTotalWTW()) + SEP +
                fmt(chain.getIntensity());
    }

    private String fmt(double val) {
        return String.format(Locale.GERMAN, "%.2f", val);
    }

    private String safeStr(Object obj) {
        return obj == null ? "" : obj.toString();
    }
}