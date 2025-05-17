package com.novatronic.masivas.backoffice.dto;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author Obi Consulting
 * @param <T>
 */
@Data
public class MasivasResponse<T> implements Serializable {

    public String codigo;
    public String mensaje;
    public T result;

    public MasivasResponse() {
    }

    public MasivasResponse(String mensaje) {
        this.result = null;
        this.mensaje = "";
    }

    public MasivasResponse(String mensaje, T result) {
        this.result = result;
        this.mensaje = mensaje;
    }

    public MasivasResponse(String codigo, String mensaje, T result) {
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.result = result;
    }

}
