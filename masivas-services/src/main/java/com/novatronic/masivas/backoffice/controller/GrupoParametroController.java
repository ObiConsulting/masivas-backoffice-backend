package com.novatronic.masivas.backoffice.controller;

import com.novatronic.masivas.backoffice.dto.ComboEstadoDTO;
import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaGrupoParametroDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroGrupoParametroDTO;
import com.novatronic.masivas.backoffice.dto.EstadoDTO;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.service.GrupoParametroService;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import com.novatronic.novalog.audit.util.Estado;
import com.novatronic.novalog.audit.util.Evento;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Obi Consulting
 */
@RequestMapping(value = "grupoParametro", produces = "application/json")
@RestController
public class GrupoParametroController {

    private final GrupoParametroService grupoParametroService;

    public GrupoParametroController(GrupoParametroService grupoParametroService) {
        this.grupoParametroService = grupoParametroService;
    }

    @PostMapping("/crear")
    public ResponseEntity<MasivasResponse<Object>> registrar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        Long idEntidad = grupoParametroService.crearGrupoParametro(request, userContext.getUsername());
        grupoParametroService.logAuditoria(request, Evento.EV_REGISTRO_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_GRUPO_PARAMETRO,
                ConstantesServices.ACCION_CREATE, ConstantesServices.MENSAJE_EXITO_CREAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_CREAR_OPERACION, idEntidad));
    }

    @PostMapping("/buscar")
    public ResponseEntity<MasivasResponse<Object>> buscar(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        CustomPaginate<DetalleConsultaGrupoParametroDTO> objPegeable = grupoParametroService.buscarGrupoParametro(request, userContext.getUsername());
        grupoParametroService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_GRUPO_PARAMETRO,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION, objPegeable));
    }

    @PostMapping("/editar")
    public ResponseEntity<MasivasResponse<Object>> editar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        Long idGrupoParametro = grupoParametroService.editarGrupoParametro(request, userContext.getUsername());
        grupoParametroService.logAuditoria(request, Evento.EV_ACTUALIZACION_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_GRUPO_PARAMETRO,
                ConstantesServices.ACCION_UPDATE, ConstantesServices.MENSAJE_EXITO_EDITAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_EDITAR_OPERACION, idGrupoParametro));
    }

    @PostMapping("/obtener")
    public ResponseEntity<MasivasResponse<Object>> obtener(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        DetalleRegistroGrupoParametroDTO grupoParametroDTO = grupoParametroService.obtenerGrupoParametro(request, userContext.getUsername());
        grupoParametroService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_GRUPO_PARAMETRO,
                ConstantesServices.ACCION_VIEW, ConstantesServices.MENSAJE_EXITO_OBTENER_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_OBTENER_OPERACION, grupoParametroDTO));
    }

    @PostMapping("/activar")
    public ResponseEntity<MasivasResponse<Object>> activar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        EstadoDTO estadoDTO = grupoParametroService.cambiarEstadoGrupoParametro(request, userContext.getUsername(), ConstantesServices.ESTADO_ACTIVO);
        grupoParametroService.logAuditoria(request, Evento.EV_ELIMINACION_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_GRUPO_PARAMETRO,
                ConstantesServices.ACCION_DELETE, estadoDTO.getMensaje());
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, estadoDTO.getMensaje(), estadoDTO.getNumExitos()));
    }

    @PostMapping("/desactivar")
    public ResponseEntity<MasivasResponse<Object>> desactivar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        EstadoDTO estadoDTO = grupoParametroService.cambiarEstadoGrupoParametro(request, userContext.getUsername(), ConstantesServices.ESTADO_INACTIVO);
        grupoParametroService.logAuditoria(request, Evento.EV_ELIMINACION_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_GRUPO_PARAMETRO,
                ConstantesServices.ACCION_DELETE, estadoDTO.getMensaje());
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, estadoDTO.getMensaje(), estadoDTO.getNumExitos()));
    }

    @GetMapping("/listar")
    public ResponseEntity<MasivasResponse<Object>> listarGrupoParametro(@AuthenticationPrincipal UserContext userContext) {
        List<ComboEstadoDTO> lista = grupoParametroService.listarGrupoParametro();
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_GENERICO, lista));
    }

}
