package com.novatronic.masivas.backoffice.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Obi Consulting
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DetalleReporteConsolidadoDTO implements Serializable {

    private String entidadDestino;
    private String moneda;
    private Long totalTransferencias;
    private BigDecimal montoProcesado;
    private BigDecimal montoRechazado;

}
