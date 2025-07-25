package com.novatronic.masivas.backoffice.controller;

import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaAplicacionDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroAplicacionDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.dto.EstadoDTO;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.service.AplicacionService;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import com.novatronic.novalog.audit.util.Estado;
import com.novatronic.novalog.audit.util.Evento;
import jakarta.validation.Valid;
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
@RequestMapping(value = "aplicacion", produces = "application/json")
@RestController
public class AplicacionController {

    @Autowired
    private AplicacionService aplicacionService;

    @PostMapping("/crear")
    public ResponseEntity<MasivasResponse<Object>> registrarAplicacion(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        Long idAplicacion = aplicacionService.crearAplicacion(request, userContext.getUsername());
        aplicacionService.logAuditoria(request, Evento.EV_REGISTRO_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.APLICACION,
                ConstantesServices.ACCION_CREATE, ConstantesServices.MENSAJE_EXITO_CREAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_CREAR_OPERACION, idAplicacion));
    }

    @PostMapping("/buscar")
    public ResponseEntity<MasivasResponse<Object>> buscarAplicacion(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        CustomPaginate<DetalleConsultaAplicacionDTO> objPegeable = aplicacionService.buscarAplicacion(request, userContext.getUsername());
        aplicacionService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.APLICACION,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION, objPegeable));
    }

    @PostMapping("/editar")
    public ResponseEntity<MasivasResponse<Object>> editarAplicacion(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        Long idAplicacion = aplicacionService.editarAplicacion(request, userContext.getUsername());
        aplicacionService.logAuditoria(request, Evento.EV_ACTUALIZACION_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.APLICACION,
                ConstantesServices.ACCION_UPDATE, ConstantesServices.MENSAJE_EXITO_EDITAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_EDITAR_OPERACION, idAplicacion));
    }

    @PostMapping("/obtener")
    public ResponseEntity<MasivasResponse<Object>> obtenerAplicacion(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        DetalleRegistroAplicacionDTO aplicacionDTO = aplicacionService.obtenerAplicacion(request, userContext.getUsername());
        aplicacionService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.APLICACION,
                ConstantesServices.ACCION_VIEW, ConstantesServices.MENSAJE_EXITO_OBTENER_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_OBTENER_OPERACION, aplicacionDTO));
    }

    @PostMapping("/activar")
    public ResponseEntity<MasivasResponse<Object>> activar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        EstadoDTO estadoDTO = aplicacionService.cambiarEstadoAplicacion(request, userContext.getUsername(), ConstantesServices.ESTADO_ACTIVO);
        aplicacionService.logAuditoria(request, Evento.EV_ELIMINACION_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.APLICACION,
                ConstantesServices.ACCION_DELETE, estadoDTO.getMensaje());
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, estadoDTO.getMensaje(), estadoDTO.getNumExitos()));
    }

    @PostMapping("/desactivar")
    public ResponseEntity<MasivasResponse<Object>> desactivar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        EstadoDTO estadoDTO = aplicacionService.cambiarEstadoAplicacion(request, userContext.getUsername(), ConstantesServices.ESTADO_INACTIVO);
        aplicacionService.logAuditoria(request, Evento.EV_ELIMINACION_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.APLICACION,
                ConstantesServices.ACCION_DELETE, estadoDTO.getMensaje());
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, estadoDTO.getMensaje(), estadoDTO.getNumExitos()));
    }

}
