package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoDirectorioDTO;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoMasivasDTO;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoTitularidadDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.ReporteDTO;
import com.novatronic.masivas.backoffice.exception.DataBaseException;
import com.novatronic.masivas.backoffice.exception.GenericException;
import com.novatronic.masivas.backoffice.exception.JasperReportException;
import com.novatronic.masivas.backoffice.repository.ArchivoDirectorioRepository;
import com.novatronic.masivas.backoffice.repository.ArchivoMasivasRepository;
import com.novatronic.masivas.backoffice.repository.ArchivoTitularidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import com.novatronic.masivas.backoffice.util.GenerarReporte;
import com.novatronic.masivas.backoffice.util.ServicesUtil;
import com.novatronic.novalog.audit.logger.NovaLogger;
import com.novatronic.novalog.audit.util.Estado;
import com.novatronic.novalog.audit.util.Evento;
import jakarta.transaction.RollbackException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.client.RestTemplate;

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
    private final GenericService genericService;
    private final RestTemplate restTemplate;

    @Value("${masivas.core.url}")
    private String apiCoreUrl;
    @Value("${reporte.logo}")
    private String logo;

    private static final NovaLogger LOGGER = NovaLogger.getLogger(ArchivoService.class);

    public ArchivoService(ArchivoDirectorioRepository archivoDirectorioRepository, ArchivoMasivasRepository archivoMasivasRepository, ArchivoTitularidadRepository archivoTitularidadRepository,
            GenericService genericService, RestTemplate restTemplate) {
        this.archivoDirectorioRepository = archivoDirectorioRepository;
        this.archivoMasivasRepository = archivoMasivasRepository;
        this.archivoTitularidadRepository = archivoTitularidadRepository;
        this.genericService = genericService;
        this.restTemplate = restTemplate;
    }

    /**
     * Método que realiza la búsqueda de los archivos de tipo directorio según
     * filtros de búsqueda
     *
     * @param request
     * @return
     */
    public CustomPaginate<DetalleConsultaArchivoDirectorioDTO> buscarArchivoDirectorio(FiltroMasivasRequest request) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.ARCHIVO_DIRECTORIO, ConstantesServices.METODO_CONSULTAR, request.toStringArchivoDirectorio());

            Pageable pageable = ServicesUtil.configurarPageSort(request);

            LocalDateTime fechaInicio = request.getFechaInicio().atStartOfDay();
            LocalDateTime fechaFin = request.getFechaFin().atTime(LocalTime.MAX);

            Page<DetalleConsultaArchivoDirectorioDTO> objPageable = archivoDirectorioRepository.buscarPorFiltros(fechaInicio, fechaFin, request.getEstado(), pageable);

            int totalPaginas = objPageable.getTotalPages();
            long totalRegistrosLong = objPageable.getTotalElements();
            int totalRegistros = (totalRegistrosLong > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) totalRegistrosLong;

            CustomPaginate<DetalleConsultaArchivoDirectorioDTO> customPaginate = new CustomPaginate<>(totalPaginas, totalRegistros, objPageable.getContent());

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
     * @return
     */
    public CustomPaginate<DetalleConsultaArchivoMasivasDTO> buscarArchivoMasivas(FiltroMasivasRequest request) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.ARCHIVO_MASIVAS, ConstantesServices.METODO_CONSULTAR, request.toStringArchivoMasivas());

            Pageable pageable = ServicesUtil.configurarPageSort(request);

            Page<DetalleConsultaArchivoMasivasDTO> objPageable = archivoMasivasRepository.buscarPorFiltros(request.getFechaInicioObtencion(), request.getFechaFinObtencion(),
                    request.getFechaInicioProcesada(), request.getFechaFinProcesada(), request.getEstado(), pageable);

            int totalPaginas = objPageable.getTotalPages();
            long totalRegistrosLong = objPageable.getTotalElements();
            int totalRegistros = (totalRegistrosLong > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) totalRegistrosLong;

            CustomPaginate<DetalleConsultaArchivoMasivasDTO> customPaginate = new CustomPaginate<>(totalPaginas, totalRegistros, objPageable.getContent());

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
     * @return
     */
    public CustomPaginate<DetalleConsultaArchivoTitularidadDTO> buscarArchivoTitularidad(FiltroMasivasRequest request) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.ARCHIVO_TITULARIDAD, ConstantesServices.METODO_CONSULTAR, request.toStringArchivoTitularidad());

            Pageable pageable = ServicesUtil.configurarPageSort(request);

            Page<DetalleConsultaArchivoTitularidadDTO> objPageable = archivoTitularidadRepository.buscarPorFiltros(request.getFechaInicioObtencion(), request.getFechaFinObtencion(),
                    request.getFechaInicioProcesada(), request.getFechaFinProcesada(), request.getEstado(), pageable);

            int totalPaginas = objPageable.getTotalPages();
            long totalRegistrosLong = objPageable.getTotalElements();
            int totalRegistros = (totalRegistrosLong > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) totalRegistrosLong;

            CustomPaginate<DetalleConsultaArchivoTitularidadDTO> customPaginate = new CustomPaginate<>(totalPaginas, totalRegistros, objPageable.getContent());

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
     * Método que realiza la búsqueda de los archivos de tipo directorio según
     * filtros de búsqueda y los exporta a un archivo pdf/xlsx
     *
     * @param request
     * @param usuario
     * @param tipoArchivo
     * @return
     */
    public ReporteDTO descargarArchivoDirectorio(FiltroMasivasRequest request, String usuario, String tipoArchivo) {

        try {
            request.setNumeroPagina(0);
            request.setRegistrosPorPagina(0);

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.ARCHIVO_DIRECTORIO, ConstantesServices.METODO_DESCARGAR, request.toStringArchivoDirectorio());
            CustomPaginate<DetalleConsultaArchivoDirectorioDTO> resultado = buscarArchivoDirectorio(request);

            HashMap<String, Object> parameters = new HashMap<>();
            //Filtros
            parameters.put("IN_FECHA_INICIO", ServicesUtil.formatearLocalDateToString(request.getFechaInicio()));
            parameters.put("IN_FECHA_FIN", ServicesUtil.formatearLocalDateToString(request.getFechaFin()));
            parameters.put(ConstantesServices.PARAM_IN_ESTADO, genericService.getNombreEstadoArchivo(request.getEstado()));

            return GenerarReporte.generarReporte(resultado.getContenido(), parameters, usuario, tipoArchivo, "reportes/reporteArchivoDirectorio.jrxml", "archivoDirectorio", logo);

        } catch (JasperReportException e) {
            throw e;
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
     * filtros de búsqueda y los exporta a un archivo pdf/xlsx
     *
     * @param request
     * @param usuario
     * @param tipoArchivo
     * @return
     */
    public ReporteDTO descargarArchivoMasivas(FiltroMasivasRequest request, String usuario, String tipoArchivo) {

        try {
            request.setNumeroPagina(0);
            request.setRegistrosPorPagina(0);

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.ARCHIVO_MASIVAS, ConstantesServices.METODO_DESCARGAR, request.toStringArchivoMasivas());
            CustomPaginate<DetalleConsultaArchivoMasivasDTO> resultado = buscarArchivoMasivas(request);

            HashMap<String, Object> parameters = new HashMap<>();
            //Filtros
            parameters.put("IN_FECHA_INICIO_OBTENCION", ServicesUtil.formatearLocalDateTimeToString(request.getFechaInicioObtencion()));
            parameters.put("IN_FECHA_FIN_OBTENCION", ServicesUtil.formatearLocalDateTimeToString(request.getFechaFinObtencion()));
            parameters.put("IN_FECHA_INICIO_PROCESADA", ServicesUtil.formatearLocalDateTimeToString(request.getFechaInicioProcesada()));
            parameters.put("IN_FECHA_FIN_PROCESADA", ServicesUtil.formatearLocalDateTimeToString(request.getFechaFinProcesada()));
            parameters.put(ConstantesServices.PARAM_IN_ESTADO, genericService.getNombreEstadoArchivo(request.getEstado()));

            return GenerarReporte.generarReporte(resultado.getContenido(), parameters, usuario, tipoArchivo, "reportes/reporteArchivoMasivas.jrxml", "archivoMasivas", logo);

        } catch (JasperReportException e) {
            throw e;
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
     * filtros de búsqueda y los exporta a un archivo pdf/xlsx
     *
     * @param request
     * @param usuario
     * @param tipoArchivo
     * @return
     */
    public ReporteDTO descargarArchivoTitularidad(FiltroMasivasRequest request, String usuario, String tipoArchivo) {

        try {
            request.setNumeroPagina(0);
            request.setRegistrosPorPagina(0);

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.ARCHIVO_TITULARIDAD, ConstantesServices.METODO_DESCARGAR, request.toStringArchivoTitularidad());
            CustomPaginate<DetalleConsultaArchivoTitularidadDTO> resultado = buscarArchivoTitularidad(request);

            HashMap<String, Object> parameters = new HashMap<>();
            //Filtros
            parameters.put("IN_FECHA_INICIO_OBTENCION", ServicesUtil.formatearLocalDateTimeToString(request.getFechaInicioObtencion()));
            parameters.put("IN_FECHA_FIN_OBTENCION", ServicesUtil.formatearLocalDateTimeToString(request.getFechaFinObtencion()));
            parameters.put("IN_FECHA_INICIO_PROCESADA", ServicesUtil.formatearLocalDateTimeToString(request.getFechaInicioProcesada()));
            parameters.put("IN_FECHA_FIN_PROCESADA", ServicesUtil.formatearLocalDateTimeToString(request.getFechaFinProcesada()));
            parameters.put(ConstantesServices.PARAM_IN_ESTADO, genericService.getNombreEstadoArchivo(request.getEstado()));

            return GenerarReporte.generarReporte(resultado.getContenido(), parameters, usuario, tipoArchivo, "reportes/reporteArchivoTitularidad.jrxml", "archivoTitularidad", logo);

        } catch (JasperReportException e) {
            throw e;
        } catch (Exception e) {
            Throwable excepcion = e.getCause();
            if (excepcion instanceof RollbackException) {
                throw new DataBaseException(e);
            }
            throw new GenericException(e);
        }

    }

    public void respaldarArchivo() {

    }

    public void logEvento(String mensaje, Object... param) {
        LOGGER.info(mensaje, param);
    }

    public <T> void logAuditoria(T request, Evento evento, Estado estado, UserContext userContext, String nombreTabla, String accion, String mensajeExito) {
        LOGGER.audit(null, request, evento, estado, userContext.getUsername(), userContext.getScaProfile(), nombreTabla, userContext.getIp(),
                null, accion, null, null, mensajeExito);
    }

}
