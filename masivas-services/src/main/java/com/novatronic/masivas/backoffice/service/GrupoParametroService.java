package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.ComboEstadoDTO;
import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaGrupoParametroDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroGrupoParametroDTO;
import com.novatronic.masivas.backoffice.dto.EstadoDTO;
import com.novatronic.masivas.backoffice.entity.TpGrupoParametro;
import com.novatronic.masivas.backoffice.exception.DataBaseException;
import com.novatronic.masivas.backoffice.exception.GenericException;
import com.novatronic.masivas.backoffice.exception.UniqueFieldException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import com.novatronic.masivas.backoffice.repository.GrupoParametroRepository;
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
public class GrupoParametroService {

    @Autowired
    private final GrupoParametroRepository grupoParametroRepository;
    private final MessageSource messageSource;

    private static final NovaLogger LOGGER = NovaLogger.getLogger(GrupoParametroService.class);

    public GrupoParametroService(GrupoParametroRepository grupoParametroRepository, MessageSource messageSource) {
        this.grupoParametroRepository = grupoParametroRepository;
        this.messageSource = messageSource;
    }

    public Long crearGrupoParametro(MasivasRequestDTO request, String usuario) {

        try {

            TpGrupoParametro grupoParametro = new TpGrupoParametro(
                    request.getCodigo(),
                    request.getDescripcion(),
                    ConstantesServices.ESTADO_INACTIVO,
                    LocalDateTime.now(),
                    usuario
            );
            LOGGER.info("before insert: ");
            grupoParametro = grupoParametroRepository.save(grupoParametro);
            LOGGER.info("result insert: " + grupoParametro);
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

    public CustomPaginate<DetalleConsultaGrupoParametroDTO> buscarGrupoParametro(FiltroMasivasRequest request, String usuario) {

        try {

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

            Page<TpGrupoParametro> objPageable = grupoParametroRepository.buscarPorFiltros(request.getCodigo(), request.getDescripcion(), request.getEstado(), pageable);

            Page<DetalleConsultaGrupoParametroDTO> dtoPage = objPageable.map(e -> modelMapper.map(e, DetalleConsultaGrupoParametroDTO.class));

            int totalPaginas = objPageable.getTotalPages();
            long totalRegistrosLong = objPageable.getTotalElements();

            int totalRegistros = (totalRegistrosLong > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) totalRegistrosLong;

            CustomPaginate customPaginate = new CustomPaginate<>(
                    totalPaginas,
                    totalRegistros,
                    dtoPage.getContent()
            );

            return customPaginate;

        } catch (Exception e) {
            Throwable excepcion = e.getCause();
            if (excepcion instanceof RollbackException) {
                throw new DataBaseException(e);
            }
            throw new GenericException(e);
        }

    }

    public Long editarGrupoParametro(MasivasRequestDTO request, String usuario) {

        try {

            TpGrupoParametro grupoParametro = grupoParametroRepository.findById(request.getIdGrupoParametro()).get();
            updateGrupoParametro(grupoParametro, request, usuario, ConstantesServices.OPERACION_EDITAR);

            System.out.println("before update: ");
            TpGrupoParametro tpGrupoParametroSaved = grupoParametroRepository.save(grupoParametro);
            System.out.println("result update: " + tpGrupoParametroSaved);
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

    public DetalleRegistroGrupoParametroDTO obtenerGrupoParametro(FiltroMasivasRequest request, String usuario) {

        try {

            ModelMapper modelMapper = new ModelMapper();
            System.out.println("before findById: ");
            TpGrupoParametro grupoParametro = grupoParametroRepository.findById(request.getIdGrupoParametro()).get();
            System.out.println("result findById: " + grupoParametro);
            DetalleRegistroGrupoParametroDTO grupoParametroDTO = new DetalleRegistroGrupoParametroDTO();

            modelMapper.map(grupoParametro, grupoParametroDTO);
            return grupoParametroDTO;

        } catch (Exception e) {
            Throwable excepcion = e.getCause();
            if (excepcion instanceof RollbackException) {
                throw new DataBaseException(e);
            }
            throw new GenericException(e);
        }
    }

    public EstadoDTO cambiarEstadoGrupoParametro(MasivasRequestDTO request, String usuario, String estado) {

        try {

            int numExito = 0;
            int totalIds = request.getIdsOperacion().size();
            String mensaje;

            for (Long id : request.getIdsOperacion()) {
                TpGrupoParametro grupoParametro = grupoParametroRepository.findById(id).get();
                request.setEstado(estado);
                updateGrupoParametro(grupoParametro, request, usuario, ConstantesServices.BLANCO);

                System.out.println("before update: ");
                TpGrupoParametro tpGrupoParametroSaved = grupoParametroRepository.save(grupoParametro);
                System.out.println("result update: " + tpGrupoParametroSaved);
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

    public List<ComboEstadoDTO> listarGrupoParametro() {

        try {

            List<TpGrupoParametro> listaGrupo = grupoParametroRepository.findAll();

            return listaGrupo.stream()
                    .map(grupo -> new ComboEstadoDTO(grupo.getCodigo(), grupo.getDescripcion()))
                    .sorted(Comparator.comparing(ComboEstadoDTO::getCodigo))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            Throwable excepcion = e.getCause();
            if (excepcion instanceof RollbackException) {
                throw new DataBaseException(e);
            }
            throw new GenericException(e);
        }

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

    public void logError(String mensajeError, Exception e) {
        LOGGER.error(mensajeError, e);
    }

    public <T> void logAuditoria(T request, Evento evento, Estado estado, UserContext userContext, String nombreTabla, String accion, String mensajeExito) {
        LOGGER.audit(null, request, evento, estado, userContext.getUsername(), userContext.getScaProfile(), nombreTabla, userContext.getIp(),
                null, accion, null, null, mensajeExito);
    }
}
