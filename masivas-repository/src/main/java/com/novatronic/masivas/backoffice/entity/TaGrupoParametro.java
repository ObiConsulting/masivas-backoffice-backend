package com.novatronic.masivas.backoffice.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author obi
 */
@Entity
@Table(name = "TA_GRUPO_PARAMETRO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaGrupoParametro extends ModelAudit<String> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "ID_GRUPO_PARAMETRO")
    private String idGrupoParametro;
    @Size(max = 50)
    @Column(name = "NOM_GRUPO")
    private String nomGrupo;
    @Size(max = 30)
    @Column(name = "NOM_TABLA")
    private String nomTabla;
    @Size(max = 200)
    @Column(name = "DES_GRUPO_PARAMETRO")
    private String desGrupoParametro;
    @Size(max = 11)
    @Column(name = "ESTADO_GRUPO")
    private String estadoGrupo;
    
    @OneToMany(mappedBy = "idGrupoParametro")
    private Collection<TaDetalleParametro> taDetalleParametroCollection;

    
    
}
