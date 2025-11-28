package esiih.modeler;

import esiih.importer.entity.ParsedPrimaryData;
import esiih.modeler.controller.Modeler;
import esiih.modeler.entity.HubOperation;
import esiih.modeler.entity.TransportChain;
import esiih.modeler.entity.TransportOperation;
import esiih.modeler.exception.InvalidParsePrimaryDataException;
import esiih.modeler.exception.InvalidPrimaryFormatException;
import esiih.modeler.exception.InvalidPrimaryValueException;
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
     * Ensures an exception is thrown when the ParsedPrimaryData argument is null.
     * Validates the null-safety guard at the beginning of modelTransportChain().
     */
    @Test
    void modelTransportChain_nullParsedData_throwsException() {
        assertThrows(InvalidParsePrimaryDataException.class,
                () -> modeler.modelTransportChain(null)
        );
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures an exception is thrown when the CSV primary string is empty.
     * Confirms that an empty dataset is treated as invalid input.
     */
    @Test
    void modelTransportChain_emptyPrimaryString_throwsException() {
        parsed.setPrimaryDataString("");

        assertThrows(InvalidParsePrimaryDataException.class,
                () -> modeler.modelTransportChain(parsed)
        );
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

        assertThrows(InvalidPrimaryValueException.class,
                () -> modeler.modelTransportChain(parsed)
        );
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

        assertThrows(InvalidPrimaryValueException.class,
                () -> modeler.modelTransportChain(parsed)
        );
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures vehicle type with minimum valid length (1) is accepted.
     * Confirms lower boundary for Vehicle Type length is valid.
     */
    @Test
    void to_vehicleTypeMinLengthValid_createsTransportOperation() throws InvalidParsePrimaryDataException {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;A;Estrada;100;50;10;20;Diesel;;;3,5;";

        parsed.setPrimaryDataString(primaryString);

        TransportChain chain = modeler.modelTransportChain(parsed);

        assertNotNull(chain);
        assertEquals(1, chain.getElements().size());
        assertTrue(chain.getElements().get(0) instanceof TransportOperation);
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures vehicle type with maximum valid length (20) is accepted.
     * Confirms upper boundary for Vehicle Type length is valid.
     */
    @Test
    void to_vehicleTypeMaxLengthValid_createsTransportOperation() throws InvalidParsePrimaryDataException {
        String vehicleType20Chars = "AAAAAAAAAAAAAAAAAAAA"; // 20 caracteres
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;" + vehicleType20Chars + ";Estrada;100;50;10;20;Diesel;;;3,5;";

        parsed.setPrimaryDataString(primaryString);

        TransportChain chain = modeler.modelTransportChain(parsed);

        assertNotNull(chain);
        assertEquals(1, chain.getElements().size());
        assertTrue(chain.getElements().get(0) instanceof TransportOperation);
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

        assertThrows(InvalidPrimaryValueException.class,
                () -> modeler.modelTransportChain(parsed)
        );
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures an exception is thrown when loaded distance exceeds limits.
     * Confirms numeric range validation for TransportOperation distances.
     */
    @Test
    void to_distanceLoadedTooHigh_throwsException() {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;Camiao;Estrada;20001;50;10;20;Diesel;;;3,5;";

        parsed.setPrimaryDataString(primaryString);

        assertThrows(InvalidPrimaryValueException.class,
                () -> modeler.modelTransportChain(parsed)
        );
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures loaded distance equal to 0 km is rejected.
     * Confirms lower invalid boundary for distanceLoaded.
     */
    @Test
    void to_distanceLoaded_zero_throwsException() {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;Camiao;Estrada;0;50;10;20;Diesel;;;3,5;";

        parsed.setPrimaryDataString(primaryString);

        assertThrows(InvalidPrimaryValueException.class,
                () -> modeler.modelTransportChain(parsed)
        );
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures loaded distance at minimum valid boundary (0.01 km) is accepted.
     * Confirms lower valid boundary for distanceLoaded.
     */
    @Test
    void to_distanceLoaded_minValid_createsTransportOperation() throws InvalidParsePrimaryDataException {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;Camiao;Estrada;0,01;50;10;20;Diesel;;;3,5;";

        parsed.setPrimaryDataString(primaryString);

        TransportChain chain = modeler.modelTransportChain(parsed);

        assertNotNull(chain);
        assertEquals(1, chain.getElements().size());
        assertInstanceOf(TransportOperation.class, chain.getElements().get(0));
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures loaded distance at maximum valid boundary (20000 km) is accepted.
     * Confirms upper valid boundary for distanceLoaded.
     */
    @Test
    void to_distanceLoaded_maxValid_createsTransportOperation() throws InvalidParsePrimaryDataException {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;Camiao;Estrada;20000;50;10;20;Diesel;;;3,5;";

        parsed.setPrimaryDataString(primaryString);

        TransportChain chain = modeler.modelTransportChain(parsed);

        assertNotNull(chain);
        assertEquals(1, chain.getElements().size());
        assertTrue(chain.getElements().get(0) instanceof TransportOperation);
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures an exception is thrown when loaded distance exceeds limits.
     * Confirms numeric range validation for TransportOperation distances.
     */
    @Test
    void to_distanceEmptyTooHigh_throwsException(){
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;Camiao;Estrada;50;20001;10;20;Diesel;;;3,5;";

        parsed.setPrimaryDataString(primaryString);

        assertThrows(InvalidPrimaryValueException.class,
                () -> modeler.modelTransportChain(parsed)
        );
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures empty distance equal to 0 km is accepted.
     * Confirms that zero empty distance is treated as a valid TransportOperation input.
     */
    @Test
    void to_distanceEmpty_zero_throwsException()  throws InvalidParsePrimaryDataException  {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;Camiao;Estrada;50;0;10;20;Diesel;;;3,5;";

        parsed.setPrimaryDataString(primaryString);

        TransportChain chain = modeler.modelTransportChain(parsed);

        assertNotNull(chain);
        assertEquals(1, chain.getElements().size());
        assertTrue(chain.getElements().get(0) instanceof TransportOperation);
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures empty distance at minimum valid boundary (0.01 km) is accepted.
     * Confirms lower valid boundary for TransportOperation empty distance.
     */
    @Test
    void to_distanceEmpty_minValid_createsTransportOperation() throws InvalidParsePrimaryDataException {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;Camiao;Estrada;50;0,01;10;20;Diesel;;;3,5;";

        parsed.setPrimaryDataString(primaryString);

        TransportChain chain = modeler.modelTransportChain(parsed);

        assertNotNull(chain);
        assertEquals(1, chain.getElements().size());
        assertTrue(chain.getElements().get(0) instanceof TransportOperation);
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures empty distance at maximum valid boundary (20000 km) is accepted.
     * Confirms upper valid boundary for TransportOperation empty distance.
     */
    @Test
    void to_distanceEmpty_maxValid_createsTransportOperation() throws InvalidParsePrimaryDataException {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;Camiao;Estrada;50;20000;10;20;Diesel;;;3,5;";

        parsed.setPrimaryDataString(primaryString);

        TransportChain chain = modeler.modelTransportChain(parsed);

        assertNotNull(chain);
        assertEquals(1, chain.getElements().size());
        assertTrue(chain.getElements().get(0) instanceof TransportOperation);
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

        assertThrows(InvalidPrimaryValueException.class,
                () -> modeler.modelTransportChain(parsed)
        );
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

        assertThrows(InvalidPrimaryValueException.class,
                () -> modeler.modelTransportChain(parsed)
        );
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures payload equal to 0 t is rejected.
     * Confirms lower invalid boundary for payload.
     */
    @Test
    void to_payload_zero_throwsException() {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;Camiao;Estrada;100;50;0;20;Diesel;;;3,5;";

        parsed.setPrimaryDataString(primaryString);

        assertThrows(InvalidPrimaryValueException.class,
                () -> modeler.modelTransportChain(parsed)
        );
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures payload at minimum valid boundary (0.01 t) is accepted.
     * Confirms lower valid boundary for payload.
     */
    @Test
    void to_payload_minValid_createsTransportOperation() throws InvalidParsePrimaryDataException {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;Camiao;Estrada;100;50;0,01;20;Diesel;;;3,5;";

        parsed.setPrimaryDataString(primaryString);

        TransportChain chain = modeler.modelTransportChain(parsed);

        assertNotNull(chain);
        assertEquals(1, chain.getElements().size());
        assertTrue(chain.getElements().get(0) instanceof TransportOperation);
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures payload at maximum valid boundary (28 t) is accepted.
     * Confirms upper valid boundary for payload.
     */
    @Test
    void to_payload_maxValid_createsTransportOperation() throws InvalidParsePrimaryDataException {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;Camiao;Estrada;100;50;28;28;Diesel;;;3,5;";

        parsed.setPrimaryDataString(primaryString);

        TransportChain chain = modeler.modelTransportChain(parsed);

        assertNotNull(chain);
        assertEquals(1, chain.getElements().size());
        assertTrue(chain.getElements().get(0) instanceof TransportOperation);
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

        assertThrows(InvalidPrimaryValueException.class,
                () -> modeler.modelTransportChain(parsed)
        );
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures max payload equal to 0 t is rejected.
     * Confirms lower invalid boundary for maxPayload.
     */
    @Test
    void to_maxPayload_zero_throwsException() {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;Camiao;Estrada;100;50;10;0;Diesel;;;3,5;";

        parsed.setPrimaryDataString(primaryString);

        assertThrows(InvalidPrimaryValueException.class,
                () -> modeler.modelTransportChain(parsed)
        );
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures payload at minimum valid boundary (0.01 t) is accepted.
     * Confirms lower valid boundary for payload.
     */
    @Test
    void to_maxPayload_minValid_createsTransportOperation() throws InvalidParsePrimaryDataException {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;Camiao;Estrada;100;50;20;0,01;Diesel;;;3,5;";

        parsed.setPrimaryDataString(primaryString);

        TransportChain chain = modeler.modelTransportChain(parsed);

        assertNotNull(chain);
        assertEquals(1, chain.getElements().size());
        assertTrue(chain.getElements().get(0) instanceof TransportOperation);
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures payload at maximum valid boundary (28 t) is accepted.
     * Confirms upper valid boundary for payload.
     */
    @Test
    void to_maxPayload_maxValid_createsTransportOperation() throws InvalidParsePrimaryDataException {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;Camiao;Estrada;100;50;28;28;Diesel;;;3,5;";

        parsed.setPrimaryDataString(primaryString);

        TransportChain chain = modeler.modelTransportChain(parsed);

        assertNotNull(chain);
        assertEquals(1, chain.getElements().size());
        assertTrue(chain.getElements().get(0) instanceof TransportOperation);
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

        assertThrows(InvalidPrimaryValueException.class,
                () -> modeler.modelTransportChain(parsed)
        );
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

        assertThrows(InvalidPrimaryValueException.class,
                () -> modeler.modelTransportChain(parsed)
        );
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures liters consumed equal to 0 is rejected.
     * Confirms lower invalid boundary for liters consumed.
     */
    @Test
    void to_litersConsumed_zero_throwsException() {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;Camiao;Estrada;100;50;10;20;Diesel;;0;;";

        parsed.setPrimaryDataString(primaryString);

        assertThrows(InvalidPrimaryValueException.class,
                () -> modeler.modelTransportChain(parsed)
        );
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures liters consumed at minimum valid boundary (0.01) is accepted.
     * Confirms lower valid boundary for liters consumed.
     */
    @Test
    void to_litersConsumed_minValid_createsTransportOperation() throws InvalidParsePrimaryDataException {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;Camiao;Estrada;100;50;10;20;Diesel;;0,01;3,5;";

        parsed.setPrimaryDataString(primaryString);

        TransportChain chain = modeler.modelTransportChain(parsed);

        assertNotNull(chain);
        assertEquals(1, chain.getElements().size());
        assertTrue(chain.getElements().get(0) instanceof TransportOperation);
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures liters consumed at maximum valid boundary (10000000) is accepted.
     * Confirms upper valid boundary for liters consumed.
     */
    @Test
    void to_litersConsumed_maxValid_createsTransportOperation() throws InvalidParsePrimaryDataException {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;Camiao;Estrada;100;50;10;20;Diesel;;10000000;3,5;";

        parsed.setPrimaryDataString(primaryString);

        TransportChain chain = modeler.modelTransportChain(parsed);

        assertNotNull(chain);
        assertEquals(1, chain.getElements().size());
        assertTrue(chain.getElements().get(0) instanceof TransportOperation);
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

        assertThrows(InvalidPrimaryValueException.class,
                () -> modeler.modelTransportChain(parsed)
        );
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures consumption l/100tkm equal to 0 is rejected.
     * Confirms lower invalid boundary for consumption liters.
     */
    @Test
    void to_consumptionLiters_zero_throwsException() {
        String primaryString = PRIMARY_HEADER + "\n" +
                "5;TO;Camiao;Estrada;50;50;15;20;Diesel;;;0;;";

        parsed.setPrimaryDataString(primaryString);

        assertThrows(InvalidPrimaryValueException.class,
                () -> modeler.modelTransportChain(parsed)
        );
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures consumption at minimum valid boundary (0.01 l/100tkm) is accepted.
     * Confirms lower valid boundary for consumption liters.
     */
    @Test
    void to_consumptionLiters_minValid_createsTransportOperation() throws InvalidParsePrimaryDataException {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;Camiao;Estrada;100;50;10;20;Diesel;;;0,01;";

        parsed.setPrimaryDataString(primaryString);

        TransportChain chain = modeler.modelTransportChain(parsed);

        assertNotNull(chain);
        assertEquals(1, chain.getElements().size());
        assertTrue(chain.getElements().get(0) instanceof TransportOperation);
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures consumption at maximum valid boundary (100 l/100tkm) is accepted.
     * Confirms upper valid boundary for consumption liters.
     */
    @Test
    void to_consumptionLiters_maxValid_createsTransportOperation() throws InvalidParsePrimaryDataException {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;TO;Camiao;Estrada;100;50;10;20;Diesel;;;100;";

        parsed.setPrimaryDataString(primaryString);

        TransportChain chain = modeler.modelTransportChain(parsed);

        assertNotNull(chain);
        assertEquals(1, chain.getElements().size());
        assertTrue(chain.getElements().get(0) instanceof TransportOperation);
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures HO duration beyond allowed maximum triggers error.
     * Confirms validation of HubOperation duration bounds.
     */
    @Test
    void ho_DurationTooHigh_throwsException() {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;HO;Hub;;;;10;;electricity;2001;;;100;";

        parsed.setPrimaryDataString(primaryString);

        assertThrows(InvalidPrimaryValueException.class,
                () -> modeler.modelTransportChain(parsed)
        );
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures HO duration equal to 0 h is rejected.
     * Confirms lower invalid boundary for HubOperation duration.
     */
    @Test
    void ho_duration_zero_throwsException() {
        String primaryString = PRIMARY_HEADER + "\n" +
                "2;HO;Hub;;;;8;;electricity;0;;;100;";

        parsed.setPrimaryDataString(primaryString);

        assertThrows(InvalidPrimaryValueException.class,
                () -> modeler.modelTransportChain(parsed)
        );
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures HO duration at minimum valid boundary (0.01 h) is accepted.
     * Confirms lower valid boundary for HubOperation duration.
     */
    @Test
    void ho_duration_minValid_createsHubOperation() throws InvalidParsePrimaryDataException {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;HO;Hub;;;;10;;electricity;0,01;;;100;";

        parsed.setPrimaryDataString(primaryString);

        TransportChain chain = modeler.modelTransportChain(parsed);

        assertNotNull(chain);
        assertEquals(1, chain.getElements().size());
        assertTrue(chain.getElements().get(0) instanceof HubOperation);
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures HO duration at maximum valid boundary (2000 h) is accepted.
     * Confirms upper valid boundary for HubOperation duration.
     */
    @Test
    void ho_duration_maxValid_createsHubOperation() throws InvalidParsePrimaryDataException {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;HO;Hub;;;;10;;electricity;2000;;;100;";

        parsed.setPrimaryDataString(primaryString);

        TransportChain chain = modeler.modelTransportChain(parsed);

        assertNotNull(chain);
        assertEquals(1, chain.getElements().size());
        assertTrue(chain.getElements().get(0) instanceof HubOperation);
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures HO energy consumption exceeding limits triggers error.
     * Confirms proper validation of HubOperation energy values.
     */
    @Test
    void ho_energyTooHigh_throwsException() {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;HO;Hub;;;;10;;electricity;1;;;100001;";

        parsed.setPrimaryDataString(primaryString);

        assertThrows(InvalidPrimaryValueException.class,
                () -> modeler.modelTransportChain(parsed)
        );
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures HO energy equal to 0 kWh is rejected.
     * Confirms lower invalid boundary for HubOperation energy.
     */
    @Test
    void ho_energy_zero_throwsException() {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;HO;Hub;;;;10;;electricity;1;;;0;";

        parsed.setPrimaryDataString(primaryString);

        assertThrows(InvalidPrimaryValueException.class,
                () -> modeler.modelTransportChain(parsed)
        );
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures HO energy at minimum valid boundary (0.01 kWh) is accepted.
     * Confirms lower valid boundary for HubOperation energy.
     */
    @Test
    void ho_energy_minValid_createsHubOperation() throws InvalidParsePrimaryDataException {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;HO;Hub;;;;10;;electricity;1;;;0,01;";

        parsed.setPrimaryDataString(primaryString);

        TransportChain chain = modeler.modelTransportChain(parsed);

        assertNotNull(chain);
        assertEquals(1, chain.getElements().size());
        assertTrue(chain.getElements().get(0) instanceof HubOperation);
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures HO energy at maximum valid boundary (100000 kWh) is accepted.
     * Confirms upper valid boundary for HubOperation energy.
     */
    @Test
    void ho_energy_maxValid_createsHubOperation() throws InvalidParsePrimaryDataException {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;HO;Hub;;;;10;;electricity;1;;;100000;";

        parsed.setPrimaryDataString(primaryString);

        TransportChain chain = modeler.modelTransportChain(parsed);

        assertNotNull(chain);
        assertEquals(1, chain.getElements().size());
        assertTrue(chain.getElements().get(0) instanceof HubOperation);
    }

    /**
     * Test the {@link Modeler#modelTransportChain(ParsedPrimaryData)},
     * Ensures an exception is thrown when HO energy consumption is negative.
     * Confirms that HubOperation energy values must be non-negative and within valid bounds.
     */
    @Test
    void ho_energyNegative_throwsException() {
        String primaryString = PRIMARY_HEADER + "\n" +
                "1;HO;Hub;;;;10;;electricity;1;;;-2;";

        parsed.setPrimaryDataString(primaryString);

        assertThrows(InvalidPrimaryValueException.class,
                () -> modeler.modelTransportChain(parsed)
        );
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

        assertThrows(InvalidPrimaryValueException.class,
                () -> modeler.modelTransportChain(parsed)
        );
    }
}
