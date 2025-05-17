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
public class DetalleRegistroCavaliDTO {

    private Long idOperacion;
    private String numRefLbtr;
    private String numRefBcrp;
    private String estado;
    private String codEstado;
    private String fecha;

    private String concepto;
    private String moneda;
    private String entidadOrigen;
    private String numCuentaEntOrigen;
    private String entidadDestino;
    private String numCuentaEntDestino;
    private BigDecimal monto;
    private String instrucciones;

    private String fechaNegociacion;
    private String numReferencia;
    private String codSAB;
    private String cciSAB;
    private String tipoParticipante;

    private String fechaCreacion;
    private String fechaModificacion;
    private String usuarioCreacion;
    private String usuarioModificacion;

}
