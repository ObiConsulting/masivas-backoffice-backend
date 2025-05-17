package com.novatronic.masivas.backoffice.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author obi
 */
@Data
@NoArgsConstructor
public class TpEntidadFinancieraBcrpDTO  implements Serializable{

    private String codigo;
    private String descripcion;
    //private String nombreLargo;
    private List<TpCuentaCorrienteBcrpDTO> cuentasCorrientes;

    public TpEntidadFinancieraBcrpDTO(String codigo, String nombreLargo, List<TpCuentaCorrienteBcrpDTO> cuentasCorrientes) {
        this.codigo = (codigo != null) ? codigo.trim() : "";
        String nombre = (nombreLargo != null) ? nombreLargo.trim() : "";
        this.descripcion = this.codigo + " - " + nombre;
        this.cuentasCorrientes = cuentasCorrientes != null ? cuentasCorrientes : new ArrayList<>();
    }

}
