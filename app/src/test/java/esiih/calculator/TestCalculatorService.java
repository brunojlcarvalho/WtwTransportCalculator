package esiih.calculator;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import esiih.calculator.boundary.CalculatorService;
import esiih.calculator.controller.Calculator;
import esiih.calculator.exception.InvalidCalculationException;
import esiih.modeler.entity.EmissionFactor;
import esiih.modeler.entity.HubOperation;
import esiih.modeler.entity.TransportChain;
import esiih.modeler.entity.TransportChainElement;
import esiih.modeler.entity.TransportOperation;
import esiih.modeler.entity.enums.FuelType;
import esiih.modeler.entity.enums.Mode;
import esiih.modeler.entity.enums.Unit;

/**
 * Test class for {@link CalculatorService}.
 */
public class TestCalculatorService {

    private CalculatorService calculatorService;
    private TransportChain transportChain;
    private List<EmissionFactor> emissionFactors;

    /**
     * Sets up the test environment. Creates an instance of {@link Calculator},
     * a {@link TransportChain}, and a list of {@link EmissionFactor}.
     */
    @BeforeEach
    void setUp() {
        calculatorService = new Calculator();
        transportChain = new TransportChain();
        emissionFactors = new ArrayList<>();
    }

    /**
     * Tests the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a null TransportChain throws a InvalidCalculationException.
     */
    @Test
    void nullTransportChain_throwsInvalidCalculationException() {
        assertThrows(InvalidCalculationException.class, () -> {
            calculatorService.calculateGEE(null, emissionFactors);
        });
    }

    /**
     * Tests the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that an empty TransportChain (with no elements) throws an
     * InvalidCalculationException.
     */
    @Test
    void emptyTransportChain_throwsInvalidCalculationException() {
        assertThrows(InvalidCalculationException.class,
                () -> calculatorService.calculateGEE(transportChain, emissionFactors));
    }

    /**
     * Tests the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a null EmissionFactors list throws a
     * InvalidCalculationException.
     */
    @Test
    void nullEmissionFactors_throwsInvalidCalculationException() {
        TransportOperation to = new TransportOperation("TO-1", Mode.ROAD, null, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                FuelType.DIESEL);
        transportChain.addElement(to);

        assertThrows(InvalidCalculationException.class, () -> {
            calculatorService.calculateGEE(transportChain, null);
        });
    }

    /**
     * Tests the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that an empty EmissionFactors list throws an
     * InvalidCalculationException.
     */
    @Test
    void emptyEmissionFactors_throwsInvalidCalculationException() {
        TransportOperation to = new TransportOperation("TO-1", Mode.ROAD, null, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                FuelType.DIESEL);
        transportChain.addElement(to);

        assertThrows(InvalidCalculationException.class,
                () -> calculatorService.calculateGEE(transportChain, emissionFactors));
    }

    /**
     * Tests the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a TransportOperation without a transport mode throws an
     * InvalidCalculationException.
     */
    @Test
    void transportOperationWithoutMode_throwsInvalidCalculationException() {
        TransportOperation to = new TransportOperation("TO-1", null, null, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                FuelType.DIESEL);
        transportChain.addElement(to);

        EmissionFactor factor = new EmissionFactor(Mode.ROAD, FuelType.DIESEL, 10.0, 0.0, 0.5, 0.1,
                Unit.KG_CO2E_PER_TKM, 0.0);
        emissionFactors.add(factor);

        assertThrows(InvalidCalculationException.class,
                () -> calculatorService.calculateGEE(transportChain, emissionFactors));
    }

    /**
     * Tests the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a TransportOperation without a fuel type throws an
     * InvalidCalculationException.
     */
    @Test
    void transportOperationWithoutFuelType_throwsInvalidCalculationException() {
        TransportOperation to = new TransportOperation("TO-1", Mode.ROAD, null, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                null);
        transportChain.addElement(to);

        EmissionFactor factor = new EmissionFactor(Mode.ROAD, FuelType.DIESEL, 10.0, 0.0, 0.5, 0.1,
                Unit.KG_CO2E_PER_TKM, 0.0);
        emissionFactors.add(factor);

        assertThrows(InvalidCalculationException.class,
                () -> calculatorService.calculateGEE(transportChain, emissionFactors));
    }

    /**
     * Tests the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a HubOperation without a hub mode throws an
     * InvalidCalculationException.
     */
    @Test
    void hubOperationWithoutMode_throwsInvalidCalculationException() {
        HubOperation ho = new HubOperation("HO-1", Mode.HUB, FuelType.ELECTRICITY, 100.0, 1.0);
        ho.setHubMode(null);
        transportChain.addElement(ho);

        EmissionFactor factor = new EmissionFactor(Mode.HUB, FuelType.ELECTRICITY, 0.0, 0.0, 0.0, 0.35,
                Unit.KG_CO2E_PER_KWH, 0.0);
        emissionFactors.add(factor);

        assertThrows(InvalidCalculationException.class,
                () -> calculatorService.calculateGEE(transportChain, emissionFactors));
    }

    /**
     * Tests the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a HubOperation without an energy source throws an
     * InvalidCalculationException.
     */
    @Test
    void hubOperationWithoutEnergySource_throwsInvalidCalculationException() {
        HubOperation ho = new HubOperation("HO-1", Mode.HUB, null, 100.0, 1.0);
        transportChain.addElement(ho);

        EmissionFactor factor = new EmissionFactor(Mode.HUB, FuelType.ELECTRICITY, 0.0, 0.0, 0.0, 0.35,
                Unit.KG_CO2E_PER_KWH, 0.0);
        emissionFactors.add(factor);

        assertThrows(InvalidCalculationException.class,
                () -> calculatorService.calculateGEE(transportChain, emissionFactors));

    }

    /**
     * Tests the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a missing EmissionFactor for a TransportOperation throws an
     * InvalidCalculationException.
     */
    @Test
    void missingEmissionFactorForTransportOperation_throwsInvalidCalculationException() {
        TransportOperation to = new TransportOperation("TO-1", Mode.ROAD, null, 100.0, 50.0, 10.0, 20.0, 0.0,
                0.0, FuelType.DIESEL);
        transportChain.addElement(to);

        EmissionFactor factor = new EmissionFactor(Mode.RAIL, FuelType.ELECTRICITY, 10.0, 0.0, 0.5, 0.1,
                Unit.KG_CO2E_PER_TKM, 0.0);
        emissionFactors.add(factor);

        assertThrows(InvalidCalculationException.class,
                () -> calculatorService.calculateGEE(transportChain, emissionFactors));

    }

    /**
     * Tests the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a missing EmissionFactor for a HubOperation throws an
     * InvalidCalculationException.
     */
    @Test
    void missingEmissionFactorForHubOperation_throwsInvalidCalculationException() {
        HubOperation ho = new HubOperation("HO-1", Mode.HUB, FuelType.ELECTRICITY, 100.0, 1.0);
        transportChain.addElement(ho);

        EmissionFactor factor = new EmissionFactor(Mode.ROAD, FuelType.DIESEL, 10.0, 0.0, 0.5, 0.1,
                Unit.KG_CO2E_PER_TKM, 0.0);
        emissionFactors.add(factor);

        assertThrows(InvalidCalculationException.class,
                () -> calculatorService.calculateGEE(transportChain, emissionFactors));
    }

    /**
     * Tests the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a valid TransportOperation with KG_CO2E_PER_TKM unit
     * calculates emissions correctly.
     */
    @Test
    void validTransportOperationWithTKMUnit_calculatesEmissionsCorrectly() throws InvalidCalculationException {
        TransportOperation to = new TransportOperation("TO-1", Mode.ROAD, null, 100.0, 50.0, 10.0, 20.0, 0.0,
                0.0, FuelType.DIESEL);
        transportChain.addElement(to);

        EmissionFactor factor = new EmissionFactor(Mode.ROAD, FuelType.DIESEL, 10.0, 0.0, 0.5, 0.1,
                Unit.KG_CO2E_PER_TKM, 0.0);
        emissionFactors.add(factor);

        calculatorService.calculateGEE(transportChain, emissionFactors);

        assertEquals(to.getEmissionTTW() + to.getEmissionWTT(), to.getEmissionWTW());
    }

    /**
     * Tests the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a valid HubOperation calculates emissions correctly.
     */
    @Test
    void validHubOperation_calculatesEmissionsCorrectly() throws InvalidCalculationException {
        HubOperation ho = new HubOperation("HO-1", Mode.HUB, FuelType.ELECTRICITY, 100.0, 2.0);
        transportChain.addElement(ho);

        EmissionFactor factor = new EmissionFactor(Mode.HUB, FuelType.ELECTRICITY, 0.0, 0.0, 0.0, 0.35,
                Unit.KG_CO2E_PER_KWH, 0.0);
        emissionFactors.add(factor);

        calculatorService.calculateGEE(transportChain, emissionFactors);

        assertEquals(0.0, ho.getEmissionTTW());
        assertEquals(70.0, ho.getEmissionWTT());
        assertEquals(70.0, ho.getEmissionWTW());
    }

    /**
     * Tests the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a TransportChain with multiple elements calculates total WTW
     * correctly.
     */
    @Test
    void transportChainWithMultipleElements_calculatesTotalWTWCorrectly() throws InvalidCalculationException {
        TransportOperation to = new TransportOperation("TO-1", Mode.ROAD, null, 100.0, 0.0, 10.0, 10.0, 0.0,
                0.0, FuelType.DIESEL);
        transportChain.addElement(to);

        HubOperation ho = new HubOperation("HO-1", Mode.HUB, FuelType.ELECTRICITY, 100.0, 1.0);
        transportChain.addElement(ho);

        EmissionFactor toFactor = new EmissionFactor(Mode.ROAD, FuelType.DIESEL, 10.0, 0.0, 0.5, 0.1,
                Unit.KG_CO2E_PER_TKM, 0.0);
        EmissionFactor hoFactor = new EmissionFactor(Mode.HUB, FuelType.ELECTRICITY, 0.0, 0.0, 0.0, 0.35,
                Unit.KG_CO2E_PER_KWH, 0.0);
        emissionFactors.add(toFactor);
        emissionFactors.add(hoFactor);

        calculatorService.calculateGEE(transportChain, emissionFactors);

        double expectedTotalWTW = to.getEmissionWTW() + ho.getEmissionWTW();
        assertEquals(expectedTotalWTW, transportChain.getTotalWTW());
    }

    /**
     * Tests the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a TransportChain calculates intensity correctly.
     */
    @Test
    void transportChain_calculatesIntensityCorrectly() throws InvalidCalculationException {
        TransportOperation to = new TransportOperation("TO-1", Mode.ROAD, null, 100.0, 0.0, 10.0, 10.0, 0.0,
                0.0, FuelType.DIESEL);
        transportChain.addElement(to);

        EmissionFactor factor = new EmissionFactor(Mode.ROAD, FuelType.DIESEL, 10.0, 0.0, 0.5, 0.1,
                Unit.KG_CO2E_PER_TKM, 0.0);
        emissionFactors.add(factor);

        calculatorService.calculateGEE(transportChain, emissionFactors);

        double expectedIntensity = transportChain.getTotalWTW() / 1000.0;

        assertEquals(expectedIntensity, transportChain.getIntensity());
    }

    /**
     * Tests the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a TransportChain with zero activity has zero intensity.
     */
    @Test
    void transportChainWithZeroActivity_hasZeroIntensity() throws InvalidCalculationException {
        TransportOperation to = new TransportOperation("TO-1", Mode.ROAD, null, 0.0, 0.0, 0.0, 10.0, 0.0, 0.0,
                FuelType.DIESEL);
        transportChain.addElement(to);

        EmissionFactor factor = new EmissionFactor(Mode.ROAD, FuelType.DIESEL, 10.0, 0.0, 0.5, 0.1,
                Unit.KG_CO2E_PER_TKM, 0.0);
        emissionFactors.add(factor);

        calculatorService.calculateGEE(transportChain, emissionFactors);

        assertEquals(0.0, transportChain.getIntensity());
    }

    /**
     * Tests the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a TransportOperation with fuel-based unit (not TKM)
     * calculates emissions using consumption.
     */
    @Test
    void transportOperationWithFuelBasedUnit_calculatesEmissionsUsingConsumption()
            throws InvalidCalculationException {
        TransportOperation to = new TransportOperation("TO-1", Mode.ROAD, null, 100.0, 0.0, 10.0, 10.0, 50.0,
                0.0, FuelType.DIESEL);
        transportChain.addElement(to);

        EmissionFactor factor = new EmissionFactor(Mode.ROAD, FuelType.DIESEL, 10.0, 0.0, 3.16, 0.38,
                Unit.LITERS, 0.0);
        emissionFactors.add(factor);

        calculatorService.calculateGEE(transportChain, emissionFactors);

        assertEquals(158.0, to.getEmissionTTW());
        assertEquals(19.0, to.getEmissionWTT());
        assertEquals(177.0, to.getEmissionWTW());
    }

    /**
     * Tests the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a TransportOperation uses consumption per t.km when liters
     * consumed is not provided.
     */
    @Test
    void transportOperationWithConsumptionPerTKm_calculatesEmissionsCorrectly() throws InvalidCalculationException {
        TransportOperation to = new TransportOperation("TO-1", Mode.ROAD, null, 100.0, 0.0, 10.0, 10.0, 0.0,
                5.0, FuelType.DIESEL);
        transportChain.addElement(to);

        EmissionFactor factor = new EmissionFactor(Mode.ROAD, FuelType.DIESEL, 10.0, 0.0, 3.16, 0.38,
                Unit.LITERS, 0.0);
        emissionFactors.add(factor);

        calculatorService.calculateGEE(transportChain, emissionFactors);

        assertTrue(to.getEmissionTTW() > 0, "TTW should be calculated from consumption per t.km");
        assertTrue(to.getEmissionWTT() > 0, "WTT should be calculated from consumption per t.km");
    }

    /**
     * Tests the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a TransportOperation uses average consumption when no
     * specific consumption is provided.
     */
    @Test
    void transportOperationWithAverageConsumption_calculatesEmissionsCorrectly()
            throws InvalidCalculationException {
        TransportOperation to = new TransportOperation("TO-1", Mode.ROAD, null, 100.0, 0.0, 10.0, 10.0, 0.0,
                0.0, FuelType.DIESEL);
        transportChain.addElement(to);

        EmissionFactor factor = new EmissionFactor(Mode.ROAD, FuelType.DIESEL, 10.0, 0.0, 3.16, 0.38,
                Unit.LITERS, 2.5);
        emissionFactors.add(factor);

        calculatorService.calculateGEE(transportChain, emissionFactors);

        assertTrue(to.getEmissionTTW() > 0, "TTW should be calculated using average consumption");
        assertTrue(to.getEmissionWTT() > 0, "WTT should be calculated using average consumption");
    }

    /**
     * Tests the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a TransportOperation with zero capacity handles loadFactor
     * correctly.
     */
    @Test
    void transportOperationWithZeroCapacity_calculatesEmissionsCorrectly() throws InvalidCalculationException {
        TransportOperation to = new TransportOperation("TO-1", Mode.ROAD, null, 100.0, 0.0, 10.0, 0.0, 0.0, 0.0,
                FuelType.DIESEL);
        transportChain.addElement(to);

        EmissionFactor factor = new EmissionFactor(Mode.ROAD, FuelType.DIESEL, 10.0, 0.0, 0.5, 0.1,
                Unit.KG_CO2E_PER_TKM, 0.0);
        emissionFactors.add(factor);

        calculatorService.calculateGEE(transportChain, emissionFactors);

        assertEquals(0.0, to.getEmissionTTW());
        assertEquals(0.0, to.getEmissionWTT());
        assertEquals(0.0, to.getEmissionWTW());
    }

    /**
     * Tests the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a HubOperation with zero duration calculates emissions
     * correctly.
     */
    @Test
    void hubOperationWithZeroDuration_calculatesEmissionsCorrectly() throws InvalidCalculationException {
        HubOperation ho = new HubOperation("HO-1", Mode.HUB, FuelType.ELECTRICITY, 100.0, 0.0);
        transportChain.addElement(ho);

        EmissionFactor factor = new EmissionFactor(Mode.HUB, FuelType.ELECTRICITY, 0.0, 0.0, 0.0, 0.35,
                Unit.KG_CO2E_PER_KWH, 0.0);
        emissionFactors.add(factor);

        calculatorService.calculateGEE(transportChain, emissionFactors);

        assertEquals(0.0, ho.getEmissionTTW());
        assertEquals(0.0, ho.getEmissionWTT());
        assertEquals(0.0, ho.getEmissionWTW());
    }

    /**
     * Tests the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that an unsupported element type throws an
     * InvalidCalculationException.
     */
    @Test
    void unsupportedElementType_throwsInvalidCalculationException() {

        TransportChainElement unsupportedElement = new TransportChainElement("UNSUPPORTED-1") {

        };
        transportChain.addElement(unsupportedElement);

        EmissionFactor factor = new EmissionFactor(Mode.ROAD, FuelType.DIESEL, 10.0, 0.0, 0.5, 0.1,
                Unit.KG_CO2E_PER_TKM, 0.0);
        emissionFactors.add(factor);

        assertThrows(InvalidCalculationException.class,
                () -> calculatorService.calculateGEE(transportChain, emissionFactors));
    }

    /**
     * Tests the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a TransportChain with null elements list throws an
     * InvalidCalculationException.
     */
    @Test
    void transportChainWithNullElements_throwsInvalidCalculationException() {
        TransportChain chainWithNullElements = new TransportChain();

        assertThrows(InvalidCalculationException.class,
                () -> calculatorService.calculateGEE(chainWithNullElements, emissionFactors));
    }
}
