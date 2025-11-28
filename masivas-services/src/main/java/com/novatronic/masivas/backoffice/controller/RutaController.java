package com.novatronic.masivas.backoffice.controller;

import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaRutaDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroRutaDTO;
import com.novatronic.masivas.backoffice.dto.ReporteDTO;
import com.novatronic.masivas.backoffice.log.Performance;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.service.RutaService;
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
@RequestMapping(value = "ruta", produces = "application/json")
@RestController
public class RutaController {

    @Autowired
    private RutaService rutaService;

    /**
     * Endpoing que realiza la busqueda de las rutas en el sistema. Recibe un
     * objeto con filtros de búsqueda, y retorna una lista paginada de rutas que
     * coincidan con dichos criterios.
     *
     * @param request
     * @param userContext
     * @return
     */
    @Performance
    @PostMapping("/buscar")
    public ResponseEntity<MasivasResponse<Object>> buscarRuta(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        CustomPaginate<DetalleConsultaRutaDTO> objPageable = rutaService.buscarRuta(request);
        rutaService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.ACCION_VIEW,
                ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION, ConstantesServices.RESPUESTA_OK_API);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION, objPageable));
    }

    /**
     * Endpoint que realiza la edición de una ruta en el sistema. Si la
     * operación es exitosa, se retorna el ID de la ruta recién editada.
     *
     * @param request
     * @param userContext
     * @return
     */
    @Performance
    @PostMapping("/editar")
    public ResponseEntity<MasivasResponse<Object>> editarRuta(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        Long idRuta = rutaService.editarRuta(request, userContext.getUsername());
        rutaService.logAuditoria(request, Evento.EV_ACTUALIZA_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.ACCION_UPDATE,
                ConstantesServices.MENSAJE_EXITO_EDITAR_RUTA, ConstantesServices.RESPUESTA_OK_API);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_EDITAR_RUTA, idRuta));
    }

    /**
     * Endpoint que devuelve los detalles de una ruta específica a partir de su
     * ID.
     *
     * @param request
     * @param userContext
     * @return
     */
    @Performance
    @PostMapping("/obtener")
    public ResponseEntity<MasivasResponse<Object>> obtenerRuta(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        DetalleRegistroRutaDTO entidadDTO = rutaService.obtenerRuta(request);
        rutaService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.ACCION_READ,
                ConstantesServices.MENSAJE_EXITO_OBTENER_OPERACION, ConstantesServices.RESPUESTA_OK_API);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_OBTENER_OPERACION, entidadDTO));
    }

    /**
     * Endpoint que genera y descarga un reporte en formato PDF con la lista de
     * rutas.
     *
     *
     * @param request
     * @param userContext
     * @return
     */
    @Performance
    @PostMapping("/descargarPDF")
    public ResponseEntity<MasivasResponse<Object>> descargarPDF(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        ReporteDTO reporteDTO = rutaService.descargarRutas(request, userContext.getUsername(), ConstantesServices.TIPO_ARCHIVO_PDF);
        rutaService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.ACCION_EXPORT,
                ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, ConstantesServices.RESPUESTA_OK_API);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, reporteDTO));
    }

    /**
     * Endpoint que genera y descarga un reporte en formato XLSX con la lista de
     * rutas.
     *
     *
     * @param request
     * @param userContext
     * @return
     */
    @Performance
    @PostMapping("/descargarXLSX")
    public ResponseEntity<MasivasResponse<Object>> descargarXLSX(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        ReporteDTO reporteDTO = rutaService.descargarRutas(request, userContext.getUsername(), ConstantesServices.TIPO_ARCHIVO_XLSX);
        rutaService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.ACCION_EXPORT,
                ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, ConstantesServices.RESPUESTA_OK_API);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, reporteDTO));
    }

}
