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
public class DetalleRegistroDepositoOvernightDTO {

    private Long idOperacion;
    private String numRefLbtr;
    private String numRefBcrp;
    private String concepto;
    private String fecha;
    private BigDecimal monto;
    private String estado;
    private String codEstado;
    private String moneda;
    private String instrucciones;
    private String numCuentaEntOrigen;

    private String fechaCreacion;
    private String fechaModificacion;
    private String usuarioCreacion;
    private String usuarioModificacion;

}
