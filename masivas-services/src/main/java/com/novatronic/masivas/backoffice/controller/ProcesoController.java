package com.novatronic.masivas.backoffice.controller;

import com.novatronic.masivas.backoffice.dto.DetalleConsultaProcesoDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroProcesoDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.service.ProcesoService;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import com.novatronic.novalog.audit.annotation.Audit;
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

    /**
     * Endpoing que realiza la busqueda de los procesos en el sistema. Recibe un
     * objeto con filtros de búsqueda, y retorna una lista paginada de procesos
     * que coincidan con dichos criterios.
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/buscar")
    @Audit(accion = Evento.EV_CONSULTA_REPORTE, origen = ConstantesServices.ACCION_VIEW, recursosAfectados = ConstantesServices.TABLA_PROCESO)
    public ResponseEntity<MasivasResponse<Object>> buscarProceso(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        Map<String, List<DetalleConsultaProcesoDTO>> objPageable = procesoService.buscarProceso(request);
        procesoService.logAuditoria(request, userContext, ConstantesServices.MENSAJE_EXITO_CONSULTA_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_CONSULTA_OPERACION, objPageable));
    }

    /**
     * Endpoint que realiza la edición de un proceso en el sistema. Si la
     * operación es exitosa, se retorna el ID del proceso recién editado.
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/editar")
    @Audit(accion = Evento.EV_ACTUALIZA_CONFIG_SISTEMA, origen = ConstantesServices.ACCION_UPDATE, recursosAfectados = ConstantesServices.TABLA_PROCESO)
    public ResponseEntity<MasivasResponse<Object>> editarProceso(@Valid @RequestBody List<DetalleRegistroProcesoDTO> request, @AuthenticationPrincipal UserContext userContext) {
        Long idProceso = procesoService.editarProceso(request, userContext.getUsername());
        procesoService.logAuditoria(request, userContext, ConstantesServices.MENSAJE_EXITO_EDITAR_SCHEDULER);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_EDITAR_SCHEDULER, idProceso));
    }

    /**
     * Endpoint que devuelve los detalles de un proceso específico a partir de
     * su ID.
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/obtener")
    @Audit(accion = Evento.EV_CONSULTA_REPORTE, origen = ConstantesServices.ACCION_READ, recursosAfectados = ConstantesServices.TABLA_PROCESO)
    public ResponseEntity<MasivasResponse<Object>> obtenerProceso(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        Map<String, List<DetalleRegistroProcesoDTO>> objPageable = procesoService.obtenerProceso(request);
        procesoService.logAuditoria(request, userContext, ConstantesServices.MENSAJE_EXITO_OBTENER_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_OBTENER_OPERACION, objPageable));
    }

}
