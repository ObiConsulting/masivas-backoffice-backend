package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaEntidadDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroEntidadDTO;
import com.novatronic.masivas.backoffice.dto.EstadoDTO;
import com.novatronic.masivas.backoffice.entity.TpEntidad;
import com.novatronic.masivas.backoffice.exception.DataBaseException;
import com.novatronic.masivas.backoffice.exception.GenericException;
import com.novatronic.masivas.backoffice.exception.UniqueFieldException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import com.novatronic.masivas.backoffice.repository.EntidadRepository;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import com.novatronic.novalog.audit.logger.NovaLogger;
import com.novatronic.novalog.audit.util.Estado;
import com.novatronic.novalog.audit.util.Evento;
import jakarta.transaction.RollbackException;
import java.time.LocalDateTime;
import org.hibernate.exception.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 *
 * @author Obi Consulting
 */
@Service
public class EntidadService {

    @Autowired
    private final EntidadRepository entidadRepository;
    private final MessageSource messageSource;

    private static final NovaLogger LOGGER = NovaLogger.getLogger(EntidadService.class);

    public EntidadService(EntidadRepository entidadRepository, MessageSource messageSource) {
        this.entidadRepository = entidadRepository;
        this.messageSource = messageSource;
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
                    ConstantesServices.ID_EXTENSION_BASE,
                    ConstantesServices.ID_EXTENSION_CONTROL,
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
                    throw new UniqueFieldException(ConstantesServices.CODIGO_ERROR_COD_ENTIDAD_UNICO, ConstantesServices.MENSAJE_ERROR_COD_ENTIDAD_UNICO, e);
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
     * @param usuario
     * @return
     */
    public CustomPaginate<DetalleConsultaEntidadDTO> buscar(FiltroMasivasRequest request, String usuario) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.ENTIDAD_FINANCIERA, ConstantesServices.METODO_CONSULTAR, request.toStringEntidad());

            ModelMapper modelMapper = new ModelMapper();
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

            Page<TpEntidad> objPageable = entidadRepository.buscarPorFiltros(request.getCodigo(), request.getNombre(), request.getEstado(), pageable);

            Page<DetalleConsultaEntidadDTO> dtoPage = objPageable.map(e -> modelMapper.map(e, DetalleConsultaEntidadDTO.class));

            int totalPaginas = objPageable.getTotalPages();
            long totalRegistrosLong = objPageable.getTotalElements();

            int totalRegistros = (totalRegistrosLong > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) totalRegistrosLong;

            CustomPaginate customPaginate = new CustomPaginate<>(
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

            TpEntidad entidad = entidadRepository.findById(request.getIdEntidad()).get();
            updateEntidad(entidad, request, usuario, ConstantesServices.OPERACION_EDITAR);
            entidadRepository.save(entidad);

            return entidad.getIdEntidad();

        } catch (Exception e) {
            Throwable excepcion = e.getCause();
            if (excepcion instanceof RollbackException) {
                excepcion = excepcion.getCause();
                if (excepcion instanceof ConstraintViolationException) {
                    throw new UniqueFieldException(ConstantesServices.CODIGO_ERROR_COD_ENTIDAD_UNICO, ConstantesServices.MENSAJE_ERROR_COD_ENTIDAD_UNICO, e);
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
     * @param usuario
     * @return
     */
    public DetalleRegistroEntidadDTO obtenerEntidad(FiltroMasivasRequest request, String usuario) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.ENTIDAD_FINANCIERA, ConstantesServices.METODO_OBTENER, request.toStringEntidadObtener());

            ModelMapper modelMapper = new ModelMapper();
            TpEntidad entidad = entidadRepository.findById(request.getIdEntidad()).get();
            DetalleRegistroEntidadDTO entidadDTO = new DetalleRegistroEntidadDTO();
            modelMapper.map(entidad, entidadDTO);

            return entidadDTO;

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
                TpEntidad entidad = entidadRepository.findById(id).get();
                request.setEstado(estado);
                updateEntidad(entidad, request, usuario, ConstantesServices.BLANCO);
                entidadRepository.save(entidad);
                numExito++;
            }

            if (numExito == totalIds) {
                if (totalIds > 1) {
                    mensaje = estado.equals(ConstantesServices.ESTADO_ACTIVO) ? ConstantesServices.MENSAJE_EXITO_ACTIVAR_OPERACIONES : ConstantesServices.MENSAJE_EXITO_DESACTIVAR_OPERACIONES;
                } else {
                    mensaje = estado.equals(ConstantesServices.ESTADO_ACTIVO) ? ConstantesServices.MENSAJE_EXITO_ACTIVAR_OPERACION : ConstantesServices.MENSAJE_EXITO_DESACTIVAR_OPERACION;
                }
            } else if (numExito > 0 && numExito < totalIds) {
                mensaje = estado.equals(ConstantesServices.ESTADO_ACTIVO) ? ConstantesServices.MENSAJE_EXITO_PARCIAL_ACTIVAR_OPERACIONES : ConstantesServices.MENSAJE_EXITO_PARCIAL_DESACTIVAR_OPERACIONES;
                mensaje = String.format(mensaje, numExito, totalIds - numExito);
            } else {
                if (totalIds > 1) {
                    mensaje = estado.equals(ConstantesServices.ESTADO_ACTIVO) ? ConstantesServices.MENSAJE_ERROR_ACTIVAR_OPERACIONES : ConstantesServices.MENSAJE_ERROR_DESACTIVAR_OPERACIONES;
                } else {
                    mensaje = estado.equals(ConstantesServices.ESTADO_ACTIVO) ? ConstantesServices.MENSAJE_ERROR_ACTIVAR_OPERACION : ConstantesServices.MENSAJE_ERROR_DESACTIVAR_OPERACION;
                }
            }

            return new EstadoDTO(mensaje, numExito);

        } catch (Exception e) {
            Throwable excepcion = e.getCause();
            if (excepcion instanceof RollbackException) {
                throw new DataBaseException(e);
            }
            throw new GenericException(e);
        }
    }

    private void updateEntidad(TpEntidad entidad, MasivasRequestDTO request, String usuario, String operacion) {

        if (operacion.equals(ConstantesServices.OPERACION_EDITAR)) {
            entidad.setCodigo(request.getCodigo());
            entidad.setNombre(request.getNombre());
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
