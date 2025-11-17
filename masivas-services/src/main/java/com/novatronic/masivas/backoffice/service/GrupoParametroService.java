package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaGrupoParametroDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroGrupoParametroDTO;
import com.novatronic.masivas.backoffice.dto.EstadoDTO;
import com.novatronic.masivas.backoffice.dto.ParametroDTO;
import com.novatronic.masivas.backoffice.dto.ReporteDTO;
import com.novatronic.masivas.backoffice.entity.TpGrupoParametro;
import com.novatronic.masivas.backoffice.exception.DataBaseException;
import com.novatronic.masivas.backoffice.exception.GenericException;
import com.novatronic.masivas.backoffice.exception.JasperReportException;
import com.novatronic.masivas.backoffice.exception.NoOperationExistsException;
import com.novatronic.masivas.backoffice.exception.UniqueFieldException;
import com.novatronic.novalog.audit.util.Estado;
import com.novatronic.novalog.audit.util.Evento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.novatronic.masivas.backoffice.repository.GrupoParametroRepository;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import com.novatronic.masivas.backoffice.util.GenerarReporte;
import com.novatronic.masivas.backoffice.util.ServicesUtil;
import com.novatronic.novalog.audit.logger.NovaLogger;
import jakarta.transaction.RollbackException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.hibernate.exception.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Obi Consulting
 */
@Service
public class GrupoParametroService {

    @Autowired
    private final GrupoParametroRepository grupoParametroRepository;
    private final ParametroCacheService parametroCacheService;
    private final CoreService coreService;

    @Value("${reporte.logo}")
    private String logo;

    private static final NovaLogger LOGGER = NovaLogger.getLogger(GrupoParametroService.class);

    public GrupoParametroService(GrupoParametroRepository grupoParametroRepository, ParametroCacheService parametroCacheService, CoreService coreService) {
        this.grupoParametroRepository = grupoParametroRepository;
        this.parametroCacheService = parametroCacheService;
        this.coreService = coreService;
    }

    /**
     * Método que realiza la creación de un grupo parámetro
     *
     * @param request
     * @param usuario
     * @return
     */
    public Long crearGrupoParametro(MasivasRequestDTO request, String usuario) {

        try {

            TpGrupoParametro grupoParametro = new TpGrupoParametro(
                    request.getCodigo(),
                    request.getDescripcion(),
                    ConstantesServices.ESTADO_INACTIVO,
                    LocalDateTime.now(),
                    usuario
            );
            grupoParametro = grupoParametroRepository.save(grupoParametro);
            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.GRUPO_PARAMETRO, ConstantesServices.METODO_REGISTRAR, grupoParametro.toString());

            //Actualizamos cache
            parametroCacheService.loadParametersGroupInCache();
            coreService.refrescarCacheCore();

            return grupoParametro.getIdGrupoParametro();

        } catch (Exception e) {
            Throwable excepcion = e.getCause();
            if (excepcion instanceof RollbackException) {
                excepcion = excepcion.getCause();
                if (excepcion instanceof ConstraintViolationException) {
                    throw new UniqueFieldException(ConstantesServices.CODIGO_ERROR_COD_GRUPO_PARAMETRO_UNICO, ConstantesServices.MENSAJE_ERROR_COD_GRUPO_PARAMETRO_UNICO, e);
                }
                throw new DataBaseException(e);
            }
            throw new GenericException(e);
        }

    }

    /**
     * Método que realiza la búsqueda de los grupos parámetro según filtros de
     * búsqueda
     *
     * @param request
     * @return
     */
    public CustomPaginate<DetalleConsultaGrupoParametroDTO> buscarGrupoParametro(FiltroMasivasRequest request) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.GRUPO_PARAMETRO, ConstantesServices.METODO_CONSULTAR, request.toStringGrupoParametro());

            ModelMapper modelMapper = new ModelMapper();
            Pageable pageable = ServicesUtil.configurarPageSort(request);

            Page<TpGrupoParametro> objPageable = grupoParametroRepository.buscarPorFiltros(request.getCodigo(), request.getDescripcion(), request.getEstadoSearch(), pageable);

            Page<DetalleConsultaGrupoParametroDTO> dtoPage = objPageable.map(e -> modelMapper.map(e, DetalleConsultaGrupoParametroDTO.class));

            int totalPaginas = objPageable.getTotalPages();
            long totalRegistrosLong = objPageable.getTotalElements();
            int totalRegistros = (totalRegistrosLong > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) totalRegistrosLong;

            CustomPaginate<DetalleConsultaGrupoParametroDTO> customPaginate = new CustomPaginate<>(totalPaginas, totalRegistros, dtoPage.getContent());

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD_RESULTADOS, customPaginate.getTotalRegistros());

            return customPaginate;

        } catch (DataAccessException e) {
            throw new DataBaseException(e);
        } catch (Exception e) {
            throw new GenericException(e);
        }

    }

    /**
     * Método que realiza la actualización de un grupo parámetro
     *
     * @param request
     * @param usuario
     * @return
     */
    public Long editarGrupoParametro(MasivasRequestDTO request, String usuario) {

        try {

            TpGrupoParametro grupoParametro = grupoParametroRepository.findById(request.getIdGrupoParametro())
                    .orElseThrow(() -> new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA));
            updateGrupoParametro(grupoParametro, request, usuario, ConstantesServices.OPERACION_EDITAR);
            grupoParametroRepository.save(grupoParametro);
            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.GRUPO_PARAMETRO, ConstantesServices.METODO_ACTUALIZAR, request.toStringGrupoParametro());

            //Actualizamos cache
            parametroCacheService.loadParametersGroupInCache();
            coreService.refrescarCacheCore();

            return grupoParametro.getIdGrupoParametro();

        } catch (NoOperationExistsException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new DataBaseException(e);
        } catch (Exception e) {
            Throwable excepcion = e.getCause();
            if (excepcion instanceof RollbackException) {
                excepcion = excepcion.getCause();
                if (excepcion instanceof ConstraintViolationException) {
                    throw new UniqueFieldException(ConstantesServices.CODIGO_ERROR_COD_GRUPO_PARAMETRO_UNICO, ConstantesServices.MENSAJE_ERROR_COD_GRUPO_PARAMETRO_UNICO, e);
                }
                throw new DataBaseException(e);
            }
            throw new GenericException(e);
        }
    }

    /**
     * Método que obtiene el registro de un grupo parámetro según id
     *
     * @param request
     * @return
     */
    public DetalleRegistroGrupoParametroDTO obtenerGrupoParametro(FiltroMasivasRequest request) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.GRUPO_PARAMETRO, ConstantesServices.METODO_OBTENER, request.toStringGrupoParametroObtener());

            ModelMapper modelMapper = new ModelMapper();
            TpGrupoParametro grupoParametro = grupoParametroRepository.findById(request.getIdGrupoParametro())
                    .orElseThrow(() -> new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA));
            DetalleRegistroGrupoParametroDTO grupoParametroDTO = new DetalleRegistroGrupoParametroDTO();
            modelMapper.map(grupoParametro, grupoParametroDTO);

            return grupoParametroDTO;

        } catch (NoOperationExistsException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new DataBaseException(e);
        } catch (Exception e) {
            throw new GenericException(e);
        }
    }

    /**
     * Método que realiza el cambio de estado de un grupo parámetro
     *
     * @param request
     * @param usuario
     * @param estado
     * @return
     */
    public EstadoDTO cambiarEstadoGrupoParametro(MasivasRequestDTO request, String usuario, String estado) {

        try {

            int numExito = 0;
            int totalIds = request.getIdsOperacion().size();
            String mensaje;

            for (Long id : request.getIdsOperacion()) {
                TpGrupoParametro grupoParametro = grupoParametroRepository.findById(id)
                        .orElseThrow(() -> new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA));
                request.setEstado(estado);
                updateGrupoParametro(grupoParametro, request, usuario, ConstantesServices.BLANCO);
                grupoParametroRepository.save(grupoParametro);
                logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.GRUPO_PARAMETRO, ConstantesServices.METODO_ACTIVAR_DESACTIVAR, request.toStringActivarDesactivar());

                numExito++;
            }

            //Actualizamos cache
            parametroCacheService.loadParametersGroupInCache();
            coreService.refrescarCacheCore();

            mensaje = ServicesUtil.obtenerMensajeRespuestaCambioEstado(numExito, totalIds, estado, ConstantesServices.GRUPO_PARAMETRO);

            return new EstadoDTO(mensaje, numExito);

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
     * Método que realiza la búsqueda de los grupos parámetro según filtros de
     * búsqueda y los exporta a un archivo pdf/xlsx
     *
     * @param request
     * @param usuario
     * @param tipoArchivo
     * @return
     */
    public ReporteDTO descargarGrupoParametro(FiltroMasivasRequest request, String usuario, String tipoArchivo) {

        try {
            request.setNumeroPagina(0);
            request.setRegistrosPorPagina(0);

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.GRUPO_PARAMETRO, ConstantesServices.METODO_DESCARGAR, request.toStringGrupoParametro());
            CustomPaginate<DetalleConsultaGrupoParametroDTO> resultado = buscarGrupoParametro(request);

            HashMap<String, Object> parameters = new HashMap<>();
            //Filtros
            parameters.put("IN_CODIGO", request.getCodigo());
            parameters.put("IN_ESTADO", request.getEstado());

            return GenerarReporte.generarReporte(resultado.getContenido(), parameters, usuario, tipoArchivo, "reportes/reporteGrupoParametro.jrxml", "grupoParametro", logo);

        } catch (JasperReportException | DataBaseException | GenericException e) {
            throw e;
        } catch (Exception e) {
            throw new GenericException(e);
        }

    }

    public List<ParametroDTO> getAllGrupoParametro() {

        logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.GRUPO_PARAMETRO, ConstantesServices.METODO_CONSULTAR, "");
        List<TpGrupoParametro> listaGrupoParametro = grupoParametroRepository.findAll();

        return listaGrupoParametro.stream()
                .map(grupo -> new ParametroDTO(String.valueOf(grupo.getIdGrupoParametro()), grupo.getDescripcion()))
                .sorted(Comparator.comparing(p -> Long.valueOf(p.getCodigo())))
                .toList();
    }

    private void updateGrupoParametro(TpGrupoParametro grupoParametro, MasivasRequestDTO request, String usuario, String operacion) {

        if (operacion.equals(ConstantesServices.OPERACION_EDITAR)) {
            grupoParametro.setCodigo(request.getCodigo());
            grupoParametro.setDescripcion(request.getDescripcion());
        } else {
            grupoParametro.setEstado(request.getEstado());
        }

        grupoParametro.setFecModificacion(LocalDateTime.now());
        grupoParametro.setUsuModificacion(usuario);
    }

    public void logEvento(String mensaje, Object... param) {
        LOGGER.info(mensaje, param);
    }

    public <T> void logAuditoria(T request, Evento idEvento, Estado estado, UserContext userContext, String recursoAfectado, String origen, String mensajeRespuesta, String codigoRespuesta) {
        LOGGER.audit(null, request, idEvento, estado, userContext.getUsername(), userContext.getScaProfile(), recursoAfectado, userContext.getIp(),
                ConstantesServices.VACIO, origen, null, null, mensajeRespuesta, codigoRespuesta);
    }

}
