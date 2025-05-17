package com.novatronic.masivas.backoffice.dto;

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
public class OperacionDetalleDTO {

    private Long idOperacion;
    private String numRefLbtr;
    private String numRefBcrp;
    private String concepto;
    private String entidadOrigen;
    private String entidadDestino;
    private String fecha;
    private String moneda;
    private BigDecimal monto;

    private Long idClienteOrigen;
    private Long idClienteDestino;
    private String clienteOrigen;
    private String clienteDestino;

    private ClienteDTO beneficiario;
    private ClienteDTO ordenante;

    private String numCuentaEntOrigen;
    private String numCuentaEntDestino;

    //private String ordenante;
    //private String beneficiario;
    private String estado;
    private String observacion;
    private String observacionCliente;
    private String afectoItf;
    private Integer abonoConfirmacion;
    private String abonoConfirmacionDescripcion;

    private String fechaCreacion;
    private String fechaModificacion;
    private String usuarioCreacion;
    private String usuarioModificacion;

    private String codigoTransaccion;

    private BigDecimal tipoCambio;
    private BigDecimal montoME;
    private String tipoMovimiento;
    private String codigoTipoMovimiento;
    private String simboloMoneda;

    private String fechaVencimiento;
    private Integer plazo;
    private Integer numSubasta;
    private String codInstrumento;
    private BigDecimal montoAdjudicado;

    // Para RON y RIN
    SubastaDTO subasta;
    List<TenenciaDTO> tenencias;

}
