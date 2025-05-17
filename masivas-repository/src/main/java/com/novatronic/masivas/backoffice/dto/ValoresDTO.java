package com.novatronic.masivas.backoffice.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author obi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValoresDTO {

    private String codEmisor;
    private String codInstrumento;
    private String codMoneda;
    private String desInstrumento;
    private String indFacilidad;
    
}
