package com.novatronic.masivas.backoffice.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Obi Consulting
 */
@Entity
@Table(name = "TP_ENTIDAD")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TpEntidad extends ModelAudit<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_ENTIDAD")
    private BigDecimal idEntidad;
    @Basic(optional = false)
    @Size(max = 20)
    @Column(name = "COD_ENTIDAD")
    private String codEntidad;
    @Basic(optional = false)
    @Size(max = 100)
    @Column(name = "DESC_ENTIDAD")
    private String descEntidad;
    @Basic(optional = false)
    @Size(max = 1)
    @Column(name = "ESTADO")
    private String estado;
    @Basic(optional = false)
    @JoinColumn(name = "ID_PERFIL", referencedColumnName = "ID_PERFIL")
    @ManyToOne
    private TpPerfil idPerfil;

}
