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
@Table(name = "TP_ARCHIVO_TIT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TpArchivoTitularidad extends ModelAudit<String> implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "ID_ARCHIVO_TIT")
    @SequenceGenerator(name = "SEQ_TP_ARCHIVO_TIT", sequenceName = "SEQ_TP_ARCHIVO_TIT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TP_ARCHIVO_TIT")
    private Long idArchivo;
    @Size(max = 50)
    @Column(name = "NOMBRE_CCE")
    private String nombre;
    @Column(name = "FEC_OBTENCION_CCE")
    private LocalDateTime fechaObtencion;
    @Column(name = "FEC_PROCESADO_CCE")
    private LocalDateTime fechaProcesada;
    @Column(name = "CANT_DECLARADO_CCE")
    private Long cantidadDeclarado;
    @Column(name = "CANT_PROCESADO_CCE")
    private Long cantidadProcesado;
    @Column(name = "ESTADO_OBTENCION_CLIENTE")
    private Long estadoObtencionCliente;
//    @Column(name = "ESTADO_PROCESADO_CLIENTE")
    @Column(name = "ESTADO_ENVIO_CCE")
    private Long estadoProcesadoCliente;
    @Column(name = "ESTADO_OBTENCION_CCE")
    private Long estadoObtencionCCE;
//    @Column(name = "ESTADO_PROCESADO_CCE")
    @Column(name = "ESTADO_ENVIO_CLIENTE")
    private Long estadoProcesadoCCE;

}
