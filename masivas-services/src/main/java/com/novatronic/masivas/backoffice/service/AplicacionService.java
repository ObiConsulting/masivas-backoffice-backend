package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaAplicacionDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroAplicacionDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.dto.EstadoDTO;
import com.novatronic.masivas.backoffice.dto.ReporteDTO;
import com.novatronic.masivas.backoffice.entity.TpAplicacion;
import com.novatronic.masivas.backoffice.exception.DataBaseException;
import com.novatronic.masivas.backoffice.exception.GenericException;
import com.novatronic.masivas.backoffice.exception.JasperReportException;
import com.novatronic.masivas.backoffice.exception.NoOperationExistsException;
import com.novatronic.masivas.backoffice.exception.UniqueFieldException;
import com.novatronic.masivas.backoffice.repository.AplicacionRepository;
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
import java.util.HashMap;
import org.hibernate.exception.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Obi Consulting
 */
@Service
public class AplicacionService {

    @Autowired
    private final AplicacionRepository aplicacionRepository;

    @Value("${reporte.logo}")
    private String logo;

    private static final NovaLogger LOGGER = NovaLogger.getLogger(AplicacionService.class);

    public AplicacionService(AplicacionRepository aplicacionRepository) {
        this.aplicacionRepository = aplicacionRepository;
    }

    /**
     * Método que realiza la creación de una aplicación
     *
     * @param request
     * @param usuario
     * @return
     */
    public Long crearAplicacion(MasivasRequestDTO request, String usuario) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.APLICACION, ConstantesServices.METODO_REGISTRAR, request.toStringAplicacion());

            TpAplicacion aplicacion = new TpAplicacion(
                    request.getCodigo(),
                    request.getNombre(),
                    ConstantesServices.ESTADO_INACTIVO,
                    LocalDateTime.now(),
                    usuario
            );
            aplicacion = aplicacionRepository.save(aplicacion);
            return aplicacion.getIdAplicacion();

        } catch (Exception e) {
            Throwable excepcion = e.getCause();
            if (excepcion instanceof RollbackException) {
                excepcion = excepcion.getCause();
                if (excepcion instanceof ConstraintViolationException) {
                    throw new UniqueFieldException(ConstantesServices.CODIGO_ERROR_COD_APLICACION_UNICA, ConstantesServices.MENSAJE_ERROR_COD_APLICACION_UNICA, e);
                }
                throw new DataBaseException(e);
            }
            throw new GenericException(e);
        }

    }

    /**
     * Método que realiza la búsqueda de las aplicaciones según filtros de
     * búsqueda
     *
     * @param request
     * @return
     */
    public CustomPaginate<DetalleConsultaAplicacionDTO> buscarAplicacion(FiltroMasivasRequest request) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.APLICACION, ConstantesServices.METODO_CONSULTAR, request.toStringEntidadAplicacion());

            ModelMapper modelMapper = new ModelMapper();
            Pageable pageable = null;

            pageable = ServicesUtil.configurarPageSort(request);

            Page<TpAplicacion> objPageable = aplicacionRepository.buscarPorFiltros(request.getCodigo(), request.getNombre(), request.getEstado(), pageable);

            Page<DetalleConsultaAplicacionDTO> dtoPage = objPageable.map(e -> modelMapper.map(e, DetalleConsultaAplicacionDTO.class));

            int totalPaginas = objPageable.getTotalPages();
            long totalRegistrosLong = objPageable.getTotalElements();

            int totalRegistros = (totalRegistrosLong > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) totalRegistrosLong;

            CustomPaginate<DetalleConsultaAplicacionDTO> customPaginate = new CustomPaginate<>(
                    totalPaginas,
                    totalRegistros,
                    dtoPage.getContent()
            );

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
     * Método que realiza la actualización de una aplicación
     *
     * @param request
     * @param usuario
     * @return
     */
    public Long editarAplicacion(MasivasRequestDTO request, String usuario) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.APLICACION, ConstantesServices.METODO_ACTUALIZAR, request.toStringAplicacion());

            TpAplicacion aplicacion = aplicacionRepository.findById(request.getIdAplicacion())
                    .orElseThrow(() -> new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA));
            updateAplicacion(aplicacion, request, usuario, ConstantesServices.OPERACION_EDITAR);
            aplicacionRepository.save(aplicacion);

            return aplicacion.getIdAplicacion();

        } catch (NoOperationExistsException e) {
            throw e;
        } catch (Exception e) {
            Throwable excepcion = e.getCause();
            if (excepcion instanceof RollbackException) {
                excepcion = excepcion.getCause();
                if (excepcion instanceof ConstraintViolationException) {
                    throw new UniqueFieldException(ConstantesServices.CODIGO_ERROR_COD_APLICACION_UNICA, ConstantesServices.MENSAJE_ERROR_COD_APLICACION_UNICA, e);
                }
                throw new DataBaseException(e);
            }
            throw new GenericException(e);
        }

    }

    /**
     * Método que obtiene el registro de una aplicación según id
     *
     * @param request
     * @return
     */
    public DetalleRegistroAplicacionDTO obtenerAplicacion(FiltroMasivasRequest request) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.APLICACION, ConstantesServices.METODO_OBTENER, request.toStringAplicacionObtener());

            ModelMapper modelMapper = new ModelMapper();
            TpAplicacion aplicacion = aplicacionRepository.findById(request.getIdAplicacion())
                    .orElseThrow(() -> new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA));
            DetalleRegistroAplicacionDTO aplicacionDTO = new DetalleRegistroAplicacionDTO();
            modelMapper.map(aplicacion, aplicacionDTO);

            return aplicacionDTO;

        } catch (NoOperationExistsException e) {
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
     * Método que realiza el cambio de estado de una aplicacion
     *
     * @param request
     * @param usuario
     * @param estado
     * @return
     */
    public EstadoDTO cambiarEstadoAplicacion(MasivasRequestDTO request, String usuario, String estado) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.APLICACION, ConstantesServices.METODO_ACTIVAR_DESACTIVAR, request.toStringActivarDesactivar());

            int numExito = 0;
            int totalIds = request.getIdsOperacion().size();
            String mensaje;

            for (Long id : request.getIdsOperacion()) {
                TpAplicacion aplicacion = aplicacionRepository.findById(id)
                        .orElseThrow(() -> new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA));

                request.setEstado(estado);
                updateAplicacion(aplicacion, request, usuario, ConstantesServices.BLANCO);
                aplicacionRepository.save(aplicacion);
                numExito++;
            }

            mensaje = ServicesUtil.obtenerMensajeRespuestaCambioEstado(numExito, totalIds, estado);

            return new EstadoDTO(mensaje, numExito);

        } catch (NoOperationExistsException e) {
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
     * Método que realiza la búsqueda de las aplicaciones según filtros de
     * búsqueda y las exporta a un archivo pdf/xlsx
     *
     * @param request
     * @param usuario
     * @param tipoArchivo
     * @return
     */
    public ReporteDTO descargarAplicacion(FiltroMasivasRequest request, String usuario, String tipoArchivo) {

        try {
            request.setNumeroPagina(0);
            request.setRegistrosPorPagina(0);

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.APLICACION, ConstantesServices.METODO_DESCARGAR, request.toStringEntidadAplicacion());
            CustomPaginate<DetalleConsultaAplicacionDTO> resultado = buscarAplicacion(request);

            HashMap<String, Object> parameters = new HashMap<>();
            //Filtros
            parameters.put("IN_CODIGO", request.getCodigo());
            parameters.put("IN_NOMBRE", request.getNombre());
            parameters.put("IN_ESTADO", request.getEstado());

            return GenerarReporte.generarReporte(resultado.getContenido(), parameters, usuario, tipoArchivo, "reportes/reporteAplicaciones.jrxml", "aplicacion", logo);

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

    private void updateAplicacion(TpAplicacion aplicacion, MasivasRequestDTO request, String usuario, String operacion) {

        if (operacion.equals(ConstantesServices.OPERACION_EDITAR)) {
            aplicacion.setCodigo(request.getCodigo());
            aplicacion.setNombre(request.getNombre());
        } else {
            aplicacion.setEstado(request.getEstado());
        }

        aplicacion.setFecModificacion(LocalDateTime.now());
        aplicacion.setUsuModificacion(usuario);
    }

    public void logEvento(String mensaje, Object... param) {
        LOGGER.info(mensaje, param);
    }

    public <T> void logAuditoria(T request, Evento evento, Estado estado, UserContext userContext, String nombreTabla, String accion, String mensajeExito) {
        LOGGER.audit(null, request, evento, estado, userContext.getUsername(), userContext.getScaProfile(), nombreTabla, userContext.getIp(),
                null, accion, null, null, mensajeExito);
    }

}
