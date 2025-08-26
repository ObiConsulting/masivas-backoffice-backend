package com.novatronic.masivas.backoffice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.novatronic.masivas.backoffice.util.FuncionesUtil;
import java.io.Serializable;
import java.math.BigDecimal;
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
public class DetalleRegistroArchivoMasivasDTO implements Serializable {

    private Long idArchivo;
    private String nombreArchivo;
    private String cuentaOrigen;
    private String numeroDocumento;
    private String cuentaDestino;
    private BigDecimal importe;
    private String moneda;
    private String tipoTransaccion;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime fecha;
    private String fechaFormato;
    private String codigoRechazo;
    private String motivoRechazo;
    private String estado;

    public DetalleRegistroArchivoMasivasDTO(Long idArchivo, String nombreArchivo, String cuentaOrigen, String numeroDocumento, String cuentaDestino, BigDecimal importe, String moneda, String tipoTransaccion,
            LocalDateTime fecha, String codigoRechazo, String motivoRechazo, String estado) {
        this.idArchivo = idArchivo;
        this.nombreArchivo = nombreArchivo;
        this.cuentaOrigen = cuentaOrigen;
        this.numeroDocumento = numeroDocumento;
        this.cuentaDestino = cuentaDestino;
        this.importe = importe.movePointLeft(2);
        this.moneda = moneda;
        this.tipoTransaccion = tipoTransaccion;
        this.fecha = fecha;
        this.fechaFormato = FuncionesUtil.formatearLocalDateTimeToString_conHora(fecha);
        this.codigoRechazo = codigoRechazo;
        this.motivoRechazo = motivoRechazo;
        this.estado = estado;
    }

}
