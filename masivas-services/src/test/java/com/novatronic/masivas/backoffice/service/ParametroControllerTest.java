package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.controller.ParametroController;
import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaParametroDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroParametroDTO;
import com.novatronic.masivas.backoffice.dto.EstadoDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.dto.ParametroDTO;
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
public class ParametroControllerTest {

    @Mock
    private ParametroService parametroService;
    @Mock
    private GenericService genericService;
    @Mock
    private UserContext userContext;
    @InjectMocks
    private ParametroController parametroController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void crearParametro_exito() {
        MasivasRequestDTO request = new MasivasRequestDTO();

        when(parametroService.crearParametro(any(), any())).thenReturn(1l);
        doNothing().when(parametroService).logAuditoria(any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = parametroController.registrar(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void buscarParametro_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        CustomPaginate<DetalleConsultaParametroDTO> response = new CustomPaginate<>();

        when(parametroService.buscarParametro(any())).thenReturn(response);
        doNothing().when(parametroService).logAuditoria(any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = parametroController.buscar(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void editarParametro_exito() {
        MasivasRequestDTO request = new MasivasRequestDTO();

        when(parametroService.editarParametro(any(), any())).thenReturn(1l);
        doNothing().when(parametroService).logAuditoria(any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = parametroController.editar(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void obtenerParametro_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        DetalleRegistroParametroDTO response = new DetalleRegistroParametroDTO();

        when(parametroService.obtenerParametro(any())).thenReturn(response);
        doNothing().when(parametroService).logAuditoria(any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = parametroController.obtener(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void activarParametro_exito() {
        MasivasRequestDTO request = new MasivasRequestDTO();
        EstadoDTO response = new EstadoDTO();

        when(parametroService.cambiarEstadoParametro(any(), any(), any())).thenReturn(response);
        doNothing().when(parametroService).logAuditoria(any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = parametroController.activar(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void desactivarParametro_exito() {
        MasivasRequestDTO request = new MasivasRequestDTO();
        EstadoDTO response = new EstadoDTO();

        when(parametroService.cambiarEstadoParametro(any(), any(), any())).thenReturn(response);
        doNothing().when(parametroService).logAuditoria(any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = parametroController.desactivar(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void descargarParametroPDF_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        ReporteDTO response = new ReporteDTO();

        when(parametroService.descargarParametro(any(), any(), any())).thenReturn(response);
        doNothing().when(parametroService).logAuditoria(any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = parametroController.descargarPDF(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void descargarParametroXLSX_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        ReporteDTO response = new ReporteDTO();

        when(parametroService.descargarParametro(any(), any(), any())).thenReturn(response);
        doNothing().when(parametroService).logAuditoria(any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = parametroController.descargarXLSX(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void listarEstadoArchivos_exito() {
        List<ParametroDTO> response = new ArrayList();

        when(genericService.getAllEstadoArchivo()).thenReturn(response);
        doNothing().when(parametroService).logAuditoria(any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = parametroController.listarEstadoArchivos(userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void listarCategoriaDirectorio_exito() {
        List<ParametroDTO> response = new ArrayList();

        when(genericService.getAllCategoriaDirectorio()).thenReturn(response);
        doNothing().when(parametroService).logAuditoria(any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = parametroController.listarCategoriaDirectorio(userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void listarTipoArchivo_exito() {
        List<ParametroDTO> response = new ArrayList();

        when(genericService.getAllTipoArchivo()).thenReturn(response);
        doNothing().when(parametroService).logAuditoria(any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = parametroController.listarTipoArchivo(userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void listarExtensionBase_exito() {
        List<ParametroDTO> response = new ArrayList();

        when(genericService.getAllExtensionBase()).thenReturn(response);
        doNothing().when(parametroService).logAuditoria(any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = parametroController.listarExtensionBase(userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void listarExtensionControl_exito() {
        List<ParametroDTO> response = new ArrayList();

        when(genericService.getAllExtensionControl()).thenReturn(response);
        doNothing().when(parametroService).logAuditoria(any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = parametroController.listarExtensionControl(userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void listarTipoTransaccion_exito() {
        List<ParametroDTO> response = new ArrayList();

        when(genericService.getAllTipoTransaccion()).thenReturn(response);
        doNothing().when(parametroService).logAuditoria(any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = parametroController.listarTipoTransaccion(userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void listarMotivoRechazo_exito() {
        List<ParametroDTO> response = new ArrayList();

        when(genericService.getAllMotivoRechazo()).thenReturn(response);
        doNothing().when(parametroService).logAuditoria(any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = parametroController.listarMotivoRechazo(userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

}
