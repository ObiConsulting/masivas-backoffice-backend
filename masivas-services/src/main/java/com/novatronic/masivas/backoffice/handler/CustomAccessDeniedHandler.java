package com.novatronic.masivas.backoffice.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 *
 * @author Obi Consulting
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {

        MasivasResponse<Object> error = new MasivasResponse<>();
        error.setCodigo("403");
        error.setMensaje("No tiene permisos para acceder a este recurso");
        error.setResult(null);

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
