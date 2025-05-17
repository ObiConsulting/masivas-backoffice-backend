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
public class DetalleRegistroInterbancariaDTO {

    private Long idOperacion;
    private String numRefLbtr;
    private String numRefBcrp;
    private String concepto;
    private String entidadOrigen;
    private String entidadDestino;
    private String fecha;
    private String moneda;
    private BigDecimal monto;

    private Long idClienteOrigen;
    private Long idClienteDestino;

    private String numCuentaEntOrigen;
    private String numCuentaEntDestino;

    private String estado;
    private String codEstado;
    private String observacion;
    private String observacionCliente;
    private String afectoItf;
    private Integer abonoConfirmacion;
    private String abonoConfirmacionDescripcion;

    private ClienteDTO beneficiario;
    private ClienteDTO ordenante;

    private String fechaCreacion;
    private String fechaModificacion;
    private String usuarioCreacion;
    private String usuarioModificacion;

}
