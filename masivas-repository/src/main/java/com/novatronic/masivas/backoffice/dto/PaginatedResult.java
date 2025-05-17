/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.novatronic.masivas.backoffice.dto;

import java.util.List;
import lombok.Data;

/**
 *
 * @author obi
 */
@Data
public class PaginatedResult<T> {
    private List<T> contenido;
    private int totalPaginas;

    public PaginatedResult(List<T> contenido, int totalPaginas) {
        this.contenido = contenido;
        this.totalPaginas = totalPaginas;
    }
    // Getters
}
