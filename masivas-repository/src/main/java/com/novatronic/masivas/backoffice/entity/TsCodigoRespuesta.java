package com.novatronic.masivas.backoffice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author obi
 */

@Entity
@Table(name = "TS_CODIGO_RESPUESTA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TsCodigoRespuesta implements Serializable {

    @Id
    @Column(name = "ID_CODIGO_RSPTA")
    private Long idCodigoRspsta;

    @Column(name = "COD_CODIGO_RSPTA")
    private String codCodigoRspsta;

    @Column(name = "ID_ORIGEN_RSPTA")
    private Long idOrigenRspsta;

    @Column(name = "NEMONICO")
    private String nemonico;

    @Column(name = "DESC_ORIGEN_RSPTA")
    private String descOrigenRspsta;

    @Column(name = "DESC_USUARIO_RSPTA")
    private String descUsuarioRspsta;

    @Column(name = "ACCION_CORRECTIVA")
    private String accionCorrectiva;

    @Column(name = "ID_CLASI_RSPTA")
    private Long idClasiRspsta;

    @Column(name = "COD_ESTADO")
    private String codEstado;

   
   
}