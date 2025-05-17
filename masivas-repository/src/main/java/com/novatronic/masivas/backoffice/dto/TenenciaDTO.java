/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.novatronic.masivas.backoffice.dto;

import java.math.BigDecimal;
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
public class TenenciaDTO {
    
    private Integer idSubasta;
    
    private Long idTenencia;
    private String codigoInstrumento;
    private String fechaOperacion;
    private String codValor;
    private String codParticipante;
     private String fechaTenencia;
     
    private String nombreTercero;
    private String numDocTercero;
    private String tipoDocTercero;
    private String tipoTenencia;
    private BigDecimal totalValorizado;
    private BigDecimal totalNominal;
    
}
