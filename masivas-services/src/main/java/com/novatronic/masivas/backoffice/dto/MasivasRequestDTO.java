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

    public String toStringGrupoParametro() {
        return "{" + "idGrupoParametro=" + idGrupoParametro + ", codigo=" + codigo + ", descripcion=" + descripcion + '}';
    }

    public String toStringParametro() {
        return "{" + "idParametro=" + idParametro + ", idGrupoParametro=" + idGrupoParametro + ", codigo=" + codigo + ", valor=" + valor + '}';
    }

    public String toStringEntidad() {
        return "{" + "codigo=" + codigo + ", nombre=" + nombre + '}';
    }

    public String toStringActivarDesactivar() {
        return "{" + "idsOperacion=" + idsOperacion + '}';
    }
}
