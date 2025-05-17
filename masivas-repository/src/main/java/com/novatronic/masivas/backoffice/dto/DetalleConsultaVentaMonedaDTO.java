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
public class DetalleConsultaVentaMonedaDTO {

    private Long idOperacion;
    private String numRefLbtr;
    private String numRefBcrp;
    private String concepto;
    private String fecha;

    private String entidadVendedora;
    private String entidadCompradora;
    private String numCuentaEntOrigen;
    private String numCuentaEntDestino;

    private String moneda;
    private BigDecimal monto;
    private BigDecimal tipoCambio;
    private BigDecimal montoME;
    private String simboloMoneda;
    private String tipoMovimiento;
    private String estado;
    
    private String abonoConfirmado;
    private String referenciaEnlaceLbtr;

}
