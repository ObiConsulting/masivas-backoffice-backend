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
    private Long idRuta;

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

    //Ruta
    private String codTipoArchivo;
    private String codCategoriaDirectorio;

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

    public Long getIdRuta() {
        return idRuta;
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

    public String getCodTipoArchivo() {
        return codTipoArchivo;
    }

    public String getCodCategoriaDirectorio() {
        return codCategoriaDirectorio;
    }

    public String toStringGrupoParametro() {
        return "{" + "codigo=" + codigo + ", estado=" + estado + '}';
    }

    public String toStringParametro() {
        return "{" + "idGrupoParametro=" + idGrupoParametro + ", codigo=" + codigo + ", estado=" + estado + '}';
    }

    public String toStringEntidad() {
        return "{" + "codigo=" + codigo + ", nombre=" + nombre + ", estado=" + estado + '}';
    }

    public String toStringArchivoDirectorio() {
        return "{" + "fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + ", estado=" + estado + '}';
    }

    public String toStringArchivoMasivas() {
        return "{" + "fechaInicioObtencion=" + fechaInicioObtencion + ", fechaFinObtencion=" + fechaFinObtencion + "fechaInicioProcesada=" + fechaInicioProcesada
                + ", fechaFinProcesada=" + fechaFinProcesada + ", estado=" + estado + '}';
    }

    public String toStringArchivoTitularidad() {
        return "{" + "fechaInicioObtencion=" + fechaInicioObtencion + ", fechaFinObtencion=" + fechaFinObtencion + "fechaInicioProcesada=" + fechaInicioProcesada
                + ", fechaFinProcesada=" + fechaFinProcesada + ", estado=" + estado + '}';
    }

    public String toStringRuta() {
        return "{" + "codTipoArchivo=" + codTipoArchivo + ", codCategoriaDirectorio=" + codCategoriaDirectorio + '}';
    }

    public String toStringGrupoParametroObtener() {
        return "{" + "idGrupoParametro=" + idGrupoParametro + '}';
    }

    public String toStringParametroObtener() {
        return "{" + "idParametro=" + idParametro + '}';
    }

    public String toStringEntidadObtener() {
        return "{" + "idEntidad=" + idEntidad + '}';
    }

    public String toStringRutaObtener() {
        return "{" + "idRuta=" + idRuta + '}';
    }
}
