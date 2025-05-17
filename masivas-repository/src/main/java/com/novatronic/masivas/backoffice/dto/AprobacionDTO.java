package com.novatronic.masivas.backoffice.dto;

import java.util.List;
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
public class AprobacionDTO {

    private String codigoTransaccion;
    private String nombreTransaccion;
    private Integer totalAprobacionesNecesarias;
    private Integer totalAprobacionesRealizadas = 0;
    
    List<OperacionListaAprobadorDTO> listaAprobadores;

}
