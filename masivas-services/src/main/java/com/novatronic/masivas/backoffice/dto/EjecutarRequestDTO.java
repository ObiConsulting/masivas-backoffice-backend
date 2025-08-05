package com.novatronic.masivas.backoffice.dto;

import java.util.List;
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
public class EjecutarRequestDTO {

    private Integer id;
    private String codigoServer;
    private String codigoEntidad;
    private String codigoOperacion;
    private String codigoAccion;
    private List<ArchivoDTO> archivos;
}
