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
@Table(name = "TP_ENTIDAD")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TpEntidad extends ModelAudit<String> implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "ID_ENTIDAD")
    @SequenceGenerator(name = "SEQ_TP_ENTIDAD", sequenceName = "SEQ_TP_ENTIDAD", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TP_ENTIDAD")
    private Long idEntidad;
    @Basic(optional = false)
    @Size(max = 20)
    @Column(name = "COD_ENTIDAD")
    private String codigo;
    @Basic(optional = false)
    @Size(max = 100)
    @Column(name = "DESC_ENTIDAD")
    private String nombre;
    @Basic(optional = false)
    @Size(max = 1)
    @Column(name = "ESTADO")
    private String estado;

    @Basic(optional = false)
    @Column(name = "ID_PERFIL")
    private Long idPerfil;

    public TpEntidad(String codigo, String nombre, String estado, Long idPerfil, Date fecCreacion, String usuCreacion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.estado = estado;
        this.idPerfil = idPerfil;
        this.setFecCreacion(fecCreacion);
        this.setUsuCreacion(usuCreacion);
    }

}
