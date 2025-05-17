/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.novatronic.masivas.backoffice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author obi
 */
@Entity
@Table(name = "TR_CONCEPTO_TRANSACCION")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrConceptoTransaccion extends ModelAudit<String> implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TrConceptoTransaccionPK trConceptoTransaccionPK;
    @Size(max = 11)
    @Column(name = "COD_ESTADO")
    private String codEstado;
    @Column(name = "COD_PANEL")
    private Short codPanel;
    
    @JoinColumn(name = "COD_CONCEPTO", referencedColumnName = "COD_CONCEPTO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TpConceptoLiquidacionBcrp tpConceptoLiquidacionBcrp;
    @JoinColumn(name = "COD_ENTIDAD_FINANCIERA", referencedColumnName = "COD_ENTIDAD_FINANCIERA", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TpEntidadFinancieraBcrp tpEntidadFinancieraBcrp;
    @JoinColumn(name = "COD_TRANSACCION", referencedColumnName = "COD_TRANSACCION", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TpTransaccion tpTransaccion;

    
    
}
