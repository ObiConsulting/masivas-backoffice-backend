package com.novatronic.masivas.backoffice.dao;

import com.novatronic.masivas.backoffice.dto.ProcedureParam;
import com.novatronic.masivas.backoffice.dto.ResultadoOperacionConsulta;
import com.novatronic.masivas.backoffice.util.Constantes;
import com.novatronic.masivas.backoffice.util.FuncionesUtil;
import com.novatronic.novalog.audit.logger.NovaLogger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.LinkedHashMap;

/**
 *
 * @author Obi Consulting
 */
public class StoredProcedurePaginator {

    private final EntityManager entityManager;
    private static final NovaLogger LOGGER = NovaLogger.getLogger(StoredProcedurePaginator.class);

    public StoredProcedurePaginator(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Map<Integer, Object> execute(String procedureName, List<ProcedureParam> parameters) {
        try {
            StoredProcedureQuery query = buildQuery(procedureName, parameters);
            query.execute();
            Map<Integer, Object> outputValues = new LinkedHashMap<>();
            for (ProcedureParam param : parameters) {
                if (param.getMode() == ParameterMode.OUT || param.getMode() == ParameterMode.INOUT) {
                    outputValues.put(param.getPosition(), query.getOutputParameterValue(param.getPosition()));
                }
            }
            return outputValues;
        } catch (Exception e) {
            LOGGER.error(e);
            throw new RuntimeException("Ocurrió un error al ejecutar el SP " + procedureName, e);
        }
    }

    public <T> ResultadoOperacionConsulta<T> executeWithResult(
            String spFullName,
            List<ProcedureParam> parameters,
            Function<Object[], T> rowMapper,
            int posCodigoResultado,
            int posMensajeError,
            int posCursor,
            Integer posTotalRegistros, // puede ser null si no se usa paginación
            Integer tamPagina // puede ser null si no se usa paginación
    ) {
        ResultadoOperacionConsulta<T> resultado = new ResultadoOperacionConsulta<>();

        try {
            StoredProcedureQuery query = buildQuery(spFullName, parameters);
            query.execute();

            String codigoResultado = (String) query.getOutputParameterValue(posCodigoResultado);
            String mensajeError = (String) query.getOutputParameterValue(posMensajeError);

            if (!Constantes.OK_BD.equals(codigoResultado)) {
                return ResultadoOperacionConsulta.error(mensajeError);
            }

            @SuppressWarnings("unchecked")
            List<Object[]> result = query.getResultList();
            List<T> contenido = result.stream().map(rowMapper).toList();

            resultado.setCodigoResultado(codigoResultado);
            resultado.setMensajeError(mensajeError);
            resultado.setContenido(contenido);

            if (posTotalRegistros != null && tamPagina != null) {
                Integer total = (Integer) query.getOutputParameterValue(posTotalRegistros);
                resultado.setTotalRegistros(total);
                resultado.setTotalPaginas(FuncionesUtil.obtenerCantidadPaginas(total, tamPagina));
            }

            return resultado;

        } catch (Exception e) {
            LOGGER.error(e);
            return ResultadoOperacionConsulta.error(Constantes.MENSAJE_ERROR_BD_USUARIO);
        }
    }

    public <T> ResultadoOperacionConsulta<T> executeSingleResult(
            String spFullName,
            List<ProcedureParam> parameters,
            Function<Object[], T> rowMapper,
            int posCursor
    ) {
        ResultadoOperacionConsulta<T> resultado = new ResultadoOperacionConsulta<>();

        try {
            StoredProcedureQuery query = buildQuery(spFullName, parameters);
            query.execute();

//            String codigoResultado = (String) query.getOutputParameterValue(posCodigoResultado);
//            String mensajeError = (String) query.getOutputParameterValue(posMensajeError);
//
//            if (!Constantes.OK_BD.equals(codigoResultado)) {
//                return ResultadoOperacionConsulta.error(mensajeError);
//            }
            @SuppressWarnings("unchecked")
            List<Object[]> result = query.getResultList();

            if (result.isEmpty()) {
                return ResultadoOperacionConsulta.error("No se encontraron resultados.");
            }

            T data = rowMapper.apply(result.get(0));
            resultado.setCodigoResultado(Constantes.OK_BD);
            //resultado.setMensajeError(mensajeError);
            resultado.setObjeto(data);

            return resultado;

        } catch (Exception e) {
            LOGGER.error(e);
            return ResultadoOperacionConsulta.error(Constantes.MENSAJE_ERROR_BD_USUARIO);
        }
    }

    public StoredProcedureQuery buildQuery(String procedureName, List<ProcedureParam> parameters) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery(procedureName);

        for (ProcedureParam param : parameters) {
            query.registerStoredProcedureParameter(param.getPosition(), param.getType(), param.getMode());
            if (param.getMode() == ParameterMode.IN || param.getMode() == ParameterMode.INOUT) {
                query.setParameter(param.getPosition(), param.getValue());
            }
        }

        return query;
    }

}
