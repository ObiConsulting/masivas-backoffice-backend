/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.novatronic.masivas.backoffice.dto;

import java.io.Serializable;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author obi Consulting
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilidadIntradiaDTO implements Serializable{

    private String codFacilidad;
    private String descripcion;
    private String codConcepto;
    private String codMoneda;
    private String descMoneda;
    private BigInteger codEstado;
    

}
