package com.novatronic.masivas.backoffice.dto;

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
public class ArchivoDTO {

    private String nombreCliente;
    private String nombreAutorizador;
    private Long trace;
}
