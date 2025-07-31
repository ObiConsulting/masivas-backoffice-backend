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
public class DetalleConsultaArchivoTitularidadDTO implements Serializable {

    private Long idArchivo;
    private String nombre;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime fechaObtencion;
    private String fechaObtencionFormato;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime fechaProcesada;
    private String fechaProcesadaFormato;
    private Long cantidadDeclarado;
    private Long cantidadProcesado;
    private String estado;

    public DetalleConsultaArchivoTitularidadDTO(Long idArchivo, String nombre, LocalDateTime fechaObtencion, LocalDateTime fechaProcesada, Long cantidadDeclarado, Long cantidadProcesado, String estado) {
        this.idArchivo = idArchivo;
        this.nombre = nombre;
        this.fechaObtencion = fechaObtencion;
        this.fechaObtencionFormato = FuncionesUtil.formatearLocalDateTimeToString_conHora(fechaObtencion);
        this.fechaProcesada = fechaProcesada;
        this.fechaProcesadaFormato = FuncionesUtil.formatearLocalDateTimeToString_conHora(fechaProcesada);
        this.cantidadDeclarado = cantidadDeclarado;
        this.cantidadProcesado = cantidadProcesado;
        this.estado = estado;
    }

}
