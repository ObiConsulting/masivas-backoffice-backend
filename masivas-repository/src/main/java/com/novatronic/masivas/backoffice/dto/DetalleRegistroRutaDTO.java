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
public class DetalleRegistroRutaDTO implements Serializable {

    private Long idRuta;
    private String categoriaDirectorio;
    private String tipoArchivo;
    private String ruta;

    //Campos de Auditoria
    private String usuCreacion;
    @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private LocalDateTime fecCreacion;
    private String usuModificacion;
    @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private LocalDateTime fecModificacion;
}
