package com.novatronic.masivas.backoffice.dto;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author obi
 */

@Data
@NoArgsConstructor
public class TaDetalleParametroDTO implements Serializable {

    private String codigo;
    private String descripcion;
    
    
    public TaDetalleParametroDTO(String codigo, String descripcion) {
        this.codigo = codigo != null ? codigo.trim() : "";
        this.descripcion = descripcion != null ? descripcion.trim() : "";
    }

    
}
