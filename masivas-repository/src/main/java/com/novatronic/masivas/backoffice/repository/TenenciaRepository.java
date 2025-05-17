package com.novatronic.masivas.backoffice.repository;

import com.novatronic.masivas.backoffice.dao.StoredProcedurePaginator;
import com.novatronic.masivas.backoffice.dto.FiltroOperacionRequest;
import com.novatronic.masivas.backoffice.dto.OperacionDetalleDTO;
import com.novatronic.masivas.backoffice.dto.ProcedureParam;
import com.novatronic.masivas.backoffice.dto.ResultadoOperacion;
import com.novatronic.masivas.backoffice.dto.ResultadoOperacionConsulta;
import com.novatronic.masivas.backoffice.dto.SubastaDTO;
import com.novatronic.masivas.backoffice.dto.TenenciaDTO;
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
public class TenenciaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    
    public ResultadoOperacion insertarTenencia(
            Long idSubasta,
            String fecOperacion,
            String codigoInstrumento,
            String codigoParticipante,
            String codigoValor,
            String fechaTenencia,
            String nombreTercero,
            String documentoTercero,
            String tipoDocumentoTercero,
            String tipoTenencia,
            BigDecimal totalValorizado,
            BigDecimal totalNominal,
            Long idOperacion,
            String usuarioCrea
    ) {

        StoredProcedurePaginator spDao = new StoredProcedurePaginator(entityManager);
        List<ProcedureParam> params = List.of(
                new ProcedureParam(1, Long.class, ParameterMode.IN, idSubasta),
                new ProcedureParam(2, String.class, ParameterMode.IN, fecOperacion),
                new ProcedureParam(3, String.class, ParameterMode.IN, codigoInstrumento),
                new ProcedureParam(4, String.class, ParameterMode.IN, codigoParticipante),
                new ProcedureParam(5, String.class, ParameterMode.IN, codigoValor),
                new ProcedureParam(6, String.class, ParameterMode.IN, fechaTenencia),
                new ProcedureParam(7, String.class, ParameterMode.IN, nombreTercero),
                new ProcedureParam(8, String.class, ParameterMode.IN, documentoTercero),
                new ProcedureParam(9, String.class, ParameterMode.IN, tipoDocumentoTercero),
                new ProcedureParam(10, String.class, ParameterMode.IN, tipoTenencia),
                new ProcedureParam(11, BigDecimal.class, ParameterMode.IN, totalValorizado),
                new ProcedureParam(12, BigDecimal.class, ParameterMode.IN, totalNominal),
                new ProcedureParam(13, Long.class, ParameterMode.IN, idOperacion),
                new ProcedureParam(14, String.class, ParameterMode.IN, usuarioCrea),
                new ProcedureParam(15, BigDecimal.class, ParameterMode.OUT, null),
                new ProcedureParam(16, String.class, ParameterMode.OUT, null),
                new ProcedureParam(17, String.class, ParameterMode.OUT, null)
        );

        try {
            Map<Integer, Object> out = spDao.execute(Constantes.PACKAGE_BD + "." + Constantes.SPI_INSERTAR_TENENCIA, params);
            return new ResultadoOperacion(
                    (BigDecimal) out.get(15),
                    (String) out.get(16),
                    (String) out.get(17)
            );
        } catch (Exception e) {
            return new ResultadoOperacion(null, Constantes.ERROR_BD, Constantes.MENSAJE_ERROR_BD_USUARIO);
        }
    }
    
    public ResultadoOperacion eliminarTenencia(
            Long idOperacion
    ) {

        StoredProcedurePaginator spDao = new StoredProcedurePaginator(entityManager);
        List<ProcedureParam> params = List.of(
                new ProcedureParam(1, Long.class, ParameterMode.IN, idOperacion),
                new ProcedureParam(2, String.class, ParameterMode.OUT, null),
                new ProcedureParam(3, String.class, ParameterMode.OUT, null)
        );
        try {
            Map<Integer, Object> out = spDao.execute(Constantes.PACKAGE_BD + "." + Constantes.SPU_ANULAR_TENENCIA, params);
            return new ResultadoOperacion(
                    null,
                    (String) out.get(2),
                    (String) out.get(3)
            );
        } catch (Exception e) {
            return new ResultadoOperacion(null, Constantes.ERROR_BD, Constantes.MENSAJE_ERROR_BD_USUARIO);
        }
    }

    
    public ResultadoOperacionConsulta<TenenciaDTO> buscarTenencias(
            BigDecimal idSubasta,
            String codigoInstrumento,
            String fechaOperacion,
            Long idOperacion,
            int numeroPagina,
            int registrosPorPagina,
            String campoOrdaniento,
            String sentidoOrdenamiento
    ) {

        StoredProcedurePaginator paginator = new StoredProcedurePaginator(entityManager);

        List<ProcedureParam> params = List.of(
                new ProcedureParam(1, BigDecimal.class, ParameterMode.IN, idSubasta),
                new ProcedureParam(2, String.class, ParameterMode.IN, codigoInstrumento),
                new ProcedureParam(3, String.class, ParameterMode.IN, fechaOperacion),
                new ProcedureParam(4, Long.class, ParameterMode.IN, idOperacion),
                new ProcedureParam(5, Integer.class, ParameterMode.IN, numeroPagina),
                new ProcedureParam(6, Integer.class, ParameterMode.IN, registrosPorPagina),
                new ProcedureParam(7, String.class, ParameterMode.IN, campoOrdaniento),
                new ProcedureParam(8, String.class, ParameterMode.IN, sentidoOrdenamiento),
                new ProcedureParam(9, Integer.class, ParameterMode.OUT, null),
                new ProcedureParam(10, String.class, ParameterMode.OUT, null),
                new ProcedureParam(11, String.class, ParameterMode.OUT, null),
                new ProcedureParam(12, void.class, ParameterMode.REF_CURSOR, null)
        );

        return paginator.executeWithResult(Constantes.PACKAGE_BD + "." + Constantes.SPS_BUSCAR_TENENCIA,
                params,
                row -> {
                    TenenciaDTO dto = new TenenciaDTO();

                    dto.setIdTenencia((Long) row[1]);
                    dto.setCodValor((String) row[2]);
                    dto.setCodigoInstrumento((String) row[3]);
                    dto.setFechaTenencia((String) row[4]);
                    dto.setTotalValorizado((BigDecimal) row[5]);
                    dto.setTotalNominal((BigDecimal) row[6]);

                    return dto;
                },
                10, 11, 12, 9, registrosPorPagina
        );
    }

}
