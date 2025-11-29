package esiih.calculator.controller;

import java.util.List;

import esiih.calculator.boundary.CalculatorService;
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
 * Implementation of the CalculatorService interface for calculation GEE
 * emissions of a transport chain.
 */
public class Calculator implements CalculatorService {

    /**
     * Calculates the GEE emissions (WTW, TTW, WTT) for the entire transport
     * chain.
     *
     * @param transportChain The transport chain to calculate.
     * @param emissionFactors The list of reference emission factors (secondary
     * data).
     * @throws InvalidCalculationException if any calculation error occurs.
     */
    @Override
    public void calculateGEE(TransportChain transportChain, List<EmissionFactor> emissionFactors) throws InvalidCalculationException {
        List<TransportChainElement> elements = transportChain.getElements();

        if (elements == null || elements.isEmpty()) {
            throw new InvalidCalculationException("TransportChain must contain elements");
        }
        if (emissionFactors.isEmpty()) {
            throw new InvalidCalculationException("EmissionFactors list must not be empty");
        }

        double totalChainWTW = 0.0;
        double totalChainActivity = 0.0;

        for (TransportChainElement element : elements) {
            if (element instanceof TransportOperation operation) {
                calcTransportOperation(operation, emissionFactors);

                // Acumula atividade (Massa * Distancia Carregado) para intensidade
                double elementActivity = operation.getPayloadTon() * operation.getDistanceLoadedKm();
                totalChainActivity += elementActivity;

            } else if (element instanceof HubOperation operation) {
                calcHubOperation(operation, emissionFactors);
            } else {
                throw new InvalidCalculationException("Unsupported element type: " + element.getClass());
            }

            totalChainWTW += element.getEmissionWTW();
        }

        transportChain.setTotalWTW(totalChainWTW);
        double intensity = (totalChainActivity == 0.0) ? 0.0 : (totalChainWTW / totalChainActivity);
        transportChain.setIntensity(intensity);
    }

    /**
     * Calculates the emissions for a Transport Operation.
     *
     * @param operation The transport operation to calculate emissions for.
     * @param emissionFactors The list of reference emission factors (secondary
     * data).
     * @throws InvalidCalculationException if any calculation error occurs.
     */
    private void calcTransportOperation(TransportOperation operation, List<EmissionFactor> emissionFactors) throws InvalidCalculationException {
        validateTO(operation);

        // 1. Cálculos de Atividade e Distância 
        double distLoaded = Math.max(0.0, operation.getDistanceLoadedKm());
        double distEmpty = Math.max(0.0, operation.getDistanceEmptyKm());
        double payload = Math.max(0.0, operation.getPayloadTon());
        double capacity = Math.max(0.0, operation.getMaxCapacityTon());

        double totalDistReal = distLoaded + distEmpty;
        double emptyReturnFactor = (totalDistReal == 0.0) ? 0.0 : (distEmpty / totalDistReal);
        double adjustedDistance = distLoaded * (1 + emptyReturnFactor);
        double loadFactor = (capacity == 0.0) ? 0.0 : clamp(payload / capacity, 0.0, 1.0);
        double effectiveLoad = capacity * loadFactor;

        // Atividade para cálculo (t.km)
        double activityTKm = effectiveLoad * adjustedDistance;

        // 2. Encontrar Fator
        EmissionFactor factor = findEmissionFactor(
                operation.getTransportMode(),
                operation.getFuelType(),
                emissionFactors);

        double ttw = 0.0;
        double wtt = 0.0;

        // 3. DECISÃO BASEADA NA UNIDADE DO FATOR
        if (factor.getUnit() == Unit.KG_CO2E_PER_TKM) {
            // Caso A: Fator baseado em Atividade
            ttw = activityTKm * factor.getTtwFactor();
            wtt = activityTKm * factor.getWttFactor();

        } else {
            // Caso B: Fator baseado em Combustível
            double consumptionTotal = 0.0;

            if (operation.getLitersConsumed() > 0) {
                consumptionTotal = operation.getLitersConsumed();
            } else if (operation.getConsumptionLitTKm() > 0) {
                consumptionTotal = (operation.getConsumptionLitTKm() / 100.0) * activityTKm;
            } else {
                consumptionTotal = factor.getAverageConsumption() * activityTKm;
            }

            ttw = consumptionTotal * factor.getTtwFactor();
            wtt = consumptionTotal * factor.getWttFactor();
        }

        double wtw = ttw + wtt;

        operation.setEmissionTTW(ttw);
        operation.setEmissionWTT(wtt);
        operation.setEmissionWTW(wtw);
    }

    /**
     * Calculates the emissions for a Hub Operation.
     *
     * @param operation The hub operation to calculate emissions for.
     * @param emissionFactors The list of reference emission factors (secondary
     * data).
     * @throws InvalidCalculationException if any calculation error occurs.
     */
    private void calcHubOperation(HubOperation operation, List<EmissionFactor> emissionFactors) throws InvalidCalculationException {
        validateHO(operation);

        // 1. Calcular o Consumo Total de Energia
        double rawEnergyInput = Math.max(0.0, operation.getEnergyConsumed());
        double duration = Math.max(0.0, operation.getDurationHours());

        double totalEnergyConsumed = 0.0;

        if (duration > 0) {
            totalEnergyConsumed = rawEnergyInput * duration;
        } else {
            totalEnergyConsumed = 0;
        }

        // 2. Encontrar fator (Modo HUB, Tipo Energia)
        EmissionFactor factor = findEmissionFactor(
                operation.getHubMode(),
                operation.getEnergySource(),
                emissionFactors);

        // 3. Calcular Emissões (kgCO2e / kWh)
        double ttw = totalEnergyConsumed * factor.getTtwFactor();
        double wtt = totalEnergyConsumed * factor.getWttFactor();
        double wtw = ttw + wtt;

        operation.setEmissionTTW(ttw);
        operation.setEmissionWTT(wtt);
        operation.setEmissionWTW(wtw);
    }

    /**
     * Validates a Transport Operation.
     *
     * @param operation The transport operation to validate.
     * @throws InvalidCalculationException if validation fails.
     */
    private void validateTO(TransportOperation operation) throws InvalidCalculationException {
        if (operation.getTransportMode() == null) {
            throw new InvalidCalculationException("Mode required for TO " + operation.getId());
        }
        if (operation.getFuelType() == null) {
            throw new InvalidCalculationException("Fuel required for TO " + operation.getId());
        }
    }

    /**
     * Validates a Hub Operation.
     *
     * @param operation The hub operation to validate.
     * @throws InvalidCalculationException if validation fails.
     */
    private void validateHO(HubOperation operation) throws InvalidCalculationException {
        if (operation.getHubMode() == null) {
            throw new InvalidCalculationException("Mode required for HO " + operation.getId());
        }
        if (operation.getEnergySource() == null) {
            throw new InvalidCalculationException("Energy Source required for HO " + operation.getId());
        }
    }

    /**
     * Clamps a value between a minimum and maximum.
     *
     * @param value The value to clamp.
     * @param min The minimum allowable value.
     * @param max The maximum allowable value.
     * @return The clamped value.
     */
    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Finds the appropriate Emission Factor based on mode and fuel type.
     *
     * @param mode The transport or hub mode.
     * @param fuelType The fuel or energy type.
     * @param emissionFactors The list of available emission factors.
     * @return The matching Emission Factor.
     * @throws InvalidCalculationException if no matching factor is found.
     */
    private EmissionFactor findEmissionFactor(Mode mode, FuelType fuelType, List<EmissionFactor> emissionFactors) throws InvalidCalculationException {
        EmissionFactor factor = emissionFactors.stream()
                .filter(f -> f.getMode() == mode && f.getFuelType() == fuelType)
                .findFirst()
                .orElse(null);

        if (factor == null) {
            throw new InvalidCalculationException(
                    "No Emission Factor found for Mode: " + mode + " and Fuel: " + fuelType);
        }
        return factor;
    }
}
