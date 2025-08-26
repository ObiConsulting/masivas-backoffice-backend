package com.novatronic.masivas.backoffice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Obi Consulting
 */
@Data
@NoArgsConstructor
public class EntidadDTO {

    private String codigo;
    private String descripcion;
    private String propietario;

    public EntidadDTO(String codigo, String descripcion, String propietario) {
        this.codigo = codigo != null ? codigo.trim() : "";
        this.descripcion = descripcion != null ? descripcion.trim() : "";
        this.propietario = propietario != null ? propietario.trim() : "0";
    }
}
