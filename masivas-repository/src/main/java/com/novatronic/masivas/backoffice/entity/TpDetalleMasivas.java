package com.novatronic.masivas.backoffice.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.math.BigDecimal;
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
    @Size(max = 64)
    @Column(name = "CCI_ORIGEN_CIFRADO")
    private String cuentaOrigen;
    @Basic(optional = false)
    @Size(max = 56)
    @Column(name = "DOCUMENTO_CLIENTE_CIFRADO")
    private String numeroDocumentoCliente;
    @Size(max = 64)
    @Column(name = "CCI_RECEPTOR_CIFRADO")
    private String cuentaDestino;
    @Basic(optional = false)
    @Column(name = "MONTO", precision = 12, scale = 2)
    private BigDecimal importe;
    @Basic(optional = false)
    @Size(max = 3)
    @Column(name = "COD_MONEDA")
    private String moneda;
    @Basic(optional = false)
    @Size(max = 1)
    @Column(name = "COD_TRANSACCION_EXTENDIDA")
    private String codTipoTransaccion;
    @Column(name = "FEC_TRANSACCION")
    private LocalDateTime fechaTransaccion;
    //00-> Aceptada, 05->Rechazada, null-> si DESC_RESPUESTA = null en PROCESO, si es !=vacio sería TimeOut 
    @Size(max = 2)
    @Column(name = "COD_RESPUESTA")
    private String codigoRespuesta;
    @Size(max = 7)
    @Column(name = "DESC_RESPUESTA")
    private String mensajeRespuesta;
    @Basic(optional = false)
    @Size(max = 4)
    @Column(name = "COD_ENTIDAD_DESTINO")
    private String codEntidadDestino;
    @Basic(optional = false)
    @Size(max = 44)
    @Column(name = "CCI_ORIGEN_HASH")
    private String cuentaOrigenHash;
    @Basic(optional = false)
    @Size(max = 44)
    @Column(name = "CCI_RECEPTOR_HASH")
    private String cuentaDestinoHash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ARCHIVO_MAS", referencedColumnName = "ID_ARCHIVO_MAS", insertable = false, updatable = false)
    private TpArchivoMasivas archivoMasivas;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COD_ENTIDAD_DESTINO", referencedColumnName = "COD_ENTIDAD", insertable = false, updatable = false)
    private TpEntidad entidad;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COD_TRANSACCION_EXTENDIDA", referencedColumnName = "COD_DETALLE_PARAMETRO", insertable = false, updatable = false)
    private TpParametro tipoTransaccion;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COD_MONEDA", referencedColumnName = "COD_DETALLE_PARAMETRO", insertable = false, updatable = false)
    private TpParametro monedaDesc;
}
