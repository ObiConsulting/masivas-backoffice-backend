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
public class DetalleRegistroVentaMonedaDTO {

    private Long idOperacion;
    private String numRefLbtr;
    private String numRefBcrp;
    private String concepto;
    private String fecha;

    private String entidadOrigen;
    private String entidadDestino;
    private String numCuentaEntOrigen;
    private String numCuentaEntDestino;
    private String moneda;
    private BigDecimal monto;
    private BigDecimal tipoCambio;
    private BigDecimal montoME;
    private String observacion;
    private String estado;
    private String codEstado;
    
    private Integer abonoConfirmacion;
    
    private String fechaCreacion;
    private String fechaModificacion;
    private String usuarioCreacion;
    private String usuarioModificacion;

}
