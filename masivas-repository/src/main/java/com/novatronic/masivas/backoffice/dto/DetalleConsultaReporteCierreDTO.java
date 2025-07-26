package com.novatronic.masivas.backoffice.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
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
public class DetalleConsultaReporteCierreDTO implements Serializable {

    private Long totalObtenidoCliente = 0L;
    private Long totalEnviadoCCE = 0L;
    private Long totalObtenidoCCE = 0L;
    private Long totalEnviadoCliente = 0L;

    private List<Object[]> detalleArchivoDirectorio;
    private List<Object[]> detalleArchivoMasivas;
    private List<Object[]> detalleArchivoTitularidad;

}
