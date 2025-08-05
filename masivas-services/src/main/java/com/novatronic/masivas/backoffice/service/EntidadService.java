package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaEntidadDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroEntidadDTO;
import com.novatronic.masivas.backoffice.dto.EstadoDTO;
import com.novatronic.masivas.backoffice.dto.ReporteDTO;
import com.novatronic.masivas.backoffice.entity.TpEntidad;
import com.novatronic.masivas.backoffice.exception.DataBaseException;
import com.novatronic.masivas.backoffice.exception.GenericException;
import com.novatronic.masivas.backoffice.exception.JasperReportException;
import com.novatronic.masivas.backoffice.exception.NoOperationExistsException;
import com.novatronic.masivas.backoffice.exception.UniqueFieldException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.novatronic.masivas.backoffice.repository.EntidadRepository;
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
public class EntidadService {

    @Autowired
    private final EntidadRepository entidadRepository;

    @Value("${reporte.logo}")
    private String logo;

    private static final NovaLogger LOGGER = NovaLogger.getLogger(EntidadService.class);

    public EntidadService(EntidadRepository entidadRepository) {
        this.entidadRepository = entidadRepository;
    }

    /**
     * Método que realiza la creación de una entidad financiera
     *
     * @param request
     * @param usuario
     * @return
     */
    public Long crearEntidad(MasivasRequestDTO request, String usuario) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.ENTIDAD_FINANCIERA, ConstantesServices.METODO_REGISTRAR, request.toStringEntidad());

            TpEntidad entidad = new TpEntidad(
                    request.getCodigo(),
                    request.getNombre(),
                    ConstantesServices.ESTADO_INACTIVO,
                    ConstantesServices.ID_PERFIL,
                    ConstantesServices.NO_PROPIETARIO,
                    null,//Extensión Base
                    null,//Extensión Control
                    LocalDateTime.now(),
                    usuario
            );
            entidad = entidadRepository.save(entidad);
            return entidad.getIdEntidad();

        } catch (Exception e) {
            Throwable excepcion = e.getCause();
            if (excepcion instanceof RollbackException) {
                excepcion = excepcion.getCause();
                if (excepcion instanceof ConstraintViolationException) {
                    throw new UniqueFieldException(ConstantesServices.CODIGO_ERROR_COD_ENTIDAD_UNICA, ConstantesServices.MENSAJE_ERROR_COD_ENTIDAD_UNICA, e);
                }
                throw new DataBaseException(e);
            }
            throw new GenericException(e);
        }

    }

    /**
     * Método que realiza la búsqueda de las entidades financieras según filtros
     * de búsqueda
     *
     * @param request
     * @return
     */
    public CustomPaginate<DetalleConsultaEntidadDTO> buscarEntidad(FiltroMasivasRequest request) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.ENTIDAD_FINANCIERA, ConstantesServices.METODO_CONSULTAR, request.toStringEntidadAplicacion());

            ModelMapper modelMapper = new ModelMapper();
            Pageable pageable = ServicesUtil.configurarPageSort(request);

            Page<TpEntidad> objPageable = entidadRepository.buscarPorFiltros(request.getCodigo(), request.getNombre(), request.getEstado(), pageable);

            Page<DetalleConsultaEntidadDTO> dtoPage = objPageable.map(e -> modelMapper.map(e, DetalleConsultaEntidadDTO.class));

            int totalPaginas = objPageable.getTotalPages();
            long totalRegistrosLong = objPageable.getTotalElements();

            int totalRegistros = (totalRegistrosLong > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) totalRegistrosLong;

            CustomPaginate<DetalleConsultaEntidadDTO> customPaginate = new CustomPaginate<>(
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
     * Método que realiza la actualización de una entidad financiera
     *
     * @param request
     * @param usuario
     * @return
     */
    public Long editarEntidad(MasivasRequestDTO request, String usuario) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.ENTIDAD_FINANCIERA, ConstantesServices.METODO_ACTUALIZAR, request.toStringEntidad());

            TpEntidad entidad = entidadRepository.findById(request.getIdEntidad())
                    .orElseThrow(() -> new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA));
            updateEntidad(entidad, request, usuario, ConstantesServices.OPERACION_EDITAR);
            entidadRepository.save(entidad);

            return entidad.getIdEntidad();

        } catch (NoOperationExistsException e) {
            throw e;
        } catch (Exception e) {
            Throwable excepcion = e.getCause();
            if (excepcion instanceof RollbackException) {
                excepcion = excepcion.getCause();
                if (excepcion instanceof ConstraintViolationException) {
                    throw new UniqueFieldException(ConstantesServices.CODIGO_ERROR_COD_ENTIDAD_UNICA, ConstantesServices.MENSAJE_ERROR_COD_ENTIDAD_UNICA, e);
                }
                throw new DataBaseException(e);
            }
            throw new GenericException(e);
        }

    }

    /**
     * Método que obtiene el registro de una entidad financiera según id
     *
     * @param request
     * @return
     */
    public DetalleRegistroEntidadDTO obtenerEntidad(FiltroMasivasRequest request) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.ENTIDAD_FINANCIERA, ConstantesServices.METODO_OBTENER, request.toStringEntidadObtener());

            ModelMapper modelMapper = new ModelMapper();
            TpEntidad entidad = entidadRepository.findById(request.getIdEntidad())
                    .orElseThrow(() -> new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA));
            DetalleRegistroEntidadDTO entidadDTO = new DetalleRegistroEntidadDTO();
            modelMapper.map(entidad, entidadDTO);

            return entidadDTO;

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
     * Método que realiza el cambio de estado de una entidad financiera
     *
     * @param request
     * @param usuario
     * @param estado
     * @return
     */
    public EstadoDTO cambiarEstadoEntidad(MasivasRequestDTO request, String usuario, String estado) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.ENTIDAD_FINANCIERA, ConstantesServices.METODO_ACTIVAR_DESACTIVAR, request.toStringActivarDesactivar());

            int numExito = 0;
            int totalIds = request.getIdsOperacion().size();
            String mensaje;

            for (Long id : request.getIdsOperacion()) {
                TpEntidad entidad = entidadRepository.findById(id)
                        .orElseThrow(() -> new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA));

                request.setEstado(estado);
                updateEntidad(entidad, request, usuario, ConstantesServices.BLANCO);
                entidadRepository.save(entidad);
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
     * Método que realiza la búsqueda de las entidades financieras según filtros
     * de búsqueda y las exporta a un archivo pdf/xlsx
     *
     * @param request
     * @param usuario
     * @param tipoArchivo
     * @return
     */
    public ReporteDTO descargarEntidades(FiltroMasivasRequest request, String usuario, String tipoArchivo) {

        try {
            request.setNumeroPagina(0);
            request.setRegistrosPorPagina(0);

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.ENTIDAD_FINANCIERA, ConstantesServices.METODO_DESCARGAR, request.toStringEntidadAplicacion());
            CustomPaginate<DetalleConsultaEntidadDTO> resultado = buscarEntidad(request);

            HashMap<String, Object> parameters = new HashMap<>();
            //Filtros
            parameters.put("IN_CODIGO", request.getCodigo());
            parameters.put("IN_NOMBRE", request.getNombre());
            parameters.put("IN_ESTADO", request.getEstado());

            return GenerarReporte.generarReporte(resultado.getContenido(), parameters, usuario, tipoArchivo, "reportes/reporteEntidadesFinancieras.jrxml", "entidadesFinancieras", logo);

        } catch (JasperReportException | DataBaseException | GenericException e) {
            throw e;
        } catch (Exception e) {
            throw new GenericException(e);
        }

    }

    private void updateEntidad(TpEntidad entidad, MasivasRequestDTO request, String usuario, String operacion) {

        if (operacion.equals(ConstantesServices.OPERACION_EDITAR)) {
            entidad.setCodigo(request.getCodigo());
            entidad.setNombre(request.getNombre());
            entidad.setIdExtensionBase(request.getIdExtensionBase());
            entidad.setIdExtensionControl(request.getIdExtensionControl());
        } else {
            entidad.setEstado(request.getEstado());
        }

        entidad.setFecModificacion(LocalDateTime.now());
        entidad.setUsuModificacion(usuario);
    }

    public void logEvento(String mensaje, Object... param) {
        LOGGER.info(mensaje, param);
    }

    public <T> void logAuditoria(T request, Evento evento, Estado estado, UserContext userContext, String nombreTabla, String accion, String mensajeExito) {
        LOGGER.audit(null, request, evento, estado, userContext.getUsername(), userContext.getScaProfile(), nombreTabla, userContext.getIp(),
                null, accion, null, null, mensajeExito);
    }

}
