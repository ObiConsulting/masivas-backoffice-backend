package com.novatronic.masivas.backoffice.dto;

import com.novatronic.masivas.backoffice.util.ConstantesServices;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Obi Consulting
 */
//@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FiltroMasivasRequest {

    private String codigoEntidad;
    private String descripcionEntidad;
    private String estado;
    private String campoOrdenamiento;
    private String sentidoOrdenamiento;
    private Integer numeroPagina;
    private Integer registrosPorPagina;

    public Integer getNumeroPagina() {
        return numeroPagina != null ? Math.max(numeroPagina - 1, 0) : ConstantesServices.PAGINA_INICIAL;
    }

    public Integer getRegistrosPorPagina() {
        return registrosPorPagina != null ? registrosPorPagina : ConstantesServices.TAMANO_PAGINA_10;
    }

    public String getSentidoOrdenamiento() {
        return sentidoOrdenamiento != null ? sentidoOrdenamiento : "DESC";
    }

    public String getCampoOrdenamiento() {
        return campoOrdenamiento != null ? campoOrdenamiento : "";
    }

    public String getCodigoEntidad() {
        return codigoEntidad != null ? codigoEntidad : "";
    }

    public String getDescripcionEntidad() {
        return descripcionEntidad != null ? descripcionEntidad : "";
    }

    public String getEstado() {
        return estado != null ? estado : "";
    }

}
