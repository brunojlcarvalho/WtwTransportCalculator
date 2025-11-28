package esiih.modeler.boundary;

import java.util.List;
import esiih.importer.entity.ParsedPrimaryData;
import esiih.importer.entity.ParsedSecondaryData;
import esiih.modeler.entity.EmissionFactor;
import esiih.modeler.entity.TransportChain;
import esiih.modeler.exception.InvalidParsePrimaryDataException;
import esiih.modeler.exception.InvalidParsedSecondaryDataException;

/**
 * Modeler service interface for modeling transport chains and emission factors.
 */
public interface ModelerService {

    TransportChain modelTransportChain(ParsedPrimaryData data) throws InvalidParsePrimaryDataException;

    List<EmissionFactor> modelEmissionFactors(ParsedSecondaryData data) throws InvalidParsedSecondaryDataException;

}
