/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.novatronic.masivas.backoffice.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author obi
 */
@Entity
@Table(name = "TP_CUENTA_CORRIENTE_BCRP")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TpCuentaCorrienteBcrp extends ModelAudit<String> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NUM_CUENTA_CORRIENTE")
    private String numCuentaCorriente;
    @Size(max = 40)
    @Column(name = "NOM_CUENTA_CORRIENTE")
    private String nomCuentaCorriente;
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COD_ENTIDAD_FINANCIERA")
    private TpEntidadFinancieraBcrp entidadFinanciera;
    
   
    @Column(name = "COD_MONEDA")
    private String codigoMoneda;
    
    
    @Column(name = "IND_LIQUIDADORA")
    private Integer indLiquidadora;
    @Column(name = "IND_LIQUIDADORA_FI")
    private Integer indLiquidadoraFi;
    @Column(name = "IND_LIQUIDADORA_OV")
    private Integer indLiquidadoraOv;
    @Size(max = 11)
    @Column(name = "COD_ESTADO")
    private String codEstado;
    
    
}
