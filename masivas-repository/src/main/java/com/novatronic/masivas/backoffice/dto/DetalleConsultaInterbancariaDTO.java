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
public class DetalleConsultaInterbancariaDTO {

    private Long idOperacion;
    private String numRefLbtr;
    private String numRefBcrp;
    private String concepto;
    private String entidadOrigen;
    private String entidadDestino;
    private String fecha;
    private String moneda;
    private BigDecimal monto;

    private String clienteOrigen;
    private String clienteDestino;

    private String numCuentaEntOrigen;
    private String numCuentaEntDestino;

    private String estado;
    private String observacion;
    private String observacionCliente;
    private String afectoItf;
    private Integer abonoConfirmacion;
    private String abonoConfirmacionDescripcion;

}
