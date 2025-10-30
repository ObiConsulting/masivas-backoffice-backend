package com.novatronic.masivas.backoffice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.novatronic.masivas.backoffice.util.Constantes;
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
    private Long idAplicacion;

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
    private String tipoAccion;
    private Long idArchivo;

    //Ruta
    private String codTipoArchivo;
    private String codCategoriaDirectorio;

    //Scheduler
    private String codOperacion;

    //Detalle Masivas
    private String nombreArchivo;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaObtencion;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaProcesada;
    private String cuentaOrigen;
    private String cuentaDestino;
    private String motivoRechazo;
    private String tipoTransaccion;

    //Campos Generales
    private String campoOrdenar;
    private String sentidoOrdenar;
    private Integer numeroPagina;
    private Integer registrosPorPagina;

    //Reporte
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fecha;
    private String moneda;

    public Integer getNumeroPagina() {
        return numeroPagina != null ? Math.max(numeroPagina - 1, 0) : Constantes.PAGINA_INICIAL;
    }

    public Integer getRegistrosPorPagina() {
        return registrosPorPagina != null ? registrosPorPagina : Constantes.TAMANO_PAGINA_10;
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

    public String getEstadoSearch() {
        return estado == null || estado.trim().isEmpty() ? null : estado;
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

    public String getCodTipoArchivoSearch() {
        return codTipoArchivo == null || codTipoArchivo.trim().isEmpty() ? null : codTipoArchivo;
    }

    public String getCodCategoriaDirectorio() {
        return codCategoriaDirectorio;
    }

    public String getCodCategoriaDirectorioSearch() {
        return codCategoriaDirectorio == null || codCategoriaDirectorio.trim().isEmpty() ? null : codCategoriaDirectorio;
    }

    public String getCodOperacion() {
        return codOperacion != null ? codOperacion : "";
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Long getIdAplicacion() {
        return idAplicacion;
    }

    public String getNombreArchivo() {
        return nombreArchivo != null ? nombreArchivo : "";
    }

    public LocalDate getFechaObtencion() {
        return fechaObtencion;
    }

    public LocalDate getFechaProcesada() {
        return fechaProcesada;
    }

    public String getCuentaOrigen() {
        return cuentaOrigen != null ? cuentaOrigen : "";
    }

    public String getCuentaDestino() {
        return cuentaDestino != null ? cuentaDestino : "";
    }

    public String getMotivoRechazo() {
        return motivoRechazo != null ? motivoRechazo : "";
    }

    public String getMotivoRechazoSearch() {
        return motivoRechazo == null || motivoRechazo.trim().isEmpty() ? null : motivoRechazo;
    }

    public String getTipoTransaccion() {
        return tipoTransaccion != null ? tipoTransaccion : "";
    }

    public String getTipoTransaccionSearch() {
        return tipoTransaccion == null || tipoTransaccion.trim().isEmpty() ? null : tipoTransaccion;
    }

    public String getTipoAccion() {
        return tipoAccion != null ? tipoAccion : "";
    }

    public Long getIdArchivo() {
        return idArchivo;
    }

    public String getMoneda() {
        return moneda != null ? moneda : "";
    }

    public String getMonedaSearch() {
        return moneda == null || moneda.trim().isEmpty() ? null : moneda;
    }

    public String toStringGrupoParametro() {
        return "{" + "codigo=" + codigo + ", estado=" + estado + '}';
    }

    public String toStringParametro() {
        return "{" + "idGrupoParametro=" + idGrupoParametro + ", codigo=" + codigo + ", estado=" + estado + '}';
    }

    public String toStringEntidadAplicacion() {
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

    public String toStringSchedulerObtener() {
        return "{" + "codOperacion=" + codOperacion + '}';
    }

    public String toStringReporteCierreObtener() {
        return "{" + "fecha=" + fecha + '}';
    }

    public String toStringReporteConsolidado() {
        return "{" + "fecha=" + fecha + ", moneda=" + moneda + '}';
    }

    public String toStringAplicacionObtener() {
        return "{" + "idAplicacion=" + idAplicacion + '}';
    }

    public String toStringRespaldarResturarObtener() {
        return "{" + "idArchivo=" + idArchivo + '}';
    }

    public String toStringDetalleMasivas() {
        return "{" + "nombreArchivo=" + nombreArchivo + ", fechaObtencion=" + fechaObtencion + "fechaProcesada=" + fechaProcesada + ", cuentaOrigen=" + cuentaOrigen
                + ", cuentaDestino=" + cuentaDestino + ", motivoRechazo=" + motivoRechazo + ", tipoTransaccion=" + tipoTransaccion + '}';
    }
}
