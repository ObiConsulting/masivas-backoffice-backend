package com.novatronic.masivas.backoffice.dto;

import com.novatronic.masivas.backoffice.util.Constantes;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Obi Consulting
 */
//@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FiltroOperacionRequest {

    private String fechaInicio;
    private String fechaFin;
    private String codConcepto;
    private String codEstado;
    private String codEntidadDestino;
    private String codEntidadOrigen;
    private String codMoneda;
    private String usuario;
    private Integer numeroPagina;
    private Integer registrosPorPagina;
    private String campoOrdenar;
    private String sentidoOrdenar;

    // Campos
    private String tipoOperacion;
    private Long idOperacion;
    private String numOperacionLBTR;
    private String numReferenciaBCRP;

    private String codFacilidad;
    // DescargarReporte
    private String pantallaOrigen;

    public String getCodFacilidad() {
        return codFacilidad != null ? codFacilidad : "";
    }

    public String getNumReferenciaBCRP() {
        return numReferenciaBCRP != null ? numReferenciaBCRP : "";
    }

    public String getUsuario() {
        return usuario;
    }

    public String getNumOperacionLBTR() {
        return numOperacionLBTR != null ? numOperacionLBTR : "";
    }

    public Long getIdOperacion() {
        return idOperacion;
    }

    public String getTipoOperacion() {
        return tipoOperacion != null ? tipoOperacion : "";
    }

    public String getFechaInicio() {
        return fechaInicio != null ? fechaInicio : "";
    }

    public String getFechaFin() {
        return fechaFin != null ? fechaFin : "";
    }

    public String getCodConcepto() {
        return codConcepto != null ? codConcepto : "";
    }

    public String getCodEstado() {
        return codEstado != null ? codEstado : "";
    }

    public String getCodEntidadDestino() {
        return codEntidadDestino != null ? codEntidadDestino : "";
    }

    public String getCodEntidadOrigen() {
        return codEntidadOrigen != null ? codEntidadOrigen : "";
    }

    public String getCodMoneda() {
        return codMoneda != null ? codMoneda : "";
    }

    public Integer getNumeroPagina() {
        return numeroPagina != null ? numeroPagina : Constantes.PAGINA_ACTUAL_DEFAULT;
    }

    public Integer getRegistrosPorPagina() {
        return registrosPorPagina != null ? registrosPorPagina : Constantes.TAMANHO_PAGINA_DEFAULT;
    }

    public String getCampoOrdenar() {
        return campoOrdenar != null ? campoOrdenar : "";
    }

    public String getSentidoOrdenar() {
        return sentidoOrdenar != null ? sentidoOrdenar : "";
    }

    public String getPantallaOrigen() {
        return pantallaOrigen != null ? pantallaOrigen : "";
    }

}
