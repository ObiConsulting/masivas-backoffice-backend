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
@Table(name = "TP_RUTA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TpRuta extends ModelAudit<String> implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "ID_RUTA")
    @SequenceGenerator(name = "SEQ_TP_RUTA", sequenceName = "SEQ_TP_RUTA", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TP_RUTA")
    private Long idRuta;
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
    @Size(max = 100)
    @Column(name = "RUTA")
    private String ruta;
    @Basic(optional = false)
    @Size(max = 1)
    @Column(name = "ESTADO")
    private String estado;

}
