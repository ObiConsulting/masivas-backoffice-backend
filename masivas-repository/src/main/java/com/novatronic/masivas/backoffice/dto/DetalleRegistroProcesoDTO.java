package com.novatronic.masivas.backoffice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Obi Consulting
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DetalleRegistroProcesoDTO implements Serializable {

    private Long idProceso;
    private String codServer;
    private String codigoAccion;
    private String horario;

    //Campos de Auditoria
    private String usuCreacion;
    @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private LocalDateTime fecCreacion;
    private String usuModificacion;
    @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private LocalDateTime fecModificacion;

    public String toStringProceso() {
        return "{" + "idProceso=" + idProceso + ", horario=" + horario + '}';
    }
}
