package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.DetalleConsultaProcesoDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroProcesoDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.entity.TpProceso;
import com.novatronic.masivas.backoffice.exception.DataBaseException;
import com.novatronic.masivas.backoffice.exception.GenericException;
import com.novatronic.masivas.backoffice.repository.ProcesoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import com.novatronic.novalog.audit.logger.NovaLogger;
import com.novatronic.novalog.audit.util.Estado;
import com.novatronic.novalog.audit.util.Evento;
import jakarta.transaction.RollbackException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Obi Consulting
 */
@Service
public class ProcesoService {

    @Autowired
    private final ProcesoRepository procesoRepository;
    private final MessageSource messageSource;

    private static final NovaLogger LOGGER = NovaLogger.getLogger(ProcesoService.class);

    public ProcesoService(ProcesoRepository procesoRepository, MessageSource messageSource) {
        this.procesoRepository = procesoRepository;
        this.messageSource = messageSource;
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
                                            proceso.getHorario()
                                    ),
                                    Collectors.toList() // Los valores serán una lista de DetalleProcesoDTO
                            )
                    ));

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
                                            proceso.getHorario(),
                                            proceso.getUsuCreacion(),
                                            proceso.getFecCreacion(),
                                            proceso.getUsuModificacion(),
                                            proceso.getFecModificacion()
                                    ),
                                    Collectors.toList() // Los valores serán una lista de DetalleProcesoDTO
                            )
                    ));

        } catch (Exception e) {
            Throwable excepcion = e.getCause();
            if (excepcion instanceof RollbackException) {
                throw new DataBaseException(e);
            }
            throw new GenericException(e);
        }

    }

//    /**
//     * Método que realiza la actualización de una ruta
//     *
//     * @param request
//     * @param usuario
//     * @return
//     */
//    public Long editarRuta(MasivasRequestDTO request, String usuario) {
//
//        try {
//
//            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.RUTA, ConstantesServices.METODO_ACTUALIZAR, request.toStringRuta());
//
//            TpRuta ruta = rutaRepository.findById(request.getIdRuta())
//                    .orElseThrow(() -> new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA));
//            updateRuta(ruta, request, usuario, ConstantesServices.OPERACION_EDITAR);
//            rutaRepository.save(ruta);
//
//            return ruta.getIdRuta();
//
//        } catch (NoOperationExistsException e) {
//            throw e;
//        } catch (Exception e) {
//            Throwable excepcion = e.getCause();
//            if (excepcion instanceof RollbackException) {
//                excepcion = excepcion.getCause();
//                throw new DataBaseException(e);
//            }
//            throw new GenericException(e);
//        }
//    }
//
//    /**
//     * Método que obtiene el registro de una ruta según id
//     *
//     * @param request
//     * @param usuario
//     * @return
//     */
//    public DetalleRegistroRutaDTO obtenerRuta(FiltroMasivasRequest request, String usuario) {
//
//        try {
//
//            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.RUTA, ConstantesServices.METODO_OBTENER, request.toStringRutaObtener());
//            DetalleRegistroRutaDTO rutaDTO = rutaRepository.buscarPorId(request.getIdRuta());
//
//            return rutaDTO;
//
//        } catch (Exception e) {
//            Throwable excepcion = e.getCause();
//            if (excepcion instanceof RollbackException) {
//                throw new DataBaseException(e);
//            }
//            throw new GenericException(e);
//        }
//    }
//
//    private void updateRuta(TpRuta ruta, MasivasRequestDTO request, String usuario, String operacion) {
//
//        if (operacion.equals(ConstantesServices.OPERACION_EDITAR)) {
//            ruta.setRuta(request.getRuta());
//        } else {
//            ruta.setEstado(request.getEstado());
//        }
//
//        ruta.setFecModificacion(LocalDateTime.now());
//        ruta.setUsuModificacion(usuario);
//    }
    public void logEvento(String mensaje, Object... param) {
        LOGGER.info(mensaje, param);
    }

    public <T> void logAuditoria(T request, Evento evento, Estado estado, UserContext userContext, String nombreTabla, String accion, String mensajeExito) {
        LOGGER.audit(null, request, evento, estado, userContext.getUsername(), userContext.getScaProfile(), nombreTabla, userContext.getIp(),
                null, accion, null, null, mensajeExito);
    }

}
