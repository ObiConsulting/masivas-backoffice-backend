package com.novatronic.masivas.backoffice.security.model;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Obi Consulting
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AuthResponse {

    private List<String> permisos;
    private String tiempoSesion;
    private String rol;
    private Map<String, Object> attributes;
    private String token;
}
