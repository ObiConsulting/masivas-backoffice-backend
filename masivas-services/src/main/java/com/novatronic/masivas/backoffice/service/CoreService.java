package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.exception.ActionRestCoreException;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import com.novatronic.novalog.audit.logger.NovaLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Obi Consulting
 */
@Service
public class CoreService {

    private final RestTemplate restTemplate;

    @Value("${masivas.core.url.recargar}")
    private String apiCoreUrlRecargar;

    private static final NovaLogger LOGGER = NovaLogger.getLogger(CoreService.class);

    public CoreService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Método que refresca la memoria cache de la api core.
     */
    public void refrescarCacheCore() {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD_PROCESO, ConstantesServices.REFRESCAR_CACHE_CORE);

            ResponseEntity<String> response = restTemplate.exchange(apiCoreUrlRecargar, HttpMethod.GET, null, String.class);

            String responseMessage = response.getBody();
            if (responseMessage == null) {
                throw new ActionRestCoreException(ConstantesServices.CODIGO_ERROR_API_CORE_RECARGAR, ConstantesServices.MENSAJE_ERROR_API_CORE_RECARGAR);
            }

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD_API_RESPONSE, apiCoreUrlRecargar, responseMessage);

        } catch (ActionRestCoreException ex) {
            LOGGER.error(ex.getMessage(), ex);
        } catch (RestClientException ex) {
            LOGGER.error(ConstantesServices.MENSAJE_ERROR_API_CORE, ex);
        }
    }

    public void logEvento(String mensaje, Object... param) {
        LOGGER.info(mensaje, param);
    }
}
