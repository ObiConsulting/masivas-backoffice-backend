package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.DetalleConsultaProcesoDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroProcesoDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.entity.TpProceso;
import com.novatronic.masivas.backoffice.exception.DataBaseException;
import com.novatronic.masivas.backoffice.exception.GenericException;
import com.novatronic.masivas.backoffice.exception.NoOperationExistsException;
import com.novatronic.masivas.backoffice.repository.ProcesoRepository;
import com.novatronic.novalog.audit.util.Estado;
import com.novatronic.novalog.audit.util.Evento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import com.novatronic.novalog.audit.logger.NovaLogger;
import jakarta.transaction.RollbackException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.dao.DataAccessException;

/**
 *
 * @author Obi Consulting
 */
@Service
public class ProcesoService {

    @Autowired
    private final ProcesoRepository procesoRepository;

    private static final NovaLogger LOGGER = NovaLogger.getLogger(ProcesoService.class);

    public ProcesoService(ProcesoRepository procesoRepository) {
        this.procesoRepository = procesoRepository;
    }

    /**
     * Método que obtiene el registro de un proceso según codigo operación
     *
     * @param request
     * @return
     */
    public Map<String, List<DetalleConsultaProcesoDTO>> buscarProceso(FiltroMasivasRequest request) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.PROCESO, ConstantesServices.METODO_OBTENER, request.toStringSchedulerObtener());

            List<String> codServersAFiltrar = Arrays.asList("2000");
            List<String> codigoAccionesAFiltrar = Arrays.asList("1", "2", "4");

            List<TpProceso> procesosFiltrados = procesoRepository.findByCodServerInAndCodigoAccionIn(codServersAFiltrar, codigoAccionesAFiltrar);

            return procesosFiltrados.stream()
                    .collect(Collectors.groupingBy(
                            TpProceso::getCodigoOperacion, // La clave del grupo será codigoOperacion
                            Collectors.mapping(
                                    (TpProceso proceso) -> new DetalleConsultaProcesoDTO(
                                            proceso.getCodServer(),
                                            proceso.getCodigoAccion(),
                                            proceso.getHorario().substring(2)
                                    ),
                                    Collectors.toList() // Los valores serán una lista de DetalleProcesoDTO
                            )
                    ));
        } catch (DataAccessException e) {
            throw new DataBaseException(e);
        } catch (Exception e) {
            throw new GenericException(e);
        }

    }

    /**
     * Método que realiza la actualización de una ruta
     *
     * @param request
     * @param usuario
     * @return
     */
    public Long editarProceso(List<DetalleRegistroProcesoDTO> request, String usuario) {

        try {

            for (DetalleRegistroProcesoDTO procesoDetalle : request) {

                logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.PROCESO, ConstantesServices.METODO_ACTUALIZAR, procesoDetalle.toStringProceso());

                TpProceso proceso = procesoRepository.findById(procesoDetalle.getIdProceso())
                        .orElseThrow(() -> new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA));
                updateProceso(proceso, procesoDetalle, usuario, ConstantesServices.OPERACION_EDITAR);
                procesoRepository.save(proceso);
            }
            return 1l;

        } catch (NoOperationExistsException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new DataBaseException(e);
        } catch (Exception e) {
            Throwable excepcion = e.getCause();
            if (excepcion instanceof RollbackException) {
                throw new DataBaseException(e);
            }
            throw new GenericException(e);
        }
    }

    /**
     * Método que obtiene el registro de un proceso según codigo operación
     *
     * @param request
     * @return
     */
    public Map<String, List<DetalleRegistroProcesoDTO>> obtenerProceso(FiltroMasivasRequest request) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.PROCESO, ConstantesServices.METODO_OBTENER, request.toStringSchedulerObtener());

            List<String> codServersAFiltrar = Arrays.asList("1000", "2000");
            List<String> codigoAccionesAFiltrar = Arrays.asList("1", "2", "4");

            List<TpProceso> procesosFiltrados = procesoRepository.findByCodServerInAndCodigoAccionInAndCodigoOperacion(codServersAFiltrar, codigoAccionesAFiltrar, request.getCodOperacion());

            return procesosFiltrados.stream()
                    .collect(Collectors.groupingBy(
                            TpProceso::getCodigoOperacion, // La clave del grupo será codigoOperacion
                            Collectors.mapping(
                                    (TpProceso proceso) -> new DetalleRegistroProcesoDTO(
                                            proceso.getIdProceso(),
                                            proceso.getCodServer(),
                                            proceso.getCodigoAccion(),
                                            proceso.getHorario().substring(2),
                                            proceso.getUsuCreacion(),
                                            proceso.getFecCreacion(),
                                            proceso.getUsuModificacion(),
                                            proceso.getFecModificacion()
                                    ),
                                    Collectors.toList() // Los valores serán una lista de DetalleProcesoDTO
                            )
                    ));
        } catch (DataAccessException e) {
            throw new DataBaseException(e);
        } catch (Exception e) {
            throw new GenericException(e);
        }

    }

    private void updateProceso(TpProceso ruta, DetalleRegistroProcesoDTO request, String usuario, String operacion) {

        if (operacion.equals(ConstantesServices.OPERACION_EDITAR)) {
            ruta.setHorario(ConstantesServices.CRONTAB_JAVA + ConstantesServices.BLANCO + request.getHorario());
        }
        ruta.setFecModificacion(LocalDateTime.now());
        ruta.setUsuModificacion(usuario);
    }

    public void logEvento(String mensaje, Object... param) {
        LOGGER.info(mensaje, param);
    }

    public <T> void logAuditoria(T request, Evento idEvento, Estado estado, UserContext userContext, String recursoAfectado, String origen, String mensajeRespuesta, String codigoRespuesta) {
        LOGGER.audit(null, request, idEvento, estado, userContext.getUsername(), userContext.getScaProfile(), recursoAfectado, userContext.getIp(),
                ConstantesServices.VACIO, origen, null,null, mensajeRespuesta, codigoRespuesta);
    }

}
