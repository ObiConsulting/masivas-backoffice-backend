package com.novatronic.masivas.backoffice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDateTime;
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
public class DetalleConsultaArchivoDirectorioDTO implements Serializable {

    private Long idArchivo;
    private String nombre;
    private String cantidadDeclarado;
    private Long estadoObtencion;
    private Long estadoProcesado;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime fechaObtencion;
}
