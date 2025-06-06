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
@Table(name = "TP_GRUPO_PARAMETRO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TpGrupoParametro extends ModelAudit<String> implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "ID_GRUPO_PARAMETRO")
    @SequenceGenerator(name = "SEQ_TP_GRUPO_PARAMETRO", sequenceName = "SEQ_TP_GRUPO_PARAMETRO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TP_GRUPO_PARAMETRO")
    private Long idGrupoParametro;
    @Basic(optional = false)
    @Size(max = 4)
    @Column(name = "COD_GRUPO_PARAMETRO")
    private String codigo;
    @Basic(optional = false)
    @Size(max = 50)
    @Column(name = "DESC_GRUPO_PARAMETRO")
    private String descripcion;
    @Basic(optional = false)
    @Size(max = 1)
    @Column(name = "ESTADO")
    private String estado;

    public TpGrupoParametro(String codigo, String descripcion, String estado, Date fecCreacion, String usuCreacion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.setFecCreacion(fecCreacion);
        this.setUsuCreacion(usuCreacion);
    }

}
