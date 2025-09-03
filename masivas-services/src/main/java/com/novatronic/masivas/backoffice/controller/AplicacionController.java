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
    @Audit(accion = Evento.EV_REGISTRO_CONFIG_SISTEMA, origen = ConstantesServices.ACCION_CREATE, recursosAfectados = ConstantesServices.TABLA_APLICACION)
    public ResponseEntity<MasivasResponse<Object>> registrarAplicacion(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        Long idAplicacion = aplicacionService.crearAplicacion(request, userContext.getUsername());
        aplicacionService.logAuditoria(request, userContext, ConstantesServices.MENSAJE_EXITO_CREAR_APLICACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_CREAR_APLICACION, idAplicacion));
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
    @Audit(accion = Evento.EV_CONSULTA_REPORTE, origen = ConstantesServices.ACCION_VIEW, recursosAfectados = ConstantesServices.TABLA_APLICACION)
    public ResponseEntity<MasivasResponse<Object>> buscarAplicacion(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        CustomPaginate<DetalleConsultaAplicacionDTO> objPageable = aplicacionService.buscarAplicacion(request);
        aplicacionService.logAuditoria(request, userContext, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION);
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
    @Audit(accion = Evento.EV_ACTUALIZA_CONFIG_SISTEMA, origen = ConstantesServices.ACCION_UPDATE, recursosAfectados = ConstantesServices.TABLA_APLICACION)
    public ResponseEntity<MasivasResponse<Object>> editarAplicacion(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        Long idAplicacion = aplicacionService.editarAplicacion(request, userContext.getUsername());
        aplicacionService.logAuditoria(request, userContext, ConstantesServices.MENSAJE_EXITO_EDITAR_APLICACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_EDITAR_APLICACION, idAplicacion));
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
    @Audit(accion = Evento.EV_CONSULTA_REPORTE, origen = ConstantesServices.ACCION_READ, recursosAfectados = ConstantesServices.TABLA_APLICACION)
    public ResponseEntity<MasivasResponse<Object>> obtenerAplicacion(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        DetalleRegistroAplicacionDTO aplicacionDTO = aplicacionService.obtenerAplicacion(request);
        aplicacionService.logAuditoria(request, userContext, ConstantesServices.MENSAJE_EXITO_OBTENER_OPERACION);
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
    @Audit(accion = Evento.EV_ELIMINA_CONFIG_SISTEMA, origen = ConstantesServices.ACCION_UPDATE, recursosAfectados = ConstantesServices.TABLA_APLICACION)
    public ResponseEntity<MasivasResponse<Object>> activar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        EstadoDTO estadoDTO = aplicacionService.cambiarEstadoAplicacion(request, userContext.getUsername(), ConstantesServices.ESTADO_ACTIVO);
        aplicacionService.logAuditoria(request, userContext, estadoDTO.getMensaje());
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
    @Audit(accion = Evento.EV_ELIMINA_CONFIG_SISTEMA, origen = ConstantesServices.ACCION_UPDATE, recursosAfectados = ConstantesServices.TABLA_APLICACION)
    public ResponseEntity<MasivasResponse<Object>> desactivar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        EstadoDTO estadoDTO = aplicacionService.cambiarEstadoAplicacion(request, userContext.getUsername(), ConstantesServices.ESTADO_INACTIVO);
        aplicacionService.logAuditoria(request, userContext, estadoDTO.getMensaje());
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
        aplicacionService.logAuditoria(request, userContext, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION);
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
        aplicacionService.logAuditoria(request, userContext, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, reporteDTO));
    }

}
