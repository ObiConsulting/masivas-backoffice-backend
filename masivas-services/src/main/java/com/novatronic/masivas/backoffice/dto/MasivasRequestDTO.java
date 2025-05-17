package com.novatronic.masivas.backoffice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Obi Consulting
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MasivasRequestDTO {

    private String codigoEntidad;
    private String descripcionEntidad;
    private String estado;

}
