/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.novatronic.masivas.backoffice.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author obi
 */
@Entity
@Table(name = "TP_CONCEPTO_LIQUIDACION_BCRP")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TpConceptoLiquidacionBcrp extends ModelAudit<String> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "COD_CONCEPTO")
    private String codConcepto;
    @Size(max = 150)
    @Column(name = "DES_CONCEPTO")
    private String desConcepto;
    @Size(max = 11)
    @Column(name = "COD_ESTADO")
    private String codEstado;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "MONTO_MINIMO")
    private BigDecimal montoMinimo;
    @Size(max = 1)
    @Column(name = "ORIGEN")
    private String origen;
    @Column(name = "PRIORIDAD")
    private Long prioridad;
    @Size(max = 2)
    @Column(name = "SERVICIO")
    private String servicio;
    @Size(max = 255)
    @Column(name = "SERVICIO_BCRP")
    private String servicioBcrp;
    
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tpConceptoLiquidacionBcrp")
    private Collection<TrConceptoTransaccion> trConceptoTransaccionCollection;

    
}
