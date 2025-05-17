package com.novatronic.masivas.backoffice.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author obi
 */
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WsRequestConfirmacionDTO extends WsRequestDTO {

    private String codigos;
    private String numReferencia;
    private String numOperacionLBTR;
    
    private String fecha;
    private String codInstrumento;

    public String getCodInstrumento() {
        return codInstrumento;
    }
    
    public String getFecha() {
        return fecha;
    }
    
    public String getNumOperacionLBTR() {
        return numOperacionLBTR;
    }
    
    public String getNumReferencia() {
        return numReferencia;
    }

    public String getCodigos() {
        return codigos;
    }
    
}
