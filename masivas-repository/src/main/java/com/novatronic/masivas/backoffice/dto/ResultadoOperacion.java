/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.novatronic.masivas.backoffice.dto;

import com.novatronic.masivas.backoffice.util.Constantes;
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
public class ResultadoOperacion {

    private BigDecimal idOperacion;
    private String codigoResultado;
    private String mensajeError;

    public static ResultadoOperacion error(String mensaje) {
        return new ResultadoOperacion(null, Constantes.ERROR_BD, mensaje);
    }
}
