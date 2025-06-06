package com.novatronic.masivas.backoffice.controller;

import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaGrupoParametroDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroGrupoParametroDTO;
import com.novatronic.masivas.backoffice.dto.EstadoDTO;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.service.GrupoParametroService;
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
@RequestMapping(value = "grupoParametro", produces = "application/json")
@RestController
public class GrupoParametroController {

    private GrupoParametroService grupoParametroService;

    public GrupoParametroController(GrupoParametroService grupoParametroService) {
        this.grupoParametroService = grupoParametroService;
    }

    @PostMapping("/crear")
    public ResponseEntity<MasivasResponse> registrar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        Long idEntidad = grupoParametroService.crearGrupoParametro(request, userContext.getUsername());
        //TODO Imprimir OK log auditoria
        return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_CREAR_OPERACION, idEntidad));
    }

    @PostMapping("/buscar")
    public ResponseEntity<MasivasResponse> buscar(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        CustomPaginate<DetalleConsultaGrupoParametroDTO> objPegeable = grupoParametroService.buscarGrupoParametro(request, userContext.getUsername());
        //TODO Imprimir OK log auditoria
        return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION, objPegeable));
    }

    @PostMapping("/editar")
    public ResponseEntity<MasivasResponse> editar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        Long idGrupoParametro = grupoParametroService.editarGrupoParametro(request, userContext.getUsername());
        //TODO Imprimir OK log auditoria
        return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_EDITAR_OPERACION, idGrupoParametro));
    }

    @PostMapping("/obtener")
    public ResponseEntity<MasivasResponse> obtener(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        DetalleRegistroGrupoParametroDTO grupoParametroDTO = grupoParametroService.obtenerGrupoParametro(request, userContext.getUsername());
        //TODO Imprimir OK log auditoria
        return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_OBTENER_OPERACION, grupoParametroDTO));
    }

    @PostMapping("/activar")
    public ResponseEntity<MasivasResponse> activar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        EstadoDTO estadoDTO = grupoParametroService.cambiarEstadoGrupoParametro(request, userContext.getUsername(), ConstantesServices.ESTADO_ACTIVO);
        //TODO Imprimir OK log auditoria
        return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, estadoDTO.getMensaje(), estadoDTO.getNumExitos()));
    }

    @PostMapping("/desactivar")
    public ResponseEntity<MasivasResponse> desactivar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        EstadoDTO estadoDTO = grupoParametroService.cambiarEstadoGrupoParametro(request, userContext.getUsername(), ConstantesServices.ESTADO_INACTIVO);
        //TODO Imprimir OK log auditoria
        return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, estadoDTO.getMensaje(), estadoDTO.getNumExitos()));
    }

}
