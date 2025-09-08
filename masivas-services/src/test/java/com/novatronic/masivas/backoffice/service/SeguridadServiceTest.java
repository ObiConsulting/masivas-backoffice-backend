package com.novatronic.masivas.backoffice.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.dto.SCAResponseDto;
import com.novatronic.masivas.backoffice.security.model.CaptchaResponse;
import com.novatronic.masivas.backoffice.security.model.LoginResponse;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.security.service.SCAService;
import com.novatronic.masivas.backoffice.security.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Obi Consulting
 */
@ExtendWith(MockitoExtension.class)
class SeguridadServiceTest {

    @Mock
    private HazelcastInstance hazelcastInstance;
    @Mock
    private MessageSource messageSource;
    @Mock
    private HttpServletRequest request;
    @Mock
    private SCAService sCAService;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private IMap<String, UserContext> userMap;
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private SeguridadService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new SeguridadService(hazelcastInstance, request, sCAService, jwtUtil, restTemplate, messageSource);
        lenient().when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        ReflectionTestUtils.setField(authService, "captchaUrl", "http://172.29.42.30:8381/captcha/validate");
    }

    @Test
    void authenticatePrimerLogin() {

        SCAResponseDto scaResponse = new SCAResponseDto("Superadmin", "token-abc", "1001", "Primer login", "rol");

        when(sCAService.login(any(), any(), any(), any())).thenReturn(scaResponse);
        when(jwtUtil.generateToken(any(), anyList(), any(), anyInt())).thenReturn("jwt-token");
        when(hazelcastInstance.getMap(any())).thenReturn((IMap) userMap);

        MasivasResponse<LoginResponse> response = authService.authenticate("user", "pass", "", "");

        assertEquals("1001", response.getCodigo());
        assertNotNull(response.getResult());
        assertEquals("jwt-token", response.getResult().getToken());
    }

    @Test
    void authenticateExitoConTimeoutYPermisos() {
        SCAResponseDto scaResponse = new SCAResponseDto("Superadmin", "token-abc", "0000", "Ok", "rol");
        Map<String, Object> attributes = Map.of("scanova.ctx.timeout", "1");
        List<String> permisos = new ArrayList<>();

        when(sCAService.login(any(), any(), any(), any())).thenReturn(scaResponse);
        when(sCAService.getUserAttributes(any())).thenReturn(attributes);
        when(sCAService.listarRecursos(any())).thenReturn(permisos);
        when(jwtUtil.generateToken(any(), anyList(), any(), anyInt())).thenReturn("jwt-token");
        when(hazelcastInstance.getMap(any())).thenReturn((IMap) userMap);

        doNothing().when(sCAService).disableToken(any(), any());

        MasivasResponse<LoginResponse> response = authService.authenticate("user", "pass", "", "");

        assertEquals("0000", response.getCodigo());
        assertEquals("jwt-token", response.getResult().getToken());
        assertEquals(1, response.getResult().getTiempoSesion());
    }

    @Test
    void authenticateExitoConTimeoutYPermisos_conCaptcha() {
        SCAResponseDto scaResponse = new SCAResponseDto("Superadmin", "token-abc", "0000", "Ok", "rol");
        Map<String, Object> attributes = Map.of("scanova.ctx.timeout", "1");
        List<String> permisos = new ArrayList<>();

        when(sCAService.login(any(), any(), any(), any())).thenReturn(scaResponse);
        when(sCAService.getUserAttributes(any())).thenReturn(attributes);
        when(sCAService.listarRecursos(any())).thenReturn(permisos);
        when(jwtUtil.generateToken(any(), anyList(), any(), anyInt())).thenReturn("jwt-token");
        when(hazelcastInstance.getMap(any())).thenReturn((IMap) userMap);

        doNothing().when(sCAService).disableToken(any(), any());

        ReflectionTestUtils.setField(authService, "captchaActive", "1");
        CaptchaResponse responseCaptcha = new CaptchaResponse();
        responseCaptcha.setValid(true);
        responseCaptcha.setExpired(false);
        ResponseEntity<CaptchaResponse> responseEntity = new ResponseEntity<>(responseCaptcha, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(CaptchaResponse.class))).thenReturn(responseEntity);

        MasivasResponse<LoginResponse> response = authService.authenticate("user", "pass", "", "");

        assertEquals("0000", response.getCodigo());
        assertEquals("jwt-token", response.getResult().getToken());
        assertEquals(1, response.getResult().getTiempoSesion());
    }

    @Test
    void authenticateExitoConTimeoutYPermisos_conCaptcha_error() {
        ReflectionTestUtils.setField(authService, "captchaActive", "1");

        ResponseEntity<CaptchaResponse> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(CaptchaResponse.class))).thenReturn(responseEntity);

        MasivasResponse<LoginResponse> response = authService.authenticate("user", "pass", "", "");

        assertEquals("9494", response.getCodigo());
    }

    @Test
    void authenticateExitoConTimeoutYPermisos_conCaptcha_excepcion_resource() {
        ReflectionTestUtils.setField(authService, "captchaActive", "1");

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(CaptchaResponse.class))).thenThrow(new ResourceAccessException("Simulando un error de timeout/red"));

        MasivasResponse<LoginResponse> response = authService.authenticate("user", "pass", "", "");

        assertEquals("9494", response.getCodigo());
    }

    @Test
    void authenticateExitoConTimeoutYPermisos_conCaptcha_excepcion_restClient() {
        ReflectionTestUtils.setField(authService, "captchaActive", "1");

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(CaptchaResponse.class))).thenThrow(new RestClientException("Simulando un error HTTP 4xx o 5xx"));

        MasivasResponse<LoginResponse> response = authService.authenticate("user", "pass", "", "");

        assertEquals("9494", response.getCodigo());
    }

    @Test
    void authenticateSinTimeout() {
        SCAResponseDto scaResponse = new SCAResponseDto("Superadmin", "token-abc", "0000", "OK", "rol");

        when(sCAService.login(any(), any(), any(), any())).thenReturn(scaResponse);
        when(sCAService.getUserAttributes(any())).thenReturn(new HashMap<>());

        MasivasResponse<LoginResponse> response = authService.authenticate("user", "pass", "", "");
        assertEquals("No se pudo obtener el timeout de sesión.", response.getMensaje());
        assertNull(response.getResult());
    }

    @Test
    void changePasswordRetornaExito() {
        SCAResponseDto scaResponse = new SCAResponseDto("Superadmin", "token-abc", "0000", "Password changed", "rol");
        when(sCAService.changePassword(any(), any(), any(), any())).thenReturn(scaResponse);

        MasivasResponse<LoginResponse> response = authService.changePassword("user", "old", "new");

        assertEquals("0000", response.getCodigo());
        assertEquals("Password changed", response.getMensaje());
    }

}
