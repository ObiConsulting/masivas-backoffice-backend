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
public class DetalleConsultaRONDTO {

    private Long idOperacion;
    private String numRefLbtr;
    private String numRefBcrp;
    private String concepto;
    private String fechaVencimiento;
    private Integer numSubasta;
    private String codInstrumento;
    private BigDecimal montoAdjudicado;
    private Integer plazo;
    private String estado;
    private String moneda;
    

}
