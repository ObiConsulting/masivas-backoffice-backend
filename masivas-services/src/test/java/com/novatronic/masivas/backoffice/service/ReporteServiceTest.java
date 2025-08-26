package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.DetalleConsultaReporteCierreDTO;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaReporteTotalizadoDTO;
import com.novatronic.masivas.backoffice.dto.DetalleReporteConsolidadoDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.ReporteDTO;
import com.novatronic.masivas.backoffice.exception.DataBaseException;
import com.novatronic.masivas.backoffice.exception.GenericException;
import com.novatronic.masivas.backoffice.repository.ArchivoDirectorioRepository;
import com.novatronic.masivas.backoffice.repository.ArchivoMasivasRepository;
import com.novatronic.masivas.backoffice.repository.ArchivoTitularidadRepository;
import com.novatronic.masivas.backoffice.repository.DetalleArchivoMasivasRepository;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import jakarta.transaction.RollbackException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author Obi Consulting
 */
@ExtendWith(MockitoExtension.class)
public class ReporteServiceTest {

    @Mock
    private ArchivoDirectorioRepository archivoDirectorioRepository;
    @Mock
    private ArchivoMasivasRepository archivoMasivasRepository;
    @Mock
    private ArchivoTitularidadRepository archivoTitularidadRepository;
    @Mock
    private DetalleArchivoMasivasRepository detalleArchivoMasivasRepository;
    @InjectMocks
    private ReporteService reporteService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(reporteService, "logo", "logo.png");
    }

    @Test
    void reporteCierre_exito() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFecha(LocalDate.now());

        List<Object[]> listaMockeada = new ArrayList<>();
        listaMockeada.add(new Object[]{"Obtenido Cliente", 100L});
        listaMockeada.add(new Object[]{"Enviado CCE", 200L});
        listaMockeada.add(new Object[]{"Obtenido CCE", 300L});
        listaMockeada.add(new Object[]{"Enviado Cliente", 400L});
        listaMockeada.add(new Object[]{"Otros", 500L});

        when(archivoDirectorioRepository.totalesPorEstado(any(), any())).thenReturn(listaMockeada);
        when(archivoMasivasRepository.totalesPorEstado(any(), any())).thenReturn(listaMockeada);
        when(archivoTitularidadRepository.totalesPorEstado(any(), any())).thenReturn(listaMockeada);

        DetalleConsultaReporteCierreDTO resultado = reporteService.reporteCierre(request);
        assertNotNull(resultado);
    }

    @Test
    void reporteCierre_excepcion_bd() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFecha(LocalDate.now());

        List<Object[]> listaMockeada = new ArrayList<>();
        listaMockeada.add(new Object[]{"Obtenido Cliente", 100L});
        listaMockeada.add(new Object[]{"Enviado CCE", 200L});
        listaMockeada.add(new Object[]{"Obtenido CCE", 300L});
        listaMockeada.add(new Object[]{"Enviado Cliente", 400L});

        InvalidDataAccessResourceUsageException genericEx = new InvalidDataAccessResourceUsageException("");
        when(archivoDirectorioRepository.totalesPorEstado(any(), any())).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            reporteService.reporteCierre(request);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void reporteCierre_excepcion_generico() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFecha(LocalDate.now());

        List<Object[]> listaMockeada = new ArrayList<>();
        listaMockeada.add(new Object[]{"Obtenido Cliente", 100L});
        listaMockeada.add(new Object[]{"Enviado CCE", 200L});
        listaMockeada.add(new Object[]{"Obtenido CCE", 300L});
        listaMockeada.add(new Object[]{"Enviado Cliente", 400L});

        RollbackException rollbackEx = new RollbackException("");
        RuntimeException genericEx = new RuntimeException("", rollbackEx);
        when(archivoDirectorioRepository.totalesPorEstado(any(), any())).thenThrow(genericEx);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            reporteService.reporteCierre(request);
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void reporteTotalizado_exito() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFecha(LocalDate.now());

        List<Object[]> listaMockeada = new ArrayList<>();
        listaMockeada.add(new Object[]{"Procesado", 100L, 15000l, 1000l, 2000l, 50l});
        listaMockeada.add(new Object[]{"Pendiente por Procesar", 200L});
        listaMockeada.add(new Object[]{"Otros", 300L});

        when(archivoMasivasRepository.reporteTotalizado(any(), any())).thenReturn(listaMockeada);

        DetalleConsultaReporteTotalizadoDTO resultado = reporteService.reporteTotalizado(request);
        assertNotNull(resultado);
    }

    @Test
    void reporteTotalizado_excepcion_bd() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFecha(LocalDate.now());

        List<Object[]> listaMockeada = new ArrayList<>();
        listaMockeada.add(new Object[]{"Obtenido Cliente", 100L});
        listaMockeada.add(new Object[]{"Enviado CCE", 200L});
        listaMockeada.add(new Object[]{"Obtenido CCE", 300L});
        listaMockeada.add(new Object[]{"Enviado Cliente", 400L});

        InvalidDataAccessResourceUsageException genericEx = new InvalidDataAccessResourceUsageException("");
        when(archivoMasivasRepository.reporteTotalizado(any(), any())).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            reporteService.reporteTotalizado(request);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void reporteTotalizado_excepcion_generico() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFecha(LocalDate.now());

        List<Object[]> listaMockeada = new ArrayList<>();
        listaMockeada.add(new Object[]{"Obtenido Cliente", 100L});
        listaMockeada.add(new Object[]{"Enviado CCE", 200L});
        listaMockeada.add(new Object[]{"Obtenido CCE", 300L});
        listaMockeada.add(new Object[]{"Enviado Cliente", 400L});

        RollbackException rollbackEx = new RollbackException("");
        RuntimeException genericEx = new RuntimeException("", rollbackEx);
        when(archivoMasivasRepository.reporteTotalizado(any(), any())).thenThrow(genericEx);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            reporteService.reporteTotalizado(request);
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void reporteConsolidado_exito() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFecha(LocalDate.now());

        List<DetalleReporteConsolidadoDTO> listaMockeada = new ArrayList<>();
        listaMockeada.add(new DetalleReporteConsolidadoDTO("Entidad 1", "604 - soles", 100L, new BigDecimal(350000l), new BigDecimal(30000l)));
        listaMockeada.add(new DetalleReporteConsolidadoDTO("Entidad 2", "840 - dolares", 100L, new BigDecimal(350000l), new BigDecimal(30000l)));
        listaMockeada.add(new DetalleReporteConsolidadoDTO("Entidad 3", "604 - soles", 100L, new BigDecimal(350000l), new BigDecimal(30000l)));

        when(detalleArchivoMasivasRepository.totalesPorEntidadDestino(any(), any(), any())).thenReturn(listaMockeada);

        List<DetalleReporteConsolidadoDTO> resultado = reporteService.reporteConsolidadoPorEntidadDestino(request);
        assertNotNull(resultado);
    }

    @Test
    void reporteConsolidado_excepcion_bd() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFecha(LocalDate.now());

        InvalidDataAccessResourceUsageException genericEx = new InvalidDataAccessResourceUsageException("");
        when(detalleArchivoMasivasRepository.totalesPorEntidadDestino(any(), any(), any())).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            reporteService.reporteConsolidadoPorEntidadDestino(request);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void reporteConsolidado_excepcion_generico() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFecha(LocalDate.now());

        RollbackException rollbackEx = new RollbackException("");
        RuntimeException genericEx = new RuntimeException("", rollbackEx);
        when(detalleArchivoMasivasRepository.totalesPorEntidadDestino(any(), any(), any())).thenThrow(genericEx);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            reporteService.reporteConsolidadoPorEntidadDestino(request);
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void descargarReporteConsolidado_exito() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFecha(LocalDate.now());

        List<DetalleReporteConsolidadoDTO> listaMockeada = new ArrayList<>();
        listaMockeada.add(new DetalleReporteConsolidadoDTO("Entidad 1", "604 - soles", 100L, new BigDecimal(350000l), new BigDecimal(30000l)));
        listaMockeada.add(new DetalleReporteConsolidadoDTO("Entidad 2", "840 - dolares", 100L, new BigDecimal(350000l), new BigDecimal(30000l)));
        listaMockeada.add(new DetalleReporteConsolidadoDTO("Entidad 3", "604 - soles", 100L, new BigDecimal(350000l), new BigDecimal(30000l)));

        when(detalleArchivoMasivasRepository.totalesPorEntidadDestino(any(), any(), any())).thenReturn(listaMockeada);

        ReporteDTO resultado = reporteService.descargarConsolidado(request, "usuario", ConstantesServices.TIPO_ARCHIVO_PDF);
        assertNotNull(resultado);
    }

    @Test
    void descargarReporteConsolidado_excepcion_bd() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFecha(LocalDate.now());

        InvalidDataAccessResourceUsageException genericEx = new InvalidDataAccessResourceUsageException("");
        when(detalleArchivoMasivasRepository.totalesPorEntidadDestino(any(), any(), any())).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            reporteService.descargarConsolidado(request, "usuario", ConstantesServices.TIPO_ARCHIVO_PDF);
        });
        assertTrue(thrown instanceof DataBaseException);

    }

    @Test
    void descargarReporteConsolidado_excepcion_generico() {

        GenericException thrown = assertThrows(GenericException.class, () -> {
            reporteService.descargarConsolidado(null, "usuario", ConstantesServices.TIPO_ARCHIVO_PDF);
        });
        assertTrue(thrown instanceof GenericException);

    }

}
