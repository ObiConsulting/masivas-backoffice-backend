package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.controller.GrupoParametroController;
import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaGrupoParametroDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroGrupoParametroDTO;
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
public class GrupoParametroControllerTest {

    @Mock
    private GrupoParametroService grupoParametroService;
    @Mock
    private UserContext userContext;
    @Mock
    private GenericService genericService;
    @InjectMocks
    private GrupoParametroController grupoParametroController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void crearGrupoParametro_exito() {
        MasivasRequestDTO request = new MasivasRequestDTO();

        when(grupoParametroService.crearGrupoParametro(any(), any())).thenReturn(1l);
        doNothing().when(grupoParametroService).logAuditoria(any(), any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = grupoParametroController.registrar(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void buscarGrupoParametro_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        CustomPaginate<DetalleConsultaGrupoParametroDTO> response = new CustomPaginate<>();

        when(grupoParametroService.buscarGrupoParametro(any())).thenReturn(response);
        doNothing().when(grupoParametroService).logAuditoria(any(), any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = grupoParametroController.buscar(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void editarGrupoParametro_exito() {
        MasivasRequestDTO request = new MasivasRequestDTO();

        when(grupoParametroService.editarGrupoParametro(any(), any())).thenReturn(1l);
        doNothing().when(grupoParametroService).logAuditoria(any(), any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = grupoParametroController.editar(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void obtenerGrupoParametro_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        DetalleRegistroGrupoParametroDTO response = new DetalleRegistroGrupoParametroDTO();

        when(grupoParametroService.obtenerGrupoParametro(any())).thenReturn(response);
        doNothing().when(grupoParametroService).logAuditoria(any(), any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = grupoParametroController.obtener(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void activarGrupoParametro_exito() {
        MasivasRequestDTO request = new MasivasRequestDTO();
        EstadoDTO response = new EstadoDTO();

        when(grupoParametroService.cambiarEstadoGrupoParametro(any(), any(), any())).thenReturn(response);
        doNothing().when(grupoParametroService).logAuditoria(any(), any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = grupoParametroController.activar(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void desactivarGrupoParametro_exito() {
        MasivasRequestDTO request = new MasivasRequestDTO();
        EstadoDTO response = new EstadoDTO();

        when(grupoParametroService.cambiarEstadoGrupoParametro(any(), any(), any())).thenReturn(response);
        doNothing().when(grupoParametroService).logAuditoria(any(), any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = grupoParametroController.desactivar(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void descargarGrupoParametroPDF_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        ReporteDTO response = new ReporteDTO();

        when(grupoParametroService.descargarGrupoParametro(any(), any(), any())).thenReturn(response);
        doNothing().when(grupoParametroService).logAuditoria(any(), any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = grupoParametroController.descargarPDF(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void descargarGrupoParametroXLSX_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        ReporteDTO response = new ReporteDTO();

        when(grupoParametroService.descargarGrupoParametro(any(), any(), any())).thenReturn(response);
        doNothing().when(grupoParametroService).logAuditoria(any(), any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = grupoParametroController.descargarXLSX(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void listarGrupoParametro_exito() {
        List<ParametroDTO> response = new ArrayList();

        when(grupoParametroService.getAllGrupoParametro()).thenReturn(response);
        doNothing().when(grupoParametroService).logAuditoria(any(), any(), any(), any(), any(), any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = grupoParametroController.listarGrupoParametro(userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

}
