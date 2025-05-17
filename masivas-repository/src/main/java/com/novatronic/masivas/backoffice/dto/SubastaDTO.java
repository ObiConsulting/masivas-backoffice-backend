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
public class SubastaDTO {

    private Long idOperacion;
    private BigDecimal idSubasta;
    private Integer numSubasta;
    private String codInstrumento;
    private BigDecimal montoAdjudicado;
    private String fechaVencimiento;
    private Integer numColocacion;
    private Integer plazo;
    private String fechaOperacion;
    private String tipoOperacion;
    private String concepto;
    private String fechaSubasta;
    
    //Auditoria
    
    

}
