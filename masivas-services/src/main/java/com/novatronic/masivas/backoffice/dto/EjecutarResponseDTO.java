package com.novatronic.masivas.backoffice.dto;

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
public class EjecutarResponseDTO {

    private String nombre;
    private String nombreCliente;
    private String nombreAutorizador;
    private String tipoExtension;
    private String appCode;
    private Long monto;
    private Long montoProcesado;
    private String hashArchivoOriginal;
    private String hashArchivoProcesado;
    private Integer trace;
    private Long cantidadRegistrosDeclarada;
    private Long cantidadRegistrosProcesada;
    private Long tamanio;
    private Long estado;
    private Integer reintento;
    private LocalDateTime fechaProceso;
    private boolean detalleMasivasInconsistente;
}
