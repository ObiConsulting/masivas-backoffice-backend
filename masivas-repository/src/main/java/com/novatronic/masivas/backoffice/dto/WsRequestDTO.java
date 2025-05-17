package com.novatronic.masivas.backoffice.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author obi
 */
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WsRequestDTO {

    private Integer pagina;
    private Integer tamPagina;
    private String campoOrdenamiento;
    private String sentidoOrdenamiento;
    private String ipTerminal;
    private Integer numRegSolicitados;
    private boolean primeraBusqueda;
    

    public Integer getNumRegSolicitados() {
        return numRegSolicitados;
    }
    
    public Integer getPagina() {
        return pagina;
    }

    public Integer getTamPagina() {
        return tamPagina;
    }

    public String getIpTerminal() {
        return ipTerminal;
    }

    
    
    public boolean isPrimeraBusqueda() {
        return primeraBusqueda;
    }
            
    public String getSentidoOrdenamiento() {
        return sentidoOrdenamiento != null ? sentidoOrdenamiento : "";
    }
    
    public String getCampoOrdenamiento() {
        return campoOrdenamiento != null ? campoOrdenamiento : "";
    }
    

//    private String rolUsuario;
//    
//    private String opcion;
//
//    private Long numRegSolicitados;
//    private Long numRegEnviados;
//    private Object codigo;
//    private String entidad;
//    private String usuarioAuditoria;
//    private String usuarioCreacion;
//    private String usuarioModificacion;
//    private String tokenLBTR;
//    private String tokenBCRP;
//    private String fechaTransaccion;
//    private String horaTransaccion;
//    private String mensaje;
//    private String numReferenciaHost;
    //Filtros de ordenamiento por columna

    

}
