package com.novatronic.masivas.backoffice.controller;

import com.hazelcast.core.HazelcastInstance;
import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.security.model.AuthRequest;
import com.novatronic.masivas.backoffice.security.model.ChangePassRequest;
import com.novatronic.masivas.backoffice.security.model.LoginResponse;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.service.SeguridadService;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import com.novatronic.novalog.audit.util.Estado;
import com.novatronic.novalog.audit.util.Evento;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Obi Consulting
 */
@RequestMapping(value = "seguridad", produces = "application/json")
@RestController
public class SeguridadController {

    @Autowired
    SeguridadService seguridadService;

    @Autowired
    HazelcastInstance hazelcastInstance;

    @RequestMapping(value = "/acceso", method = RequestMethod.POST)
    public ResponseEntity<MasivasResponse<LoginResponse>> acceso(@RequestBody @Valid AuthRequest authRequest) throws Exception {

        MasivasResponse<LoginResponse> respuesta;
        try {

            respuesta = seguridadService.authenticate(authRequest.getUsername(), authRequest.getPassword(), authRequest.getCaptchaId(), authRequest.getUserInput());

            seguridadService.logEvento(respuesta.getMensaje());
//            seguridadService.logAuditoria(authRequest, Evento.EV_LOGIN_USUARIO, Estado.ESTADO_EXITO, userContext, ConstantesServices.SCA, ConstantesServices.ACCION_LOGIN, respuesta.getMensaje());
            return ResponseEntity.status(HttpStatus.OK).body(respuesta);

        } catch (Exception e) {

            seguridadService.logError(ConstantesServices.MENSAJE_ERROR_GENERICO, e);
//            seguridadService.logAuditoria(authRequest, Evento.EV_LOGIN_USUARIO, Estado.ESTADO_FRACASO, userContext, ConstantesServices.SCA, ConstantesServices.ACCION_LOGIN, ConstantesServices.MENSAJE_ERROR_GENERICO);

            respuesta = new MasivasResponse<>(ConstantesServices.CODIGO_ERROR_GENERICO, ConstantesServices.MENSAJE_ERROR_GENERICO, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @RequestMapping(value = "/cambiarClavePrimeraVez", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('PRIMER_LOGIN')")
    public ResponseEntity<MasivasResponse<LoginResponse>> cambiarClavePrimeraVez(@RequestBody @Valid ChangePassRequest authRequest) throws Exception {

        MasivasResponse<LoginResponse> respuesta;
        try {

            respuesta = seguridadService.changePassFirstTime(authRequest.getUsername(), authRequest.getPassword(), authRequest.getNewpassword());

            seguridadService.logEvento(respuesta.getMensaje());
//            seguridadService.logAuditoria(authRequest, Evento.EV_ACTUALIZACION_CONFIG_SISTEMA, Estado.ESTADO_EXITO, authRequest.getUsername(), ConstantesServices.INTEGRACION_SCA, ConstantesServices.ACCION_CHANGE_PASSWORD, respuesta.getMensaje());

            return ResponseEntity.status(HttpStatus.OK).body(respuesta);

        } catch (Exception e) {

            seguridadService.logError(ConstantesServices.MENSAJE_ERROR_GENERICO, e);
//            seguridadService.logAuditoria(authRequest, Evento.EV_ACTUALIZACION_CONFIG_SISTEMA, Estado.ESTADO_FRACASO, user, ConstantesServices.INTEGRACION_SCA, ConstantesServices.ACCION_CHANGE_PASSWORD, ConstantesServices.MENSAJE_ERROR_GENERICO);

            respuesta = new MasivasResponse<>(ConstantesServices.CODIGO_ERROR_GENERICO, ConstantesServices.MENSAJE_ERROR_GENERICO, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @GetMapping("/permisos")
    public ResponseEntity<MasivasResponse<CustomPaginate<String>>> permisos(@AuthenticationPrincipal UserContext userContext) throws Exception {
        try {
            List<String> valores = userContext.getAuthoritiesList();

            CustomPaginate customPaginate = new CustomPaginate<>(1, valores.size(), valores);
            MasivasResponse<CustomPaginate<String>> respuesta = new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, "Operación correcta", customPaginate);

            seguridadService.logEvento(respuesta.getMensaje());
            seguridadService.logAuditoria(userContext, Evento.EV_ACTUALIZACION_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.INTEGRACION_SCA, ConstantesServices.ACCION_PERMISSION, respuesta.getMensaje());

            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {

            seguridadService.logError(ConstantesServices.MENSAJE_ERROR_GENERICO, e);
            seguridadService.logAuditoria(userContext, Evento.EV_ACTUALIZACION_CONFIG_SISTEMA, Estado.ESTADO_FRACASO, userContext, ConstantesServices.INTEGRACION_SCA, ConstantesServices.ACCION_PERMISSION, ConstantesServices.MENSAJE_ERROR_GENERICO);

            MasivasResponse<CustomPaginate<String>> res = new MasivasResponse<>(ConstantesServices.CODIGO_ERROR_GENERICO, ConstantesServices.MENSAJE_ERROR_GENERICO, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @GetMapping("/cerrarsesion")
    public ResponseEntity<MasivasResponse<Void>> cerrarSesion(@AuthenticationPrincipal UserContext userContext) throws Exception {

        MasivasResponse<Void> respuesta;
        try {

            seguridadService.logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD_ACCION, ConstantesServices.LOGOUT, "{" + "usuario=" + userContext.getUsername() + '}');

            hazelcastInstance.getMap(ConstantesServices.MAP_LOGUEADOS).remove(userContext.getUsername());
            SecurityContextHolder.clearContext();
            respuesta = new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_CERRAR_SESION, null);

            seguridadService.logEvento(respuesta.getMensaje());
            seguridadService.logAuditoria(userContext, Evento.EV_LOGOFF_USUARIO, Estado.ESTADO_EXITO, userContext, ConstantesServices.INTEGRACION_SCA, ConstantesServices.ACCION_LOGOUT, respuesta.getMensaje());

            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {

            seguridadService.logError(ConstantesServices.MENSAJE_ERROR_GENERICO, e);
            seguridadService.logAuditoria(userContext, Evento.EV_LOGOFF_USUARIO, Estado.ESTADO_FRACASO, userContext, ConstantesServices.INTEGRACION_SCA, ConstantesServices.ACCION_LOGOUT, ConstantesServices.MENSAJE_ERROR_GENERICO);

            respuesta = new MasivasResponse<>(ConstantesServices.CODIGO_ERROR_GENERICO, ConstantesServices.MENSAJE_ERROR_GENERICO, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @RequestMapping(value = "/cambiarClave", method = RequestMethod.POST)
    public ResponseEntity<MasivasResponse<LoginResponse>> cambiarClave(@RequestBody @Valid ChangePassRequest authRequest, @AuthenticationPrincipal UserContext userContext) throws Exception {

        MasivasResponse<LoginResponse> respuesta;
        try {

            respuesta = seguridadService.changePassFirstTime(authRequest.getUsername(), authRequest.getPassword(), authRequest.getNewpassword());

            seguridadService.logEvento(respuesta.getMensaje());
            seguridadService.logAuditoria(authRequest, Evento.EV_ACTUALIZACION_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.INTEGRACION_SCA, ConstantesServices.ACCION_CHANGE_PASSWORD, respuesta.getMensaje());

            return ResponseEntity.status(HttpStatus.OK).body(respuesta);

        } catch (Exception e) {

            seguridadService.logError(ConstantesServices.MENSAJE_ERROR_GENERICO, e);
            seguridadService.logAuditoria(authRequest, Evento.EV_ACTUALIZACION_CONFIG_SISTEMA, Estado.ESTADO_FRACASO, userContext, ConstantesServices.INTEGRACION_SCA, ConstantesServices.ACCION_CHANGE_PASSWORD, ConstantesServices.MENSAJE_ERROR_GENERICO);

            respuesta = new MasivasResponse<>(ConstantesServices.CODIGO_ERROR_GENERICO, ConstantesServices.MENSAJE_ERROR_GENERICO, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }
}
