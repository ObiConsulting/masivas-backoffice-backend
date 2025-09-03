package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.ArchivoDTO;
import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoDirectorioDTO;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoMasivasDTO;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoTitularidadDTO;
import com.novatronic.masivas.backoffice.dto.EjecutarRequestDTO;
import com.novatronic.masivas.backoffice.dto.ArchivoResponseDTO;
import com.novatronic.masivas.backoffice.dto.EjecutarResponseDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.ReporteDTO;
import com.novatronic.masivas.backoffice.entity.TpArchivoDirectorio;
import com.novatronic.masivas.backoffice.entity.TpArchivoMasivas;
import com.novatronic.masivas.backoffice.entity.TpArchivoTitularidad;
import com.novatronic.masivas.backoffice.exception.ActionRestCoreException;
import com.novatronic.masivas.backoffice.exception.DataBaseException;
import com.novatronic.masivas.backoffice.exception.GenericException;
import com.novatronic.masivas.backoffice.exception.JasperReportException;
import com.novatronic.masivas.backoffice.exception.NoOperationExistsException;
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
import jakarta.transaction.RollbackException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
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

        } catch (DataAccessException e) {
            throw new DataBaseException(e);
        } catch (Exception e) {
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

        } catch (DataAccessException e) {
            throw new DataBaseException(e);
        } catch (Exception e) {
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

        } catch (DataAccessException e) {
            throw new DataBaseException(e);
        } catch (Exception e) {
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

        } catch (JasperReportException | DataBaseException | GenericException e) {
            throw e;
        } catch (Exception e) {
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

        } catch (JasperReportException | DataBaseException | GenericException e) {
            throw e;
        } catch (Exception e) {
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

        } catch (JasperReportException | DataBaseException | GenericException e) {
            throw e;
        } catch (Exception e) {
            throw new GenericException(e);
        }

    }

    /**
     * Método que realiza las operaciones de respaldar(backup) y
     * restaurar(restore) de un archivo de tipo directorio.
     *
     * @param request
     * @return
     */
    public String gestionarOperacionDirectorio(FiltroMasivasRequest request) {

        try {
            String codigoAccion = "";
            String mensaje;

            switch (request.getTipoAccion()) {
                case ConstantesServices.TIPO_ACCION_RESPALDAR -> {
                    logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD_ACCION, ConstantesServices.RESPALDAR_ARCHIVO, request.toStringRespaldarResturarObtener());
                    codigoAccion = ConstantesServices.COD_ACCION_RESPALDAR;
                    mensaje = ConstantesServices.MENSAJE_EXITO_RESPALDAR_ARCHIVO;
                }
                case ConstantesServices.TIPO_ACCION_RESTAURAR -> {
                    logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD_ACCION, ConstantesServices.RESTAURAR_ARCHIVO, request.toStringRespaldarResturarObtener());
                    codigoAccion = ConstantesServices.COD_ACCION_RESTAURAR;
                    mensaje = ConstantesServices.MENSAJE_EXITO_RESTAURAR_ARCHIVO;
                }
                default -> {
                    throw new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA);
                }
            }

            Optional<TpArchivoDirectorio> archivoDirectorio = archivoDirectorioRepository.findById(request.getIdArchivo());

            if (!archivoDirectorio.isPresent()) {
                throw new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA);
            }

            TpArchivoDirectorio directorio = archivoDirectorio.get();
            ArchivoDTO archivo = new ArchivoDTO(
                    null,
                    directorio.getNombre(),
                    directorio.getTrace());

            List<ArchivoDTO> listaArchivo = new ArrayList<>();
            listaArchivo.add(archivo);

            ArchivoResponseDTO respuesta = invocarServicio(listaArchivo, ConstantesServices.TIPO_DIRECTORIO, codigoAccion);

            //Actualizamos el registro
            directorio.setEstadoFisico(respuesta.getEstado());
            directorio.setFechaModificacionFisica(respuesta.getFechaProceso());

            archivoDirectorioRepository.save(directorio);

            return mensaje;

        } catch (NoOperationExistsException | ActionRestCoreException | RestClientException e) {
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
     * Método que realiza las operaciones de respaldar(backup) y
     * restaurar(restore) de un archivo de tipo masivas.
     *
     * @param request
     * @return
     */
    public String gestionarOperacionMasivas(FiltroMasivasRequest request) {

        try {
            String codigoAccion = "";
            String mensaje;

            switch (request.getTipoAccion()) {
                case ConstantesServices.TIPO_ACCION_RESPALDAR -> {
                    logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD_ACCION, ConstantesServices.RESPALDAR_ARCHIVO, request.toStringRespaldarResturarObtener());
                    codigoAccion = ConstantesServices.COD_ACCION_RESPALDAR;
                    mensaje = ConstantesServices.MENSAJE_EXITO_RESPALDAR_ARCHIVO;
                }
                case ConstantesServices.TIPO_ACCION_RESTAURAR -> {
                    logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD_ACCION, ConstantesServices.RESTAURAR_ARCHIVO, request.toStringRespaldarResturarObtener());
                    codigoAccion = ConstantesServices.COD_ACCION_RESTAURAR;
                    mensaje = ConstantesServices.MENSAJE_EXITO_RESTAURAR_ARCHIVO;
                }
                default -> {
                    throw new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA);
                }
            }

            Optional<TpArchivoMasivas> archivoMasivas = archivoMasivasRepository.findById(request.getIdArchivo());

            if (!archivoMasivas.isPresent()) {
                throw new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA);
            }

            TpArchivoMasivas masivas = archivoMasivas.get();
            ArchivoDTO archivo = new ArchivoDTO(
                    masivas.getNombre(),
                    masivas.getNombreCCE(),
                    masivas.getTrace());

            List<ArchivoDTO> listaArchivo = new ArrayList<>();
            listaArchivo.add(archivo);

            ArchivoResponseDTO respuesta = invocarServicio(listaArchivo, ConstantesServices.TIPO_MASIVAS, codigoAccion);

            //Actualizamos el registro
            masivas.setEstadoFisico(respuesta.getEstado());
            masivas.setFechaModificacionFisica(respuesta.getFechaProceso());

            archivoMasivasRepository.save(masivas);

            return mensaje;

        } catch (NoOperationExistsException | ActionRestCoreException | RestClientException e) {
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
     * Método que realiza las operaciones de respaldar(backup) y
     * restaurar(restore) de un archivo de tipo titularidad.
     *
     * @param request
     * @return
     */
    public String gestionarOperacionTitularidad(FiltroMasivasRequest request) {

        try {
            String codigoAccion = "";
            String mensaje;

            switch (request.getTipoAccion()) {
                case ConstantesServices.TIPO_ACCION_RESPALDAR -> {
                    logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD_ACCION, ConstantesServices.RESPALDAR_ARCHIVO, request.toStringRespaldarResturarObtener());
                    codigoAccion = ConstantesServices.COD_ACCION_RESPALDAR;
                    mensaje = ConstantesServices.MENSAJE_EXITO_RESPALDAR_ARCHIVO;
                }
                case ConstantesServices.TIPO_ACCION_RESTAURAR -> {
                    logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD_ACCION, ConstantesServices.RESTAURAR_ARCHIVO, request.toStringRespaldarResturarObtener());
                    codigoAccion = ConstantesServices.COD_ACCION_RESTAURAR;
                    mensaje = ConstantesServices.MENSAJE_EXITO_RESTAURAR_ARCHIVO;
                }
                default -> {
                    throw new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA);
                }
            }

            Optional<TpArchivoTitularidad> archivoTitularidad = archivoTitularidadRepository.findById(request.getIdArchivo());

            if (!archivoTitularidad.isPresent()) {
                throw new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA);
            }

            TpArchivoTitularidad titularidad = archivoTitularidad.get();
            ArchivoDTO archivo = new ArchivoDTO(
                    titularidad.getNombre(),
                    titularidad.getNombreCCE(),
                    titularidad.getTrace());

            List<ArchivoDTO> listaArchivo = new ArrayList<>();
            listaArchivo.add(archivo);

            ArchivoResponseDTO respuesta = invocarServicio(listaArchivo, ConstantesServices.TIPO_TITULARIDAD, codigoAccion);

            //Actualizamos el registro
            titularidad.setEstadoFisico(respuesta.getEstado());
            titularidad.setFechaModificacionFisica(respuesta.getFechaProceso());

            archivoTitularidadRepository.save(titularidad);

            return mensaje;

        } catch (NoOperationExistsException | ActionRestCoreException | RestClientException e) {
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

    private ArchivoResponseDTO invocarServicio(List<ArchivoDTO> archivo, String codigoOperacion, String codigoAccion) {

        try {

            String codServer = ConstantesServices.COD_SERVER_MASIVAS;
            String codEntidad = genericService.getCodigoEntidadPropietaria();
            EjecutarRequestDTO ejecutarRequestDTO = new EjecutarRequestDTO(
                    ServicesUtil.generarNumeroAleatorio(),
                    codServer,
                    codEntidad,
                    codigoOperacion,
                    codigoAccion,
                    archivo
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<EjecutarRequestDTO> entity = new HttpEntity<>(ejecutarRequestDTO, headers);
            ResponseEntity<EjecutarResponseDTO> response = restTemplate.exchange(apiCoreUrl, HttpMethod.POST, entity, new ParameterizedTypeReference<EjecutarResponseDTO>() {
            });

            EjecutarResponseDTO ejecutarResponseDTO = response.getBody();
            if (ejecutarResponseDTO == null) {
                throw new ActionRestCoreException(ConstantesServices.CODIGO_ERROR_API_CORE_ACCION, ConstantesServices.MENSAJE_ERROR_API_CORE_ACCION);
            }

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD_API_RESPONSE, apiCoreUrl, ejecutarResponseDTO);

            //Verificamos si existe un error en el response
            if (!ejecutarResponseDTO.getCodigoRespuesta().equals(ConstantesServices.CODIGO_OK_WS)) {
                throw new ActionRestCoreException(ConstantesServices.CODIGO_ERROR_API_CORE_ACCION, ConstantesServices.MENSAJE_ERROR_API_CORE_ACCION);
            }
            return ejecutarResponseDTO.getArchivos().get(0);

        } catch (ActionRestCoreException | RestClientException e) {
            throw e;
        }
    }

    public void logEvento(String mensaje, Object... param) {
        LOGGER.info(mensaje, param);
    }

    public <T> void logAuditoria(T request, UserContext userContext, String mensajeExito) {
        LOGGER.auditSuccess(null, request, userContext.getUsername(), userContext.getScaProfile(), userContext.getIp(), ConstantesServices.VACIO, mensajeExito, ConstantesServices.RESPUESTA_OK_API);
    }

    public void logError(String mensajeError, Exception e) {
        LOGGER.error(mensajeError, e);
    }

}
