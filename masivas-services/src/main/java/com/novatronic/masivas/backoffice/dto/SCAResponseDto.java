package com.novatronic.masivas.backoffice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Obi Consulting
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SCAResponseDto {

    private String username;
    private String token;
    private String responseCode;
    private String responseDescription;
    private String rolname;

}
