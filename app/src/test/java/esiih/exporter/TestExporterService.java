package esiih.exporter;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import esiih.exporter.boundary.ExporterService;
import esiih.exporter.controller.Exporter;
import esiih.exporter.exception.InvalidExportException;
import esiih.modeler.entity.HubOperation;
import esiih.modeler.entity.TransportChain;
import esiih.modeler.entity.TransportChainElement;
import esiih.modeler.entity.TransportOperation;
import esiih.modeler.entity.enums.FuelType;
import esiih.modeler.entity.enums.Mode;

/**
 * Test class for {@link ExporterService}.
 */
public class TestExporterService {

    private ExporterService exporterService;
    private TransportChain transportChain;
    private File file;

    /**
     * Sets up the test environment. Creates an instance of {@link Exporter} and
     * a {@link File}. Creates a mock {@link TransportChain} with data.
     */
    @BeforeEach
    void setUp() {
        exporterService = new Exporter();
        file = new File("transport_chain_export.csv");
        transportChain = createMockTransportChain();
    }

    /**
     * Tears down the test environment. Deletes the file if it exists.
     */
    @AfterEach
    void tearDown() {
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Tests the
     * {@link ExporterService#exportReport(TransportChain, String)}
     * method with a valid chain. Verifies if the file is created and validates
     * the content structure.
     *
     * @throws IOException if an I/O error occurs
     * @throws InvalidExportException if the export fails due to invalid data
     */
    @Test
    void testexportReport_ValidChain() throws IOException, InvalidExportException {
        exporterService.exportReport(transportChain, file.getPath());

        assertTrue(file.exists());
    }

    /**
     * Tests the
     * {@link ExporterService#exportReport(TransportChain, String)}
     * method with a null file path. Ensures that an
     * {@link InvalidExportException} is thrown.
     */
    @Test
    void testexportReport_NullFilePath() {
        assertThrows(InvalidExportException.class,
                () -> exporterService.exportReport(transportChain, null)
        );
    }

    /**
     * Tests the
     * {@link ExporterService#exportReport(TransportChain, String)}
     * method with a null {@link TransportChain}. Ensures that an
     * {@link InvalidExportException} is thrown.
     */
    @Test
    void testexportReport_NullChain() {
        assertThrows(InvalidExportException.class,
                () -> exporterService.exportReport(null, file.getPath())
        );
    }

    /**
     * Tests the
     * {@link ExporterService#exportReport(TransportChain, String)}
     * method with an empty chain. Modifies the mock chain to be empty and
     * verifies that an exception is thrown.
     */
    @Test
    void testexportReport_EmptyChain() {
        transportChain = new TransportChain();

        assertThrows(InvalidExportException.class,
                () -> exporterService.exportReport(transportChain, file.getPath())
        );
    }

    /**
     * Tests the
     * {@link ExporterService#exportReport(TransportChain, String)}
     * method with a chain containing a null element. Ensures that an
     * {@link InvalidExportException} is thrown.
     */
    @Test
    void testexportReport_ChainWithNullElement() {
        transportChain.addElement(null);

        assertThrows(InvalidExportException.class,
                () -> exporterService.exportReport(transportChain, file.getPath())
        );
    }

    /**
     * Tests the
     * {@link ExporterService#exportReport(TransportChain, String)}
     * method with a chain containing an unknown element type. Ensures that an
     * {@link InvalidExportException} is thrown.
     */
    @Test
    void testexportReport_UnknownElement() {
        transportChain.addElement(new TransportChainElement("UNK") {
        });

        assertThrows(InvalidExportException.class,
                () -> exporterService.exportReport(transportChain, file.getPath())
        );
    }

    /**
     * Tests I/O error handling in
     * {@link ExporterService#exportReport(TransportChain, String)}.
     * Tries to write to a directory path instead of a file to trigger an
     * {@link IOException}.
     */
    @Test
    void testexportReport_IOError() {
        File directory = new File(".");

        assertThrows(IOException.class,
                () -> exporterService.exportReport(transportChain, directory.getAbsolutePath())
        );
    }

    /**
     * Helper method to create a Mock {@link TransportChain} with populated
     * data.
     *
     * @return a {@link TransportChain}
     */
    private TransportChain createMockTransportChain() {
        TransportChain chain = new TransportChain();

        TransportOperation to = new TransportOperation("TO1", Mode.ROAD, "Carro", 100.0, 100.0, 10.0, 28.0, 15, 3.5, FuelType.DIESEL);
        to.setEmissionWTW(50.0);
        to.setEmissionWTT(20.0);
        to.setEmissionTTW(30.0);
        chain.addElement(to);

        HubOperation ho = new HubOperation("HO1", Mode.HUB, FuelType.ELECTRICITY, 500.0, 5.0);
        ho.setEmissionWTW(10.0);
        ho.setEmissionWTT(5.0);
        ho.setEmissionTTW(5.0);
        chain.addElement(ho);

        return chain;
    }
}
