package com.novatronic.masivas.backoffice.dto;

import com.novatronic.masivas.backoffice.util.ConstantesServices;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Obi Consulting
 */
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FiltroMasivasRequest {

    //IDs
    private Long idEntidad;
    private Long idGrupoParametro;

    private String codigo;
    private String estado;

    //Entidad
    private String nombre;

    //Grupo Parametro
    private String descripcion;

    //Campos Generales
    private String campoOrdenar;
    private String sentidoOrdenar;
    private Integer numeroPagina;
    private Integer registrosPorPagina;

    public Integer getNumeroPagina() {
        return numeroPagina != null ? Math.max(numeroPagina - 1, 0) : ConstantesServices.PAGINA_INICIAL;
    }

    public Integer getRegistrosPorPagina() {
        return registrosPorPagina != null ? registrosPorPagina : ConstantesServices.TAMANO_PAGINA_10;
    }

    public String getSentidoOrdenar() {
        return sentidoOrdenar != null ? sentidoOrdenar : "DESC";
    }

    public String getCampoOrdenar() {
        return campoOrdenar != null ? campoOrdenar : "";
    }

    public String getCodigo() {
        return codigo != null ? codigo : "";
    }

    public String getNombre() {
        return nombre != null ? nombre : "";
    }

    public String getDescripcion() {
        return descripcion != null ? descripcion : "";
    }

    public String getEstado() {
        return estado != null ? estado : "";
    }

    public Long getIdEntidad() {
        return idEntidad;
    }

    public Long getIdGrupoParametro() {
        return idGrupoParametro;
    }
}
