package com.novatronic.masivas.backoffice.repository;

import com.novatronic.masivas.backoffice.dao.StoredProcedurePaginator;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaFacilidadIntradiaDTO;
import com.novatronic.masivas.backoffice.dto.FiltroOperacionRequest;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroFacilidadIntradiaDTO;
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
public class FacilidadIntradiaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultadoOperacion insertarOperacion(
            String codFacilidad,
            BigDecimal monto,
            String codMoneda,
            String tipoCambio,
            String codEntOrigen,
            String usuarioCrea
    ) {

        StoredProcedurePaginator spDao = new StoredProcedurePaginator(entityManager);
        List<ProcedureParam> params = List.of(
                new ProcedureParam(1, String.class, ParameterMode.IN, codFacilidad),
                new ProcedureParam(2, BigDecimal.class, ParameterMode.IN, monto),
                new ProcedureParam(3, String.class, ParameterMode.IN, codMoneda),
                new ProcedureParam(4, String.class, ParameterMode.IN, tipoCambio),
                new ProcedureParam(5, String.class, ParameterMode.IN, codEntOrigen),
                new ProcedureParam(6, String.class, ParameterMode.IN, usuarioCrea),
                new ProcedureParam(7, BigDecimal.class, ParameterMode.OUT, null),
                new ProcedureParam(8, String.class, ParameterMode.OUT, null),
                new ProcedureParam(9, String.class, ParameterMode.OUT, null)
        );

        try {
            Map<Integer, Object> out = spDao.execute(Constantes.PACKAGE_BD + "." + Constantes.SPI_INSERTAR_FACILIDADINTRADIA, params);

            return new ResultadoOperacion(
                    (BigDecimal) out.get(7),
                    (String) out.get(8),
                    (String) out.get(9)
            );
        } catch (Exception e) {
            return new ResultadoOperacion(null, Constantes.ERROR_BD, Constantes.MENSAJE_ERROR_BD_USUARIO);
        }
    }

    public ResultadoOperacion actualizarOperacion(
            Long idOperacion,
            String codFacilidad,
            BigDecimal monto,
            String codMoneda,
            String tipoCambio,
            String codEntOrigen,
            String usuarioModifica
    ) {

        StoredProcedurePaginator spDao = new StoredProcedurePaginator(entityManager);
        try {
            List<ProcedureParam> params = List.of(
                    new ProcedureParam(1, Long.class, ParameterMode.IN, idOperacion),
                    new ProcedureParam(2, String.class, ParameterMode.IN, codFacilidad),
                    new ProcedureParam(3, BigDecimal.class, ParameterMode.IN, monto),
                    new ProcedureParam(4, String.class, ParameterMode.IN, codMoneda),
                    new ProcedureParam(5, String.class, ParameterMode.IN, tipoCambio),
                    new ProcedureParam(6, String.class, ParameterMode.IN, codEntOrigen),
                    new ProcedureParam(7, String.class, ParameterMode.IN, usuarioModifica),
                    new ProcedureParam(8, String.class, ParameterMode.OUT, null),
                    new ProcedureParam(9, String.class, ParameterMode.OUT, null)
            );

            Map<Integer, Object> result = spDao.execute(
                    Constantes.PACKAGE_BD + "." + Constantes.SPU_MODIFICAR_FACILIDADINTRADIA,
                    params
            );
            return new ResultadoOperacion(
                    idOperacion != null ? BigDecimal.valueOf(idOperacion) : null,
                    (String) result.get(8),
                    (String) result.get(9)
            );

        } catch (Exception e) {
            return new ResultadoOperacion(null, Constantes.ERROR_BD, Constantes.MENSAJE_ERROR_BD_USUARIO);
        }
    }

    public ResultadoOperacionConsulta<DetalleConsultaFacilidadIntradiaDTO> buscar(FiltroOperacionRequest request, String usuario) {

        StoredProcedurePaginator paginator = new StoredProcedurePaginator(entityManager);

        if (request.getFechaInicio() != null) {
            request.setFechaInicio(FuncionesUtil.convertToDateWithoutSeparators(request.getFechaInicio()));
        }
        if (request.getFechaFin() != null) {
            request.setFechaFin(FuncionesUtil.convertToDateWithoutSeparators(request.getFechaFin()));
        }

        List<ProcedureParam> params = List.of(
                new ProcedureParam(1, String.class, ParameterMode.IN, request.getFechaInicio()),
                new ProcedureParam(2, String.class, ParameterMode.IN, request.getFechaFin()),
                new ProcedureParam(3, String.class, ParameterMode.IN, request.getCodFacilidad()),
                new ProcedureParam(4, String.class, ParameterMode.IN, request.getCodEstado()),
                new ProcedureParam(5, String.class, ParameterMode.IN, usuario),
                new ProcedureParam(6, Integer.class, ParameterMode.IN, request.getNumeroPagina()),
                new ProcedureParam(7, Integer.class, ParameterMode.IN, request.getRegistrosPorPagina()),
                new ProcedureParam(8, String.class, ParameterMode.IN, request.getCampoOrdenar()),
                new ProcedureParam(9, String.class, ParameterMode.IN, request.getSentidoOrdenar()),
                new ProcedureParam(10, Integer.class, ParameterMode.OUT, null),
                new ProcedureParam(11, String.class, ParameterMode.OUT, null),
                new ProcedureParam(12, String.class, ParameterMode.OUT, null),
                new ProcedureParam(13, void.class, ParameterMode.REF_CURSOR, null)
        );

        return paginator.executeWithResult(Constantes.PACKAGE_BD + "." + Constantes.SPS_BUSCAR_FACILIDADINTRA,
                params,
                row -> {
                    DetalleConsultaFacilidadIntradiaDTO dto = new DetalleConsultaFacilidadIntradiaDTO();
                    dto.setIdOperacion((Long) row[1]);
                    dto.setNumRefLbtr((String) row[2]);
                    dto.setNumRefBcrp((String) row[3]);
                    dto.setFacilidad((String) row[5]);
                    dto.setMonto((BigDecimal) row[6]);
                    dto.setSimboloMoneda((String) row[9]);
                    dto.setEstado((String) row[11]);
                    dto.setTipoMovimiento((String) row[12]);
                    dto.setTipoCambio((BigDecimal) row[13]);
                    dto.setNumReferenciaOrigen((String) row[14]);

                    return dto;
                },
                11, 12, 13, 10, request.getRegistrosPorPagina()
        );
    }

    public ResultadoOperacionConsulta<DetalleRegistroFacilidadIntradiaDTO> obtenerDetalle(Long idOperacion, String numOperacionLBTR) {
        StoredProcedurePaginator dao = new StoredProcedurePaginator(entityManager);

        List<ProcedureParam> params = List.of(
                new ProcedureParam(1, Long.class, ParameterMode.IN, idOperacion),
                new ProcedureParam(2, void.class, ParameterMode.REF_CURSOR, null)
        );

        return dao.executeSingleResult(Constantes.PACKAGE_BD + "." + Constantes.SPS_DATA_FACILIDADINTRA,
                params,
                row -> {
                    DetalleRegistroFacilidadIntradiaDTO dto = new DetalleRegistroFacilidadIntradiaDTO();
                    dto.setIdOperacion((Long) row[0]);
                    dto.setFecha((String) row[1]);
                    dto.setNumRefLbtr((String) row[3]);
                    dto.setNumRefBcrp((String) row[4]);
                    dto.setCodFacilidad((String) row[5]);
                    dto.setCodMoneda((String) row[7]);
                    dto.setTipoCambio((BigDecimal) row[9]);
                    dto.setMonto((BigDecimal) row[10]);
                    
                    dto.setUsuarioCreacion((String) row[11]);
                    dto.setFechaCreacion((String) row[12]);
                    dto.setUsuarioModificacion((String) row[13]);
                    dto.setFechaModificacion((String) row[14]);
                    
                    dto.setNumCuentaOrigen((String) row[15]);
                    dto.setNumCuentaDestino((String) row[16]);
                    dto.setCodConcepto((String) row[17]);
                    dto.setCodEntidadDestino((String) row[18]);
                    dto.setCodEstado((String) row[19]);
                    dto.setEstado((String) row[2]);

                    return dto;
                },
                2// posición REF_CURSOR
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
                    Constantes.PACKAGE_BD + "." + Constantes.SPU_ANULAR_FACILIDADINTRADIA,
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
