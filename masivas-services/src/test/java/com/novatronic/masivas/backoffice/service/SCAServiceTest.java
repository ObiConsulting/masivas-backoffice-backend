package com.novatronic.masivas.backoffice.service;

import com.novatronic.components.aas.AAService;
import com.novatronic.components.aas.AAServiceFactory;
import com.novatronic.components.aas.exception.AASException;
import com.novatronic.masivas.backoffice.dto.SCAResponseDto;
import com.novatronic.masivas.backoffice.security.service.SCAService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author Obi Consulting
 */
@ExtendWith(MockitoExtension.class)
class SCAServiceTest {

    @Mock
    private AAServiceFactory factory;
    @Mock
    private AAService aaService;
    @InjectMocks
    private SCAService scaAutentication;
    private Properties mockProperties;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        mockProperties = new Properties();
        scaAutentication = new SCAService(mockProperties);

        ReflectionTestUtils.setField(scaAutentication, "aaFactory", factory);
        ReflectionTestUtils.setField(scaAutentication, "aaService", aaService);
        lenient().when(factory.getAAServiceInstance()).thenReturn(aaService);
    }

    @Test
    void login_exito() throws Exception {
        when(aaService.generateToken(anyMap())).thenReturn("token123");
        when(aaService.getResponCode()).thenReturn("0000");
        when(aaService.listProfiles("token123")).thenReturn(List.of("ROLE_ADMIN"));

        SCAResponseDto response = scaAutentication.login("EMPRESA", "APP", "usuario", "clave");

        assertEquals("0000", response.getResponseCode());
        assertEquals("token123", response.getToken());
        assertEquals("ROLE_ADMIN", response.getRolname());
    }

    @Test
    void login_errorAAS() throws Exception {
        when(aaService.generateToken(anyMap())).thenThrow(new AASException("ERR01", "ERR01"));

        SCAResponseDto response = scaAutentication.login("EMPRESA", "APP", "usuario", "clave");

        assertEquals("ERR01", response.getResponseCode());

    }

    @Test
    void listarRecursos_success() throws Exception {
        when(aaService.listResources("token123")).thenReturn(List.of("PERMISO_1", "PERMISO_2"));

        List<String> recursos = scaAutentication.listarRecursos("token123");

        assertEquals(2, recursos.size());
    }

    @Test
    void listarProfiles_success() throws Exception {
        when(aaService.listProfiles("token456")).thenReturn(List.of("ROLE1"));

        List<String> profiles = scaAutentication.listarProfiles("token456");

        assertEquals(1, profiles.size());
    }

    @Test
    void changePassword_exito() throws Exception {

        SCAResponseDto response = scaAutentication.changePassword("EMPRESA", "123", "456", "usuario");
        assertEquals("0000", response.getResponseCode());
        assertEquals("Cambio de contraseña realizado correctamente", response.getResponseDescription());
    }

    @Test
    void disableToken_invocaMetodoAAService() {
        scaAutentication.disableToken("user", "token");
        verify(aaService).disableToken("user", "token");
    }

    @Test
    void getUserAttributes_retornaMapa() {
        Map<String, Object> atributos = Map.of("clave", "valor");
        when(aaService.getUserAttributes("token")).thenReturn(atributos);

        Map<String, Object> result = scaAutentication.getUserAttributes("token");

        assertEquals("valor", result.get("clave"));
    }

    @Test
    void stop_invocaShutdown() throws Exception {
        scaAutentication.stop();
        verify(aaService).shutdown();
    }
}
