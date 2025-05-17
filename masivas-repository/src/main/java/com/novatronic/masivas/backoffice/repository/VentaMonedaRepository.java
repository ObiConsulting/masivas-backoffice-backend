package com.novatronic.masivas.backoffice.repository;

import com.novatronic.masivas.backoffice.dao.StoredProcedurePaginator;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaVentaMonedaDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroCompraMonedaDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroVentaMonedaDTO;
import com.novatronic.masivas.backoffice.dto.FiltroOperacionRequest;
import com.novatronic.masivas.backoffice.dto.ProcedureParam;
import com.novatronic.masivas.backoffice.dto.ResultadoOperacion;
import com.novatronic.masivas.backoffice.dto.ResultadoOperacionConsulta;
import com.novatronic.masivas.backoffice.util.Constantes;
import com.novatronic.masivas.backoffice.util.FuncionesUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Obi Consulting
 */
@Repository
public class VentaMonedaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultadoOperacionConsulta<DetalleConsultaVentaMonedaDTO> buscar(FiltroOperacionRequest request, String usuario) {

        StoredProcedurePaginator paginator = new StoredProcedurePaginator(entityManager);

        if (request.getFechaInicio() != null) {
            request.setFechaInicio(FuncionesUtil.convertToDateWithoutSeparators(request.getFechaInicio()));
        }
        if (request.getFechaFin() != null) {
            request.setFechaFin(FuncionesUtil.convertToDateWithoutSeparators(request.getFechaFin()));
        }

        List<ProcedureParam> params = List.of(
                new ProcedureParam(1, String.class, ParameterMode.IN, request.getNumOperacionLBTR()),
                new ProcedureParam(2, String.class, ParameterMode.IN, request.getNumReferenciaBCRP()),
                new ProcedureParam(3, String.class, ParameterMode.IN, request.getFechaInicio()),
                new ProcedureParam(4, String.class, ParameterMode.IN, request.getFechaFin()),
                new ProcedureParam(5, String.class, ParameterMode.IN, request.getCodEstado()),
                new ProcedureParam(6, String.class, ParameterMode.IN, request.getCodEntidadOrigen()),
                new ProcedureParam(7, String.class, ParameterMode.IN, usuario),
                new ProcedureParam(8, Integer.class, ParameterMode.IN, request.getNumeroPagina()),
                new ProcedureParam(9, Integer.class, ParameterMode.IN, request.getRegistrosPorPagina()),
                new ProcedureParam(10, String.class, ParameterMode.IN, request.getCampoOrdenar()),
                new ProcedureParam(11, String.class, ParameterMode.IN, request.getSentidoOrdenar()),
                new ProcedureParam(12, Integer.class, ParameterMode.OUT, null),
                new ProcedureParam(13, String.class, ParameterMode.OUT, null),
                new ProcedureParam(14, String.class, ParameterMode.OUT, null),
                new ProcedureParam(15, void.class, ParameterMode.REF_CURSOR, null)
        );

        return paginator.executeWithResult(Constantes.PACKAGE_BD + "." + Constantes.SPS_BUSCAR_VENTA_MONEDA,
                params,
                row -> {
                    DetalleConsultaVentaMonedaDTO dto = new DetalleConsultaVentaMonedaDTO();
                    dto.setIdOperacion((Long) row[1]);
                    dto.setNumRefLbtr((String) row[2]);
                    dto.setNumRefBcrp((String) row[3]);
                    dto.setConcepto((String) row[4]);
                    //dto.setMoneda((String) row[3]);
                    dto.setEntidadCompradora((String) row[5]);
                    dto.setEntidadVendedora((String) row[6]);
                    dto.setNumCuentaEntOrigen((String) row[7]);
                    dto.setNumCuentaEntDestino((String) row[8]);
                    dto.setMoneda((String) row[9]);
                    dto.setMonto((BigDecimal) row[10]);
                    dto.setSimboloMoneda((String) row[11]);
                    dto.setMontoME((BigDecimal) row[12]);
                    dto.setTipoCambio((BigDecimal) row[13]);
                    dto.setFecha((String) row[14]);
                    dto.setTipoMovimiento((String) row[15]);
                    dto.setReferenciaEnlaceLbtr((String) row[16]);
                    dto.setEstado((String) row[17]);
                    // aqui veine el codMoneda
                    dto.setAbonoConfirmado((String) row[19]);

                    return dto;
                },
                13, 14, 15, 12, request.getRegistrosPorPagina()
        );
    }

    public ResultadoOperacionConsulta<DetalleRegistroVentaMonedaDTO> obtenerDetalle(Long idOperacion, String numOperacionLBTR) {
        StoredProcedurePaginator dao = new StoredProcedurePaginator(entityManager);

        List<ProcedureParam> params = List.of(
                new ProcedureParam(1, Long.class, ParameterMode.IN, idOperacion),
                new ProcedureParam(2, void.class, ParameterMode.REF_CURSOR, null)
        );

        return dao.executeSingleResult(Constantes.PACKAGE_BD + "." + Constantes.SPS_DATA_VENTA_MONEDA,
                params,
                row -> {
                    DetalleRegistroVentaMonedaDTO dto = new DetalleRegistroVentaMonedaDTO();
                    dto.setIdOperacion((Long) row[0]);
                    dto.setFecha((String) row[1]);

                    dto.setNumRefLbtr((String) row[3]);
                    dto.setNumRefBcrp((String) row[4]);

                    dto.setConcepto((String) row[5]);
                    dto.setMoneda((String) row[7]);
                    dto.setEntidadOrigen((String) row[9]);
                    dto.setNumCuentaEntOrigen((String) row[11]);
                    dto.setEntidadDestino((String) row[12]);
                    dto.setNumCuentaEntDestino((String) row[14]);

                    dto.setMontoME((BigDecimal) row[15]);
                    dto.setMonto((BigDecimal) row[16]);
                    dto.setTipoCambio((BigDecimal) row[17]);

                    dto.setUsuarioCreacion((String) row[18]);
                    dto.setFechaCreacion((String) row[19]);
                    dto.setUsuarioModificacion((String) row[20]);
                    dto.setFechaModificacion((String) row[21]);
                    dto.setObservacion((String) row[22]);
                    dto.setCodEstado((String) row[23]);

                    dto.setAbonoConfirmacion((Integer) row[26]);

                    dto.setEstado((String) row[2]);

                    return dto;
                },
                2 // posición REF_CURSOR
        );
    }

    public ResultadoOperacion anular(
            String numOperacion,
            String usuarioModifica
    ) {

        StoredProcedurePaginator spDao = new StoredProcedurePaginator(entityManager);
        try {
            List<ProcedureParam> params = List.of(
                    new ProcedureParam(1, String.class, ParameterMode.IN, numOperacion),
                    new ProcedureParam(2, String.class, ParameterMode.IN, usuarioModifica),
                    new ProcedureParam(3, String.class, ParameterMode.OUT, null),
                    new ProcedureParam(4, String.class, ParameterMode.OUT, null)
            );

            Map<Integer, Object> result = spDao.execute(
                    Constantes.PACKAGE_BD + "." + Constantes.SPU_ANULAR_COMPRA_MONEDA,
                    params
            );
            return new ResultadoOperacion(
                    null,
                    (String) result.get(3),
                    (String) result.get(4)
            );

        } catch (Exception e) {
            return new ResultadoOperacion(null, Constantes.ERROR_BD, Constantes.MENSAJE_ERROR_BD_USUARIO);
        }
    }

}
