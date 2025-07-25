package com.novatronic.masivas.backoffice.controller;

import com.novatronic.masivas.backoffice.dto.DetalleConsultaProcesoDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroProcesoDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.service.ProcesoService;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import com.novatronic.novalog.audit.util.Estado;
import com.novatronic.novalog.audit.util.Evento;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Obi Consulting
 */
@RequestMapping(value = "proceso", produces = "application/json")
@RestController
public class ProcesoController {

    @Autowired
    private ProcesoService procesoService;

    @PostMapping("/buscar")
    public ResponseEntity<MasivasResponse<Object>> buscarProceso(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        Map<String, List<DetalleConsultaProcesoDTO>> objPegeable = procesoService.buscarProceso(request);
        procesoService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_PROCESO,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_CONSULTA_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_CONSULTA_OPERACION, objPegeable));
    }

    @PostMapping("/editar")
    public ResponseEntity<MasivasResponse<Object>> editarProceso(@Valid @RequestBody List<DetalleRegistroProcesoDTO> request, @AuthenticationPrincipal UserContext userContext) {
        Long idProceso = procesoService.editarProceso(request, userContext.getUsername());
        procesoService.logAuditoria(request, Evento.EV_ACTUALIZACION_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_PROCESO,
                ConstantesServices.ACCION_UPDATE, ConstantesServices.MENSAJE_EXITO_EDITAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_EDITAR_OPERACION, idProceso));
    }

    @PostMapping("/obtener")
    public ResponseEntity<MasivasResponse<Object>> obtenerProceso(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        Map<String, List<DetalleRegistroProcesoDTO>> objPegeable = procesoService.obtenerProceso(request);
        procesoService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_PROCESO,
                ConstantesServices.ACCION_VIEW, ConstantesServices.MENSAJE_EXITO_OBTENER_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_OBTENER_OPERACION, objPegeable));
    }

}
