package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaRutaDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroRutaDTO;
import com.novatronic.masivas.backoffice.entity.TpRuta;
import com.novatronic.masivas.backoffice.exception.DataBaseException;
import com.novatronic.masivas.backoffice.exception.GenericException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import com.novatronic.masivas.backoffice.repository.RutaRepository;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import com.novatronic.novalog.audit.logger.NovaLogger;
import com.novatronic.novalog.audit.util.Estado;
import com.novatronic.novalog.audit.util.Evento;
import jakarta.transaction.RollbackException;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 *
 * @author Obi Consulting
 */
@Service
public class RutaService {

    @Autowired
    private final RutaRepository rutaRepository;
    private final MessageSource messageSource;

    private static final NovaLogger LOGGER = NovaLogger.getLogger(RutaService.class);

    public RutaService(RutaRepository rutaRepository, MessageSource messageSource) {
        this.rutaRepository = rutaRepository;
        this.messageSource = messageSource;
    }

    /**
     * Método que realiza la búsqueda de las rutas según filtros de búsqueda
     *
     * @param request
     * @param usuario
     * @return
     */
    public CustomPaginate<DetalleConsultaRutaDTO> buscarRuta(FiltroMasivasRequest request, String usuario) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.RUTA, ConstantesServices.METODO_CONSULTAR, request.toStringRuta());

            Pageable pageable = null;

            if (request.getCampoOrdenar().isEmpty()) {
                pageable = PageRequest.of(request.getNumeroPagina(), request.getRegistrosPorPagina());
            } else {
                if (ConstantesServices.ORDEN_ASC.equals(request.getSentidoOrdenar())) {
                    pageable = PageRequest.of(request.getNumeroPagina(), request.getRegistrosPorPagina(), Sort.by(request.getCampoOrdenar()).ascending());
                } else if (ConstantesServices.ORDEN_DESC.equals(request.getSentidoOrdenar())) {
                    pageable = PageRequest.of(request.getNumeroPagina(), request.getRegistrosPorPagina(), Sort.by(request.getCampoOrdenar()).descending());
                }
            }

            Page<DetalleConsultaRutaDTO> objPageable = rutaRepository.buscarPorFiltros(request.getCodTipoArchivo(), request.getCodCategoriaDirectorio(), pageable);

            int totalPaginas = objPageable.getTotalPages();
            long totalRegistrosLong = objPageable.getTotalElements();
            int totalRegistros = (totalRegistrosLong > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) totalRegistrosLong;

            CustomPaginate customPaginate = new CustomPaginate<>(totalPaginas, totalRegistros, objPageable.getContent());

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD_RESULTADOS, customPaginate.getTotalRegistros());

            return customPaginate;

        } catch (Exception e) {
            Throwable excepcion = e.getCause();
            if (excepcion instanceof RollbackException) {
                throw new DataBaseException(e);
            }
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
    public Long editarRuta(MasivasRequestDTO request, String usuario) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.RUTA, ConstantesServices.METODO_ACTUALIZAR, request.toStringRuta());

            TpRuta ruta = rutaRepository.findById(request.getIdRuta()).get();
            updateRuta(ruta, request, usuario, ConstantesServices.OPERACION_EDITAR);
            rutaRepository.save(ruta);

            return ruta.getIdRuta();

        } catch (Exception e) {
            Throwable excepcion = e.getCause();
            if (excepcion instanceof RollbackException) {
                excepcion = excepcion.getCause();
                throw new DataBaseException(e);
            }
            throw new GenericException(e);
        }
    }

    /**
     * Método que obtiene el registro de una ruta según id
     *
     * @param request
     * @param usuario
     * @return
     */
    public DetalleRegistroRutaDTO obtenerRuta(FiltroMasivasRequest request, String usuario) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.RUTA, ConstantesServices.METODO_OBTENER, request.toStringRutaObtener());
            DetalleRegistroRutaDTO rutaDTO = rutaRepository.buscarPorId(request.getIdRuta());

            return rutaDTO;

        } catch (Exception e) {
            Throwable excepcion = e.getCause();
            if (excepcion instanceof RollbackException) {
                throw new DataBaseException(e);
            }
            throw new GenericException(e);
        }
    }

    private void updateRuta(TpRuta ruta, MasivasRequestDTO request, String usuario, String operacion) {

        if (operacion.equals(ConstantesServices.OPERACION_EDITAR)) {
            ruta.setRuta(request.getRuta());
        } else {
            ruta.setEstado(request.getEstado());
        }

        ruta.setFecModificacion(LocalDateTime.now());
        ruta.setUsuModificacion(usuario);
    }

    public void logEvento(String mensaje, Object... param) {
        LOGGER.info(mensaje, param);
    }

    public <T> void logAuditoria(T request, Evento evento, Estado estado, UserContext userContext, String nombreTabla, String accion, String mensajeExito) {
        LOGGER.audit(null, request, evento, estado, userContext.getUsername(), userContext.getScaProfile(), nombreTabla, userContext.getIp(),
                null, accion, null, null, mensajeExito);
    }

}
