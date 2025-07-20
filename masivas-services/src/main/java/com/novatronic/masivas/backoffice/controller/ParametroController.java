package com.novatronic.masivas.backoffice.controller;

import com.novatronic.masivas.backoffice.dto.ComboEstadoDTO;
import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaParametroDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroParametroDTO;
import com.novatronic.masivas.backoffice.dto.EstadoDTO;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.service.ParametroService;
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
@RequestMapping(value = "parametro", produces = "application/json")
@RestController
public class ParametroController {

    private final ParametroService parametroService;

    public ParametroController(ParametroService parametroService) {
        this.parametroService = parametroService;
    }

    @PostMapping("/crear")
    public ResponseEntity<MasivasResponse> registrar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        Long idParametro = parametroService.crearParametro(request, userContext.getUsername());
        parametroService.logAuditoria(request, Evento.EV_REGISTRO_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_PARAMETRO,
                ConstantesServices.ACCION_CREATE, ConstantesServices.MENSAJE_EXITO_CREAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_CREAR_OPERACION, idParametro));
    }

    @PostMapping("/buscar")
    public ResponseEntity<MasivasResponse> buscar(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        CustomPaginate<DetalleConsultaParametroDTO> objPegeable = parametroService.buscarParametro(request, userContext.getUsername());
        parametroService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_PARAMETRO,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION, objPegeable));
    }

    @PostMapping("/editar")
    public ResponseEntity<MasivasResponse> editar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        Long idParametro = parametroService.editarParametro(request, userContext.getUsername());
        parametroService.logAuditoria(request, Evento.EV_ACTUALIZACION_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_PARAMETRO,
                ConstantesServices.ACCION_UPDATE, ConstantesServices.MENSAJE_EXITO_EDITAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_EDITAR_OPERACION, idParametro));
    }

    @PostMapping("/obtener")
    public ResponseEntity<MasivasResponse> obtener(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        DetalleRegistroParametroDTO parametroDTO = parametroService.obtenerParametro(request, userContext.getUsername());
        parametroService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_PARAMETRO,
                ConstantesServices.ACCION_VIEW, ConstantesServices.MENSAJE_EXITO_OBTENER_OPERACION);
        return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_OBTENER_OPERACION, parametroDTO));
    }

    @PostMapping("/activar")
    public ResponseEntity<MasivasResponse> activar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        EstadoDTO estadoDTO = parametroService.cambiarEstadoParametro(request, userContext.getUsername(), ConstantesServices.ESTADO_ACTIVO);
        parametroService.logAuditoria(request, Evento.EV_ELIMINACION_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_PARAMETRO,
                ConstantesServices.ACCION_DELETE, estadoDTO.getMensaje());
        return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, estadoDTO.getMensaje(), estadoDTO.getNumExitos()));
    }

    @PostMapping("/desactivar")
    public ResponseEntity<MasivasResponse> desactivar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        EstadoDTO estadoDTO = parametroService.cambiarEstadoParametro(request, userContext.getUsername(), ConstantesServices.ESTADO_INACTIVO);
        parametroService.logAuditoria(request, Evento.EV_ELIMINACION_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_PARAMETRO,
                ConstantesServices.ACCION_DELETE, estadoDTO.getMensaje());
        return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, estadoDTO.getMensaje(), estadoDTO.getNumExitos()));
    }

    @GetMapping("/listarEstadoArchivos")
    public ResponseEntity<MasivasResponse> listarEstadoArchivos(@AuthenticationPrincipal UserContext userContext) {
        List<ComboEstadoDTO> listaEstado = parametroService.listarParametrosPorGrupoConsulta(ConstantesServices.ID_GRUPO_ESTADO_ARCHIVOS);
        //TODO Imprimir OK log auditoria
        return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_GENERICO, listaEstado));
    }

    @GetMapping("/listarCategoriaDirectorio")
    public ResponseEntity<MasivasResponse> listarCategoriaDirectorio(@AuthenticationPrincipal UserContext userContext) {
        List<ComboEstadoDTO> listaEstado = parametroService.listarCategoriDirectorio(ConstantesServices.ID_GRUPO_CATEGORIA_DIRECTORIO);
        //TODO Imprimir OK log auditoria
        return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_GENERICO, listaEstado));
    }

    @GetMapping("/listarTipoArchivo")
    public ResponseEntity<MasivasResponse> listarTipoArchivo(@AuthenticationPrincipal UserContext userContext) {
        List<ComboEstadoDTO> listaEstado = parametroService.listarParametrosPorGrupoConsulta(ConstantesServices.ID_GRUPO_TIPO_ARCHIVO);
        //TODO Imprimir OK log auditoria
        return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_GENERICO, listaEstado));
    }

    @GetMapping("/listarExtensionBase")
    public ResponseEntity<MasivasResponse> listarExtensionBase(@AuthenticationPrincipal UserContext userContext) {
        List<ComboEstadoDTO> listaEstado = parametroService.listarParametrosPorGrupoFormulario(ConstantesServices.ID_GRUPO_EXTENSION_BASE);
        //TODO Imprimir OK log auditoria
        return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_GENERICO, listaEstado));
    }

    @GetMapping("/listarExtensionControl")
    public ResponseEntity<MasivasResponse> listarExtensionControl(@AuthenticationPrincipal UserContext userContext) {
        List<ComboEstadoDTO> listaEstado = parametroService.listarParametrosPorGrupoFormulario(ConstantesServices.ID_GRUPO_EXTENSION_CONTROL);
        //TODO Imprimir OK log auditoria
        return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_GENERICO, listaEstado));
    }
}
