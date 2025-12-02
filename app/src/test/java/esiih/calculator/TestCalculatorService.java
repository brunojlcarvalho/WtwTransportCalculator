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

public class TestCalculatorService {

    private CalculatorService calculatorService;
    private TransportChain transportChain;
    private List<EmissionFactor> emissionFactors;

    @BeforeEach
    void setUp() {
        calculatorService = new Calculator();
        transportChain = new TransportChain("TC-1", "Test Transport Chain");
        emissionFactors = new ArrayList<>();
    }

    /**
     * Test the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a null TransportChain throws a NullPointerException.
     */
    @Test
    void nullTransportChain_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            calculatorService.calculateGEE(null, emissionFactors);
        });
    }

    /**
     * Test the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that an empty TransportChain (with no elements) throws an
     * InvalidCalculationException.
     */
    @Test
    void emptyTransportChain_throwsInvalidCalculationException() {
        InvalidCalculationException exception = assertThrows(
                InvalidCalculationException.class,
                () -> calculatorService.calculateGEE(transportChain, emissionFactors));

        assertEquals("TransportChain must contain elements", exception.getMessage());
    }

    /**
     * Test the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a null EmissionFactors list throws a NullPointerException.
     */
    @Test
    void nullEmissionFactors_throwsNullPointerException() {
        TransportOperation to = new TransportOperation("TO-1");
        to.setTransportMode(Mode.ROAD);
        to.setFuelType(FuelType.DIESEL);
        transportChain.addElement(to);

        assertThrows(NullPointerException.class, () -> {
            calculatorService.calculateGEE(transportChain, null);
        });
    }

    /**
     * Test the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that an empty EmissionFactors list throws an
     * InvalidCalculationException.
     */
    @Test
    void emptyEmissionFactors_throwsInvalidCalculationException() {
        TransportOperation to = new TransportOperation("TO-1");
        to.setTransportMode(Mode.ROAD);
        to.setFuelType(FuelType.DIESEL);
        transportChain.addElement(to);

        InvalidCalculationException exception = assertThrows(
                InvalidCalculationException.class,
                () -> calculatorService.calculateGEE(transportChain, emissionFactors));

        assertEquals("EmissionFactors list must not be empty", exception.getMessage());
    }

    /**
     * Test the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a TransportOperation without a transport mode throws an
     * InvalidCalculationException.
     */
    @Test
    void transportOperationWithoutMode_throwsInvalidCalculationException() {
        TransportOperation to = new TransportOperation("TO-1");
        to.setFuelType(FuelType.DIESEL);
        transportChain.addElement(to);

        EmissionFactor factor = new EmissionFactor(Mode.ROAD, FuelType.DIESEL, 10.0, 0.0, 0.5, 0.1,
                Unit.KG_CO2E_PER_TKM, 0.0);
        emissionFactors.add(factor);

        InvalidCalculationException exception = assertThrows(
                InvalidCalculationException.class,
                () -> calculatorService.calculateGEE(transportChain, emissionFactors));

        assertTrue(exception.getMessage().contains("Mode required for TO"));
    }

    /**
     * Test the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a TransportOperation without a fuel type throws an
     * InvalidCalculationException.
     */
    @Test
    void transportOperationWithoutFuelType_throwsInvalidCalculationException() {
        TransportOperation to = new TransportOperation("TO-1");
        to.setTransportMode(Mode.ROAD);
        transportChain.addElement(to);

        EmissionFactor factor = new EmissionFactor(Mode.ROAD, FuelType.DIESEL, 10.0, 0.0, 0.5, 0.1,
                Unit.KG_CO2E_PER_TKM, 0.0);
        emissionFactors.add(factor);

        InvalidCalculationException exception = assertThrows(
                InvalidCalculationException.class,
                () -> calculatorService.calculateGEE(transportChain, emissionFactors));

        assertTrue(exception.getMessage().contains("Fuel required for TO"));
    }

    /**
     * Test the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a HubOperation without a hub mode throws an
     * InvalidCalculationException.
     */
    @Test
    void hubOperationWithoutMode_throwsInvalidCalculationException() {
        HubOperation ho = new HubOperation("HO-1");
        ho.setHubMode(null);
        ho.setEnergySource(FuelType.ELECTRICITY);
        ho.setDurationHours(1.0);
        ho.setEnergyConsumed(100.0);
        transportChain.addElement(ho);

        EmissionFactor factor = new EmissionFactor(Mode.HUB, FuelType.ELECTRICITY, 0.0, 0.0, 0.0, 0.35,
                Unit.KG_CO2E_PER_KWH, 0.0);
        emissionFactors.add(factor);

        InvalidCalculationException exception = assertThrows(
                InvalidCalculationException.class,
                () -> calculatorService.calculateGEE(transportChain, emissionFactors));

        assertTrue(exception.getMessage().contains("Mode required for HO"));
    }

    /**
     * Test the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a HubOperation without an energy source throws an
     * InvalidCalculationException.
     */
    @Test
    void hubOperationWithoutEnergySource_throwsInvalidCalculationException() {
        HubOperation ho = new HubOperation("HO-1");
        ho.setHubMode(Mode.HUB);
        ho.setEnergySource(null);
        ho.setDurationHours(1.0);
        ho.setEnergyConsumed(100.0);
        transportChain.addElement(ho);

        EmissionFactor factor = new EmissionFactor(Mode.HUB, FuelType.ELECTRICITY, 0.0, 0.0, 0.0, 0.35,
                Unit.KG_CO2E_PER_KWH, 0.0);
        emissionFactors.add(factor);

        InvalidCalculationException exception = assertThrows(
                InvalidCalculationException.class,
                () -> calculatorService.calculateGEE(transportChain, emissionFactors));

        assertTrue(exception.getMessage().contains("Energy Source required for HO"));
    }

    /**
     * Test the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a missing EmissionFactor for a TransportOperation throws an
     * InvalidCalculationException.
     */
    @Test
    void missingEmissionFactorForTransportOperation_throwsInvalidCalculationException() {
        TransportOperation to = new TransportOperation("TO-1");
        to.setTransportMode(Mode.ROAD);
        to.setFuelType(FuelType.DIESEL);
        to.setDistanceLoadedKm(100.0);
        to.setDistanceEmptyKm(50.0);
        to.setPayloadTon(10.0);
        to.setMaxCapacityTon(20.0);
        transportChain.addElement(to);

        EmissionFactor factor = new EmissionFactor(Mode.RAIL, FuelType.ELECTRICITY, 10.0, 0.0, 0.5, 0.1,
                Unit.KG_CO2E_PER_TKM, 0.0);
        emissionFactors.add(factor);

        InvalidCalculationException exception = assertThrows(
                InvalidCalculationException.class,
                () -> calculatorService.calculateGEE(transportChain, emissionFactors));

        assertTrue(exception.getMessage().contains("No Emission Factor found"));
        assertTrue(exception.getMessage().contains("ROAD") || exception.getMessage().contains("DIESEL"));
    }

    /**
     * Test the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a missing EmissionFactor for a HubOperation throws an
     * InvalidCalculationException.
     */
    @Test
    void missingEmissionFactorForHubOperation_throwsInvalidCalculationException() {
        HubOperation ho = new HubOperation("HO-1");
        ho.setHubMode(Mode.HUB);
        ho.setEnergySource(FuelType.ELECTRICITY);
        ho.setDurationHours(1.0);
        ho.setEnergyConsumed(100.0);
        transportChain.addElement(ho);

        EmissionFactor factor = new EmissionFactor(Mode.ROAD, FuelType.DIESEL, 10.0, 0.0, 0.5, 0.1,
                Unit.KG_CO2E_PER_TKM, 0.0);
        emissionFactors.add(factor);

        InvalidCalculationException exception = assertThrows(
                InvalidCalculationException.class,
                () -> calculatorService.calculateGEE(transportChain, emissionFactors));

        assertTrue(exception.getMessage().contains("No Emission Factor found"));
        assertTrue(exception.getMessage().contains("HUB") || exception.getMessage().contains("ELECTRICITY"));
    }

    /**
     * Test the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a valid TransportOperation with KG_CO2E_PER_TKM unit
     * calculates emissions correctly.
     */
    @Test
    void validTransportOperationWithTKMUnit_calculatesEmissionsCorrectly() throws InvalidCalculationException {
        TransportOperation to = new TransportOperation("TO-1");
        to.setTransportMode(Mode.ROAD);
        to.setFuelType(FuelType.DIESEL);
        to.setDistanceLoadedKm(100.0);
        to.setDistanceEmptyKm(50.0);
        to.setPayloadTon(10.0);
        to.setMaxCapacityTon(20.0);
        transportChain.addElement(to);

        EmissionFactor factor = new EmissionFactor(Mode.ROAD, FuelType.DIESEL, 10.0, 0.0, 0.5, 0.1,
                Unit.KG_CO2E_PER_TKM, 0.0);
        emissionFactors.add(factor);

        calculatorService.calculateGEE(transportChain, emissionFactors);

        assertTrue(to.getEmissionTTW() > 0, "TTW emissions should be calculated");
        assertTrue(to.getEmissionWTT() > 0, "WTT emissions should be calculated");
        assertTrue(to.getEmissionWTW() > 0, "WTW emissions should be calculated");
        assertEquals(to.getEmissionTTW() + to.getEmissionWTT(), to.getEmissionWTW(), 0.01,
                "WTW should equal TTW + WTT");
    }

    /**
     * Test the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a valid HubOperation calculates emissions correctly.
     */
    @Test
    void validHubOperation_calculatesEmissionsCorrectly() throws InvalidCalculationException {
        HubOperation ho = new HubOperation("HO-1");
        ho.setHubMode(Mode.HUB);
        ho.setEnergySource(FuelType.ELECTRICITY);
        ho.setDurationHours(2.0);
        ho.setEnergyConsumed(100.0);
        transportChain.addElement(ho);

        EmissionFactor factor = new EmissionFactor(Mode.HUB, FuelType.ELECTRICITY, 0.0, 0.0, 0.0, 0.35,
                Unit.KG_CO2E_PER_KWH, 0.0);
        emissionFactors.add(factor);

        calculatorService.calculateGEE(transportChain, emissionFactors);

        assertEquals(0.0, ho.getEmissionTTW(), 0.01, "TTW should be 0 for electricity");
        assertEquals(70.0, ho.getEmissionWTT(), 0.01, "WTT should be 70.0");
        assertEquals(70.0, ho.getEmissionWTW(), 0.01, "WTW should be 70.0");
    }

    /**
     * Test the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a TransportChain with multiple elements calculates total WTW
     * correctly.
     */
    @Test
    void transportChainWithMultipleElements_calculatesTotalWTWCorrectly() throws InvalidCalculationException {
        TransportOperation to = new TransportOperation("TO-1");
        to.setTransportMode(Mode.ROAD);
        to.setFuelType(FuelType.DIESEL);
        to.setDistanceLoadedKm(100.0);
        to.setDistanceEmptyKm(0.0);
        to.setPayloadTon(10.0);
        to.setMaxCapacityTon(10.0);
        transportChain.addElement(to);

        HubOperation ho = new HubOperation("HO-1");
        ho.setHubMode(Mode.HUB);
        ho.setEnergySource(FuelType.ELECTRICITY);
        ho.setDurationHours(1.0);
        ho.setEnergyConsumed(100.0);
        transportChain.addElement(ho);

        EmissionFactor toFactor = new EmissionFactor(Mode.ROAD, FuelType.DIESEL, 10.0, 0.0, 0.5, 0.1,
                Unit.KG_CO2E_PER_TKM, 0.0);
        EmissionFactor hoFactor = new EmissionFactor(Mode.HUB, FuelType.ELECTRICITY, 0.0, 0.0, 0.0, 0.35,
                Unit.KG_CO2E_PER_KWH, 0.0);
        emissionFactors.add(toFactor);
        emissionFactors.add(hoFactor);

        calculatorService.calculateGEE(transportChain, emissionFactors);

        double expectedTotalWTW = to.getEmissionWTW() + ho.getEmissionWTW();
        assertEquals(expectedTotalWTW, transportChain.getTotalWTW(), 0.01,
                "Total WTW should equal sum of all element WTW emissions");
    }

    /**
     * Test the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a TransportChain calculates intensity correctly.
     */
    @Test
    void transportChain_calculatesIntensityCorrectly() throws InvalidCalculationException {
        TransportOperation to = new TransportOperation("TO-1");
        to.setTransportMode(Mode.ROAD);
        to.setFuelType(FuelType.DIESEL);
        to.setDistanceLoadedKm(100.0);
        to.setDistanceEmptyKm(0.0);
        to.setPayloadTon(10.0);
        to.setMaxCapacityTon(10.0);
        transportChain.addElement(to);

        EmissionFactor factor = new EmissionFactor(Mode.ROAD, FuelType.DIESEL, 10.0, 0.0, 0.5, 0.1,
                Unit.KG_CO2E_PER_TKM, 0.0);
        emissionFactors.add(factor);

        calculatorService.calculateGEE(transportChain, emissionFactors);

        double expectedIntensity = transportChain.getTotalWTW() / 1000.0;

        assertTrue(transportChain.getIntensity() > 0, "Intensity should be calculated");
        assertEquals(expectedIntensity, transportChain.getIntensity(), 0.01,
                "Intensity should equal totalWTW divided by activity");
    }

    /**
     * Test the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a TransportChain with zero activity has zero intensity.
     */
    @Test
    void transportChainWithZeroActivity_hasZeroIntensity() throws InvalidCalculationException {
        TransportOperation to = new TransportOperation("TO-1");
        to.setTransportMode(Mode.ROAD);
        to.setFuelType(FuelType.DIESEL);
        to.setDistanceLoadedKm(0.0);
        to.setDistanceEmptyKm(0.0);
        to.setPayloadTon(0.0);
        to.setMaxCapacityTon(10.0);
        transportChain.addElement(to);

        EmissionFactor factor = new EmissionFactor(Mode.ROAD, FuelType.DIESEL, 10.0, 0.0, 0.5, 0.1,
                Unit.KG_CO2E_PER_TKM, 0.0);
        emissionFactors.add(factor);

        calculatorService.calculateGEE(transportChain, emissionFactors);

        assertEquals(0.0, transportChain.getIntensity(), 0.01, "Intensity should be zero when activity is zero");
    }

    /**
     * Test the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a TransportOperation with fuel-based unit (not TKM)
     * calculates emissions using consumption.
     */
    @Test
    void transportOperationWithFuelBasedUnit_calculatesEmissionsUsingConsumption() throws InvalidCalculationException {
        TransportOperation to = new TransportOperation("TO-1");
        to.setTransportMode(Mode.ROAD);
        to.setFuelType(FuelType.DIESEL);
        to.setDistanceLoadedKm(100.0);
        to.setDistanceEmptyKm(0.0);
        to.setPayloadTon(10.0);
        to.setMaxCapacityTon(10.0);
        to.setLitersConsumed(50.0);
        transportChain.addElement(to);

        EmissionFactor factor = new EmissionFactor(Mode.ROAD, FuelType.DIESEL, 10.0, 0.0, 3.16, 0.38,
                Unit.LITERS, 0.0);
        emissionFactors.add(factor);

        calculatorService.calculateGEE(transportChain, emissionFactors);

        assertEquals(158.0, to.getEmissionTTW(), 0.01, "TTW should be calculated from consumption");
        assertEquals(19.0, to.getEmissionWTT(), 0.01, "WTT should be calculated from consumption");
        assertEquals(177.0, to.getEmissionWTW(), 0.01, "WTW should equal TTW + WTT");
    }

    /**
     * Test the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a TransportOperation uses consumption per t.km when liters
     * consumed is not provided.
     */
    @Test
    void transportOperationWithConsumptionPerTKm_calculatesEmissionsCorrectly() throws InvalidCalculationException {
        TransportOperation to = new TransportOperation("TO-1");
        to.setTransportMode(Mode.ROAD);
        to.setFuelType(FuelType.DIESEL);
        to.setDistanceLoadedKm(100.0);
        to.setDistanceEmptyKm(0.0);
        to.setPayloadTon(10.0);
        to.setMaxCapacityTon(10.0);
        to.setConsumptionLitTKm(5.0);
        transportChain.addElement(to);

        EmissionFactor factor = new EmissionFactor(Mode.ROAD, FuelType.DIESEL, 10.0, 0.0, 3.16, 0.38,
                Unit.LITERS, 0.0);
        emissionFactors.add(factor);

        calculatorService.calculateGEE(transportChain, emissionFactors);

        assertTrue(to.getEmissionTTW() > 0, "TTW should be calculated from consumption per t.km");
        assertTrue(to.getEmissionWTT() > 0, "WTT should be calculated from consumption per t.km");
    }

    /**
     * Test the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a TransportOperation uses average consumption when no
     * specific consumption is provided.
     */
    @Test
    void transportOperationWithAverageConsumption_calculatesEmissionsCorrectly() throws InvalidCalculationException {
        TransportOperation to = new TransportOperation("TO-1");
        to.setTransportMode(Mode.ROAD);
        to.setFuelType(FuelType.DIESEL);
        to.setDistanceLoadedKm(100.0);
        to.setDistanceEmptyKm(0.0);
        to.setPayloadTon(10.0);
        to.setMaxCapacityTon(10.0);
        to.setLitersConsumed(0.0);
        to.setConsumptionLitTKm(0.0);
        transportChain.addElement(to);

        EmissionFactor factor = new EmissionFactor(Mode.ROAD, FuelType.DIESEL, 10.0, 0.0, 3.16, 0.38,
                Unit.LITERS, 2.5);
        emissionFactors.add(factor);

        calculatorService.calculateGEE(transportChain, emissionFactors);

        assertTrue(to.getEmissionTTW() > 0, "TTW should be calculated using average consumption");
        assertTrue(to.getEmissionWTT() > 0, "WTT should be calculated using average consumption");
    }

    /**
     * Test the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a TransportOperation with zero capacity handles loadFactor
     * correctly.
     */
    @Test
    void transportOperationWithZeroCapacity_calculatesEmissionsCorrectly() throws InvalidCalculationException {
        TransportOperation to = new TransportOperation("TO-1");
        to.setTransportMode(Mode.ROAD);
        to.setFuelType(FuelType.DIESEL);
        to.setDistanceLoadedKm(100.0);
        to.setDistanceEmptyKm(0.0);
        to.setPayloadTon(10.0);
        to.setMaxCapacityTon(0.0);
        transportChain.addElement(to);

        EmissionFactor factor = new EmissionFactor(Mode.ROAD, FuelType.DIESEL, 10.0, 0.0, 0.5, 0.1,
                Unit.KG_CO2E_PER_TKM, 0.0);
        emissionFactors.add(factor);

        calculatorService.calculateGEE(transportChain, emissionFactors);

        assertEquals(0.0, to.getEmissionTTW(), 0.01, "TTW should be 0 when capacity is 0");
        assertEquals(0.0, to.getEmissionWTT(), 0.01, "WTT should be 0 when capacity is 0");
        assertEquals(0.0, to.getEmissionWTW(), 0.01, "WTW should be 0 when capacity is 0");
    }

    /**
     * Test the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a HubOperation with zero duration calculates emissions
     * correctly.
     */
    @Test
    void hubOperationWithZeroDuration_calculatesEmissionsCorrectly() throws InvalidCalculationException {
        HubOperation ho = new HubOperation("HO-1");
        ho.setHubMode(Mode.HUB);
        ho.setEnergySource(FuelType.ELECTRICITY);
        ho.setDurationHours(0.0);
        ho.setEnergyConsumed(100.0);
        transportChain.addElement(ho);

        EmissionFactor factor = new EmissionFactor(Mode.HUB, FuelType.ELECTRICITY, 0.0, 0.0, 0.0, 0.35,
                Unit.KG_CO2E_PER_KWH, 0.0);
        emissionFactors.add(factor);

        calculatorService.calculateGEE(transportChain, emissionFactors);

        assertEquals(0.0, ho.getEmissionTTW(), 0.01, "TTW should be 0 when duration is 0");
        assertEquals(0.0, ho.getEmissionWTT(), 0.01, "WTT should be 0 when duration is 0");
        assertEquals(0.0, ho.getEmissionWTW(), 0.01, "WTW should be 0 when duration is 0");
    }

    /**
     * Test the {@link CalculatorService#calculateGEE(TransportChain, List)}
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

        InvalidCalculationException exception = assertThrows(
                InvalidCalculationException.class,
                () -> calculatorService.calculateGEE(transportChain, emissionFactors));

        assertTrue(exception.getMessage().contains("Unsupported element type"));
    }

    /**
     * Test the {@link CalculatorService#calculateGEE(TransportChain, List)}
     * Ensures that a TransportChain with null elements list throws an
     * InvalidCalculationException.
     */
    @Test
    void transportChainWithNullElements_throwsInvalidCalculationException() {
        TransportChain chainWithNullElements = new TransportChain("TC-NULL", "Test") {
            @Override
            public java.util.List<TransportChainElement> getElements() {
                return null;
            }
        };

        InvalidCalculationException exception = assertThrows(
                InvalidCalculationException.class,
                () -> calculatorService.calculateGEE(chainWithNullElements, emissionFactors));

        assertEquals("TransportChain must contain elements", exception.getMessage());
    }
}
