package esiih.importer.controller;

/**
 * Responsible for validating the headers of the Primary Data CSV file.
 */
public class PrimaryHeaderParser {

    /**
     * Checks if the headers are valid for Primary Data.
     *
     * @param headers the array of header strings extracted from the CSV
     * @return true if the headers match the expected schema, false otherwise
     */
    public boolean isValidPrimaryDataHeaders(String[] headers) {
        String[] expectedHeaders = {
            "Etapa", "Tipo Operacao", "Veiculo Transporte", "Modo Transporte",
            "Distancia Carregado (km)", "Distancia Vazio (km)", "Carga Transportada (t)",
            "Carga Maxima (t)", "Tipo Combustivel", "Tempo (h)", "Litros Consumidos (l)",
            "Consumos (l/100tkm)", "Energia(kWh)"
        };

        if (headers == null || headers.length != expectedHeaders.length) {
            return false;
        }

        for (int i = 0; i < headers.length; i++) {
            if (!headers[i].trim().equalsIgnoreCase(expectedHeaders[i])) {
                return false;
            }
        }
        return true;
    }
}
