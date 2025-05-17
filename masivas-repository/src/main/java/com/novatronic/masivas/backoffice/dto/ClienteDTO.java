package com.novatronic.masivas.backoffice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author obi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {
    
    
    @NotNull(message = "{cliente.codTipoDocumento.vacio}")
    @NotEmpty(message = "{cliente.codTipoDocumento.vacio}")
    private String codTipoDocumento;
    @Size(max = 250, message = "{cliente.direccion.longitud}")
    private String direccion;
    private String estado;
    @NotNull(message = "{cliente.nombre.vacio}")
    @NotEmpty(message = "{cliente.nombre.vacio}")
    @Size(min = 1, max = 80, message = "{cliente.nombre.longitud}")
    private String nombre;
    @NotNull(message = "{cliente.numCuentaCCI.vacio}")
    @NotEmpty(message = "{cliente.numCuentaCCI.vacio}")
    @Size(min = 1, max = 20, message = "{cliente.numCuentaCCI.longitud}")
    private String numCuentaCCI;
    @NotNull(message = "{cliente.numDocumento.vacio}")
    @Size(min = 1, max = 11, message = "{cliente.numDocumento.longitud}")
    private String numDocumento;
    
    
    //private String tipoDocumento;
    
}
