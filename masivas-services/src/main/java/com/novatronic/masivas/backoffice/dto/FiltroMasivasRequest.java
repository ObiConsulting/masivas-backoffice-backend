package com.novatronic.masivas.backoffice.dto;

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
