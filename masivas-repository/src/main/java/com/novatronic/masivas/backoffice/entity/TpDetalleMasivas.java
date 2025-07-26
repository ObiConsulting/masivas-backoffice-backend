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
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Obi Consulting
 */
@Entity
@Table(name = "TP_DETALLE_MASIVAS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TpDetalleMasivas extends ModelAudit<String> implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "ID_ARCHIVO_MAS")
    @SequenceGenerator(name = "SEQ_TP_DETALLE_MASIVAS", sequenceName = "SEQ_TP_DETALLE_MASIVAS", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TP_DETALLE_MASIVAS")
    private Long idArchivo;
    @Basic(optional = false)
    @Size(max = 20)
    @Column(name = "CCI_ORIGEN")
    private String cuentaOrigen;
    @Basic(optional = false)
    @Size(max = 12)
    @Column(name = "NUMERO_DOCUMENTO_CLIENTE")
    private String numeroDocumentoCliente;
    @Basic(optional = false)
    @Size(max = 20)
    @Column(name = "CCI_RECEPTOR")
    private String cuentaDestino;
    @Basic(optional = false)
    @Column(name = "MONTO", precision = 15, scale = 0)
    private String importe;
    @Basic(optional = false)
    @Size(max = 1)
    @Column(name = "COD_TRANSACCION_EXTENDIDA")
    private String tipoTransaccion;
    @Transient
//    @Column(name = "COD_TRANSACCION_EXTENDIDA")//00-> Aceptdda, 05->Rechazada,null-> si descresp =null en PROCESO, si es !=vacio sería TimeOut 
    private String estado;
    @Column(name = "FEC_TRANSACCION")
    private LocalDateTime fechaTransaccion;
    @Size(max = 2)
    @Column(name = "COD_RESPUESTA")
    private String codigoRespuesta;
    @Size(max = 7)
    @Column(name = "DESC_RESPUESTA")
    private String mensajeRespuesta;

}
