package com.novatronic.masivas.backoffice.dto;

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
public class DetalleRegistroArchivoMasivasDTO implements Serializable {

    private Long idArchivo;
    private String nombreArchivo;
    private String cuentaOrigen;
    private String numeroDocumento;
    private String cuentaDestino;
    private Long importe;
    private String tipoTransaccion;
    private LocalDateTime fecha;
    private String codigoRechazo;
    private String motivoRechazo;
    private String estado;

}
