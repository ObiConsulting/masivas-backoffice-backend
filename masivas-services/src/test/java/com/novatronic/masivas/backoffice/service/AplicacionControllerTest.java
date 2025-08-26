package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.controller.AplicacionController;
import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaAplicacionDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroAplicacionDTO;
import com.novatronic.masivas.backoffice.dto.EstadoDTO;
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
public class AplicacionControllerTest {

    @Mock
    private AplicacionService aplicacionService;
    @Mock
    private UserContext userContext;
    @InjectMocks
    private AplicacionController aplicacionController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void crearAplicacion_exito() {
        MasivasRequestDTO request = new MasivasRequestDTO();

        when(aplicacionService.crearAplicacion(any(), any())).thenReturn(1l);
        doNothing().when(aplicacionService).logAuditoria(any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = aplicacionController.registrarAplicacion(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void buscarAplicacion_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        CustomPaginate<DetalleConsultaAplicacionDTO> response = new CustomPaginate<>();

        when(aplicacionService.buscarAplicacion(any())).thenReturn(response);
        doNothing().when(aplicacionService).logAuditoria(any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = aplicacionController.buscarAplicacion(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void editarAplicacion_exito() {
        MasivasRequestDTO request = new MasivasRequestDTO();

        when(aplicacionService.editarAplicacion(any(), any())).thenReturn(1l);
        doNothing().when(aplicacionService).logAuditoria(any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = aplicacionController.editarAplicacion(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void obtenerAplicacion_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        DetalleRegistroAplicacionDTO response = new DetalleRegistroAplicacionDTO();

        when(aplicacionService.obtenerAplicacion(any())).thenReturn(response);
        doNothing().when(aplicacionService).logAuditoria(any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = aplicacionController.obtenerAplicacion(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void activarAplicacion_exito() {
        MasivasRequestDTO request = new MasivasRequestDTO();
        EstadoDTO response = new EstadoDTO();

        when(aplicacionService.cambiarEstadoAplicacion(any(), any(), any())).thenReturn(response);
        doNothing().when(aplicacionService).logAuditoria(any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = aplicacionController.activar(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void desactivarAplicacion_exito() {
        MasivasRequestDTO request = new MasivasRequestDTO();
        EstadoDTO response = new EstadoDTO();

        when(aplicacionService.cambiarEstadoAplicacion(any(), any(), any())).thenReturn(response);
        doNothing().when(aplicacionService).logAuditoria(any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = aplicacionController.desactivar(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void descargarAplicacionPDF_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        ReporteDTO response = new ReporteDTO();

        when(aplicacionService.descargarAplicacion(any(), any(), any())).thenReturn(response);
        doNothing().when(aplicacionService).logAuditoria(any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = aplicacionController.descargarPDF(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void descargarAplicacionXLSX_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        ReporteDTO response = new ReporteDTO();

        when(aplicacionService.descargarAplicacion(any(), any(), any())).thenReturn(response);
        doNothing().when(aplicacionService).logAuditoria(any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = aplicacionController.descargarXLSX(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

}
