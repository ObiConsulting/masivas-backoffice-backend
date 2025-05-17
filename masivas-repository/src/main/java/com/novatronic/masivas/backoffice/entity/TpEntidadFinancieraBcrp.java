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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author obi
 */
@Entity
@Table(name = "TP_ENTIDAD_FINANCIERA_BCRP")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TpEntidadFinancieraBcrp extends ModelAudit<String> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "COD_ENTIDAD_FINANCIERA")
    private String codEntidadFinanciera;
    @Column(name = "COD_ESTADO")
    private BigInteger codEstado;
    @Size(max = 35)
    @Column(name = "NOM_CORTO")
    private String nomCorto;
    @Size(max = 80)
    @Column(name = "NOM_LARGO")
    private String nomLargo;
    @Size(max = 250)
    @Column(name = "DIRECCION")
    private String direccion;
    @Size(max = 300)
    @Column(name = "REFERENCIA")
    private String referencia;
    @Size(max = 20)
    @Column(name = "RUC")
    private String ruc;
    @Size(max = 11)
    @Column(name = "IND_ACCESO_LBTR")
    private String indAccesoLbtr;
    @Size(max = 255)
    @Column(name = "URL_HOST")
    private String urlHost;
    
    @Size(max = 2)
    @Column(name = "FLAG_OWNER")
    private String flagOwner;

    @OneToMany(mappedBy = "entidadFinanciera", fetch = FetchType.LAZY)
    private List<TpCuentaCorrienteBcrp> cuentasCorrientes;
    
}
