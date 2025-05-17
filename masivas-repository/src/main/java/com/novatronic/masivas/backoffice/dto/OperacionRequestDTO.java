package com.novatronic.masivas.backoffice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
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
public class OperacionRequestDTO {

    //@NotNull(message = "{operacion.codConcepto.vacio}")
    //@NotEmpty(message = "{operacion.codConcepto.vacio}")
    @Size(min = 4, max = 4, message = "{operacion.codConcepto.longitud}")
    private String codigoConcepto;

    //@NotNull(message = "{operacion.codEntidadDestino.vacio}")
    //@NotEmpty(message = "{operacion.codEntidadDestino.vacio}")
    //@Size(min = 4, max = 4, message = "{operacion.codEntidadDestino.longitud}")
    private String codigoEntDestino;

    @NotNull(message = "{operacion.entidadOrigen.vacio}")
    @NotEmpty(message = "{operacion.entidadOrigen.vacio}")
    @Size(min = 4, max = 4, message = "{operacion.entidadOrigen.longitud}")
    private String codigoEntOrigen;

    private String numCuentaEntOrigen;
    private String numCuentaEntDestino;
    //@NotNull(message = "{operacion.monto.nulo}")
    private BigDecimal monto;
    private BigDecimal tipoCambio;

    @NotNull(message = "{operacion.codigomoneda.vacio}")
    @NotEmpty(message = "{operacion.codigomoneda.vacio}")
    @Size(min = 2, max = 2, message = "{operacion.codigomoneda.longitud}")
    private String codMoneda;

    private String observacion;
    private String observacionCliente;
    private Integer afectoItf;

    @Valid
    private ClienteDTO beneficiario;

    @Valid
    private ClienteDTO ordenante;

    // Esto es para cuando quieran actualizar
    private Long idOperacion;
    private String codEstadoNuevo;
    private String codEstadoActual;
    private BigDecimal montoME;

    // Para RON y RIN
    private String codInstrumento;
    SubastaDTO subasta;
    List<TenenciaDTO> tenencias;

    // Para Cavali
    private String instrucciones;
    private String fechaNegociacion;
    private String numRefCavali;
    private String codSAB;
    private String cciSAB;
    private String codParticipante;
    private String codFacilidad;
    
    private String tipoCambioIntradia;
    
    

}
