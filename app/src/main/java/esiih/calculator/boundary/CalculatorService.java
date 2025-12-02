package esiih.calculator.boundary;

import java.util.List;

import esiih.calculator.exception.InvalidCalculationException;
import esiih.modeler.entity.EmissionFactor;
import esiih.modeler.entity.TransportChain;

/**
 * Service interface for calculating emissions of a transport chain.
 */
public interface CalculatorService {

    /**
     * Calculates the GEE emissions (WTW, TTW, WTT) for the entire transport
     * chain.
     *
     * @param transportChain The transport chain to calculate.
     * @param emissionFactors The list of reference emission factors (secondary
     * data).
     */
    void calculateGEE(TransportChain transportChain, List<EmissionFactor> emissionFactors) throws InvalidCalculationException;
}
