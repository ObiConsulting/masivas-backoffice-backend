package com.novatronic.masivas.backoffice.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author obi
 */
@Entity
@Table(name = "TA_DETALLE_PARAMETRO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaDetalleParametro extends ModelAudit<String> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "ID_DETALLE_PARAMETRO")
    private String idDetalleParametro;
    @Size(max = 100)
    @Column(name = "NOM_PARAMETRO")
    private String nomParametro;
    @Size(max = 30)
    @Column(name = "VALOR_PARAMETRO")
    private String valorParametro;
    @Size(max = 200)
    @Column(name = "DES_DETALLE_PARAMETRO")
    private String desDetalleParametro;
    @Size(max = 15)
    @Column(name = "ESTADO_PARAMETRO")
    private String estadoParametro;
    
    @JoinColumn(name = "ID_GRUPO_PARAMETRO", referencedColumnName = "ID_GRUPO_PARAMETRO")
    @ManyToOne
    private TaGrupoParametro idGrupoParametro;

    
    
}
