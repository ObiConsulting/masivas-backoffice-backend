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
public class DetalleConsultaParametroDTO implements Serializable {

    private Long idParametro;
    private String codigo;
    private String valor;
    private String nombreGrupoParametro;
    private String estado;

}
