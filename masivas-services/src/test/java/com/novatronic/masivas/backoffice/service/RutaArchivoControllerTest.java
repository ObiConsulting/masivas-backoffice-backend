package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.controller.RutaController;
import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaRutaDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroRutaDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
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
public class RutaArchivoControllerTest {

    @Mock
    private RutaService rutaService;
    @Mock
    private UserContext userContext;
    @InjectMocks
    private RutaController rutaController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void buscarRuta_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        CustomPaginate<DetalleConsultaRutaDTO> response = new CustomPaginate<>();

        when(rutaService.buscarRuta(any())).thenReturn(response);
        doNothing().when(rutaService).logAuditoria(any(), any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = rutaController.buscarRuta(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void editarRuta_exito() {
        MasivasRequestDTO request = new MasivasRequestDTO();

        when(rutaService.editarRuta(any(), any())).thenReturn(1l);
        doNothing().when(rutaService).logAuditoria(any(), any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = rutaController.editarRuta(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void obtenerRuta_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        DetalleRegistroRutaDTO response = new DetalleRegistroRutaDTO();

        when(rutaService.obtenerRuta(any())).thenReturn(response);
        doNothing().when(rutaService).logAuditoria(any(), any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = rutaController.obtenerRuta(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void descargarPDF_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        ReporteDTO response = new ReporteDTO();

        when(rutaService.descargarRutas(any(), any(), any())).thenReturn(response);
        doNothing().when(rutaService).logAuditoria(any(), any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = rutaController.descargarPDF(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void descargarXLSX_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        ReporteDTO response = new ReporteDTO();

        when(rutaService.descargarRutas(any(), any(), any())).thenReturn(response);
        doNothing().when(rutaService).logAuditoria(any(), any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = rutaController.descargarXLSX(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

}
