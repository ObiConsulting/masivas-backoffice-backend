package com.novatronic.masivas.backoffice.controller;

import com.novatronic.masivas.backoffice.dto.ParametroDTO;
import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaGrupoParametroDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroGrupoParametroDTO;
import com.novatronic.masivas.backoffice.dto.EstadoDTO;
import com.novatronic.masivas.backoffice.dto.ReporteDTO;
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

    /**
     * Endpoint que realiza la creación de un grupo parámetro en el sistema. Si
     * la operación es exitosa, se retorna el ID del grupo parámetro recién
     * creado.
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/crear")
    public ResponseEntity<MasivasResponse<Object>> registrar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        Long idEntidad = grupoParametroService.crearGrupoParametro(request, userContext.getUsername());
        grupoParametroService.logAuditoria(request, Evento.EV_REGISTRO_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_GRUPO_PARAMETRO,
                ConstantesServices.ACCION_CREATE, ConstantesServices.MENSAJE_EXITO_CREAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_CREAR_OPERACION, idEntidad));
    }

    /**
     * Endpoing que realiza la busqueda de los grupos de parámetros en el
     * sistema. Recibe un objeto con filtros de búsqueda, y retorna una lista
     * paginada de grupos de parámetros que coincidan con dichos criterios.
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/buscar")
    public ResponseEntity<MasivasResponse<Object>> buscar(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        CustomPaginate<DetalleConsultaGrupoParametroDTO> objPageable = grupoParametroService.buscarGrupoParametro(request);
        grupoParametroService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_GRUPO_PARAMETRO,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION, objPageable));
    }

    /**
     * Endpoint que realiza la edición de un grupo parámetro en el sistema. Si
     * la operación es exitosa, se retorna el ID del grupo parámetro recién
     * editado.
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/editar")
    public ResponseEntity<MasivasResponse<Object>> editar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        Long idGrupoParametro = grupoParametroService.editarGrupoParametro(request, userContext.getUsername());
        grupoParametroService.logAuditoria(request, Evento.EV_ACTUALIZACION_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_GRUPO_PARAMETRO,
                ConstantesServices.ACCION_UPDATE, ConstantesServices.MENSAJE_EXITO_EDITAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_EDITAR_OPERACION, idGrupoParametro));
    }

    /**
     * Endpoint que devuelve los detalles de un grupo parámetro específico a
     * partir de su ID.
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/obtener")
    public ResponseEntity<MasivasResponse<Object>> obtener(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        DetalleRegistroGrupoParametroDTO grupoParametroDTO = grupoParametroService.obtenerGrupoParametro(request);
        grupoParametroService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_GRUPO_PARAMETRO,
                ConstantesServices.ACCION_VIEW, ConstantesServices.MENSAJE_EXITO_OBTENER_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_OBTENER_OPERACION, grupoParametroDTO));
    }

    /**
     * Endpoint que Activa uno o varios grupos de parámetros a partir de su ID.
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/activar")
    public ResponseEntity<MasivasResponse<Object>> activar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        EstadoDTO estadoDTO = grupoParametroService.cambiarEstadoGrupoParametro(request, userContext.getUsername(), ConstantesServices.ESTADO_ACTIVO);
        grupoParametroService.logAuditoria(request, Evento.EV_ELIMINACION_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_GRUPO_PARAMETRO,
                ConstantesServices.ACCION_DELETE, estadoDTO.getMensaje());
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, estadoDTO.getMensaje(), estadoDTO.getNumExitos()));
    }

    /**
     * Endpoint que Inactiva uno o varios grupos de parámetros a partir de su
     * ID.
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/desactivar")
    public ResponseEntity<MasivasResponse<Object>> desactivar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        EstadoDTO estadoDTO = grupoParametroService.cambiarEstadoGrupoParametro(request, userContext.getUsername(), ConstantesServices.ESTADO_INACTIVO);
        grupoParametroService.logAuditoria(request, Evento.EV_ELIMINACION_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_GRUPO_PARAMETRO,
                ConstantesServices.ACCION_DELETE, estadoDTO.getMensaje());
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, estadoDTO.getMensaje(), estadoDTO.getNumExitos()));
    }

    /**
     * Endpoint que genera y descarga un reporte en formato PDF con la lista de
     * grupos de parámetros.
     *
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/descargarPDF")
    public ResponseEntity<MasivasResponse<Object>> descargarPDF(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        ReporteDTO reporteDTO = grupoParametroService.descargarGrupoParametro(request, userContext.getUsername(), ConstantesServices.TIPO_ARCHIVO_PDF);
        grupoParametroService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_GRUPO_PARAMETRO,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, reporteDTO));
    }

    /**
     * Endpoint que genera y descarga un reporte en formato XLSX con la lista de
     * grupos de parámetros.
     *
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/descargarXLSX")
    public ResponseEntity<MasivasResponse<Object>> descargarXLSX(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        ReporteDTO reporteDTO = grupoParametroService.descargarGrupoParametro(request, userContext.getUsername(), ConstantesServices.TIPO_ARCHIVO_XLSX);
        grupoParametroService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_GRUPO_PARAMETRO,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, reporteDTO));
    }

    /**
     * Endpoint que obtiene la lista completa de todos los grupos de parámetros.
     *
     * @param userContext
     * @return
     */
    @GetMapping("/listar")
    public ResponseEntity<MasivasResponse<Object>> listarGrupoParametro(@AuthenticationPrincipal UserContext userContext) {
        List<ParametroDTO> lista = grupoParametroService.getAllGrupoParametro();
        grupoParametroService.logAuditoria(null, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_GRUPO_PARAMETRO,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_GENERICO, lista));
    }

}
