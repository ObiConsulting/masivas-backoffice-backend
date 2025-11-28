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
public class DetalleConsultaReporteTotalizadoDTO implements Serializable {

    private Long totalProcesado = 0L;
    private Long totalPendiente = 0L;
    private String montoProcesadoDolar;
    private String montoProcesadoSol;
    private String montoRechazadoDolar;
    private String montoRechazadoSol;
}
