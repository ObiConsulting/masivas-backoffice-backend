package com.novatronic.masivas.backoffice.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 *
 * @author Obi Consulting
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        MasivasResponse<Object> error = new MasivasResponse<>();
        error.setCodigo("401");
        error.setMensaje("No autenticado o token inválido");
        error.setResult(null);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
