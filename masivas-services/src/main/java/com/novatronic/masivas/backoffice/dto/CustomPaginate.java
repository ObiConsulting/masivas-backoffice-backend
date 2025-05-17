package com.novatronic.masivas.backoffice.dto;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Obi Consulting
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomPaginate<T> implements Serializable {

    private Integer totalPaginas;
    private Integer totalRegistros;
    private List<T> contenido;

}
