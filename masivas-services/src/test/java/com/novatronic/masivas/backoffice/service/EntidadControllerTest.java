package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.controller.EntidadController;
import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaEntidadDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroEntidadDTO;
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
public class EntidadControllerTest {

    @Mock
    private EntidadService entidadService;
    @Mock
    private UserContext userContext;
    @InjectMocks
    private EntidadController entidadController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void crearEntidad_exito() {
        MasivasRequestDTO request = new MasivasRequestDTO();

        when(entidadService.crearEntidad(any(), any())).thenReturn(1l);
        doNothing().when(entidadService).logAuditoria(any(), any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = entidadController.registrarEntidad(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void buscarEntidad_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        CustomPaginate<DetalleConsultaEntidadDTO> response = new CustomPaginate<>();

        when(entidadService.buscarEntidad(any())).thenReturn(response);
        doNothing().when(entidadService).logAuditoria(any(), any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = entidadController.buscarEntidad(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void editarEntidad_exito() {
        MasivasRequestDTO request = new MasivasRequestDTO();

        when(entidadService.editarEntidad(any(), any())).thenReturn(1l);
        doNothing().when(entidadService).logAuditoria(any(), any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = entidadController.editarEntidad(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void obtenerEntidad_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        DetalleRegistroEntidadDTO response = new DetalleRegistroEntidadDTO();

        when(entidadService.obtenerEntidad(any())).thenReturn(response);
        doNothing().when(entidadService).logAuditoria(any(), any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = entidadController.obtenerEntidad(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void activarEntidad_exito() {
        MasivasRequestDTO request = new MasivasRequestDTO();
        EstadoDTO response = new EstadoDTO();

        when(entidadService.cambiarEstadoEntidad(any(), any(), any())).thenReturn(response);
        doNothing().when(entidadService).logAuditoria(any(), any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = entidadController.activar(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void desactivarEntidad_exito() {
        MasivasRequestDTO request = new MasivasRequestDTO();
        EstadoDTO response = new EstadoDTO();

        when(entidadService.cambiarEstadoEntidad(any(), any(), any())).thenReturn(response);
        doNothing().when(entidadService).logAuditoria(any(), any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = entidadController.desactivar(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void descargarEntidadPDF_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        ReporteDTO response = new ReporteDTO();

        when(entidadService.descargarEntidades(any(), any(), any())).thenReturn(response);
        doNothing().when(entidadService).logAuditoria(any(), any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = entidadController.descargarPDF(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void descargarEntidadXLSX_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        ReporteDTO response = new ReporteDTO();

        when(entidadService.descargarEntidades(any(), any(), any())).thenReturn(response);
        doNothing().when(entidadService).logAuditoria(any(), any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = entidadController.descargarXLSX(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

}
