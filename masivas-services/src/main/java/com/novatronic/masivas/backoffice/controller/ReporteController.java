package com.novatronic.masivas.backoffice.controller;

import com.novatronic.masivas.backoffice.dto.DetalleConsultaReporteCierreDTO;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaReporteTotalizadoDTO;
import com.novatronic.masivas.backoffice.dto.DetalleReporteConsolidadoDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.dto.ReporteDTO;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.service.ReporteService;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import com.novatronic.novalog.audit.util.Estado;
import com.novatronic.novalog.audit.util.Evento;
import jakarta.validation.Valid;
import java.util.List;
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
@RequestMapping(value = "reporte", produces = "application/json")
@RestController
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    /**
     * Endpoint que genera y retorna un reporte detallado del cierre diario.
     *
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/cierre/buscar")
    public ResponseEntity<MasivasResponse<Object>> reporteCierre(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        DetalleConsultaReporteCierreDTO reporte = reporteService.reporteCierre(request);
        reporteService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.REPORTE_CIERRE,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_GENERICO, ConstantesServices.RESPUESTA_OK_API);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_GENERICO, reporte));
    }

    /**
     * Endpoint que genera y retorna un reporte totalizado de las operaciones
     * masivas.
     *
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/totalizado")
    public ResponseEntity<MasivasResponse<Object>> reporteTotalizado(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        DetalleConsultaReporteTotalizadoDTO reporte = reporteService.reporteTotalizado(request);
        reporteService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.REPORTE_CIERRE,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_GENERICO, ConstantesServices.RESPUESTA_OK_API);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_GENERICO, reporte));
    }

    /**
     * Endpoint que genera y retorna un reporte consolidado agrupado por entidad
     *
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/consolidado")
    public ResponseEntity<MasivasResponse<Object>> reporteConsolidado(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        List<DetalleReporteConsolidadoDTO> reporte = reporteService.reporteConsolidadoPorEntidadDestino(request);
        reporteService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.REPORTE_CONSOLIDADO,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_GENERICO, ConstantesServices.RESPUESTA_OK_API);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_GENERICO, reporte));
    }

    /**
     * Endpoint que genera y descarga un reporte consolidado en formato PDF.
     *
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/consolidado/descargarPDF")
    public ResponseEntity<MasivasResponse<Object>> descargarConsolidadoPDF(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        ReporteDTO reporteDTO = reporteService.descargarConsolidado(request, userContext.getUsername(), ConstantesServices.TIPO_ARCHIVO_PDF);
        reporteService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.REPORTE_CONSOLIDADO,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, ConstantesServices.RESPUESTA_OK_API);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, reporteDTO));
    }

    /**
     * Endpoint que genera y descarga un reporte consolidado en formato XLSX.
     *
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/consolidado/descargarXLSX")
    public ResponseEntity<MasivasResponse<Object>> descargarConsolidadoXLSX(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        ReporteDTO reporteDTO = reporteService.descargarConsolidado(request, userContext.getUsername(), ConstantesServices.TIPO_ARCHIVO_XLSX);
        reporteService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.REPORTE_CONSOLIDADO,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, ConstantesServices.RESPUESTA_OK_API);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, reporteDTO));
    }
}
