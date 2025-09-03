package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.controller.ArchivoController;
import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoDirectorioDTO;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoMasivasDTO;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoTitularidadDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroArchivoMasivasDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.dto.ReporteDTO;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
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
public class ArchivoControllerTest {

    @Mock
    private ArchivoService archivoService;
    @Mock
    private DetalleMasivasService detalleMasivasService;
    @Mock
    private UserContext userContext;
    @InjectMocks
    private ArchivoController archivoController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void buscarArchivoDirectorio_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        CustomPaginate<DetalleConsultaArchivoDirectorioDTO> response = new CustomPaginate<>();

        when(archivoService.buscarArchivoDirectorio(any())).thenReturn(response);
        doNothing().when(archivoService).logAuditoria(any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = archivoController.buscarArchivoDirectorio(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void buscarArchivoMasivas_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        CustomPaginate<DetalleConsultaArchivoMasivasDTO> response = new CustomPaginate<>();

        when(archivoService.buscarArchivoMasivas(any())).thenReturn(response);
        doNothing().when(archivoService).logAuditoria(any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = archivoController.buscarArchivoMasivas(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void buscarArchivoTitularidad_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        CustomPaginate<DetalleConsultaArchivoTitularidadDTO> response = new CustomPaginate<>();

        when(archivoService.buscarArchivoTitularidad(any())).thenReturn(response);
        doNothing().when(archivoService).logAuditoria(any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = archivoController.buscarArchivoTitularidad(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void buscarDetalleMasivas_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        CustomPaginate<DetalleRegistroArchivoMasivasDTO> response = new CustomPaginate<>();

        when(detalleMasivasService.buscarDetalleMasivas(any())).thenReturn(response);
        doNothing().when(detalleMasivasService).logAuditoria(any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = archivoController.buscarDetalleMasivas(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void descargarArchivoDirectorioPDF_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        ReporteDTO response = new ReporteDTO();

        when(archivoService.descargarArchivoDirectorio(any(), any(), any())).thenReturn(response);
        doNothing().when(archivoService).logAuditoria(any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = archivoController.descargarArchivoDirectorioPDF(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void descargarArchivoDirectorioXLSX_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        ReporteDTO response = new ReporteDTO();

        when(archivoService.descargarArchivoDirectorio(any(), any(), any())).thenReturn(response);
        doNothing().when(archivoService).logAuditoria(any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = archivoController.descargarArchivoDirectorioXLSX(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void descargarArchivoMasivasPDF_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        ReporteDTO response = new ReporteDTO();

        when(archivoService.descargarArchivoMasivas(any(), any(), any())).thenReturn(response);
        doNothing().when(archivoService).logAuditoria(any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = archivoController.descargarArchivoMasivasPDF(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void descargarArchivoMasivasXLSX_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        ReporteDTO response = new ReporteDTO();

        when(archivoService.descargarArchivoMasivas(any(), any(), any())).thenReturn(response);
        doNothing().when(archivoService).logAuditoria(any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = archivoController.descargarArchivoMasivasXLSX(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void descargarArchivoTitularidadPDF_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        ReporteDTO response = new ReporteDTO();

        when(archivoService.descargarArchivoTitularidad(any(), any(), any())).thenReturn(response);
        doNothing().when(archivoService).logAuditoria(any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = archivoController.descargarArchivoTitularidadPDF(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void descargarArchivoTitularidadXLSX_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        ReporteDTO response = new ReporteDTO();

        when(archivoService.descargarArchivoTitularidad(any(), any(), any())).thenReturn(response);
        doNothing().when(archivoService).logAuditoria(any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = archivoController.descargarArchivoTitularidadXLSX(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void descargarDetalleArchivoMasivasPDF_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        ReporteDTO response = new ReporteDTO();

        when(detalleMasivasService.descargarDetalleArchivoMasivas(any(), any(), any())).thenReturn(response);
        doNothing().when(detalleMasivasService).logAuditoria(any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = archivoController.descargarDetalleArchivoMasivasPDF(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void descargarDetalleMasivasXLSX_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        ReporteDTO response = new ReporteDTO();

        when(detalleMasivasService.descargarDetalleArchivoMasivas(any(), any(), any())).thenReturn(response);
        doNothing().when(detalleMasivasService).logAuditoria(any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = archivoController.descargarDetalleMasivasXLSX(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void gestionarOperacionDirectorio_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();

        when(archivoService.gestionarOperacionDirectorio(any())).thenReturn("OK");
        doNothing().when(archivoService).logAuditoria(any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = archivoController.gestionarOperacionDirectorio(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void gestionarOperacionMasivas_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();

        when(archivoService.gestionarOperacionMasivas(any())).thenReturn("OK");
        doNothing().when(archivoService).logAuditoria(any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = archivoController.gestionarOperacionMasivas(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void gestionarOperacionTitularidad_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();

        when(archivoService.gestionarOperacionTitularidad(any())).thenReturn("OK");
        doNothing().when(archivoService).logAuditoria(any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = archivoController.gestionarOperacionTitularidad(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

}
