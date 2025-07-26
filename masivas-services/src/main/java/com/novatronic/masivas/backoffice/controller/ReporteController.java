package com.novatronic.masivas.backoffice.controller;

import com.novatronic.masivas.backoffice.dto.DetalleConsultaReporteCierreDTO;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaReporteTotalizadoDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.service.ReporteService;
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
@RequestMapping(value = "reporte", produces = "application/json")
@RestController
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @PostMapping("/cierre/buscar")
    public ResponseEntity<MasivasResponse<Object>> reporteCierre(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        DetalleConsultaReporteCierreDTO objPegeable = reporteService.reporteCierre(request, userContext.getUsername());
        reporteService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.REPORTE_CIERRE,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_GENERICO);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_GENERICO, objPegeable));
    }

    @PostMapping("/totalizado")
    public ResponseEntity<MasivasResponse<Object>> reporteTotalizado(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        DetalleConsultaReporteTotalizadoDTO objPegeable = reporteService.reporteTotalizado(request, userContext.getUsername());
        reporteService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.REPORTE_TOTALIZADO,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_GENERICO);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_GENERICO, objPegeable));
    }

}
