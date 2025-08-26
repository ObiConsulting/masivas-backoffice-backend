package com.novatronic.masivas.backoffice.dto;

import java.time.LocalDateTime;
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
public class EjecutarResponseDTO {

    private String codigoRespuesta;
    private String mensajeRespuesta;
    private List<ArchivoResponseDTO> archivos;
}
