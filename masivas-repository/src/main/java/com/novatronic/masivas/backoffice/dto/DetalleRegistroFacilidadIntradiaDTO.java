package com.novatronic.masivas.backoffice.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author obi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleRegistroFacilidadIntradiaDTO {

    private Long idOperacion;
    private String fecha;
    private String numRefLbtr;
    private String numRefBcrp;
    private String codFacilidad;
    private String codMoneda;
    private BigDecimal monto;
    private String estado;
    private String codEstado;
    private BigDecimal tipoCambio;
  
    
    private String codConcepto;
    private String numCuentaDestino;
    private String codEntidadDestino;
    private String numCuentaOrigen;

    private String fechaCreacion;
    private String fechaModificacion;
    private String usuarioCreacion;
    private String usuarioModificacion;

}
