package com.novatronic.masivas.backoffice.dto;

import java.io.Serializable;
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
public class DetalleRegistroGrupoParametroDTO implements Serializable {

    private Long idGrupoParametro;
    private String codigo;
    private String descripcion;
    private String estado;

    private String usuCreacion;
    private String fecCreacion;
    private String usuModificacion;
    private String fecModificacion;
}
