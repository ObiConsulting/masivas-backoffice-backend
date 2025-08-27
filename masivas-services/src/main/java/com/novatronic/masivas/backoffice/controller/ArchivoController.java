package com.novatronic.masivas.backoffice.controller;

import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoDirectorioDTO;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoMasivasDTO;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoTitularidadDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroArchivoMasivasDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.dto.ReporteDTO;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.service.ArchivoService;
import com.novatronic.masivas.backoffice.service.DetalleMasivasService;
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
@RequestMapping(value = "archivo", produces = "application/json")
@RestController
public class ArchivoController {

    @Autowired
    private ArchivoService archivoService;
    @Autowired
    private DetalleMasivasService detalleMasivasService;

    /**
     * Endpoing que realiza la busqueda de los archivos de tipo directorio en el
     * sistema. Recibe un objeto con filtros de búsqueda, y retorna una lista
     * paginada de archivos de tipo directorio que coincidan con dichos
     * criterios.
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/directorio/buscar")
    public ResponseEntity<MasivasResponse<Object>> buscarArchivoDirectorio(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        CustomPaginate<DetalleConsultaArchivoDirectorioDTO> objPageable = archivoService.buscarArchivoDirectorio(request);
        archivoService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_ARCHIVO_DIRECTORIO,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION, objPageable));
    }

    /**
     * Endpoing que realiza la busqueda de los archivos de tipo masivas en el
     * sistema. Recibe un objeto con filtros de búsqueda, y retorna una lista
     * paginada de archivos de tipo masivas que coincidan con dichos criterios.
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/masivas/buscar")
    public ResponseEntity<MasivasResponse<Object>> buscarArchivoMasivas(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        CustomPaginate<DetalleConsultaArchivoMasivasDTO> objPageable = archivoService.buscarArchivoMasivas(request);
        archivoService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_ARCHIVO_MASIVAS,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION, objPageable));
    }

    /**
     * Endpoing que realiza la busqueda de los archivos de tipo titularidad en
     * el sistema. Recibe un objeto con filtros de búsqueda, y retorna una lista
     * paginada de archivos de tipo titularidad que coincidan con dichos
     * criterios.
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/titularidad/buscar")
    public ResponseEntity<MasivasResponse<Object>> buscarArchivoTitularidad(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        CustomPaginate<DetalleConsultaArchivoTitularidadDTO> objPageable = archivoService.buscarArchivoTitularidad(request);
        archivoService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_ARCHIVO_TITULARIDAD,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION, objPageable));
    }

    /**
     * Endpoing que realiza la busqueda del detalle de los archivos de tipo
     * masivas en el sistema. Recibe un objeto con filtros de búsqueda, y
     * retorna una lista paginada del detalle de los archivos de tipo masivas
     * que coincidan con dichos criterios.
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/masivas/detalle/buscar")
    public ResponseEntity<MasivasResponse<Object>> buscarDetalleMasivas(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        CustomPaginate<DetalleRegistroArchivoMasivasDTO> objPageable = detalleMasivasService.buscarDetalleMasivas(request);
        detalleMasivasService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_DETALLE_MASIVAS,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION, objPageable));
    }

    /**
     * Endpoint que genera y descarga un reporte en formato PDF con la lista de
     * archivos de tipo directorio.
     *
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/directorio/descargarPDF")
    public ResponseEntity<MasivasResponse<Object>> descargarArchivoDirectorioPDF(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        ReporteDTO reporteDTO = archivoService.descargarArchivoDirectorio(request, userContext.getUsername(), ConstantesServices.TIPO_ARCHIVO_PDF);
        archivoService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_ARCHIVO_DIRECTORIO,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, reporteDTO));
    }

    /**
     * Endpoint que genera y descarga un reporte en formato XLSX con la lista de
     * archivos de tipo directorio.
     *
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/directorio/descargarXLSX")
    public ResponseEntity<MasivasResponse<Object>> descargarArchivoDirectorioXLSX(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        ReporteDTO reporteDTO = archivoService.descargarArchivoDirectorio(request, userContext.getUsername(), ConstantesServices.TIPO_ARCHIVO_XLSX);
        archivoService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_ARCHIVO_DIRECTORIO,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, reporteDTO));
    }

    /**
     * Endpoint que genera y descarga un reporte en formato PDF con la lista de
     * archivos de tipo masivas.
     *
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/masivas/descargarPDF")
    public ResponseEntity<MasivasResponse<Object>> descargarArchivoMasivasPDF(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        ReporteDTO reporteDTO = archivoService.descargarArchivoMasivas(request, userContext.getUsername(), ConstantesServices.TIPO_ARCHIVO_PDF);
        archivoService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_ARCHIVO_MASIVAS,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, reporteDTO));
    }

    /**
     * Endpoint que genera y descarga un reporte en formato XLSX con la lista de
     * archivos de tipo masivas.
     *
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/masivas/descargarXLSX")
    public ResponseEntity<MasivasResponse<Object>> descargarArchivoMasivasXLSX(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        ReporteDTO reporteDTO = archivoService.descargarArchivoMasivas(request, userContext.getUsername(), ConstantesServices.TIPO_ARCHIVO_XLSX);
        archivoService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_ARCHIVO_MASIVAS,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, reporteDTO));
    }

    /**
     * Endpoint que genera y descarga un reporte en formato PDF con la lista de
     * archivos de tipo titularidad.
     *
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/titularidad/descargarPDF")
    public ResponseEntity<MasivasResponse<Object>> descargarArchivoTitularidadPDF(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        ReporteDTO reporteDTO = archivoService.descargarArchivoTitularidad(request, userContext.getUsername(), ConstantesServices.TIPO_ARCHIVO_PDF);
        archivoService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_ARCHIVO_TITULARIDAD,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, reporteDTO));
    }

    /**
     * Endpoint que genera y descarga un reporte en formato XLSX con la lista de
     * archivos de tipo titularidad.
     *
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/titularidad/descargarXLSX")
    public ResponseEntity<MasivasResponse<Object>> descargarArchivoTitularidadXLSX(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        ReporteDTO reporteDTO = archivoService.descargarArchivoTitularidad(request, userContext.getUsername(), ConstantesServices.TIPO_ARCHIVO_XLSX);
        archivoService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_ARCHIVO_TITULARIDAD,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, reporteDTO));
    }

    /**
     * Endpoint que genera y descarga un reporte en formato PDF con la lista de
     * detalles de archivos de tipo masivas.
     *
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/masivas/detalle/descargarPDF")
    public ResponseEntity<MasivasResponse<Object>> descargarDetalleArchivoMasivasPDF(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        ReporteDTO reporteDTO = detalleMasivasService.descargarDetalleArchivoMasivas(request, userContext.getUsername(), ConstantesServices.TIPO_ARCHIVO_PDF);
        detalleMasivasService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_ARCHIVO_MASIVAS,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, reporteDTO));
    }

    /**
     * Endpoint que genera y descarga un reporte en formato XLSX con la lista de
     * detalles de archivos de tipo masivas.
     *
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/masivas/detalle/descargarXLSX")
    public ResponseEntity<MasivasResponse<Object>> descargarDetalleMasivasXLSX(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        ReporteDTO reporteDTO = detalleMasivasService.descargarDetalleArchivoMasivas(request, userContext.getUsername(), ConstantesServices.TIPO_ARCHIVO_XLSX);
        detalleMasivasService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_ARCHIVO_MASIVAS,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, reporteDTO));
    }

    /**
     * Endpoint que respalda o restaura un archivo de tipo directorio.
     *
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/directorio/ejecutar")
    public ResponseEntity<MasivasResponse<Object>> gestionarOperacionDirectorio(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        Long respuesta = archivoService.gestionarOperacionDirectorio(request);
        archivoService.logAuditoria(request, Evento.EV_ACTUALIZACION_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_ARCHIVO_DIRECTORIO,
                ConstantesServices.ACCION_BACKUP_RESTORE, ConstantesServices.MENSAJE_EXITO_GENERICO);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_GENERICO, respuesta));
    }

    /**
     * Endpoint que respalda o restaura un archivo de tipo masivas.
     *
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/masivas/ejecutar")
    public ResponseEntity<MasivasResponse<Object>> gestionarOperacionMasivas(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        Long respuesta = archivoService.gestionarOperacionMasivas(request);
        archivoService.logAuditoria(request, Evento.EV_ACTUALIZACION_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_ARCHIVO_MASIVAS,
                ConstantesServices.ACCION_BACKUP_RESTORE, ConstantesServices.MENSAJE_EXITO_GENERICO);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_GENERICO, respuesta));
    }

    /**
     * Endpoint que respalda o restaura un archivo de tipo titularidad.
     *
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/titularidad/ejecutar")
    public ResponseEntity<MasivasResponse<Object>> gestionarOperacionTitularidad(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        Long respuesta = archivoService.gestionarOperacionTitularidad(request);
        archivoService.logAuditoria(request, Evento.EV_ACTUALIZACION_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_ARCHIVO_TITULARIDAD,
                ConstantesServices.ACCION_BACKUP_RESTORE, ConstantesServices.MENSAJE_EXITO_GENERICO);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_GENERICO, respuesta));
    }

}
