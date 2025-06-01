package com.novatronic.masivas.backoffice.service;

import com.hazelcast.core.HazelcastInstance;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.dto.SCAResponseDto;
import com.novatronic.masivas.backoffice.security.model.LoginResponse;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.security.service.SCAAutentication;
import com.novatronic.masivas.backoffice.security.util.JwtUtil;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import com.novatronic.novalog.audit.logger.NovaLogger;
import jakarta.servlet.http.HttpServletRequest;
import java.io.Serializable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *
 * @author Obi Consulting
 */
@Service
public class SeguridadService {

    private static final String CODIGO_EXITO = "0000";
    private static final String CODIGO_ADVERTENCIA = "1002";

    private static final String CODIGO_PRIMER_LOGIN = "1001";
    private static final String TIMEOUT_ATTR = "scanova.ctx.timeout";

    private final HazelcastInstance hazelcastInstance;
    private final HttpServletRequest request;
    private final SCAAutentication scaAutentication;
    private final JwtUtil jwtUtil;

    @Value("${masivas.sca.empresa}")
    private String empresa;

    @Value("${masivas.sca.aplicacion}")
    private String aplicacion;

    private static final NovaLogger LOGGER = NovaLogger.getLogger(SeguridadService.class);

    public SeguridadService(HazelcastInstance hazelcastInstance,
            HttpServletRequest request,
            ParametroCacheService parametroCacheService,
            SCAAutentication scaAutentication,
            JwtUtil jwtUtil) {
        this.hazelcastInstance = hazelcastInstance;
        this.request = request;
        this.scaAutentication = scaAutentication;
        this.jwtUtil = jwtUtil;
    }

    public MasivasResponse<LoginResponse> authenticate(String username, String password) {
        MasivasResponse<LoginResponse> respuesta = new MasivasResponse<>();

        SCAResponseDto result = scaAutentication.login(empresa, aplicacion, username, password);
        respuesta.setCodigo(result.getResponseCode());
        respuesta.setMensaje(result.getResponseDescription());

        switch (result.getResponseCode()) {
            case CODIGO_PRIMER_LOGIN -> {
                int tiempoSesion = 5;
                List<String> permisos = List.of(ConstantesServices.PERMISO_PRIMER_LOGIN);
                //Map<String, Object> atributos = Collections.emptyMap();
                Map<String, Serializable> atributos = Collections.emptyMap();

                UserContext userContext = buildUserContext(username, permisos, atributos, tiempoSesion, result);
                String token = jwtUtil.generateToken(username, permisos, "", tiempoSesion);
                userContext.setToken(token);

                hazelcastInstance.getMap(ConstantesServices.MAP_LOGUEADOS).put(userContext.getUsername(), userContext, tiempoSesion, TimeUnit.MINUTES);

                respuesta.setResult(LoginResponse.builder()
                        .token(token)
                        .tiempoSesion(tiempoSesion)
                        .build());
            }
            case CODIGO_EXITO, CODIGO_ADVERTENCIA -> {
                Map<String, Object> atributos = scaAutentication.getUserAttributes(result.getToken());
                if (atributos == null || !atributos.containsKey(TIMEOUT_ATTR)) {
                    respuesta.setMensaje("No se pudo obtener el timeout de sesión.");
                    return respuesta;
                }

                Map<String, Serializable> safeAttributes = atributos.entrySet().stream()
                        .filter(e -> e.getValue() instanceof Serializable)
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> (Serializable) e.getValue()
                        ));

                int tiempoSesion = 6 * Integer.parseInt(atributos.get(TIMEOUT_ATTR).toString());
                List<String> permisos = scaAutentication.listarRecursos(result.getToken());
                permisos.add(ConstantesServices.PERMISO_USUARIO_VALIDO);

                scaAutentication.disableToken(username, result.getToken());

                UserContext userContext = buildUserContext(username, permisos, safeAttributes, tiempoSesion, result);

                List<String> permisosjwt = List.of(ConstantesServices.PERMISO_USUARIO_VALIDO);
                String token = jwtUtil.generateToken(username, permisosjwt, userContext.getScaProfile(), tiempoSesion);
                userContext.setToken(token);

                hazelcastInstance.getMap(ConstantesServices.MAP_LOGUEADOS).put(userContext.getUsername(), userContext, tiempoSesion, TimeUnit.MINUTES);

                respuesta.setResult(LoginResponse.builder()
                        .token(token)
                        .tiempoSesion(tiempoSesion)
                        .build());
            }
            default -> {
                // Se devuelve solo código y descripción

            }
        }

        return respuesta;
    }

    public MasivasResponse<LoginResponse> changePassFirstTime(String username, String password, String newPassword) {

        MasivasResponse<LoginResponse> respuesta = new MasivasResponse<>();
        SCAResponseDto result = scaAutentication.changePassword(empresa, password, newPassword, username);

        respuesta.setCodigo(result.getResponseCode());
        respuesta.setMensaje(result.getResponseDescription());
        return respuesta;
    }

    private UserContext buildUserContext(String username,
            List<String> permisos,
            Map<String, Serializable> atributos,
            int tiempoSesion,
            SCAResponseDto result) {

//        List<GrantedAuthority> authorities = permisos.stream()
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
        return UserContext.create(
                username,
                permisos,
                atributos,
                tiempoSesion,
                result.getToken(),
                result.getResponseCode(),
                result.getRolname(),
                request.getRemoteAddr(),
                ""
        );
    }

    public MasivasResponse<LoginResponse> authenticateFake(String username, String password) {
        MasivasResponse<LoginResponse> respuesta = new MasivasResponse<>();

        //SCAResponseDto result = scaAutentication.login(empresa, aplicacion, username, password);
        respuesta.setCodigo("0000");
        respuesta.setMensaje("OK");
        SCAResponseDto result = new SCAResponseDto();
        result.setToken("ABC");
        result.setResponseCode("0000");
        result.setRolname("Superadmin");

        int tiempoSesion = 60;
        List<String> permisos = List.of(ConstantesServices.PERMISO_USUARIO_VALIDO);

        Map<String, Serializable> atributos = Collections.emptyMap();

        UserContext userContext = buildUserContext(username, permisos, atributos, tiempoSesion, result);
        String token = jwtUtil.generateToken(username, permisos, "", tiempoSesion);
        userContext.setToken(token);

        hazelcastInstance.getMap(ConstantesServices.MAP_LOGUEADOS).put(userContext.getUsername(), userContext, tiempoSesion, TimeUnit.MINUTES);

        respuesta.setResult(LoginResponse.builder()
                .token(token)
                .tiempoSesion(tiempoSesion)
                .build());

        return respuesta;
    }

}
