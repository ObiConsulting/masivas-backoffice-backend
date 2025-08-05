package com.novatronic.masivas.backoffice.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Obi Consulting
 */
@Entity
@Table(name = "TP_ARCHIVO_MAS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TpArchivoMasivas extends ModelAudit<String> implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "ID_ARCHIVO_MAS")
    @SequenceGenerator(name = "SEQ_TP_ARCHIVO_MAS", sequenceName = "SEQ_TP_ARCHIVO_MAS", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TP_ARCHIVO_MAS")
    private Long idArchivo;
    @Column(name = "TRACE_ARCHIVO", precision = 6, scale = 0)
    private Long trace;
    @Size(max = 50)
    @Column(name = "NOMBRE_CLIENTE")
    private String nombre;
    @Size(max = 50)
    @Column(name = "NOMBRE_CCE")
    private String nombreCCE;
//    @Column(name = "FEC_OBTENCION_CCE")
    @Column(name = "FEC_OBTENCION_CLIENTE")
    private LocalDateTime fechaObtencion;
    @Column(name = "FEC_PROCESADO_CCE")
    private LocalDateTime fechaProcesada;
    @Column(name = "CANT_DECLARADO_CCE")
    private Long cantidadDeclarado;
    @Column(name = "CANT_PROCESADO_CCE")
    private Long cantidadProcesado;
    @Column(name = "ESTADO_OBTENCION_CLIENTE")
    private Long estadoObtenidoCliente;
    @Column(name = "ESTADO_PROCESADO_CLIENTE")
    private Long estadoProcesadoCliente;
    @Column(name = "ESTADO_ENVIO_CCE")
    private Long estadoEnviadoCCE;
    @Column(name = "ESTADO_OBTENCION_CCE")
    private Long estadoObtenidoCCE;
    @Column(name = "ESTADO_PROCESADO_CCE")
    private Long estadoProcesadoCCE;
    @Column(name = "ESTADO_ENVIO_CLIENTE")
    private Long estadoEnviadoCliente;
    @Column(name = "MONTO_PROCESADO_CCE", precision = 16, scale = 0)
    private Long montoProcesado;
    @Column(name = "MONTO_RECHAZADO", precision = 16, scale = 0)
    private Long montoRechazado;
    @Column(name = "ESTADO_FISICO")
    private Long estadoFisico;
    @Column(name = "FEC_MODIFICACION_FISICA")
    private LocalDateTime fechaModificacionFisica;

}
