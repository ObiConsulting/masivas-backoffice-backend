package com.novatronic.masivas.backoffice.controller;

import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaEntidadDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroEntidadDTO;
import com.novatronic.masivas.backoffice.dto.EstadoDTO;
import com.novatronic.masivas.backoffice.dto.ReporteDTO;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.service.EntidadService;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import com.novatronic.novalog.audit.annotation.Audit;
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
@RequestMapping(value = "entidad", produces = "application/json")
@RestController
public class EntidadController {

    @Autowired
    private EntidadService entidadService;

    /**
     * Endpoint que realiza la creación de una entidad en el sistema. Si la
     * operación es exitosa, se retorna el ID de la entidad recién creada.
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/crear")
    @Audit(accion = Evento.EV_REGISTRO_CONFIG_SISTEMA, origen = ConstantesServices.ACCION_CREATE, recursosAfectados = ConstantesServices.TABLA_ENTIDAD)
    public ResponseEntity<MasivasResponse<Object>> registrarEntidad(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        Long idEntidad = entidadService.crearEntidad(request, userContext.getUsername());
        entidadService.logAuditoria(request, userContext, ConstantesServices.MENSAJE_EXITO_CREAR_ENTIDAD);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_CREAR_ENTIDAD, idEntidad));
    }

    /**
     * Endpoing que realiza la busqueda de las entidades en el sistema. Recibe
     * un objeto con filtros de búsqueda, y retorna una lista paginada de
     * entidades que coincidan con dichos criterios.
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/buscar")
    public ResponseEntity<MasivasResponse<Object>> buscarEntidad(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        CustomPaginate<DetalleConsultaEntidadDTO> objPageable = entidadService.buscarEntidad(request);
        entidadService.logAuditoria(request, userContext, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION, objPageable));
    }

    /**
     * Endpoint que realiza la edición de una entidad en el sistema. Si la
     * operación es exitosa, se retorna el ID de la entidad recién editada.
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/editar")
    @Audit(accion = Evento.EV_ACTUALIZA_CONFIG_SISTEMA, origen = ConstantesServices.ACCION_UPDATE, recursosAfectados = ConstantesServices.TABLA_ENTIDAD)
    public ResponseEntity<MasivasResponse<Object>> editarEntidad(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        Long idEntidad = entidadService.editarEntidad(request, userContext.getUsername());
        entidadService.logAuditoria(request, userContext, ConstantesServices.MENSAJE_EXITO_EDITAR_ENTIDAD);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_EDITAR_ENTIDAD, idEntidad));
    }

    /**
     * Endpoint que devuelve los detalles de una entidad específica a partir de
     * su ID.
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/obtener")
    @Audit(accion = Evento.EV_CONSULTA_REPORTE, origen = ConstantesServices.ACCION_VIEW, recursosAfectados = ConstantesServices.TABLA_ENTIDAD)
    public ResponseEntity<MasivasResponse<Object>> obtenerEntidad(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        DetalleRegistroEntidadDTO entidadDTO = entidadService.obtenerEntidad(request);
        entidadService.logAuditoria(request, userContext, ConstantesServices.MENSAJE_EXITO_OBTENER_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_OBTENER_OPERACION, entidadDTO));
    }

    /**
     * Endpoint que Activa una o varias entidades a partir de su ID.
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/activar")
    @Audit(accion = Evento.EV_ELIMINA_CONFIG_SISTEMA, origen = ConstantesServices.ACCION_DELETE, recursosAfectados = ConstantesServices.TABLA_ENTIDAD)
    public ResponseEntity<MasivasResponse<Object>> activar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        EstadoDTO estadoDTO = entidadService.cambiarEstadoEntidad(request, userContext.getUsername(), ConstantesServices.ESTADO_ACTIVO);
        entidadService.logAuditoria(request, userContext, estadoDTO.getMensaje());
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, estadoDTO.getMensaje(), estadoDTO.getNumExitos()));
    }

    /**
     * Endpoint que Inactiva una o varias entidades a partir de su ID.
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/desactivar")
    @Audit(accion = Evento.EV_ELIMINA_CONFIG_SISTEMA, origen = ConstantesServices.ACCION_DELETE, recursosAfectados = ConstantesServices.TABLA_ENTIDAD)
    public ResponseEntity<MasivasResponse<Object>> desactivar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        EstadoDTO estadoDTO = entidadService.cambiarEstadoEntidad(request, userContext.getUsername(), ConstantesServices.ESTADO_INACTIVO);
        entidadService.logAuditoria(request, userContext, estadoDTO.getMensaje());
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, estadoDTO.getMensaje(), estadoDTO.getNumExitos()));
    }

    /**
     * Endpoint que genera y descarga un reporte en formato PDF con la lista de
     * entidades.
     *
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/descargarPDF")
    @Audit(accion = Evento.EV_CONSULTA_REPORTE, origen = ConstantesServices.ACCION_READ, recursosAfectados = ConstantesServices.TABLA_ENTIDAD)
    public ResponseEntity<MasivasResponse<Object>> descargarPDF(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        ReporteDTO reporteDTO = entidadService.descargarEntidades(request, userContext.getUsername(), ConstantesServices.TIPO_ARCHIVO_PDF);
        entidadService.logAuditoria(request, userContext, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, reporteDTO));
    }

    /**
     * Endpoint que genera y descarga un reporte en formato XLSX con la lista de
     * entidades.
     *
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/descargarXLSX")
    @Audit(accion = Evento.EV_CONSULTA_REPORTE, origen = ConstantesServices.ACCION_READ, recursosAfectados = ConstantesServices.TABLA_ENTIDAD)
    public ResponseEntity<MasivasResponse<Object>> descargarXLSX(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        ReporteDTO reporteDTO = entidadService.descargarEntidades(request, userContext.getUsername(), ConstantesServices.TIPO_ARCHIVO_XLSX);
        entidadService.logAuditoria(request, userContext, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, reporteDTO));
    }

}
