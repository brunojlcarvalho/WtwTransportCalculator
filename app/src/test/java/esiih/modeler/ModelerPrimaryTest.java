package esiih.modeler;

import esiih.importer.entity.ParsedPrimaryData;
import esiih.modeler.controller.Modeler;
import esiih.modeler.entity.HubOperation;
import esiih.modeler.entity.TransportChain;
import esiih.modeler.entity.TransportOperation;
import esiih.modeler.exception.InvalidParsePrimaryDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ModelerPrimaryTest {

    private Modeler modeler;
    private ParsedPrimaryData parsed;
    String PRIMARY_HEADER = "Etapa;Tipo Operacao;Veiculo Transporte;Modo Transporte;Distancia Carregado (km);Distancia Vazio (km);Carga Transportada (t);Carga Maxima (t);Tipo Combustivel;Tempo (h);Litros Consumidos (l);Consumos (l/100tkm);Energia(kWh);";

    @BeforeEach
    void setUp() {
        modeler = new Modeler();
        parsed = new ParsedPrimaryData();
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures an exception is thrown when the ParsedPrimaryData argument is null.
     * Validates the null-safety guard at the beginning of modelTransportChain().
     */
    @Test
    void modelTransportChain_nullParsedData_throwsException() {
        InvalidParsePrimaryDataException ex = assertThrows(
                InvalidParsePrimaryDataException.class,
                () -> modeler.modelTransportChain(null)
        );
        assertEquals("ParsedPrimaryData is null", ex.getMessage());
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures an exception is thrown when the CSV primary string is empty.
     * Confirms that an empty dataset is treated as invalid input.
     */
    @Test
    void modelTransportChain_emptyPrimaryString_throwsException() {
        parsed.setPrimaryDataString("");

        InvalidParsePrimaryDataException ex = assertThrows(
                InvalidParsePrimaryDataException.class,
                () -> modeler.modelTransportChain(parsed)
        );
        assertEquals("ParsedPrimaryData string is null or empty", ex.getMessage());
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Validates that a valid TO + HO input creates corresponding elements.
     * Confirms that both TransportOperation and HubOperation are instantiated.
     */
    @Test
    void modelTransportChain_validTOAndHO_createsElements() throws InvalidParsePrimaryDataException {

        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;Camiao;Estrada;100;50;10;20;Diesel;;;3,5;\n" +
                "2;HO;Hub;;;;10;;electricity;1;;;100;\n";

        parsed.setPrimaryDataString(primaryString);

        TransportChain chain = modeler.modelTransportChain(parsed);

        assertNotNull(chain);
        assertEquals(2, chain.getElements().size());
        assertTrue(chain.getElements().get(0) instanceof TransportOperation);
        assertTrue(chain.getElements().get(1) instanceof HubOperation);
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures an exception is thrown when vehicle type is missing.
     * Confirms enforcement of mandatory TO textual fields.
     */
    @Test
    void tovehicleTypeEmpty_throwsException() {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;;Estrada;100;50;10;20;Diesel;;;3,5;";

        parsed.setPrimaryDataString(primaryString);

        InvalidParsePrimaryDataException ex = assertThrows(
                InvalidParsePrimaryDataException.class,
                () -> modeler.modelTransportChain(parsed)
        );

        assertEquals("Vehicle type is null or empty", ex.getMessage());
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures an exception is thrown when vehicle type exceeds max length.
     * Confirms validation of TransportOperation string field limits.
     */
    @Test
    void to_vehicleTypeTooLong_throwsException() {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;AAAAAAAAAAAAAAAAAAAAAAA;Estrada;100;50;10;20;Diesel;;;3,5;";

        parsed.setPrimaryDataString(primaryString);

        InvalidParsePrimaryDataException ex = assertThrows(
                InvalidParsePrimaryDataException.class,
                () -> modeler.modelTransportChain(parsed)
        );

        assertEquals("Vehicle type length must be between 0 and 20", ex.getMessage());
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures invalid transport mode strings are detected.
     * Confirms Modeler rejects unknown Mode values.
     */
    @Test
    void to_invalidModeString_throwsException() {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;Camiao;TESTE;100;50;10;20;Diesel;;;3,5;";

        parsed.setPrimaryDataString(primaryString);

        InvalidParsePrimaryDataException ex = assertThrows(
                InvalidParsePrimaryDataException.class,
                () -> modeler.modelTransportChain(parsed)
        );

        assertEquals("Invalid mode in CSV file", ex.getMessage());
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures an exception is thrown when loaded distance exceeds limits.
     * Confirms numeric range validation for TransportOperation distances.
     */
    @Test
    void to_distanceLoadedTooHigh_throwsException() {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;Camiao;Estrada;25000;50;10;20;Diesel;;;3,5;";

        parsed.setPrimaryDataString(primaryString);

        InvalidParsePrimaryDataException ex = assertThrows(
                InvalidParsePrimaryDataException.class,
                () -> modeler.modelTransportChain(parsed)
        );

        assertEquals("Invalid distance loaded value.", ex.getMessage());
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures an exception is thrown when empty distance is negative.
     * Confirms Modeler prevents invalid negative values in TO fields.
     */
    @Test
    void to_distanceEmptyNegative_throwsException() {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;Camiao;Estrada;100;-10;10;20;Diesel;;;3,5;";

        parsed.setPrimaryDataString(primaryString);

        InvalidParsePrimaryDataException ex = assertThrows(
                InvalidParsePrimaryDataException.class,
                () -> modeler.modelTransportChain(parsed)
        );

        assertEquals("Invalid empty distance value.", ex.getMessage());
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures payload above allowed maximum triggers an exception.
     * Confirms Modeler enforces payload range validation.
     */
    @Test
    void to_payloadTooHigh_throwsException() {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;Camiao;Estrada;100;50;30;20;Diesel;;;3,5;";

        parsed.setPrimaryDataString(primaryString);

        InvalidParsePrimaryDataException ex = assertThrows(
                InvalidParsePrimaryDataException.class,
                () -> modeler.modelTransportChain(parsed)
        );

        assertEquals("Invalid payload value.", ex.getMessage());
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures max payload cannot be negative.
     * Confirms Modeler validates TO capacity values properly.
     */
    @Test
    void to_maxPayloadNegative_throwsException() {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;Camiao;Estrada;100;10;10;-40;Diesel;;;3,5;";

        parsed.setPrimaryDataString(primaryString);

        InvalidParsePrimaryDataException ex = assertThrows(
                InvalidParsePrimaryDataException.class,
                () -> modeler.modelTransportChain(parsed)
        );

        assertEquals("Invalid payload value.", ex.getMessage());
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures invalid or unknown fuel types are rejected.
     * Confirms proper validation of FuelType parsing.
     */
    @Test
    void to_invalidFuelString_throwsException() {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;Camiao;Estrada;100;50;10;20;TESTE;;;3,5;";

        parsed.setPrimaryDataString(primaryString);

        InvalidParsePrimaryDataException ex = assertThrows(
                InvalidParsePrimaryDataException.class,
                () -> modeler.modelTransportChain(parsed)
        );

        assertEquals("Invalid fuel mode in CSV file", ex.getMessage());
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures negative fuel consumption (liters) is not allowed.
     * Confirms correct numeric validation on consumption fields.
     */
    @Test
    void to_negativeLitersConsumed_throwsException() {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;Camiao;Estrada;100;50;10;20;Diesel;;-10;3,5;";

        parsed.setPrimaryDataString(primaryString);

        InvalidParsePrimaryDataException ex = assertThrows(
                InvalidParsePrimaryDataException.class,
                () -> modeler.modelTransportChain(parsed)
        );

        assertEquals("Invalid liters Consumed value.", ex.getMessage());
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures TO consumption (L/100tkm) above limit is rejected.
     * Confirms upper-bound validation for consumption values.
     */
    @Test
    void to_consumptionLitersTooHigh_throwsException() {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;Camiao;Estrada;100;50;10;20;Diesel;;;150;";

        parsed.setPrimaryDataString(primaryString);

        InvalidParsePrimaryDataException ex = assertThrows(
                InvalidParsePrimaryDataException.class,
                () -> modeler.modelTransportChain(parsed)
        );

        assertEquals("Invalid consumption liters value.", ex.getMessage());
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures HO duration beyond allowed maximum triggers error.
     * Confirms validation of HubOperation duration bounds.
     */
    @Test
    void ho_DurationTooHigh_throwsException() {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;HO;Hub;;;;10;;electricity;2500;;;100;";

        parsed.setPrimaryDataString(primaryString);

        InvalidParsePrimaryDataException ex = assertThrows(
                InvalidParsePrimaryDataException.class,
                () -> modeler.modelTransportChain(parsed)
        );

        assertEquals("Invalid duration value.", ex.getMessage());
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures HO energy consumption exceeding limits triggers error.
     * Confirms proper validation of HubOperation energy values.
     */
    @Test
    void ho_energyTooHigh_throwsException() {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;HO;Hub;;;;10;;electricity;1;;;200000;";

        parsed.setPrimaryDataString(primaryString);

        InvalidParsePrimaryDataException ex = assertThrows(
                InvalidParsePrimaryDataException.class,
                () -> modeler.modelTransportChain(parsed)
        );

        assertEquals("Invalid energy value.", ex.getMessage());
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)}, method with energy negative
     */
    @Test
    void ho_energyNegative_throwsException() {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;HO;Hub;;;;10;;electricity;1;;;-2;";

        parsed.setPrimaryDataString(primaryString);

        InvalidParsePrimaryDataException ex = assertThrows(
                InvalidParsePrimaryDataException.class,
                () -> modeler.modelTransportChain(parsed)
        );

        assertEquals("Invalid energy value.", ex.getMessage());
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures invalid energy source strings are detected in HO.
     * Confirms enforcement of FuelType validity for HubOperation.
     */
    @Test
    void ho_invalidEnergySource_throwsException() {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;HO;Hub;;;;10;;INVALID;1;;;100;";

        parsed.setPrimaryDataString(primaryString);

        InvalidParsePrimaryDataException ex = assertThrows(
                InvalidParsePrimaryDataException.class,
                () -> modeler.modelTransportChain(parsed)
        );

        assertEquals("Invalid hub energy source", ex.getMessage());
    }
}
