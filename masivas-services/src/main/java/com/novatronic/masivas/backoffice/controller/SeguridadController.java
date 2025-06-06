package com.novatronic.masivas.backoffice.controller;

import com.hazelcast.core.HazelcastInstance;
import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.security.model.AuthRequest;
import com.novatronic.masivas.backoffice.security.model.ChangePassRequest;
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
    public ResponseEntity<MasivasResponse> acceso(@RequestBody @Valid AuthRequest authRequest) throws Exception {
        MasivasResponse res;
        try {
            res = seguridadService.authenticate(authRequest.getUsername(), authRequest.getPassword());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e) {
            res = new MasivasResponse(ConstantesServices.CODIGO_ERROR_GENERICO, ConstantesServices.MENSAJE_ERROR_GENERICO, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @RequestMapping(value = "/cambiarClavePrimeraVez", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('PRIMER_LOGIN')")
    public ResponseEntity<MasivasResponse> cambiarClavePrimeraVez(@RequestBody @Valid ChangePassRequest authRequest) throws Exception {
        MasivasResponse res;
        try {
            res = seguridadService.changePassFirstTime(authRequest.getUsername(), authRequest.getPassword(), authRequest.getNewpassword());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e) {
            res = new MasivasResponse(ConstantesServices.CODIGO_ERROR_GENERICO, ConstantesServices.MENSAJE_ERROR_GENERICO, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @GetMapping("/permisos")
    public ResponseEntity<MasivasResponse> permisos(@AuthenticationPrincipal UserContext userContext) throws Exception {
        MasivasResponse res;
        try {

            List<String> valores = userContext.getAuthoritiesList();

            CustomPaginate customPaginate = new CustomPaginate<>(1, valores.size(), valores);
            return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, "Operación correcta", customPaginate)
            );

        } catch (Exception e) {
            res = new MasivasResponse(ConstantesServices.CODIGO_ERROR_GENERICO, ConstantesServices.MENSAJE_ERROR_GENERICO, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @GetMapping("/cerrarsesion")
    public ResponseEntity<MasivasResponse> cerrarSesion(@AuthenticationPrincipal UserContext userContext) throws Exception {
        MasivasResponse res;
        try {
            // Elimina del Hazelcast
            hazelcastInstance.getMap(ConstantesServices.MAP_LOGUEADOS).remove(userContext.getUsername());
            // Limpia el contexto de Spring (opcional, buena práctica)
            SecurityContextHolder.clearContext();

            return ResponseEntity.ok(new MasivasResponse<>("200", "Sesión cerrada correctamente", null));

        } catch (Exception e) {
            res = new MasivasResponse(ConstantesServices.CODIGO_ERROR_GENERICO, ConstantesServices.MENSAJE_ERROR_GENERICO, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @RequestMapping(value = "/cambiarClave", method = RequestMethod.POST)
    public ResponseEntity<MasivasResponse> cambiarClave(@RequestBody @Valid ChangePassRequest authRequest) throws Exception {
        MasivasResponse res;
        try {
            res = seguridadService.changePassFirstTime(authRequest.getUsername(), authRequest.getPassword(), authRequest.getNewpassword());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e) {
            res = new MasivasResponse(ConstantesServices.CODIGO_ERROR_GENERICO, ConstantesServices.MENSAJE_ERROR_GENERICO, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }
}
