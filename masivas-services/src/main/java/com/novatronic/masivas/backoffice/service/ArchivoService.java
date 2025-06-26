package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoDirectorioDTO;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoMasivasDTO;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoTitularidadDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.exception.DataBaseException;
import com.novatronic.masivas.backoffice.exception.GenericException;
import com.novatronic.masivas.backoffice.repository.ArchivoDirectorioRepository;
import com.novatronic.masivas.backoffice.repository.ArchivoMasivasRepository;
import com.novatronic.masivas.backoffice.repository.ArchivoTitularidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import com.novatronic.novalog.audit.logger.NovaLogger;
import com.novatronic.novalog.audit.util.Estado;
import com.novatronic.novalog.audit.util.Evento;
import jakarta.transaction.RollbackException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 *
 * @author Obi Consulting
 */
@Service
public class ArchivoService {

    @Autowired
    private final ArchivoDirectorioRepository archivoDirectorioRepository;
    @Autowired
    private final ArchivoMasivasRepository archivoMasivasRepository;
    @Autowired
    private final ArchivoTitularidadRepository archivoTitularidadRepository;
    private final MessageSource messageSource;

    private static final NovaLogger LOGGER = NovaLogger.getLogger(ArchivoService.class);

    public ArchivoService(ArchivoDirectorioRepository archivoDirectorioRepository, ArchivoMasivasRepository archivoMasivasRepository,
            ArchivoTitularidadRepository archivoTitularidadRepository, MessageSource messageSource) {
        this.archivoDirectorioRepository = archivoDirectorioRepository;
        this.archivoMasivasRepository = archivoMasivasRepository;
        this.archivoTitularidadRepository = archivoTitularidadRepository;
        this.messageSource = messageSource;
    }

    /**
     * Método que realiza la búsqueda de los archivos de tipo directorio según
     * filtros de búsqueda
     *
     * @param request
     * @param usuario
     * @return
     */
    public CustomPaginate<DetalleConsultaArchivoDirectorioDTO> buscarArchivoDirectorio(FiltroMasivasRequest request, String usuario) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.ARCHIVO_DIRECTORIO, ConstantesServices.METODO_CONSULTAR, request.toStringArchivoDirectorio());

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
            LocalDateTime fechaInicio = request.getFechaInicio().atStartOfDay();
            LocalDateTime fechaFin = request.getFechaFin().atTime(LocalTime.MAX);

            Page<DetalleConsultaArchivoDirectorioDTO> objPageable = archivoDirectorioRepository.buscarPorFiltros(fechaInicio, fechaFin, request.getEstado(), pageable);

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
     * Método que realiza la búsqueda de los archivos de tipo masivas según
     * filtros de búsqueda
     *
     * @param request
     * @param usuario
     * @return
     */
    public CustomPaginate<DetalleConsultaArchivoMasivasDTO> buscarArchivoMasivas(FiltroMasivasRequest request, String usuario) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.ARCHIVO_MASIVAS, ConstantesServices.METODO_CONSULTAR, request.toStringArchivoMasivas());

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

            Page<DetalleConsultaArchivoMasivasDTO> objPageable = archivoMasivasRepository.buscarPorFiltros(request.getFechaInicioObtencion(), request.getFechaFinObtencion(),
                    request.getFechaInicioProcesada(), request.getFechaFinProcesada(), request.getEstado(), pageable);

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
     * Método que realiza la búsqueda de los archivos de tipo titularidad según
     * filtros de búsqueda
     *
     * @param request
     * @param usuario
     * @return
     */
    public CustomPaginate<DetalleConsultaArchivoTitularidadDTO> buscarArchivoTitularidad(FiltroMasivasRequest request, String usuario) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.ARCHIVO_TITULARIDAD, ConstantesServices.METODO_CONSULTAR, request.toStringArchivoTitularidad());

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

            Page<DetalleConsultaArchivoTitularidadDTO> objPageable = archivoTitularidadRepository.buscarPorFiltros(request.getFechaInicioObtencion(), request.getFechaFinObtencion(),
                    request.getFechaInicioProcesada(), request.getFechaFinProcesada(), request.getEstado(), pageable);

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

    public void logEvento(String mensaje, Object... param) {
        LOGGER.info(mensaje, param);
    }

    public <T> void logAuditoria(T request, Evento evento, Estado estado, UserContext userContext, String nombreTabla, String accion, String mensajeExito) {
        LOGGER.audit(null, request, evento, estado, userContext.getUsername(), userContext.getScaProfile(), nombreTabla, userContext.getIp(),
                null, accion, null, null, mensajeExito);
    }

}
