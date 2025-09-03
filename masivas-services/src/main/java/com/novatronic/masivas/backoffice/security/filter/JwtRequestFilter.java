package com.novatronic.masivas.backoffice.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.security.util.JwtUtil;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Optional;

/**
 *
 * @author Obi Consulting
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final HazelcastInstance hazelcastInstance;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtRequestFilter(JwtUtil jwtUtil, HazelcastInstance hazelcastInstance) {
        this.jwtUtil = jwtUtil;
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        try {
            Optional<String> optionalToken = extractToken(request);
            if (optionalToken.isPresent()) {
                String token = optionalToken.get();
                String username = jwtUtil.extractUsername(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    if (!jwtUtil.validateToken(token, username)) {
                        sendUnauthorizedResponse(response, "401", "Token no válido o caducado");
                        return;
                    }

                    UserContext userContext = (UserContext) hazelcastInstance.getMap(ConstantesServices.MAP_LOGUEADOS).get(username);

                    if (userContext == null) {
                        sendUnauthorizedResponse(response, "401", "Sesión expirada o no encontrada");
                        return;
                    }

                    Authentication auth = new UsernamePasswordAuthenticationToken(userContext, null, userContext.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }

            chain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {
            logger.error("401 - Token expirado", ex);
            sendUnauthorizedResponse(response, "401", "Token expirado");
        } catch (SignatureException | MalformedJwtException ex) {
            logger.error("401 - Token inválido", ex);
            sendUnauthorizedResponse(response, "401", "Token inválido");
        } catch (IllegalArgumentException ex) {
            logger.error("401 - Token no proporcionado", ex);
            sendUnauthorizedResponse(response, "401", "Token no proporcionado");
        } catch (ClassCastException ex) {
            logger.error("401 - Error en sesión. Por favor inicie sesión nuevamente.", ex);
            sendUnauthorizedResponse(response, "401", "Error en sesión. Por favor inicie sesión nuevamente.");
        } catch (ServletException | IOException ex) {
            logger.error("401 - Error interno de autenticación", ex);
            sendUnauthorizedResponse(response, "401", "Error interno de autenticación");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/usuario/acceso")
                || path.startsWith("/public")
                || path.startsWith("/swagger")
                || path.startsWith("/v3/api-docs");
    }

    private Optional<String> extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return Optional.of(header.substring(7));
        }
        return Optional.empty();
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String codigo, String mensaje) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        MasivasResponse<Object> error = new MasivasResponse<>();
        error.setCodigo(codigo);
        error.setMensaje(mensaje);
        error.setResult(null);

        String json = objectMapper.writeValueAsString(error);
        response.getWriter().write(json);
        response.getWriter().flush();
    }
}
