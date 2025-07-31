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

    @PostMapping("/directorio/buscar")
    public ResponseEntity<MasivasResponse<Object>> buscarArchivoDirectorio(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        CustomPaginate<DetalleConsultaArchivoDirectorioDTO> objPageable = archivoService.buscarArchivoDirectorio(request);
        archivoService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_ARCHIVO_DIRECTORIO,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION, objPageable));
    }

    @PostMapping("/masivas/buscar")
    public ResponseEntity<MasivasResponse<Object>> buscarArchivoMasivas(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        CustomPaginate<DetalleConsultaArchivoMasivasDTO> objPageable = archivoService.buscarArchivoMasivas(request);
        archivoService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_ARCHIVO_MASIVAS,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION, objPageable));
    }

    @PostMapping("/titularidad/buscar")
    public ResponseEntity<MasivasResponse<Object>> buscarArchivoTitularidad(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        CustomPaginate<DetalleConsultaArchivoTitularidadDTO> objPageable = archivoService.buscarArchivoTitularidad(request);
        archivoService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_ARCHIVO_TITULARIDAD,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION, objPageable));
    }

    @PostMapping("/masivas/detalle/buscar")
    public ResponseEntity<MasivasResponse<Object>> buscarDetalleMasivas(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        CustomPaginate<DetalleRegistroArchivoMasivasDTO> objPageable = detalleMasivasService.buscarDetalleMasivas(request);
        archivoService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_DETALLE_MASIVAS,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION, objPageable));
    }

    @PostMapping("/directorio/descargarPDF")
    public ResponseEntity<MasivasResponse<Object>> descargarArchivoDirectorioPDF(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        ReporteDTO reporteDTO = archivoService.descargarArchivoDirectorio(request, userContext.getUsername(), ConstantesServices.TIPO_ARCHIVO_PDF);
        archivoService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_ARCHIVO_DIRECTORIO,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, reporteDTO));
    }

    @PostMapping("/directorio/descargarXLSX")
    public ResponseEntity<MasivasResponse<Object>> descargarArchivoDirectorioXLSX(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        ReporteDTO reporteDTO = archivoService.descargarArchivoDirectorio(request, userContext.getUsername(), ConstantesServices.TIPO_ARCHIVO_XLSX);
        archivoService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_ARCHIVO_DIRECTORIO,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, reporteDTO));
    }

    @PostMapping("/masivas/descargarPDF")
    public ResponseEntity<MasivasResponse<Object>> descargarArchivoMasivasPDF(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        ReporteDTO reporteDTO = archivoService.descargarArchivoMasivas(request, userContext.getUsername(), ConstantesServices.TIPO_ARCHIVO_PDF);
        archivoService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_ARCHIVO_MASIVAS,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, reporteDTO));
    }

    @PostMapping("/masivas/descargarXLSX")
    public ResponseEntity<MasivasResponse<Object>> descargarArchivoMasivasXLSX(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        ReporteDTO reporteDTO = archivoService.descargarArchivoMasivas(request, userContext.getUsername(), ConstantesServices.TIPO_ARCHIVO_XLSX);
        archivoService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_ARCHIVO_MASIVAS,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, reporteDTO));
    }

    @PostMapping("/titularidad/descargarPDF")
    public ResponseEntity<MasivasResponse<Object>> descargarArchivoTitularidadPDF(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        ReporteDTO reporteDTO = archivoService.descargarArchivoTitularidad(request, userContext.getUsername(), ConstantesServices.TIPO_ARCHIVO_PDF);
        archivoService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_ARCHIVO_TITULARIDAD,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, reporteDTO));
    }

    @PostMapping("/titularidad/descargarXLSX")
    public ResponseEntity<MasivasResponse<Object>> descargarArchivoTitularidadXLSX(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        ReporteDTO reporteDTO = archivoService.descargarArchivoTitularidad(request, userContext.getUsername(), ConstantesServices.TIPO_ARCHIVO_XLSX);
        archivoService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_ARCHIVO_TITULARIDAD,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, reporteDTO));
    }

    @PostMapping("/masivas/detalle/descargarPDF")
    public ResponseEntity<MasivasResponse<Object>> descargarDetalleArchivoMasivasPDF(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        ReporteDTO reporteDTO = detalleMasivasService.descargarDetalleArchivoMasivas(request, userContext.getUsername(), ConstantesServices.TIPO_ARCHIVO_PDF);
        detalleMasivasService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_ARCHIVO_MASIVAS,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, reporteDTO));
    }

    @PostMapping("/masivas/detalle/descargarXLSX")
    public ResponseEntity<MasivasResponse<Object>> descargarDetalleMasivasXLSX(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        ReporteDTO reporteDTO = detalleMasivasService.descargarDetalleArchivoMasivas(request, userContext.getUsername(), ConstantesServices.TIPO_ARCHIVO_XLSX);
        detalleMasivasService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_ARCHIVO_MASIVAS,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, reporteDTO));
    }
}
