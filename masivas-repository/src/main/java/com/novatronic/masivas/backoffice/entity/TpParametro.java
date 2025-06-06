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
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Obi Consulting
 */
@Entity
@Table(name = "TS_DETALLE_PARAMETRO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TpParametro extends ModelAudit<String> implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "ID_DETALLE_PARAMETRO")
    @SequenceGenerator(name = "SEQ_TS_DETALLE_PARAMETRO", sequenceName = "SEQ_TS_DETALLE_PARAMETRO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TS_DETALLE_PARAMETRO")
    private Long idParametro;
    @Basic(optional = false)
    @Size(max = 4)
    @Column(name = "COD_DETALLE_PARAMETRO")
    private String codigo;
    @Basic(optional = false)
    @Size(max = 50)
    @Column(name = "DESC_DETALLE_PARAMETRO")
    private String valor;
    @Basic(optional = false)
    @Size(max = 1)
    @Column(name = "ESTADO")
    private String estado;
    @Column(name = "ID_GRUPO_PARAMETRO")
    private Long idGrupoParametro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_GRUPO_PARAMETRO", referencedColumnName = "ID_GRUPO_PARAMETRO", insertable = false, updatable = false)
    private TpGrupoParametro grupoParametro;

    public TpParametro(String codigo, String valor, String estado, Long idGrupoParametro, Date fecCreacion, String usuCreacion) {
        this.codigo = codigo;
        this.valor = valor;
        this.estado = estado;
        this.idGrupoParametro = idGrupoParametro;
        this.setFecCreacion(fecCreacion);
        this.setUsuCreacion(usuCreacion);
    }

}
