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
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Obi Consulting
 */
@Entity
@Table(name = "TP_APLICACION")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TpAplicacion extends ModelAudit<String> implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "ID_APLICACION")
    @SequenceGenerator(name = "SEQ_TP_APLICACION", sequenceName = "SEQ_TP_APLICACION", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TP_APLICACION")
    private Long idAplicacion;
    @Basic(optional = false)
    @Column(name = "ID_ENTIDAD")
    private Long idEntidad;
    @Basic(optional = false)
    @Size(max = 20)
    @Column(name = "COD_APLICACION")
    private String codigo;
    @Basic(optional = false)
    @Size(max = 100)
    @Column(name = "DESC_APLICACION")
    private String nombre;
    @Basic(optional = false)
    @Size(max = 1)
    @Column(name = "ESTADO")
    private String estado;

    public TpAplicacion(Long idEntidad, String codigo, String nombre, String estado, LocalDateTime fecCreacion, String usuCreacion) {
        this.idEntidad = idEntidad;
        this.codigo = codigo;
        this.nombre = nombre;
        this.estado = estado;
        this.setFecCreacion(fecCreacion);
        this.setUsuCreacion(usuCreacion);
    }

}
