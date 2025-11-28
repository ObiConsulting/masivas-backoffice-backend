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
@ToString
public class DetalleConsultaArchivoMasivasDTO implements Serializable {

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
    private BigDecimal montoProcesadoDolar;
    private BigDecimal montoProcesadoSol;
    private BigDecimal montoRechazadoDolar;
    private BigDecimal montoRechazadoSol;
    private String estado;
    private boolean flagRespaldar;
    private boolean flagRestaurar;

    public DetalleConsultaArchivoMasivasDTO(Long idArchivo, String nombre, LocalDateTime fechaObtencion, LocalDateTime fechaProcesada, Long cantidadDeclarado, Long cantidadProcesado,
                                            BigDecimal montoProcesadoDolar, BigDecimal montoProcesadoSol, BigDecimal montoRechazadoDolar, BigDecimal montoRechazadoSol, String estado, boolean flagRespaldar, boolean flagRestaurar) {
        this.idArchivo = idArchivo;
        this.nombre = nombre;
        this.fechaObtencion = fechaObtencion;
        this.fechaObtencionFormato = FuncionesUtil.formatearLocalDateTimeToString_conHora(fechaObtencion);
        this.fechaProcesada = fechaProcesada;
        this.fechaProcesadaFormato = FuncionesUtil.formatearLocalDateTimeToString_conHora(fechaProcesada);
        this.cantidadDeclarado = cantidadDeclarado;
        this.cantidadProcesado = cantidadProcesado;
        this.montoProcesadoDolar = FuncionesUtil.convertirABigDecimal(montoProcesadoDolar).movePointLeft(2);
        this.montoProcesadoSol = FuncionesUtil.convertirABigDecimal(montoProcesadoSol).movePointLeft(2);
        this.montoRechazadoDolar = FuncionesUtil.convertirABigDecimal(montoRechazadoDolar).movePointLeft(2);
        this.montoRechazadoSol = FuncionesUtil.convertirABigDecimal(montoRechazadoSol).movePointLeft(2);
        this.estado = estado;
        this.flagRespaldar = flagRespaldar;
        this.flagRestaurar = flagRestaurar;
    }

}
