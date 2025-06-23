package com.novatronic.masivas.backoffice.controller;

import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoDirectorioDTO;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoMasivasDTO;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoTitularidadDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.service.ArchivoService;
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

    @PostMapping("/directorio/buscar")
    public ResponseEntity<MasivasResponse> buscarArchivoDirectorio(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        CustomPaginate<DetalleConsultaArchivoDirectorioDTO> objPegeable = archivoService.buscarArchivoDirectorio(request, userContext.getUsername());
        archivoService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_ARCHIVO_DIRECTORIO,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION, objPegeable));
    }

    @PostMapping("/masivas/buscar")
    public ResponseEntity<MasivasResponse> buscarArchivoMasivas(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        CustomPaginate<DetalleConsultaArchivoMasivasDTO> objPegeable = archivoService.buscarArchivoMasivas(request, userContext.getUsername());
        archivoService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_ARCHIVO_MASIVAS,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION, objPegeable));
    }

    @PostMapping("/titularidad/buscar")
    public ResponseEntity<MasivasResponse> buscarArchivoTitularidad(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        CustomPaginate<DetalleConsultaArchivoTitularidadDTO> objPegeable = archivoService.buscarArchivoTitularidad(request, userContext.getUsername());
        archivoService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_ARCHIVO_TITULARIDAD,
                ConstantesServices.ACCION_READ, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION);
        return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION, objPegeable));
    }

}
