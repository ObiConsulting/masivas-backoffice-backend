package com.novatronic.masivas.backoffice.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Obi Consulting
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MasivasRequestDTO {

    //IDs
    private Long idEntidad;
    private Long idGrupoParametro;
    private Long idParametro;

    //Campos
    private String codigo;
    private String estado;

    //Activar/Desactivar
    private List<Long> idsOperacion;

    //Entidad
    private String nombre;
    //Grupo Parametro
    private String descripcion;
    //Parametro
    private String valor;

}
