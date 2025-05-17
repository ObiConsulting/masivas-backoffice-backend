package com.novatronic.masivas.backoffice.dto;

import com.novatronic.masivas.backoffice.util.Constantes;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Obi Consulting
 */
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ResultadoOperacionConsulta<T> {

    private String codigoResultado;
    private String mensajeError;
    private Integer totalPaginas;
    private Integer totalRegistros;

    private List<T> contenido; // Para varios
    private T objeto;          // Para un solo resultado

    public static ResultadoOperacionConsulta error(String mensaje) {
        return new ResultadoOperacionConsulta(Constantes.ERROR_SP_BD, mensaje, null, null, Collections.emptyList(), null);
    }
}
