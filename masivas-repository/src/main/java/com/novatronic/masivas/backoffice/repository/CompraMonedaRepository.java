package com.novatronic.masivas.backoffice.repository;

import com.novatronic.masivas.backoffice.dao.StoredProcedurePaginator;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaCompraMonedaDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroCompraMonedaDTO;
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
public class CompraMonedaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultadoOperacion insertarOperacion(
            String codConcepto,
            String codEntDestino,
            String codEntOrigen,
            String numCuentaOrigen,
            String numCuentaDestino,
            BigDecimal montome,
            BigDecimal tipoCambio,
            String codMoneda,
            String observacion,
            String usuarioCrea
    ) {

        StoredProcedurePaginator spDao = new StoredProcedurePaginator(entityManager);
        List<ProcedureParam> params = List.of(
                new ProcedureParam(1, String.class, ParameterMode.IN, codConcepto),
                new ProcedureParam(2, String.class, ParameterMode.IN, codEntDestino),
                new ProcedureParam(3, String.class, ParameterMode.IN, codEntOrigen),
                new ProcedureParam(4, String.class, ParameterMode.IN, numCuentaOrigen),
                new ProcedureParam(5, String.class, ParameterMode.IN, numCuentaDestino),
                new ProcedureParam(6, BigDecimal.class, ParameterMode.IN, montome),
                new ProcedureParam(7, BigDecimal.class, ParameterMode.IN, tipoCambio),
                new ProcedureParam(8, String.class, ParameterMode.IN, codMoneda),
                new ProcedureParam(9, String.class, ParameterMode.IN, observacion),
                new ProcedureParam(10, String.class, ParameterMode.IN, usuarioCrea),
                new ProcedureParam(11, BigDecimal.class, ParameterMode.OUT, null),
                new ProcedureParam(12, String.class, ParameterMode.OUT, null),
                new ProcedureParam(13, String.class, ParameterMode.OUT, null)
        );

        try {
            Map<Integer, Object> out = spDao.execute(Constantes.PACKAGE_BD + "." + Constantes.SPI_INSERTAR_COMPRA_MONEDA, params);

            return new ResultadoOperacion(
                    (BigDecimal) out.get(11),
                    (String) out.get(12),
                    (String) out.get(13)
            );
        } catch (Exception e) {
            return new ResultadoOperacion(null, Constantes.ERROR_BD, Constantes.MENSAJE_ERROR_BD_USUARIO);
        }
    }

    public ResultadoOperacion actualizarOperacion(
            Long idOperacion,
            String codConcepto,
            String codEntDestino,
            String codEntOrigen,
            String numCuentaOrigen,
            String numCuentaDestino,
            BigDecimal montome,
            BigDecimal tipoCambio,
            String codMoneda,
            String observacion,
            String usuarioModifica
    ) {

        StoredProcedurePaginator spDao = new StoredProcedurePaginator(entityManager);
        try {
            List<ProcedureParam> params = List.of(
                    new ProcedureParam(1, Long.class, ParameterMode.IN, idOperacion),
                    new ProcedureParam(2, String.class, ParameterMode.IN, codConcepto),
                    new ProcedureParam(3, String.class, ParameterMode.IN, codEntDestino),
                    new ProcedureParam(4, String.class, ParameterMode.IN, codEntOrigen),
                    new ProcedureParam(5, String.class, ParameterMode.IN, numCuentaOrigen),
                    new ProcedureParam(6, String.class, ParameterMode.IN, numCuentaDestino),
                    new ProcedureParam(7, BigDecimal.class, ParameterMode.IN, montome),
                    new ProcedureParam(8, BigDecimal.class, ParameterMode.IN, tipoCambio),
                    new ProcedureParam(9, String.class, ParameterMode.IN, codMoneda),
                    new ProcedureParam(10, String.class, ParameterMode.IN, observacion),
                    new ProcedureParam(11, String.class, ParameterMode.IN, usuarioModifica),
                    new ProcedureParam(12, String.class, ParameterMode.OUT, null),
                    new ProcedureParam(13, String.class, ParameterMode.OUT, null)
            );

            Map<Integer, Object> result = spDao.execute(
                    Constantes.PACKAGE_BD + "." + Constantes.SPU_MODIFICAR_COMPRA_MONEDA,
                    params
            );
            return new ResultadoOperacion(
                    idOperacion != null ? BigDecimal.valueOf(idOperacion) : null,
                    (String) result.get(12),
                    (String) result.get(13)
            );

        } catch (Exception e) {
            return new ResultadoOperacion(null, Constantes.ERROR_BD, Constantes.MENSAJE_ERROR_BD_USUARIO);
        }
    }

    public ResultadoOperacionConsulta<DetalleConsultaCompraMonedaDTO> buscarCompraMoneda(FiltroOperacionRequest request, String usuario) {

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
                new ProcedureParam(5, String.class, ParameterMode.IN, request.getCodEntidadDestino()),
                new ProcedureParam(6, String.class, ParameterMode.IN, request.getCodEntidadOrigen()),
                new ProcedureParam(7, String.class, ParameterMode.IN, request.getCodEstado()),
                new ProcedureParam(8, String.class, ParameterMode.IN, usuario),
                new ProcedureParam(9, Integer.class, ParameterMode.IN, request.getNumeroPagina()),
                new ProcedureParam(10, Integer.class, ParameterMode.IN, request.getRegistrosPorPagina()),
                new ProcedureParam(11, String.class, ParameterMode.IN, request.getCampoOrdenar()),
                new ProcedureParam(12, String.class, ParameterMode.IN, request.getSentidoOrdenar()),
                new ProcedureParam(13, Integer.class, ParameterMode.OUT, null),
                new ProcedureParam(14, String.class, ParameterMode.OUT, null),
                new ProcedureParam(15, String.class, ParameterMode.OUT, null),
                new ProcedureParam(16, void.class, ParameterMode.REF_CURSOR, null)
        );

        return paginator.executeWithResult(Constantes.PACKAGE_BD + "." + Constantes.SPS_BUSCAR_COMPRA_MONEDA,
                params,
                row -> {
                    DetalleConsultaCompraMonedaDTO dto = new DetalleConsultaCompraMonedaDTO();
                    dto.setIdOperacion((Long) row[1]);
                    dto.setNumRefLbtr((String) row[2]);
                    dto.setNumRefBcrp((String) row[3]);
                    dto.setConcepto((String) row[5]);
                    //dto.setMoneda((String) row[3]);
                    dto.setEntidadOrigen((String) row[6]);
                    dto.setEntidadDestino((String) row[7]);
                    dto.setNumCuentaEntDestino((String) row[8]);
                    dto.setNumCuentaEntOrigen((String) row[9]);
                    dto.setFecha((String) row[10]);
                    dto.setMontoME((BigDecimal) row[11]);
                    dto.setMonto((BigDecimal) row[12]);
                    dto.setObservacion((String) row[13]);
                    dto.setTipoCambio((BigDecimal) row[14]);

                    dto.setTipoMovimiento((String) row[15]);
                    dto.setMoneda((String) row[17]);
                    dto.setSimboloMoneda((String) row[18]);
                    dto.setEstado((String) row[19]);
                    return dto;
                },
                14, 15, 16, 13, request.getRegistrosPorPagina()
        );
    }

    public ResultadoOperacionConsulta<DetalleRegistroCompraMonedaDTO> obtenerDetalle(Long idOperacion, String numOperacionLBTR) {
        StoredProcedurePaginator dao = new StoredProcedurePaginator(entityManager);

        List<ProcedureParam> params = List.of(
                new ProcedureParam(1, Long.class, ParameterMode.IN, idOperacion),
                new ProcedureParam(2, void.class, ParameterMode.REF_CURSOR, null)
        );

        return dao.executeSingleResult(Constantes.PACKAGE_BD + "." + Constantes.SPS_DATA_COMPRA_MONEDA,
                params,
                row -> {
                    DetalleRegistroCompraMonedaDTO dto = new DetalleRegistroCompraMonedaDTO();
                    dto.setIdOperacion((Long) row[0]);
                    dto.setFecha((String) row[1]);

                    dto.setNumRefLbtr((String) row[3]);
                    dto.setNumRefBcrp((String) row[4]);

                    dto.setConcepto((String) row[5]);
                    dto.setMoneda((String) row[6]);
                    dto.setEntidadOrigen((String) row[8]);
                    dto.setNumCuentaEntOrigen((String) row[10]);
                    dto.setEntidadDestino((String) row[11]);
                    dto.setNumCuentaEntDestino((String) row[13]);

                    dto.setMontoME((BigDecimal) row[14]);
                    dto.setMonto((BigDecimal) row[15]);
                    dto.setTipoCambio((BigDecimal) row[16]);

                    dto.setUsuarioCreacion((String) row[17]);
                    dto.setFechaCreacion((String) row[18]);
                    dto.setUsuarioModificacion((String) row[19]);
                    dto.setFechaModificacion((String) row[20]);
                    dto.setObservacion((String) row[21]);
                    dto.setCodEstado((String) row[22]);
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
