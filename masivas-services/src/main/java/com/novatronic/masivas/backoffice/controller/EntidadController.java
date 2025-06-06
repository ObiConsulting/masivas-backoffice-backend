package com.novatronic.masivas.backoffice.controller;

import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaEntidadDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroEntidadDTO;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.service.EntidadService;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Obi Consulting
 */
@RequestMapping(value = "entidad", produces = "application/json")
@RestController
public class EntidadController {

    @Autowired
    private EntidadService entidadService;

    @PostMapping("/crear")
    public ResponseEntity<MasivasResponse> registrarEntidad(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        try {
            Long idEntidad = entidadService.crearEntidad(request, userContext.getUsername());
            //TODO Imprimir OK log auditoria
            return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_CREAR_OPERACION, idEntidad));
        } catch (DataIntegrityViolationException e) {
            //TODO Imprimir error log auditoria
            MasivasResponse res = new MasivasResponse(ConstantesServices.CODIGO_ERROR_COD_ENTIDAD_UNICO, ConstantesServices.MENSAJE_ERROR_COD_ENTIDAD_UNICO, null);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (CannotCreateTransactionException | DataAccessException e) {
            //TODO Imprimir error log auditoria
            MasivasResponse res = new MasivasResponse(ConstantesServices.CODIGO_ERROR_BD, ConstantesServices.MENSAJE_ERROR_BD, null);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e) {
            //TODO Imprimir error log auditoria
            MasivasResponse res = new MasivasResponse(ConstantesServices.CODIGO_ERROR_GENERICO, ConstantesServices.MENSAJE_ERROR_GENERICO, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @PostMapping("/buscar")
    public ResponseEntity<MasivasResponse> buscarEntidad(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {

        try {
            CustomPaginate<DetalleConsultaEntidadDTO> objPegeable = entidadService.buscar(request, userContext.getUsername());
            //TODO Imprimir OK log auditoria
            return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION, objPegeable));

        } catch (CannotCreateTransactionException | DataAccessException e) {
            //TODO Imprimir error log auditoria
            MasivasResponse res = new MasivasResponse(ConstantesServices.CODIGO_ERROR_BD, ConstantesServices.MENSAJE_ERROR_BD, null);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e) {
            //TODO Imprimir error log auditoria
            MasivasResponse res = new MasivasResponse(ConstantesServices.CODIGO_ERROR_GENERICO, ConstantesServices.MENSAJE_ERROR_GENERICO, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }

    }

    @PostMapping("/editar")
    public ResponseEntity<MasivasResponse> editarEntidad(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        try {
            Long idEntidad = entidadService.editarEntidad(request, userContext.getUsername());
            //TODO Imprimir OK log auditoria
            return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_EDITAR_OPERACION, idEntidad));
        } catch (DataIntegrityViolationException e) {
            //TODO Imprimir error log auditoria
            MasivasResponse res = new MasivasResponse(ConstantesServices.CODIGO_ERROR_COD_ENTIDAD_UNICO, ConstantesServices.MENSAJE_ERROR_COD_ENTIDAD_UNICO, null);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (CannotCreateTransactionException | DataAccessException e) {
            //TODO Imprimir error log auditoria
            MasivasResponse res = new MasivasResponse(ConstantesServices.CODIGO_ERROR_BD, ConstantesServices.MENSAJE_ERROR_BD, null);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e) {
            //TODO Imprimir error log auditoria
            MasivasResponse res = new MasivasResponse(ConstantesServices.CODIGO_ERROR_GENERICO, ConstantesServices.MENSAJE_ERROR_GENERICO, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @PostMapping("/obtener")
    public ResponseEntity<MasivasResponse> obtenerEntidad(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        try {
            DetalleRegistroEntidadDTO entidadDTO = entidadService.obtenerEntidad(request, userContext.getUsername());
            //TODO Imprimir OK log auditoria
            return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_OBTENER_OPERACION, entidadDTO));
        } catch (CannotCreateTransactionException | DataAccessException e) {
            //TODO Imprimir error log auditoria
            MasivasResponse res = new MasivasResponse(ConstantesServices.CODIGO_ERROR_BD, ConstantesServices.MENSAJE_ERROR_BD, null);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e) {
            //TODO Imprimir error log auditoria
            MasivasResponse res = new MasivasResponse(ConstantesServices.CODIGO_ERROR_GENERICO, ConstantesServices.MENSAJE_ERROR_GENERICO, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @PostMapping("/eliminar")
    public ResponseEntity<MasivasResponse> eliminarEntidad(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        try {
            Long idEntidad = entidadService.eliminarEntidad(request, userContext.getUsername());
            //TODO Imprimir OK log auditoria
            return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_EDITAR_OPERACION, idEntidad));
        } catch (CannotCreateTransactionException | DataAccessException e) {
            //TODO Imprimir error log auditoria
            MasivasResponse res = new MasivasResponse(ConstantesServices.CODIGO_ERROR_BD, ConstantesServices.MENSAJE_ERROR_BD, null);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e) {
            //TODO Imprimir error log auditoria
            MasivasResponse res = new MasivasResponse(ConstantesServices.CODIGO_ERROR_GENERICO, ConstantesServices.MENSAJE_ERROR_GENERICO, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

}
