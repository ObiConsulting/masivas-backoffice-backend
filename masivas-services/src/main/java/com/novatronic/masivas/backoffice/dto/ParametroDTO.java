package com.novatronic.masivas.backoffice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Obi Consulting
 */
@Data
@NoArgsConstructor
public class ParametroDTO {

    private String codigo;
    private String descripcion;

    public ParametroDTO(String codigo, String descripcion) {
        this.codigo = codigo != null ? codigo.trim() : "";
        this.descripcion = descripcion != null ? descripcion.trim() : "";
    }
}
