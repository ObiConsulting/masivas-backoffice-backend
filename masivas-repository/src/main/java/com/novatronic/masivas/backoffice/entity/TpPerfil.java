/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.novatronic.masivas.backoffice.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Raúl Vargas
 */
@Entity
@Table(name = "TP_PERFIL")
@NamedQueries({
    @NamedQuery(name = "TpPerfil.findAll", query = "SELECT t FROM TpPerfil t"),
    @NamedQuery(name = "TpPerfil.findByIdPerfil", query = "SELECT t FROM TpPerfil t WHERE t.idPerfil = :idPerfil"),
    @NamedQuery(name = "TpPerfil.findByCodPerfil", query = "SELECT t FROM TpPerfil t WHERE t.codPerfil = :codPerfil"),
    @NamedQuery(name = "TpPerfil.findByDescPerfil", query = "SELECT t FROM TpPerfil t WHERE t.descPerfil = :descPerfil"),
    @NamedQuery(name = "TpPerfil.findByEstado", query = "SELECT t FROM TpPerfil t WHERE t.estado = :estado"),
    @NamedQuery(name = "TpPerfil.findByFecCreacion", query = "SELECT t FROM TpPerfil t WHERE t.fecCreacion = :fecCreacion"),
    @NamedQuery(name = "TpPerfil.findByFecModificacion", query = "SELECT t FROM TpPerfil t WHERE t.fecModificacion = :fecModificacion"),
    @NamedQuery(name = "TpPerfil.findByUsuCreacion", query = "SELECT t FROM TpPerfil t WHERE t.usuCreacion = :usuCreacion"),
    @NamedQuery(name = "TpPerfil.findByUsuModificacion", query = "SELECT t FROM TpPerfil t WHERE t.usuModificacion = :usuModificacion")})
public class TpPerfil implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_PERFIL")
    private BigDecimal idPerfil;
    @Basic(optional = false)
    @Column(name = "COD_PERFIL")
    private String codPerfil;
    @Basic(optional = false)
    @Column(name = "DESC_PERFIL")
    private String descPerfil;
    @Basic(optional = false)
    @Column(name = "ESTADO")
    private String estado;
    @Basic(optional = false)
    @Column(name = "FEC_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreacion;
    @Column(name = "FEC_MODIFICACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecModificacion;
    @Basic(optional = false)
    @Column(name = "USU_CREACION")
    private String usuCreacion;
    @Column(name = "USU_MODIFICACION")
    private String usuModificacion;
    @OneToMany(mappedBy = "idPerfil")
    private Collection<TpEntidad> tpEntidadCollection;

    public TpPerfil() {
    }

    public TpPerfil(BigDecimal idPerfil) {
        this.idPerfil = idPerfil;
    }

    public TpPerfil(BigDecimal idPerfil, String codPerfil, String descPerfil, String estado, Date fecCreacion, String usuCreacion) {
        this.idPerfil = idPerfil;
        this.codPerfil = codPerfil;
        this.descPerfil = descPerfil;
        this.estado = estado;
        this.fecCreacion = fecCreacion;
        this.usuCreacion = usuCreacion;
    }

    public BigDecimal getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(BigDecimal idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getCodPerfil() {
        return codPerfil;
    }

    public void setCodPerfil(String codPerfil) {
        this.codPerfil = codPerfil;
    }

    public String getDescPerfil() {
        return descPerfil;
    }

    public void setDescPerfil(String descPerfil) {
        this.descPerfil = descPerfil;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFecCreacion() {
        return fecCreacion;
    }

    public void setFecCreacion(Date fecCreacion) {
        this.fecCreacion = fecCreacion;
    }

    public Date getFecModificacion() {
        return fecModificacion;
    }

    public void setFecModificacion(Date fecModificacion) {
        this.fecModificacion = fecModificacion;
    }

    public String getUsuCreacion() {
        return usuCreacion;
    }

    public void setUsuCreacion(String usuCreacion) {
        this.usuCreacion = usuCreacion;
    }

    public String getUsuModificacion() {
        return usuModificacion;
    }

    public void setUsuModificacion(String usuModificacion) {
        this.usuModificacion = usuModificacion;
    }

    public Collection<TpEntidad> getTpEntidadCollection() {
        return tpEntidadCollection;
    }

    public void setTpEntidadCollection(Collection<TpEntidad> tpEntidadCollection) {
        this.tpEntidadCollection = tpEntidadCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPerfil != null ? idPerfil.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TpPerfil)) {
            return false;
        }
        TpPerfil other = (TpPerfil) object;
        if ((this.idPerfil == null && other.idPerfil != null) || (this.idPerfil != null && !this.idPerfil.equals(other.idPerfil))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.novatronic.masivas.backoffice.entity.TpPerfil[ idPerfil=" + idPerfil + " ]";
    }
    
}
