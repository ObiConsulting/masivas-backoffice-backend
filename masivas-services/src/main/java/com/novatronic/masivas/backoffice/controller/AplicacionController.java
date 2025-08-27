package com.novatronic.masivas.backoffice.controller;

import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaAplicacionDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroAplicacionDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.dto.EstadoDTO;
import com.novatronic.masivas.backoffice.dto.ReporteDTO;
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

    /**
     * Endpoint que realiza la creación de una aplicación en el sistema. Si la
     * operación es exitosa, se retorna el ID de la aplicación recién creada.
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/crear")
    public ResponseEntity<MasivasResponse<Object>> registrarAplicacion(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        Long idAplicacion = aplicacionService.crearAplicacion(request, userContext.getUsername());
        aplicacionService.logAuditoria(request, Evento.EV_REGISTRO_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_APLICACION,
                ConstantesServices.ACCION_CREATE, ConstantesServices.MENSAJE_EXITO_CREAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_CREAR_OPERACION, idAplicacion));
    }

    /**
     * Endpoing que realiza la busqueda de las aplicaciones en el sistema.
     * Recibe un objeto con filtros de búsqueda, y retorna una lista paginada de
     * aplicaciones que coincidan con dichos criterios.
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/buscar")
    public ResponseEntity<MasivasResponse<Object>> buscarAplicacion(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        CustomPaginate<DetalleConsultaAplicacionDTO> objPageable = aplicacionService.buscarAplicacion(request);
        aplicacionService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_APLICACION,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION, objPageable));
    }

    /**
     * Endpoint que realiza la edición de una aplicación en el sistema. Si la
     * operación es exitosa, se retorna el ID de la aplicación recién editada.
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/editar")
    public ResponseEntity<MasivasResponse<Object>> editarAplicacion(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        Long idAplicacion = aplicacionService.editarAplicacion(request, userContext.getUsername());
        aplicacionService.logAuditoria(request, Evento.EV_ACTUALIZACION_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_APLICACION,
                ConstantesServices.ACCION_UPDATE, ConstantesServices.MENSAJE_EXITO_EDITAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_EDITAR_OPERACION, idAplicacion));
    }

    /**
     * Endpoint que devuelve los detalles de una aplicación específica a partir
     * de su ID.
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/obtener")
    public ResponseEntity<MasivasResponse<Object>> obtenerAplicacion(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        DetalleRegistroAplicacionDTO aplicacionDTO = aplicacionService.obtenerAplicacion(request);
        aplicacionService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_APLICACION,
                ConstantesServices.ACCION_VIEW, ConstantesServices.MENSAJE_EXITO_OBTENER_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_OBTENER_OPERACION, aplicacionDTO));
    }

    /**
     * Endpoint que Activa una o varias aplicaciones a partir de su ID.
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/activar")
    public ResponseEntity<MasivasResponse<Object>> activar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        EstadoDTO estadoDTO = aplicacionService.cambiarEstadoAplicacion(request, userContext.getUsername(), ConstantesServices.ESTADO_ACTIVO);
        aplicacionService.logAuditoria(request, Evento.EV_ELIMINACION_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_APLICACION,
                ConstantesServices.ACCION_DELETE, estadoDTO.getMensaje());
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, estadoDTO.getMensaje(), estadoDTO.getNumExitos()));
    }

    /**
     * Endpoint que Inactiva una o varias aplicaciones a partir de su ID.
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/desactivar")
    public ResponseEntity<MasivasResponse<Object>> desactivar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        EstadoDTO estadoDTO = aplicacionService.cambiarEstadoAplicacion(request, userContext.getUsername(), ConstantesServices.ESTADO_INACTIVO);
        aplicacionService.logAuditoria(request, Evento.EV_ELIMINACION_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_APLICACION,
                ConstantesServices.ACCION_DELETE, estadoDTO.getMensaje());
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, estadoDTO.getMensaje(), estadoDTO.getNumExitos()));
    }

    /**
     * Endpoint que genera y descarga un reporte en formato PDF con la lista de
     * aplicaciones.
     *
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/descargarPDF")
    public ResponseEntity<MasivasResponse<Object>> descargarPDF(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        ReporteDTO reporteDTO = aplicacionService.descargarAplicacion(request, userContext.getUsername(), ConstantesServices.TIPO_ARCHIVO_PDF);
        aplicacionService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_APLICACION,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, reporteDTO));
    }

    /**
     * Endpoint que genera y descarga un reporte en formato XLSX con la lista de
     * aplicaciones.
     *
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/descargarXLSX")
    public ResponseEntity<MasivasResponse<Object>> descargarXLSX(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        ReporteDTO reporteDTO = aplicacionService.descargarAplicacion(request, userContext.getUsername(), ConstantesServices.TIPO_ARCHIVO_XLSX);
        aplicacionService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_APLICACION,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, reporteDTO));
    }

}
