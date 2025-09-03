package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.controller.ProcesoController;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaProcesoDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroProcesoDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class ProcesoControllerTest {

    @Mock
    private ProcesoService procesoService;
    @Mock
    private UserContext userContext;
    @InjectMocks
    private ProcesoController procesoController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void buscarProceso_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        Map<String, List<DetalleConsultaProcesoDTO>> response = new HashMap<>();

        when(procesoService.buscarProceso(any())).thenReturn(response);
        doNothing().when(procesoService).logAuditoria(any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = procesoController.buscarProceso(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void editarProceso_exito() {
        List<DetalleRegistroProcesoDTO> request = new ArrayList();

        when(procesoService.editarProceso(any(), any())).thenReturn(1l);
        doNothing().when(procesoService).logAuditoria(any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = procesoController.editarProceso(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

    @Test
    void obtenerProceso_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        Map<String, List<DetalleRegistroProcesoDTO>> response = new HashMap<>();

        when(procesoService.obtenerProceso(any())).thenReturn(response);
        doNothing().when(procesoService).logAuditoria(any(), any(), any());

        ResponseEntity<MasivasResponse<Object>> resultado = procesoController.obtenerProceso(request, userContext);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

}
