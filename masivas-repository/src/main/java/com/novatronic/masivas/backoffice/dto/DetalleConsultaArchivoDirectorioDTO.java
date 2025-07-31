package com.novatronic.masivas.backoffice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.novatronic.masivas.backoffice.util.FuncionesUtil;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Obi Consulting
 */
@Data
@NoArgsConstructor
//@AllArgsConstructor
@ToString
public class DetalleConsultaArchivoDirectorioDTO implements Serializable {

    private Long idArchivo;
    private String nombre;
    private Long cantidadDeclarado;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime fechaObtencion;
    private String fechaObtencionFormato;
    private String codigoEstado;

    public DetalleConsultaArchivoDirectorioDTO(Long idArchivo, String nombre, Long cantidadDeclarado, LocalDateTime fechaObtencion, String codigoEstado) {
        this.idArchivo = idArchivo;
        this.nombre = nombre;
        this.cantidadDeclarado = cantidadDeclarado;
        this.fechaObtencion = fechaObtencion;
        this.fechaObtencionFormato = FuncionesUtil.formatearLocalDateTimeToString_sinnHora(fechaObtencion);
        this.codigoEstado = codigoEstado;
    }

}
