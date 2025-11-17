package com.novatronic.masivas.backoffice.security.util;

import com.novatronic.masivas.backoffice.util.LogUtil;
import com.novatronic.novalog.audit.logger.NovaLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author Obi Consulting
 */
@Component
public class MaskUtil {

    private static final NovaLogger LOGGER = NovaLogger.getLogger(MaskUtil.class);

    @Value("${mask.displayMode}")
    private String displayMode;
    @Value("${mask.length}")
    private int maskLength;//
    @Value("${mask.character}")
    private String maskCharacter;//*

    /**
     * Formatea un campo según el modo de visualización configurado.
     *
     * @param field el campo a formatear
     * @return el campo formateado
     */
    public String format(String field) {
        try {

            if (field == null) {
                return field;
            }

            return switch (displayMode.toLowerCase()) {
                case "masked" ->
                    mask(field);
                default ->
                    field;
            };
        } catch (Exception e) {
            LOGGER.error(LogUtil.generarMensajeLogError(null,"Error mientras se formatea el campo: " + e.getMessage(),null));
            return field;
        }
    }

    /**
     * Enmascara un campo mostrando solo los últimos caracteres según la
     * longitud configurada.
     *
     * @param data el campo a enmascarar
     * @return el campo enmascarado
     */
    private String mask(String data) {
        try {
            if (data.length() <= maskLength) {
                return data;
            }
            String visiblePart = data.substring(data.length() - maskLength);
            StringBuilder masked = new StringBuilder();
            for (int i = 0; i < data.length() - maskLength; i++) {
                masked.append(maskCharacter);
            }
            masked.append(visiblePart);
            return masked.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error while masking", e);
        }
    }
}
