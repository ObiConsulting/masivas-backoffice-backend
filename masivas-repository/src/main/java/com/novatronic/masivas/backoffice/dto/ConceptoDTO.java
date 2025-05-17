/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.novatronic.masivas.backoffice.dto;

import java.io.Serializable;
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
public class ConceptoDTO implements Serializable{

    private String codConcepto;
    private String desConcepto;
    private String codTransaccion;
    private int codPanel;
    private String codMoneda;
    private String descripcionMoneda;
    private String simbolo;

}
