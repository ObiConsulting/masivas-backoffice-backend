/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.novatronic.masivas.backoffice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
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

public class TmMonedaDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @NotNull
    @Size(min = 1, max = 2)
    private String codMoneda;
    @Size(max = 30)
    private String descripcion;
    @Size(max = 5)
    private String simbolo;
    private BigInteger codEstado;
    @Size(max = 100)
    private String usuCreacion;
    private Date fecCreacion;
    @Size(max = 100)
    private String usuModificacion;
    private Date fecModificacion;
    
   
    
}
