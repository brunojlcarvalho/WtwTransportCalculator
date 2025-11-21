package esiih.modeler.boundary;

import esiih.importer.entity.ParsedPrimaryData;
import esiih.modeler.entity.TransportChain;
import esiih.modeler.exception.InvalidParsePrimaryDataException;

/**
 * Modeler service interface for modeling transport chains and emission factors.
 */
public interface ModelerService {

    TransportChain modelTransportChain(ParsedPrimaryData data) throws InvalidParsePrimaryDataException;

}
