package com.novatronic.masivas.backoffice.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Obi Consulting
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class LoginResponse {

    private String token;
    private Integer tiempoSesion;

}
