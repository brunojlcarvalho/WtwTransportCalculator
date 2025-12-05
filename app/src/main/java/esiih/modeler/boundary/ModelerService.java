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

    /**
     * Models a transport chain from parsed primary data.
     * 
     * @param data ParsedPrimaryData object containing the primary data.
     * @return A TransportChain object modeled from the primary data.
     * @throws InvalidParsePrimaryDataException If modeling cannot be performed.
     */
    TransportChain modelTransportChain(ParsedPrimaryData data) throws InvalidParsePrimaryDataException;

    /**
     * Models a list of emission factors from parsed secondary data.
     * 
     * @param data ParsedSecondaryData object containing the secondary data.
     * @return A list of EmissionFactor objects modeled from the secondary data.
     * @throws InvalidParsedSecondaryDataException If modeling cannot be performed.
     */
    List<EmissionFactor> modelEmissionFactors(ParsedSecondaryData data) throws InvalidParsedSecondaryDataException;

}
