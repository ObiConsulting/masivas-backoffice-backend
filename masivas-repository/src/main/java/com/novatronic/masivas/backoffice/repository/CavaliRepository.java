package com.novatronic.masivas.backoffice.repository;

import com.novatronic.masivas.backoffice.dao.StoredProcedurePaginator;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaCavaliDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroCavaliDTO;
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
public class CavaliRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultadoOperacion crearOperacion(
            String codConcepto,
            String codMoneda,
            String codEntOrigen,
            BigDecimal monto,
            String instrucciones,
            String fecha,
            String numRefCavali,
            String codSAB,
            String cciSAB,
            String codParticipante,
            String usuarioCreacion
    ) {

        StoredProcedurePaginator spDao = new StoredProcedurePaginator(entityManager);
        List<ProcedureParam> params = List.of(
                new ProcedureParam(1, String.class, ParameterMode.IN, codConcepto),
                new ProcedureParam(2, String.class, ParameterMode.IN, codMoneda),
                new ProcedureParam(3, String.class, ParameterMode.IN, codEntOrigen),
                new ProcedureParam(4, BigDecimal.class, ParameterMode.IN, monto),
                new ProcedureParam(5, String.class, ParameterMode.IN, instrucciones),
                new ProcedureParam(6, String.class, ParameterMode.IN, fecha),
                new ProcedureParam(7, String.class, ParameterMode.IN, numRefCavali),
                new ProcedureParam(8, String.class, ParameterMode.IN, codSAB),
                new ProcedureParam(9, String.class, ParameterMode.IN, cciSAB),
                new ProcedureParam(10, String.class, ParameterMode.IN, codParticipante),
                new ProcedureParam(11, String.class, ParameterMode.IN, usuarioCreacion),
                new ProcedureParam(12, BigDecimal.class, ParameterMode.OUT, null),
                new ProcedureParam(13, String.class, ParameterMode.OUT, null),
                new ProcedureParam(14, String.class, ParameterMode.OUT, null)
        );

        try {
            Map<Integer, Object> out = spDao.execute(Constantes.PACKAGE_BD + "." + Constantes.SPI_INSERTAR_TRANSFCAVALI, params);

            return new ResultadoOperacion(
                    (BigDecimal) out.get(12),
                    (String) out.get(13),
                    (String) out.get(14)
            );
        } catch (Exception e) {
            return new ResultadoOperacion(null, Constantes.ERROR_BD, Constantes.MENSAJE_ERROR_BD_USUARIO);
        }
    }

    public ResultadoOperacion actualizarOperacion(
            Long idOperacion,
            String codConcepto,
            String codMoneda,
            String codEntOrigen,
            BigDecimal monto,
            String instrucciones,
            String fecha,
            String numRefCavali,
            String codSAB,
            String cciSAB,
            String codParticipante,
            String usuarioModificacion
    ) {

        StoredProcedurePaginator spDao = new StoredProcedurePaginator(entityManager);
        try {
            List<ProcedureParam> params = List.of(
                    new ProcedureParam(1, Long.class, ParameterMode.IN, idOperacion),
                    new ProcedureParam(2, String.class, ParameterMode.IN, codConcepto),
                    new ProcedureParam(3, String.class, ParameterMode.IN, codMoneda),
                    new ProcedureParam(4, String.class, ParameterMode.IN, codEntOrigen),
                    new ProcedureParam(5, BigDecimal.class, ParameterMode.IN, monto),
                    new ProcedureParam(6, String.class, ParameterMode.IN, instrucciones),
                    new ProcedureParam(7, String.class, ParameterMode.IN, fecha),
                    new ProcedureParam(8, String.class, ParameterMode.IN, numRefCavali),
                    new ProcedureParam(9, String.class, ParameterMode.IN, codSAB),
                    new ProcedureParam(10, String.class, ParameterMode.IN, cciSAB),
                    new ProcedureParam(11, String.class, ParameterMode.IN, codParticipante),
                    new ProcedureParam(12, String.class, ParameterMode.IN, usuarioModificacion),
                    new ProcedureParam(13, String.class, ParameterMode.OUT, null),
                    new ProcedureParam(14, String.class, ParameterMode.OUT, null)
            );

            Map<Integer, Object> result = spDao.execute(
                    Constantes.PACKAGE_BD + "." + Constantes.SPU_MODIFICAR_TRANSFCAVALI,
                    params
            );
            return new ResultadoOperacion(
                    idOperacion != null ? BigDecimal.valueOf(idOperacion) : null,
                    (String) result.get(13),
                    (String) result.get(14)
            );

        } catch (Exception e) {
            return new ResultadoOperacion(null, Constantes.ERROR_BD, Constantes.MENSAJE_ERROR_BD_USUARIO);
        }
    }

    public ResultadoOperacionConsulta<DetalleConsultaCavaliDTO> bucarTransferenciaCavali(FiltroOperacionRequest request, String usuario) {

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
                new ProcedureParam(3, String.class, ParameterMode.IN, request.getNumOperacionLBTR()),
                new ProcedureParam(4, String.class, ParameterMode.IN, request.getNumReferenciaBCRP()),
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

        return paginator.executeWithResult(Constantes.PACKAGE_BD + "." + Constantes.SPS_BUSCAR_TRANSFCAVALI,
                params,
                row -> {
                    DetalleConsultaCavaliDTO dto = new DetalleConsultaCavaliDTO();
                    dto.setIdOperacion((Long) row[1]);
                    dto.setNumRefLbtr((String) row[2]);
                    dto.setNumRefBcrp((String) row[3]);
                    dto.setConcepto((String) row[5]);
                    dto.setFecha((String) row[6]);
                    dto.setMonto((BigDecimal) row[7]);
                    dto.setSimboloMoneda((String) row[8]);
                    dto.setEstado((String) row[10]);
                    dto.setFechaNegociacion((String) row[14]);
                    dto.setNumReferencia((String) row[15]);
                    return dto;
                },
                13, 14, 15, 12, request.getRegistrosPorPagina()
        );
    }

    public ResultadoOperacionConsulta<DetalleRegistroCavaliDTO> obtenerDetalle(Long idOperacion, String numOperacionLBTR) {
        StoredProcedurePaginator dao = new StoredProcedurePaginator(entityManager);

        List<ProcedureParam> params = List.of(
                new ProcedureParam(1, Long.class, ParameterMode.IN, idOperacion),
                new ProcedureParam(2, void.class, ParameterMode.REF_CURSOR, null)
        );

        return dao.executeSingleResult(Constantes.PACKAGE_BD + "." + Constantes.SPS_DATA_TRANSFCAVALI,
                params,
                row -> {
                    DetalleRegistroCavaliDTO dto = new DetalleRegistroCavaliDTO();
                    dto.setIdOperacion((Long) row[0]);
                    dto.setFecha((String) row[1]);
                    dto.setNumRefLbtr((String) row[3]);
                    dto.setNumRefBcrp((String) row[4]);
                    dto.setConcepto((String) row[5]);
                    dto.setMoneda((String) row[7]);
                    dto.setEntidadOrigen((String) row[9]);
                    dto.setNumCuentaEntOrigen((String) row[10]);
                    dto.setEntidadDestino((String) row[11]);
                    dto.setNumCuentaEntDestino((String) row[13]);
                    dto.setMonto((BigDecimal) row[14]);
                    dto.setInstrucciones((String) row[15]);
                    dto.setFechaNegociacion((String) row[16]);
                    dto.setNumReferencia((String) row[17]);
                    dto.setCodSAB((String) row[18]);
                    dto.setCciSAB((String) row[19]);
                    dto.setTipoParticipante((String) row[20]);
                    dto.setUsuarioCreacion((String) row[22]);
                    dto.setFechaCreacion((String) row[23]);
                    dto.setUsuarioModificacion((String) row[24]);
                    dto.setFechaModificacion((String) row[25]);
                    dto.setCodEstado((String) row[26]);
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
                    Constantes.PACKAGE_BD + "." + Constantes.SPU_ANULAR_TRANSFCAVALI,
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
