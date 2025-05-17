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
public class SubastaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultadoOperacion insertarSubasta(
            String codInstrumento,
            String fecSubasta,
            String fecVencimiento,
            String fecOperacion,
            String montoAdjudicado,
            String numeroColocacion,
            String plazo,
            String numSubasta,
            String tipoOperacion,
            String descTipoOperacion,
            Long idOperacion,
            String usuarioCrea
    ) {

        StoredProcedurePaginator spDao = new StoredProcedurePaginator(entityManager);
        List<ProcedureParam> params = List.of(
                new ProcedureParam(1, String.class, ParameterMode.IN, codInstrumento),
                new ProcedureParam(2, String.class, ParameterMode.IN, fecSubasta),
                new ProcedureParam(3, String.class, ParameterMode.IN, fecVencimiento),
                new ProcedureParam(4, String.class, ParameterMode.IN, fecOperacion),
                new ProcedureParam(5, String.class, ParameterMode.IN, montoAdjudicado),
                new ProcedureParam(6, String.class, ParameterMode.IN, numeroColocacion),
                new ProcedureParam(7, String.class, ParameterMode.IN, plazo),
                new ProcedureParam(8, String.class, ParameterMode.IN, numSubasta),
                new ProcedureParam(9, String.class, ParameterMode.IN, tipoOperacion),
                new ProcedureParam(10, String.class, ParameterMode.IN, descTipoOperacion),
                new ProcedureParam(11, Long.class, ParameterMode.IN, idOperacion),
                new ProcedureParam(12, String.class, ParameterMode.IN, usuarioCrea),
                new ProcedureParam(13, BigDecimal.class, ParameterMode.OUT, null),
                new ProcedureParam(14, String.class, ParameterMode.OUT, null),
                new ProcedureParam(15, String.class, ParameterMode.OUT, null)
        );

        try {
            Map<Integer, Object> out = spDao.execute(Constantes.PACKAGE_BD + "." + Constantes.SPI_INSERTAR_SUBASTA, params);
            return new ResultadoOperacion(
                    (BigDecimal) out.get(13),
                    (String) out.get(14),
                    (String) out.get(15)
            );
        } catch (Exception e) {
            return new ResultadoOperacion(null, Constantes.ERROR_BD, Constantes.MENSAJE_ERROR_BD_USUARIO);
        }
    }

    public ResultadoOperacion eliminarSubasta(
            Long idOperacion
    ) {

        StoredProcedurePaginator spDao = new StoredProcedurePaginator(entityManager);
        List<ProcedureParam> params = List.of(
                new ProcedureParam(1, Long.class, ParameterMode.IN, idOperacion),
                new ProcedureParam(2, String.class, ParameterMode.OUT, null),
                new ProcedureParam(3, String.class, ParameterMode.OUT, null)
        );
        try {
            Map<Integer, Object> out = spDao.execute(Constantes.PACKAGE_BD + "." + Constantes.SPU_ANULAR_SUBASTA, params);
            return new ResultadoOperacion(
                    null,
                    (String) out.get(2),
                    (String) out.get(3)
            );
        } catch (Exception e) {
            return new ResultadoOperacion(null, Constantes.ERROR_BD, Constantes.MENSAJE_ERROR_BD_USUARIO);
        }
    }

    public ResultadoOperacionConsulta<SubastaDTO> buscarSubasta(Long idOperacion) {

        StoredProcedurePaginator dao = new StoredProcedurePaginator(entityManager);

        List<ProcedureParam> params = List.of(
                new ProcedureParam(1, Long.class, ParameterMode.IN, idOperacion),
                new ProcedureParam(2, String.class, ParameterMode.OUT, null),
                new ProcedureParam(3, String.class, ParameterMode.OUT, null),
                new ProcedureParam(4, void.class, ParameterMode.REF_CURSOR, null)
        );

        return dao.executeSingleResult(Constantes.PACKAGE_BD + "." + Constantes.SPS_DATA_OP_SUBASTA,
                params,
                row -> {
                    SubastaDTO dto = new SubastaDTO();
                    dto.setIdSubasta((BigDecimal) row[0]);
                    dto.setNumSubasta((Integer) row[1]);
                    dto.setCodInstrumento((String) row[2]);
                    dto.setTipoOperacion((String) row[3]);
                    dto.setFechaVencimiento((String) row[4]);
                    dto.setMontoAdjudicado((BigDecimal) row[5]);
                    dto.setNumColocacion((Integer) row[6]);
                    dto.setPlazo((Integer) row[7]);
                    dto.setFechaSubasta((String) row[9]);
                    dto.setFechaOperacion((String) row[10]);

                    return dto;
                },
                4
        );
    }

}
