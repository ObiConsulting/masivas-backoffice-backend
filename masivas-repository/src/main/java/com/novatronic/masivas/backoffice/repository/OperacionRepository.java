package com.novatronic.masivas.backoffice.repository;

import com.novatronic.masivas.backoffice.dao.StoredProcedurePaginator;
import com.novatronic.masivas.backoffice.dto.FiltroOperacionRequest;
import com.novatronic.masivas.backoffice.dto.AprobacionDTO;
import com.novatronic.masivas.backoffice.dto.OperacionListaAprobadorDTO;
import com.novatronic.masivas.backoffice.dto.ProcedureParam;
import com.novatronic.masivas.backoffice.dto.ResultadoOperacion;
import com.novatronic.masivas.backoffice.dto.ResultadoOperacionConsulta;
import com.novatronic.masivas.backoffice.util.Constantes;
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
public class OperacionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultadoOperacion insertarAprobarOperacion(Long idOperacion, String usuario, String codigoEntidad) {

        StoredProcedurePaginator spDao = new StoredProcedurePaginator(entityManager);
        List<ProcedureParam> params = List.of(
                new ProcedureParam(1, Long.class, ParameterMode.IN, idOperacion),
                new ProcedureParam(2, String.class, ParameterMode.IN, usuario),
                new ProcedureParam(3, String.class, ParameterMode.IN, codigoEntidad),
                new ProcedureParam(4, String.class, ParameterMode.OUT, null),
                new ProcedureParam(5, String.class, ParameterMode.OUT, null)
        );

        try {
            Map<Integer, Object> out = spDao.execute(Constantes.PACKAGE_BD + "." + Constantes.SPI_INSERTAR_OPERACION_APROB, params);

            return new ResultadoOperacion(
                    idOperacion != null ? BigDecimal.valueOf(idOperacion) : null,
                    (String) out.get(4),
                    (String) out.get(5)
            );
        } catch (Exception e) {
            return new ResultadoOperacion(null, Constantes.ERROR_BD, Constantes.MENSAJE_ERROR_BD_USUARIO);
        }
    }

    public ResultadoOperacionConsulta<OperacionListaAprobadorDTO> buscarAprobadoresPorIdOperacion(FiltroOperacionRequest request) {

        StoredProcedurePaginator paginator = new StoredProcedurePaginator(entityManager);

        List<ProcedureParam> params = List.of(
                new ProcedureParam(1, Long.class, ParameterMode.IN, request.getIdOperacion()),
                new ProcedureParam(2, Integer.class, ParameterMode.IN, request.getNumeroPagina()),
                new ProcedureParam(3, Integer.class, ParameterMode.IN, request.getRegistrosPorPagina()),
                new ProcedureParam(4, Integer.class, ParameterMode.OUT, null),
                new ProcedureParam(5, String.class, ParameterMode.OUT, null),
                new ProcedureParam(6, String.class, ParameterMode.OUT, null),
                new ProcedureParam(7, void.class, ParameterMode.REF_CURSOR, null)
        );

        return paginator.executeWithResult(Constantes.PACKAGE_BD + "." + Constantes.SPS_BUSCAR_OPERACION_APROB,
                params,
                row -> {
                    OperacionListaAprobadorDTO dto = new OperacionListaAprobadorDTO();
                    dto.setCodigo((Long) row[1]);
                    dto.setIdUsuarioAprobador((String) row[2]);
                    dto.setFechaAprobacion((String) row[3]);
                    return dto;
                },
                5, 6, 7, 4, request.getRegistrosPorPagina()
        );
    }

    public ResultadoOperacionConsulta<AprobacionDTO> obtenerTotalAprobacionesPorTipoOperacion(FiltroOperacionRequest request) {

        StoredProcedurePaginator dao = new StoredProcedurePaginator(entityManager);

        List<ProcedureParam> params = List.of(
                new ProcedureParam(1, String.class, ParameterMode.IN, request.getTipoOperacion()),
                new ProcedureParam(2, String.class, ParameterMode.IN, request.getCodEntidadOrigen()),
                new ProcedureParam(3, Integer.class, ParameterMode.IN, request.getNumeroPagina()),
                new ProcedureParam(4, Integer.class, ParameterMode.IN, request.getRegistrosPorPagina()),
                new ProcedureParam(5, String.class, ParameterMode.IN, request.getCampoOrdenar()),
                new ProcedureParam(6, String.class, ParameterMode.IN, request.getSentidoOrdenar()),
                new ProcedureParam(7, Integer.class, ParameterMode.OUT, null),
                new ProcedureParam(8, String.class, ParameterMode.OUT, null),
                new ProcedureParam(9, String.class, ParameterMode.OUT, null),
                new ProcedureParam(10, void.class, ParameterMode.REF_CURSOR, null)
        );

        return dao.executeSingleResult(Constantes.PACKAGE_BD + "." + Constantes.SPS_BUSCAR_APROBACION,
                params,
                row -> {
                    AprobacionDTO dto = new AprobacionDTO();
                    dto.setCodigoTransaccion((String) row[1]);
                    dto.setNombreTransaccion((String) row[2]);
                    dto.setTotalAprobacionesNecesarias((Integer) row[4]);

                    return dto;
                },
                10 // posición REF_CURSOR
        );
    }

}
