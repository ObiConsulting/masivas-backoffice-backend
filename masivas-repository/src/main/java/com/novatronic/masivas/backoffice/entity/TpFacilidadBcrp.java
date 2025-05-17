package com.novatronic.masivas.backoffice.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author obi
 */
@Entity
@Table(name = "TP_FACILIDAD_BCRP")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TpFacilidadBcrp implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "COD_FACILIDAD")
    private String codFacilidad;
    @Size(max = 80)
    @Column(name = "DESCRIPCION")
    private String descripcion;

    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "COD_CONCEPTO")
    private String codConcepto;

    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "COD_MONEDA")
    private String codMoneda;

    @Column(name = "COD_ESTADO")
    private BigInteger codEstado;

}
