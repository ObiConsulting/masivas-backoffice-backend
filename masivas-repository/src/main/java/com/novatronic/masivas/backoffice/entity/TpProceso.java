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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Obi Consulting
 */
@Entity
@Table(name = "TP_PROCESO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TpProceso extends ModelAudit<String> implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "ID_PROCESO")
    @SequenceGenerator(name = "SEQ_TP_PROCESO", sequenceName = "SEQ_TP_PROCESO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TP_PROCESO")
    private Long idProceso;
    @Basic(optional = false)
    @Size(max = 20)
    @Column(name = "COD_SERVER")
    private String codServer;
    @Basic(optional = false)
    @Size(max = 20)
    @Column(name = "COD_ENTIDAD")
    private String codigoEntidad;
    @Basic(optional = false)
    @Size(max = 3)
    @Column(name = "COD_OPERACION")
    private String codigoOperacion;
    @Basic(optional = false)
    @Size(max = 3)
    @Column(name = "COD_ACCION")
    private String codigoAccion;
    @Basic(optional = false)
    @Size(max = 50)
    @Column(name = "HORARIO")
    private String horario;

}
