package com.novatronic.masivas.backoffice.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Obi Consulting
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DetalleConsultaRutaDTO implements Serializable {

    private Long idRuta;
    private String categoriaDirectorio;
    private String tipoArchivo;
    private String ruta;

}
