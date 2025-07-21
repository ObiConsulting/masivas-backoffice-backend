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
        MasivasResponse<LoginResponse> res;
        try {
            res = seguridadService.authenticate(authRequest.getUsername(), authRequest.getPassword());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e) {
            res = new MasivasResponse<>(ConstantesServices.CODIGO_ERROR_GENERICO, ConstantesServices.MENSAJE_ERROR_GENERICO, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @RequestMapping(value = "/cambiarClavePrimeraVez", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('PRIMER_LOGIN')")
    public ResponseEntity<MasivasResponse<LoginResponse>> cambiarClavePrimeraVez(@RequestBody @Valid ChangePassRequest authRequest) throws Exception {
        MasivasResponse<LoginResponse> res;
        try {
            res = seguridadService.changePassFirstTime(authRequest.getUsername(), authRequest.getPassword(), authRequest.getNewpassword());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e) {
            res = new MasivasResponse<>(ConstantesServices.CODIGO_ERROR_GENERICO, ConstantesServices.MENSAJE_ERROR_GENERICO, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @GetMapping("/permisos")
    public ResponseEntity<MasivasResponse<CustomPaginate<String>>> permisos(@AuthenticationPrincipal UserContext userContext) throws Exception {
        try {
            List<String> valores = userContext.getAuthoritiesList();

            CustomPaginate customPaginate = new CustomPaginate<>(1, valores.size(), valores);
            return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, "Operación correcta", customPaginate));

        } catch (Exception e) {
            MasivasResponse<CustomPaginate<String>> res = new MasivasResponse<>(ConstantesServices.CODIGO_ERROR_GENERICO, ConstantesServices.MENSAJE_ERROR_GENERICO, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @GetMapping("/cerrarsesion")
    public ResponseEntity<MasivasResponse<Void>> cerrarSesion(@AuthenticationPrincipal UserContext userContext) throws Exception {

        try {
            // Elimina del Hazelcast
            hazelcastInstance.getMap(ConstantesServices.MAP_LOGUEADOS).remove(userContext.getUsername());
            // Limpia el contexto de Spring (opcional, buena práctica)
            SecurityContextHolder.clearContext();

//            return ResponseEntity.ok(new MasivasResponse<>("200", "Sesión cerrada correctamente", null));
            return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_CERRAR_SESION, null));

        } catch (Exception e) {
            MasivasResponse<Void> res = new MasivasResponse<>(ConstantesServices.CODIGO_ERROR_GENERICO, ConstantesServices.MENSAJE_ERROR_GENERICO, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @RequestMapping(value = "/cambiarClave", method = RequestMethod.POST)
    public ResponseEntity<MasivasResponse<LoginResponse>> cambiarClave(@RequestBody @Valid ChangePassRequest authRequest) throws Exception {
        MasivasResponse<LoginResponse> res;
        try {
            res = seguridadService.changePassFirstTime(authRequest.getUsername(), authRequest.getPassword(), authRequest.getNewpassword());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e) {
            res = new MasivasResponse<>(ConstantesServices.CODIGO_ERROR_GENERICO, ConstantesServices.MENSAJE_ERROR_GENERICO, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }
}
