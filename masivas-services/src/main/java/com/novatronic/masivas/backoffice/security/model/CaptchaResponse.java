package com.novatronic.masivas.backoffice.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Obi Consulting
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaResponse {

    private boolean valid;
    private boolean expired;
}
