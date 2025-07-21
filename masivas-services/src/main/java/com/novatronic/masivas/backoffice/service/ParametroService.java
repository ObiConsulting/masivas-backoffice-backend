package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.ComboEstadoDTO;
import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaParametroDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroParametroDTO;
import com.novatronic.masivas.backoffice.dto.EstadoDTO;
import com.novatronic.masivas.backoffice.entity.TpParametro;
import com.novatronic.masivas.backoffice.exception.DataBaseException;
import com.novatronic.masivas.backoffice.exception.GenericException;
import com.novatronic.masivas.backoffice.exception.NoOperationExistsException;
import com.novatronic.masivas.backoffice.exception.UniqueFieldException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import com.novatronic.masivas.backoffice.repository.ParametroRepository;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import com.novatronic.novalog.audit.logger.NovaLogger;
import com.novatronic.novalog.audit.util.Estado;
import com.novatronic.novalog.audit.util.Evento;
import jakarta.transaction.RollbackException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
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
public class ParametroService {

    @Autowired
    private final ParametroRepository parametroRepository;
    private final MessageSource messageSource;

    private static final NovaLogger LOGGER = NovaLogger.getLogger(ParametroService.class);

    public ParametroService(ParametroRepository parametroRepository, MessageSource messageSource) {
        this.parametroRepository = parametroRepository;
        this.messageSource = messageSource;
    }

    /**
     * Método que realiza la creación de un parámetro
     *
     * @param request
     * @param usuario
     * @return
     */
    public Long crearParametro(MasivasRequestDTO request, String usuario) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.PARAMETRO, ConstantesServices.METODO_REGISTRAR, request.toStringParametro());

            TpParametro parametro = new TpParametro(
                    request.getCodigo(),
                    request.getValor(),
                    ConstantesServices.ESTADO_INACTIVO,
                    request.getIdGrupoParametro(),
                    LocalDateTime.now(),
                    usuario
            );
            parametro = parametroRepository.save(parametro);
            return parametro.getIdParametro();

        } catch (Exception e) {
            Throwable excepcion = e.getCause();
            if (excepcion instanceof RollbackException) {
                excepcion = excepcion.getCause();
                if (excepcion instanceof ConstraintViolationException) {
                    throw new UniqueFieldException(ConstantesServices.CODIGO_ERROR_COD_PARAMETRO_UNICO, ConstantesServices.MENSAJE_ERROR_COD_PARAMETRO_UNICO, e);
                }
                throw new DataBaseException(e);
            }
            throw new GenericException(e);
        }

    }

    /**
     * Método que realiza la búsqueda de los parámetros según filtros de
     * búsqueda
     *
     * @param request
     * @param usuario
     * @return
     */
    public CustomPaginate<DetalleConsultaParametroDTO> buscarParametro(FiltroMasivasRequest request, String usuario) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.PARAMETRO, ConstantesServices.METODO_CONSULTAR, request.toStringParametro());

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

            Page<DetalleConsultaParametroDTO> objPageable = parametroRepository.buscarPorFiltros(request.getCodigo(), request.getIdGrupoParametro(), request.getEstado(), pageable);

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
     * Método que realiza la actualización de un parámetro
     *
     * @param request
     * @param usuario
     * @return
     */
    public Long editarParametro(MasivasRequestDTO request, String usuario) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.PARAMETRO, ConstantesServices.METODO_ACTUALIZAR, request.toStringParametro());

            TpParametro parametro = parametroRepository.findById(request.getIdParametro())
                    .orElseThrow(() -> new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA));
            updateParametro(parametro, request, usuario, ConstantesServices.OPERACION_EDITAR);
            parametroRepository.save(parametro);

            return parametro.getIdParametro();

        } catch (NoOperationExistsException e) {
            throw e;
        } catch (Exception e) {
            Throwable excepcion = e.getCause();
            if (excepcion instanceof RollbackException) {
                excepcion = excepcion.getCause();
                if (excepcion instanceof ConstraintViolationException) {
                    throw new UniqueFieldException(ConstantesServices.CODIGO_ERROR_COD_PARAMETRO_UNICO, ConstantesServices.MENSAJE_ERROR_COD_PARAMETRO_UNICO, e);
                }
                throw new DataBaseException(e);
            }
            throw new GenericException(e);
        }
    }

    /**
     * Método que obtiene el registro de un parámetro según id
     *
     * @param request
     * @param usuario
     * @return
     */
    public DetalleRegistroParametroDTO obtenerParametro(FiltroMasivasRequest request, String usuario) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.PARAMETRO, ConstantesServices.METODO_OBTENER, request.toStringParametroObtener());

            ModelMapper modelMapper = new ModelMapper();
            TpParametro parametro = parametroRepository.findById(request.getIdParametro())
                    .orElseThrow(() -> new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA));
            DetalleRegistroParametroDTO parametroDTO = new DetalleRegistroParametroDTO();
            modelMapper.map(parametro, parametroDTO);

            return parametroDTO;

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
     * Método que realiza el cambio de estado de un parámetro
     *
     * @param request
     * @param usuario
     * @param estado
     * @return
     */
    public EstadoDTO cambiarEstadoParametro(MasivasRequestDTO request, String usuario, String estado) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.PARAMETRO, ConstantesServices.METODO_ACTIVAR_DESACTIVAR, request.toStringActivarDesactivar());

            int numExito = 0;
            int totalIds = request.getIdsOperacion().size();
            String mensaje;

            for (Long id : request.getIdsOperacion()) {
                TpParametro parametro = parametroRepository.findById(id)
                        .orElseThrow(() -> new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA));
                request.setEstado(estado);
                updateParametro(parametro, request, usuario, ConstantesServices.BLANCO);
                parametroRepository.save(parametro);
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

    public List<ComboEstadoDTO> listarParametrosPorGrupoConsulta(Long idGrupoParametro) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD_ACCION, ConstantesServices.LISTAR_PARAMETROS, "{" + ConstantesServices.AUDIT_CAMPO_GRUPO_PARAMETRO + idGrupoParametro + "}");

            List<TpParametro> listaGrupo = parametroRepository.getByIdGrupoParametro(idGrupoParametro);

            return listaGrupo.stream()
                    .sorted(Comparator.comparing(TpParametro::getCodigo))
                    .map(estado -> new ComboEstadoDTO(estado.getCodigo(), estado.getValor()))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            Throwable excepcion = e.getCause();
            if (excepcion instanceof RollbackException) {
                throw new DataBaseException(e);
            }
            throw new GenericException(e);
        }

    }

    public List<ComboEstadoDTO> listarParametrosPorGrupoFormulario(Long idGrupoParametro) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD_ACCION, ConstantesServices.LISTAR_PARAMETROS, "{" + ConstantesServices.AUDIT_CAMPO_GRUPO_PARAMETRO + idGrupoParametro + "}");

            List<TpParametro> listaGrupo = parametroRepository.getByIdGrupoParametro(idGrupoParametro);

            return listaGrupo.stream()
                    .sorted(Comparator.comparing(TpParametro::getIdParametro))
                    .map(estado -> new ComboEstadoDTO(estado.getIdParametro().toString(), estado.getValor()))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            Throwable excepcion = e.getCause();
            if (excepcion instanceof RollbackException) {
                throw new DataBaseException(e);
            }
            throw new GenericException(e);
        }

    }

    public List<ComboEstadoDTO> listarCategoriaDirectorio(Long idGrupoParametro) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD_ACCION, ConstantesServices.LISTAR_PARAMETROS, "{" + ConstantesServices.AUDIT_CAMPO_GRUPO_PARAMETRO + idGrupoParametro + "}");

            List<ComboEstadoDTO> listaCompleta = listarParametrosPorGrupoConsulta(idGrupoParametro);

            return listaCompleta.stream()
                    .filter(estado -> "1000".equals(estado.getCodigo()) || "2000".equals(estado.getCodigo()))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            Throwable excepcion = e.getCause();
            if (excepcion instanceof RollbackException) {
                throw new DataBaseException(e);
            }
            throw new GenericException(e);
        }

    }

    private void updateParametro(TpParametro parametro, MasivasRequestDTO request, String usuario, String operacion) {

        if (operacion.equals(ConstantesServices.OPERACION_EDITAR)) {
            parametro.setIdGrupoParametro(request.getIdGrupoParametro());
            parametro.setCodigo(request.getCodigo());
            parametro.setValor(request.getValor());
        } else {
            parametro.setEstado(request.getEstado());
        }

        parametro.setFecModificacion(LocalDateTime.now());
        parametro.setUsuModificacion(usuario);
    }

    public void logEvento(String mensaje, Object... param) {
        LOGGER.info(mensaje, param);
    }

    public <T> void logAuditoria(T request, Evento evento, Estado estado, UserContext userContext, String nombreTabla, String accion, String mensajeExito) {
        LOGGER.audit(null, request, evento, estado, userContext.getUsername(), userContext.getScaProfile(), nombreTabla, userContext.getIp(),
                null, accion, null, null, mensajeExito);
    }

//    public void logError(String mensajeError, Exception e) {
//        LOGGER.error(mensajeError, e);
//    }
}
