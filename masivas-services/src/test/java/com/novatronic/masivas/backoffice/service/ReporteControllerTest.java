package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.controller.ReporteController;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaReporteCierreDTO;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaReporteTotalizadoDTO;
import com.novatronic.masivas.backoffice.dto.DetalleReporteConsolidadoDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.dto.ReporteDTO;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Obi Consulting
 */
@ExtendWith(MockitoExtension.class)
public class ReporteControllerTest {

    @Mock
    private ReporteService reporteService;
    @Mock
    private UserContext userContext;
    @InjectMocks
    private ReporteController reporteController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void reporteCierre_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        DetalleConsultaReporteCierreDTO response = new DetalleConsultaReporteCierreDTO();

        when(reporteService.reporteCierre(any())).thenReturn(response);
        doNothing().when(reporteService).logAuditoria(any(), any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = reporteController.reporteCierre(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void reporteTotalizado_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        DetalleConsultaReporteTotalizadoDTO response = new DetalleConsultaReporteTotalizadoDTO();

        when(reporteService.reporteTotalizado(any())).thenReturn(response);
        doNothing().when(reporteService).logAuditoria(any(), any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = reporteController.reporteTotalizado(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void reporteConsolidado_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        List<DetalleReporteConsolidadoDTO> response = new ArrayList<>();

        when(reporteService.reporteConsolidadoPorEntidadDestino(any())).thenReturn(response);
        doNothing().when(reporteService).logAuditoria(any(), any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = reporteController.reporteConsolidado(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void descargarConsolidadoPDF_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        ReporteDTO response = new ReporteDTO();

        when(reporteService.descargarConsolidado(any(), any(), any())).thenReturn(response);
        doNothing().when(reporteService).logAuditoria(any(), any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = reporteController.descargarConsolidadoPDF(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void descargarConsolidadoXLSX_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        ReporteDTO response = new ReporteDTO();

        when(reporteService.descargarConsolidado(any(), any(), any())).thenReturn(response);
        doNothing().when(reporteService).logAuditoria(any(), any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = reporteController.descargarConsolidadoXLSX(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }
}
