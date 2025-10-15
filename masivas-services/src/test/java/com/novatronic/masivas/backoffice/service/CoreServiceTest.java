package com.novatronic.masivas.backoffice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Obi Consulting
 */
@ExtendWith(MockitoExtension.class)
public class CoreServiceTest {

    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private CoreService coreService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(coreService, "apiCoreUrlRecargar", "http://172.29.42.30:8608/gestor/tareas/recargar");
    }

    @Test
    void refrescarCache_exito() {

        String successResponse = "Cache recargada exitosamente.";
        ResponseEntity<String> successEntity = new ResponseEntity<>(successResponse, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(String.class))).thenReturn(successEntity);

        coreService.refrescarCacheCore();

        verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET), any(), eq(String.class));
    }
}
