package com.novatronic.masivas.backoffice.repository;

import com.novatronic.masivas.backoffice.dao.StoredProcedurePaginator;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaInterbancariaDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroInterbancariaDTO;
import com.novatronic.masivas.backoffice.dto.FiltroOperacionRequest;
import com.novatronic.masivas.backoffice.dto.ProcedureParam;
import com.novatronic.masivas.backoffice.dto.ResultadoOperacion;
import com.novatronic.masivas.backoffice.dto.ResultadoOperacionConsulta;
import com.novatronic.masivas.backoffice.util.Constantes;
import com.novatronic.masivas.backoffice.util.FuncionesUtil;
import com.novatronic.novalog.audit.annotation.Audit;
import com.novatronic.novalog.audit.util.Evento;
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
public class InterbancariaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Audit(accion = Evento.EV_CONSULTA_REPORTE, origen = "interbancaria/insertar", tabla = "tp_operacion")
    public ResultadoOperacion insertarOperacionInterbancaria(
            String codConcepto,
            String codEntDestino,
            String codEntOrigen,
            String numCuentaOrigen,
            String numCuentaDestino,
            BigDecimal monto,
            String codMoneda,
            String observacion,
            String observacionCliente,
            Integer afectoItf,
            String idClienteDestino,
            String idClienteOrigen,
            String usuarioCrea
    ) {

        StoredProcedurePaginator spDao = new StoredProcedurePaginator(entityManager);
        List<ProcedureParam> params = List.of(
                new ProcedureParam(1, String.class, ParameterMode.IN, codConcepto),
                new ProcedureParam(2, String.class, ParameterMode.IN, codEntDestino),
                new ProcedureParam(3, String.class, ParameterMode.IN, codEntOrigen),
                new ProcedureParam(4, String.class, ParameterMode.IN, numCuentaOrigen),
                new ProcedureParam(5, String.class, ParameterMode.IN, numCuentaDestino),
                new ProcedureParam(6, BigDecimal.class, ParameterMode.IN, monto),
                new ProcedureParam(7, String.class, ParameterMode.IN, codMoneda),
                new ProcedureParam(8, String.class, ParameterMode.IN, observacion),
                new ProcedureParam(9, String.class, ParameterMode.IN, observacionCliente),
                new ProcedureParam(10, Integer.class, ParameterMode.IN, afectoItf),
                new ProcedureParam(11, String.class, ParameterMode.IN, idClienteDestino),
                new ProcedureParam(12, String.class, ParameterMode.IN, idClienteOrigen),
                new ProcedureParam(13, String.class, ParameterMode.IN, usuarioCrea),
                new ProcedureParam(14, BigDecimal.class, ParameterMode.OUT, null),
                new ProcedureParam(15, String.class, ParameterMode.OUT, null),
                new ProcedureParam(16, String.class, ParameterMode.OUT, null)
        );

        try {
            Map<Integer, Object> out = spDao.execute(Constantes.PACKAGE_BD + "." + Constantes.SPI_INSERTAR_INTERBANCARIA, params);

            return new ResultadoOperacion(
                    (BigDecimal) out.get(14),
                    (String) out.get(15),
                    (String) out.get(16)
            );
        } catch (Exception e) {
            return new ResultadoOperacion(null, Constantes.ERROR_BD, Constantes.MENSAJE_ERROR_BD_USUARIO);
        }
    }

    public ResultadoOperacion actualizarOperacionInterbancaria(
            Long idOperacion,
            String codConcepto,
            String codEntDestino,
            String codEntOrigen,
            String numCuentaOrigen,
            String numCuentaDestino,
            BigDecimal monto,
            String codMoneda,
            String observacion,
            String observacionCliente,
            Integer afectoItf,
            String idClienteDestino,
            String idClienteOrigen,
            String codEstadoNuevo,
            String codEstadoActual,
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
                    new ProcedureParam(7, BigDecimal.class, ParameterMode.IN, monto),
                    new ProcedureParam(8, String.class, ParameterMode.IN, codMoneda),
                    new ProcedureParam(9, String.class, ParameterMode.IN, observacion),
                    new ProcedureParam(10, String.class, ParameterMode.IN, observacionCliente),
                    new ProcedureParam(11, Integer.class, ParameterMode.IN, afectoItf),
                    new ProcedureParam(12, String.class, ParameterMode.IN, idClienteDestino),
                    new ProcedureParam(13, String.class, ParameterMode.IN, idClienteOrigen),
                    new ProcedureParam(14, String.class, ParameterMode.IN, codEstadoNuevo),
                    new ProcedureParam(15, String.class, ParameterMode.IN, codEstadoActual),
                    new ProcedureParam(16, String.class, ParameterMode.IN, usuarioModifica),
                    new ProcedureParam(17, String.class, ParameterMode.OUT, null),
                    new ProcedureParam(18, String.class, ParameterMode.OUT, null)
            );

            Map<Integer, Object> result = spDao.execute(
                    Constantes.PACKAGE_BD + "." + Constantes.SPU_MODIFICAR_INTERBANCARIA,
                    params
            );
            return new ResultadoOperacion(
                    idOperacion != null ? BigDecimal.valueOf(idOperacion) : null,
                    (String) result.get(17),
                    (String) result.get(18)
            );

        } catch (Exception e) {
            return new ResultadoOperacion(null, Constantes.ERROR_BD, Constantes.MENSAJE_ERROR_BD_USUARIO);
        }
    }

    @Audit(accion = Evento.EV_CONSULTA_REPORTE, origen = "interbancaria/buscar", tabla = "tp_operacion")
    public ResultadoOperacionConsulta<DetalleConsultaInterbancariaDTO> buscarInterbancarias(FiltroOperacionRequest request, String usuario) {

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
                new ProcedureParam(3, String.class, ParameterMode.IN, request.getCodConcepto()),
                new ProcedureParam(4, String.class, ParameterMode.IN, request.getCodEstado()),
                new ProcedureParam(5, String.class, ParameterMode.IN, request.getCodEntidadOrigen()),
                new ProcedureParam(6, String.class, ParameterMode.IN, request.getCodEntidadDestino()),
                new ProcedureParam(7, String.class, ParameterMode.IN, request.getCodMoneda()),
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

        return paginator.executeWithResult(Constantes.PACKAGE_BD + "." + Constantes.SPS_BUSCAR_INTERBANCARIA,
                params,
                row -> {
                    DetalleConsultaInterbancariaDTO dto = new DetalleConsultaInterbancariaDTO();
                    dto.setIdOperacion((Long) row[1]);
                    dto.setMoneda((String) row[3]);
                    dto.setNumRefLbtr((String) row[4]);
                    dto.setNumRefBcrp((String) row[5]);
                    dto.setConcepto((String) row[6]);
                    dto.setEntidadOrigen((String) row[7]);
                    dto.setEntidadDestino((String) row[8]);
                    dto.setFecha((String) row[9]);
                    dto.setMonto((BigDecimal) row[10]);
                    dto.setNumCuentaEntOrigen((String) row[11]);
                    dto.setClienteOrigen((String) row[13]);
                    dto.setClienteDestino((String) row[14]);
                    dto.setNumCuentaEntDestino((String) row[15]);
                    dto.setAbonoConfirmacionDescripcion((String) row[17]);
                    dto.setEstado((String) row[18]);
                    return dto;
                },
                14, 15, 16, 13, request.getRegistrosPorPagina()
        );
    }

    public ResultadoOperacionConsulta<DetalleRegistroInterbancariaDTO> obtenerDetalleInterbancaria(Long idOperacion, String numOperacionLBTR) {
        StoredProcedurePaginator dao = new StoredProcedurePaginator(entityManager);

        List<ProcedureParam> params = List.of(
                new ProcedureParam(1, Long.class, ParameterMode.IN, idOperacion),
                new ProcedureParam(2, String.class, ParameterMode.IN, numOperacionLBTR),
                new ProcedureParam(3, void.class, ParameterMode.REF_CURSOR, null)
        );

        return dao.executeSingleResult(Constantes.PACKAGE_BD + "." + Constantes.SPS_DATA_INTERBANCARIA,
                params,
                row -> {
                    DetalleRegistroInterbancariaDTO dto = new DetalleRegistroInterbancariaDTO();
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

                    dto.setIdClienteOrigen((Long) row[25]);
                    dto.setIdClienteDestino((Long) row[26]);

                    dto.setUsuarioCreacion((String) row[16]);
                    dto.setFechaCreacion((String) row[17]);
                    dto.setUsuarioModificacion((String) row[18]);
                    dto.setFechaModificacion((String) row[19]);
                    dto.setObservacion((String) row[20]);
                    dto.setObservacionCliente((String) row[22]);

                    dto.setCodEstado((String) row[21]);
                    dto.setEstado((String) row[2]);

                    dto.setAfectoItf(String.valueOf(row[24]));
                    dto.setAbonoConfirmacion((Integer) row[29]);
                    dto.setMonto((BigDecimal) row[15]);

                    return dto;
                },
                3 // posición REF_CURSOR
        );
    }

    public ResultadoOperacionConsulta<DetalleConsultaInterbancariaDTO> buscarAbonosCliente(FiltroOperacionRequest request, String usuario) {
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
                new ProcedureParam(3, String.class, ParameterMode.IN, request.getCodConcepto()),
                new ProcedureParam(4, String.class, ParameterMode.IN, request.getCodEntidadOrigen()),
                new ProcedureParam(5, String.class, ParameterMode.IN, request.getCodEntidadDestino()),
                new ProcedureParam(6, String.class, ParameterMode.IN, request.getCodMoneda()),
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

        return paginator.executeWithResult(Constantes.PACKAGE_BD + "." + Constantes.SPS_BUSCAR_CONFIRMAR_INTER,
                params,
                row -> {
                    DetalleConsultaInterbancariaDTO dto = new DetalleConsultaInterbancariaDTO();
                    dto.setIdOperacion((Long) row[1]);
                    dto.setMoneda((String) row[3]);
                    dto.setNumRefLbtr((String) row[4]);
                    dto.setNumRefBcrp((String) row[5]);
                    dto.setConcepto((String) row[6]);
                    dto.setEntidadOrigen((String) row[7]);
                    dto.setEntidadDestino((String) row[8]);
                    dto.setFecha((String) row[9]);
                    dto.setMonto((BigDecimal) row[10]);
                    dto.setNumCuentaEntOrigen((String) row[11]);

                    dto.setClienteOrigen((String) row[13]);
                    dto.setClienteDestino((String) row[14]);

                    dto.setNumCuentaEntDestino((String) row[15]);
                    dto.setAbonoConfirmacionDescripcion((String) row[17]);
                    dto.setEstado((String) row[18]);
                    return dto;
                },
                13, 14, 15, 12, request.getRegistrosPorPagina()
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
                    Constantes.PACKAGE_BD + "." + Constantes.SPU_ANULAR_INTERBANCARIA,
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
