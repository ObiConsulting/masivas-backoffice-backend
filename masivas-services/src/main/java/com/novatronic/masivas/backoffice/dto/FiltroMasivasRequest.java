package com.novatronic.masivas.backoffice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Obi Consulting
 */
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FiltroMasivasRequest {

    //IDs
    private Long idEntidad;
    private Long idGrupoParametro;
    private Long idParametro;

    private String codigo;
    private String estado;

    //Entidad
    private String nombre;

    //Grupo Parametro
    private String descripcion;

    //Archivos
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaInicio;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaFin;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime fechaInicioObtencion;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime fechaFinObtencion;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime fechaInicioProcesada;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime fechaFinProcesada;

    //Campos Generales
    private String campoOrdenar;
    private String sentidoOrdenar;
    private Integer numeroPagina;
    private Integer registrosPorPagina;

    public Integer getNumeroPagina() {
        return numeroPagina != null ? Math.max(numeroPagina - 1, 0) : ConstantesServices.PAGINA_INICIAL;
    }

    public Integer getRegistrosPorPagina() {
        return registrosPorPagina != null ? registrosPorPagina : ConstantesServices.TAMANO_PAGINA_10;
    }

    public String getSentidoOrdenar() {
        return sentidoOrdenar != null ? sentidoOrdenar : "DESC";
    }

    public String getCampoOrdenar() {
        return campoOrdenar != null ? campoOrdenar : "";
    }

    public String getCodigo() {
        return codigo != null ? codigo : "";
    }

    public String getNombre() {
        return nombre != null ? nombre : "";
    }

    public String getDescripcion() {
        return descripcion != null ? descripcion : "";
    }

    public String getEstado() {
        return estado != null ? estado : "";
    }

    public Long getIdEntidad() {
        return idEntidad;
    }

    public Long getIdGrupoParametro() {
        return idGrupoParametro;
    }

    public Long getIdParametro() {
        return idParametro;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public LocalDateTime getFechaInicioObtencion() {
        return fechaInicioObtencion;
    }

    public LocalDateTime getFechaFinObtencion() {
        return fechaFinObtencion;
    }

    public LocalDateTime getFechaInicioProcesada() {
        return fechaInicioProcesada;
    }

    public LocalDateTime getFechaFinProcesada() {
        return fechaFinProcesada;
    }

}
