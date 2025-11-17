package com.novatronic.masivas.backoffice.controller;

import com.hazelcast.core.HazelcastInstance;
import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.log.LogSecurity;
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

    @PostMapping("/acceso")
    public ResponseEntity<MasivasResponse<LoginResponse>> acceso(@RequestBody @Valid AuthRequest authRequest) {

        MasivasResponse<LoginResponse> respuesta;
        try {

            respuesta = seguridadService.authenticate(authRequest.getUsername(), authRequest.getPassword(), authRequest.getCaptchaId(), authRequest.getUserInput());

            seguridadService.logEvento(respuesta.getMensaje());
            LogSecurity.log("Inicio de sesión",authRequest.getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(respuesta);

        } catch (Exception e) {

            seguridadService.logError(ConstantesServices.MENSAJE_ERROR_GENERICO, e);
            LogSecurity.alert(authRequest.getUsername(),"Inicio de sesión",null);
            respuesta = new MasivasResponse<>(ConstantesServices.CODIGO_ERROR_GENERICO, ConstantesServices.MENSAJE_ERROR_GENERICO, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @PostMapping("/cambiarClavePrimeraVez")
    @PreAuthorize("hasAuthority('PRIMER_LOGIN')")
    public ResponseEntity<MasivasResponse<LoginResponse>> cambiarClavePrimeraVez(@RequestBody @Valid ChangePassRequest authRequest) {

        MasivasResponse<LoginResponse> respuesta;
        try {

            respuesta = seguridadService.changePassword(authRequest.getUsername(), authRequest.getPassword(), authRequest.getNewpassword());
            LogSecurity.log("Cambio de contraseña por primer login",authRequest.getUsername());
            seguridadService.logEvento(respuesta.getMensaje());

            return ResponseEntity.status(HttpStatus.OK).body(respuesta);

        } catch (Exception e) {

            seguridadService.logError(ConstantesServices.MENSAJE_ERROR_GENERICO, e);
            LogSecurity.alert(authRequest.getUsername(),"Cambio de contraseña por primer login",null);
            respuesta = new MasivasResponse<>(ConstantesServices.CODIGO_ERROR_GENERICO, ConstantesServices.MENSAJE_ERROR_GENERICO, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @GetMapping("/permisos")
    public ResponseEntity<MasivasResponse<CustomPaginate<String>>> permisos(@AuthenticationPrincipal UserContext userContext) {
        try {
            List<String> valores = userContext.getAuthoritiesList();

            CustomPaginate customPaginate = new CustomPaginate<>(1, valores.size(), valores);
            MasivasResponse<CustomPaginate<String>> respuesta = new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, "Operación correcta", customPaginate);

            seguridadService.logEvento(respuesta.getMensaje());
            seguridadService.logAuditoria(null, Evento.EV_ACTUALIZA_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.INTEGRACION_SCA, ConstantesServices.ACCION_PERMISSION,
                    respuesta.getMensaje(), ConstantesServices.RESPUESTA_OK_API);

            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {

            seguridadService.logError(ConstantesServices.MENSAJE_ERROR_GENERICO, e);
            seguridadService.logAuditoria(null, Evento.EV_ACTUALIZA_CONFIG_SISTEMA, Estado.ESTADO_FRACASO, userContext, ConstantesServices.INTEGRACION_SCA, ConstantesServices.ACCION_PERMISSION,
                    ConstantesServices.MENSAJE_ERROR_GENERICO, ConstantesServices.CODIGO_ERROR_GENERICO);

            MasivasResponse<CustomPaginate<String>> res = new MasivasResponse<>(ConstantesServices.CODIGO_ERROR_GENERICO, ConstantesServices.MENSAJE_ERROR_GENERICO, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @GetMapping("/cerrarsesion")
    public ResponseEntity<MasivasResponse<Void>> cerrarSesion(@AuthenticationPrincipal UserContext userContext) {

        MasivasResponse<Void> respuesta;
        try {

            seguridadService.logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD_ACCION, ConstantesServices.LOGOUT, "{" + "usuario=" + userContext.getUsername() + '}');

            hazelcastInstance.getMap(ConstantesServices.MAP_LOGUEADOS).remove(userContext.getUsername());
            SecurityContextHolder.clearContext();
            respuesta = new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_CERRAR_SESION, null);

            seguridadService.logEvento(respuesta.getMensaje());
            seguridadService.logAuditoria(userContext, Evento.EV_LOGOFF_USUARIO, Estado.ESTADO_EXITO, userContext, ConstantesServices.INTEGRACION_SCA, ConstantesServices.ACCION_LOGOUT,
                    respuesta.getMensaje(), ConstantesServices.RESPUESTA_OK_API);

            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {

            seguridadService.logError(ConstantesServices.MENSAJE_ERROR_GENERICO, e);
            seguridadService.logAuditoria(userContext, Evento.EV_LOGOFF_USUARIO, Estado.ESTADO_FRACASO, userContext, ConstantesServices.INTEGRACION_SCA, ConstantesServices.ACCION_LOGOUT,
                    ConstantesServices.MENSAJE_ERROR_GENERICO, ConstantesServices.CODIGO_ERROR_GENERICO);

            respuesta = new MasivasResponse<>(ConstantesServices.CODIGO_ERROR_GENERICO, ConstantesServices.MENSAJE_ERROR_GENERICO, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @PostMapping("/cambiarClave")
    public ResponseEntity<MasivasResponse<LoginResponse>> cambiarClave(@RequestBody @Valid ChangePassRequest authRequest, @AuthenticationPrincipal UserContext userContext) {

        MasivasResponse<LoginResponse> respuesta;
        try {

            respuesta = seguridadService.changePassword(authRequest.getUsername(), authRequest.getPassword(), authRequest.getNewpassword());

            seguridadService.logEvento(respuesta.getMensaje());
            seguridadService.logAuditoria(authRequest, Evento.EV_ACTUALIZA_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.INTEGRACION_SCA, ConstantesServices.ACCION_CHANGE_PASSWORD,
                    respuesta.getMensaje(), ConstantesServices.RESPUESTA_OK_API);
            LogSecurity.log("Cambio de contraseña",authRequest.getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(respuesta);

        } catch (Exception e) {

            seguridadService.logError(ConstantesServices.MENSAJE_ERROR_GENERICO, e);
            seguridadService.logAuditoria(authRequest, Evento.EV_ACTUALIZA_CONFIG_SISTEMA, Estado.ESTADO_FRACASO, userContext, ConstantesServices.INTEGRACION_SCA, ConstantesServices.ACCION_CHANGE_PASSWORD,
                    ConstantesServices.MENSAJE_ERROR_GENERICO, ConstantesServices.CODIGO_ERROR_GENERICO);

            respuesta = new MasivasResponse<>(ConstantesServices.CODIGO_ERROR_GENERICO, ConstantesServices.MENSAJE_ERROR_GENERICO, null);
            LogSecurity.alert(authRequest.getUsername(),"Cambio de contraseña",null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }
}
