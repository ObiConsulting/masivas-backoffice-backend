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
public class OperacionListaAprobadorDTO {
    
    private Long codigo;
    private String idUsuarioAprobador;
    private String fechaAprobacion;
    
    
}
