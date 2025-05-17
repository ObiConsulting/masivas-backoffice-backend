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
public class DetalleRegistroRINDTO {

    private Long idOperacion;
    private String numRefLbtr;
    private String numRefBcrp;
    private String concepto;
    private String fecha;
    private String codInstrumento;
    private String moneda;
    private BigDecimal monto;
    private String codEntidadOrigen;
    private String estado;
    private String codEstado;
    
    
    private String fechaCreacion;
    private String fechaModificacion;
    private String usuarioCreacion;
    private String usuarioModificacion;
    
    List<TenenciaDTO> tenencias;

}
