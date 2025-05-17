package com.novatronic.masivas.backoffice.dto;

import java.math.BigDecimal;
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
public class DetalleConsultaFacilidadIntradiaDTO {

    private Long idOperacion;
    private String numRefLbtr;
    private String numRefBcrp;
    private String facilidad;
    private String tipoMovimiento;
    private String simboloMoneda;
    private BigDecimal monto;
    private String estado;
    private String numReferenciaOrigen;
    private BigDecimal tipoCambio;

}
