package com.novatronic.masivas.backoffice.controller;

import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaParametroDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroParametroDTO;
import com.novatronic.masivas.backoffice.dto.EstadoDTO;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.service.ParametroService;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import jakarta.validation.Valid;
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
@RequestMapping(value = "parametro", produces = "application/json")
@RestController
public class ParametroController {

    private ParametroService parametroService;

    public ParametroController(ParametroService parametroService) {
        this.parametroService = parametroService;
    }

    @PostMapping("/crear")
    public ResponseEntity<MasivasResponse> registrar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        Long idParametro = parametroService.crearParametro(request, userContext.getUsername());
        //TODO Imprimir OK log auditoria
        return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_CREAR_OPERACION, idParametro));
    }

    @PostMapping("/buscar")
    public ResponseEntity<MasivasResponse> buscar(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        CustomPaginate<DetalleConsultaParametroDTO> objPegeable = parametroService.buscarParametro(request, userContext.getUsername());
        //TODO Imprimir OK log auditoria
        return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION, objPegeable));
    }

    @PostMapping("/editar")
    public ResponseEntity<MasivasResponse> editar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        Long idParametro = parametroService.editarParametro(request, userContext.getUsername());
        //TODO Imprimir OK log auditoria
        return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_EDITAR_OPERACION, idParametro));
    }

    @PostMapping("/obtener")
    public ResponseEntity<MasivasResponse> obtener(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        DetalleRegistroParametroDTO parametroDTO = parametroService.obtenerParametro(request, userContext.getUsername());
        //TODO Imprimir OK log auditoria
        return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_OBTENER_OPERACION, parametroDTO));
    }

    @PostMapping("/activar")
    public ResponseEntity<MasivasResponse> activar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        EstadoDTO estadoDTO = parametroService.cambiarEstadoParametro(request, userContext.getUsername(), ConstantesServices.ESTADO_ACTIVO);
        //TODO Imprimir OK log auditoria
        return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, estadoDTO.getMensaje(), estadoDTO.getNumExitos()));
    }

    @PostMapping("/desactivar")
    public ResponseEntity<MasivasResponse> desactivar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        EstadoDTO estadoDTO = parametroService.cambiarEstadoParametro(request, userContext.getUsername(), ConstantesServices.ESTADO_INACTIVO);
        //TODO Imprimir OK log auditoria
        return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, estadoDTO.getMensaje(), estadoDTO.getNumExitos()));
    }

}
