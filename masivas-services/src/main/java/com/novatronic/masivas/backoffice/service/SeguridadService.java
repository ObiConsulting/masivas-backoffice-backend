package com.novatronic.masivas.backoffice.service;

import com.hazelcast.core.HazelcastInstance;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.dto.SCAResponseDto;
import com.novatronic.masivas.backoffice.log.LogAuditoria;
import com.novatronic.masivas.backoffice.security.model.CaptchaRequest;
import com.novatronic.masivas.backoffice.security.model.CaptchaResponse;
import com.novatronic.masivas.backoffice.security.model.LoginResponse;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.security.service.SCAService;
import com.novatronic.masivas.backoffice.security.util.JwtUtil;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import com.novatronic.masivas.backoffice.util.LogUtil;
import com.novatronic.novalog.audit.logger.NovaLogger;
import com.novatronic.novalog.audit.util.Estado;
import com.novatronic.novalog.audit.util.Evento;
import jakarta.servlet.http.HttpServletRequest;
import java.io.Serializable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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
    private final SCAService scaAutentication;
    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;
    private final MessageSource messageSource;

    @Value("${masivas.captcha.active}")
    private String captchaActive;
    @Value("${masivas.captcha.url}")
    private String captchaUrl;
    @Value("${masivas.sca.empresa}")
    private String empresa;
    @Value("${masivas.sca.aplicacion}")
    private String aplicacion;

    private static final NovaLogger LOGGER = NovaLogger.getLogger(SeguridadService.class);

    public SeguridadService(HazelcastInstance hazelcastInstance,
            HttpServletRequest request,
            SCAService scaAutentication,
            JwtUtil jwtUtil,
            RestTemplate restTemplate,
            @Qualifier("usuarioMessageSource") MessageSource messageSource) {
        this.hazelcastInstance = hazelcastInstance;
        this.request = request;
        this.scaAutentication = scaAutentication;
        this.jwtUtil = jwtUtil;
        this.restTemplate = restTemplate;
        this.messageSource = messageSource;
    }

    /**
     * Método que inicia sesión con el SCA
     *
     * @param username
     * @param password
     * @param captchaId
     * @param captchaText
     * @return
     */
    public MasivasResponse<LoginResponse> authenticate(String username, String password, String captchaId, String captchaText) {

        MasivasResponse<LoginResponse> respuesta = new MasivasResponse<>();

        logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD_ACCION, ConstantesServices.LOGIN, "{" + ConstantesServices.AUDIT_CAMPO_USUARIO + username + ConstantesServices.AUDIT_CAMPO_PASSWORD + password + '}');

        // validacion de captcha
        if (ConstantesServices.ESTADO_ACTIVO.equals(captchaActive)) {
            CaptchaResponse captchaResponse = validarCaptcha(captchaId, captchaText);
            if (captchaResponse == null || !captchaResponse.isValid()) {
                respuesta.setCodigo("9494");
                respuesta.setMensaje("Validación Erronea");
                return respuesta;
            }
        }

        SCAResponseDto result = scaAutentication.login(empresa, aplicacion, username, password);
        respuesta.setCodigo(result.getResponseCode());
        respuesta.setMensaje(messageSource.getMessage(result.getResponseCode(), null, Locale.getDefault()));

        switch (result.getResponseCode()) {

            case CODIGO_PRIMER_LOGIN -> {

                int tiempoSesion = 5;

                List<String> permisos = List.of(ConstantesServices.PERMISO_PRIMER_LOGIN);
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

                int tiempoSesion = Integer.parseInt(atributos.get(TIMEOUT_ATTR).toString()) * 6;
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
                        .tiempoSesion(tiempoSesion / 6)
                        .build());
            }
        }

        return respuesta;
    }

    /**
     * Método que cambia la contraseña de un usuario especifico
     *
     * @param username
     * @param password
     * @param newPassword
     * @return
     */
    public MasivasResponse<LoginResponse> changePassword(String username, String password, String newPassword) {

        logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD_ACCION, ConstantesServices.CAMBIAR_CONTRASENA, "{" + ConstantesServices.AUDIT_CAMPO_USUARIO + username + ConstantesServices.AUDIT_CAMPO_PASSWORD + password + ",newPassword=" + newPassword + '}');

        MasivasResponse<LoginResponse> respuesta = new MasivasResponse<>();
        SCAResponseDto result = scaAutentication.changePassword(empresa, password, newPassword, username);

        respuesta.setCodigo(result.getResponseCode());
        respuesta.setMensaje(result.getResponseDescription());

        return respuesta;
    }

    private UserContext buildUserContext(String username, List<String> permisos, Map<String, Serializable> atributos, int tiempoSesion, SCAResponseDto result) {
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

    private CaptchaResponse validarCaptcha(String captchaId, String userInput) {
        try {
            CaptchaRequest captchaRequest = new CaptchaRequest();
            captchaRequest.setCaptchaId(captchaId);
            captchaRequest.setUserInput(userInput);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<CaptchaRequest> entity = new HttpEntity<>(captchaRequest, headers);

            ResponseEntity<CaptchaResponse> response = restTemplate.exchange(captchaUrl, HttpMethod.POST, entity, CaptchaResponse.class);

            return response.getBody();

        } catch (ResourceAccessException e) {
            // Timeout o problema de red
            LOGGER.error(LogUtil.generarMensajeLogError(ConstantesServices.MENSAJE_ERROR_EXCEPTION), e);
            return null;
        } catch (RestClientException e) {
            LOGGER.error(LogUtil.generarMensajeLogError(ConstantesServices.MENSAJE_ERROR_EXCEPTION), e);
            return null;

        }
    }

    public void logEvento(String mensaje, Object... param) {
        LOGGER.info(mensaje, param);
    }

    public <T> void logAuditoria(T request, Evento idEvento, Estado estado, UserContext userContext, String origen, String mensajeRespuesta, String codigoRespuesta) {
        String idMensaje=LogAuditoria.resolveTrxId();
        LOGGER.audit(null, request, idEvento, estado, userContext.getUsername(), userContext.getScaProfile(), ConstantesServices.INTEGRACION_SCA, userContext.getIp(),
                idMensaje, origen, null, null, mensajeRespuesta, codigoRespuesta);
    }

    public void logError(String mensajeError, Exception e) {
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error(LogUtil.generarMensajeLogError(mensajeError), e);
        }
    }

}
