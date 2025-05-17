package com.novatronic.masivas.backoffice.dto;

import java.io.Serializable;
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
public class TpCuentaCorrienteBcrpDTO implements Serializable {

    private String numCuentaCorriente;
    private String nomCuentaCorriente;
    private String codigoMoneda;
    private Integer indLiquidadora;
    private Integer indLiquidadoraFi;
    private Integer indLiquidadoraOv;

}
