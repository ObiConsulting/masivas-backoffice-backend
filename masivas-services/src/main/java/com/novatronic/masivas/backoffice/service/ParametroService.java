package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaParametroDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroParametroDTO;
import com.novatronic.masivas.backoffice.dto.EstadoDTO;
import com.novatronic.masivas.backoffice.entity.TpParametro;
import com.novatronic.masivas.backoffice.exception.DataBaseException;
import com.novatronic.masivas.backoffice.exception.GenericException;
import com.novatronic.masivas.backoffice.exception.UniqueFieldException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import com.novatronic.masivas.backoffice.repository.ParametroRepository;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import com.novatronic.novalog.audit.logger.NovaLogger;
import jakarta.transaction.RollbackException;
import jakarta.transaction.Transactional;
import java.util.Date;
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

    public Long crearParametro(MasivasRequestDTO request, String usuario) {

        try {

            TpParametro parametro = new TpParametro(
                    request.getCodigo(),
                    request.getValor(),
                    ConstantesServices.ESTADO_INACTIVO,
                    request.getIdGrupoParametro(),
                    new Date(),
                    usuario
            );
            LOGGER.info("before insert: ");
            parametro = parametroRepository.save(parametro);
            LOGGER.info("result insert: " + parametro);
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

    public CustomPaginate<DetalleConsultaParametroDTO> buscarParametro(FiltroMasivasRequest request, String usuario) {

        try {

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

            return customPaginate;

        } catch (Exception e) {
            Throwable excepcion = e.getCause();
            if (excepcion instanceof RollbackException) {
                throw new DataBaseException(e);
            }
            throw new GenericException(e);
        }

    }

    public Long editarParametro(MasivasRequestDTO request, String usuario) {

        try {

            TpParametro parametro = parametroRepository.findById(request.getIdParametro()).get();
            updateParametro(parametro, request, usuario, ConstantesServices.OPERACION_EDITAR);

            System.out.println("before update: ");
            TpParametro tpParametroSaved = parametroRepository.save(parametro);
            System.out.println("result update: " + tpParametroSaved);
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

    public DetalleRegistroParametroDTO obtenerParametro(FiltroMasivasRequest request, String usuario) {

        try {

            ModelMapper modelMapper = new ModelMapper();
            System.out.println("before findById: ");
            TpParametro parametro = parametroRepository.findById(request.getIdParametro()).get();
            System.out.println("result findById: " + parametro);
            DetalleRegistroParametroDTO parametroDTO = new DetalleRegistroParametroDTO();

            modelMapper.map(parametro, parametroDTO);
            return parametroDTO;

        } catch (Exception e) {
            Throwable excepcion = e.getCause();
            if (excepcion instanceof RollbackException) {
                throw new DataBaseException(e);
            }
            throw new GenericException(e);
        }
    }

    @Transactional
    public EstadoDTO cambiarEstadoParametro(MasivasRequestDTO request, String usuario, String estado) {

        try {

            int numExito = 0;
            int totalIds = request.getIdsOperacion().size();
            String mensaje;

            for (Long id : request.getIdsOperacion()) {
                TpParametro parametro = parametroRepository.findById(id).get();
                request.setEstado(estado);
                updateParametro(parametro, request, usuario, ConstantesServices.BLANCO);

                System.out.println("before update: ");
                TpParametro tpParametroSaved = parametroRepository.save(parametro);
                System.out.println("result update: " + tpParametroSaved);
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

    private void updateParametro(TpParametro parametro, MasivasRequestDTO request, String usuario, String operacion) {

        if (operacion.equals(ConstantesServices.OPERACION_EDITAR)) {
            parametro.setIdGrupoParametro(request.getIdGrupoParametro());
            parametro.setCodigo(request.getCodigo());
            parametro.setValor(request.getValor());
        } else {
            parametro.setEstado(request.getEstado());
        }

        parametro.setFecModificacion(new Date());
        parametro.setUsuModificacion(usuario);
    }

    public void logError(String mensajeError, Exception e) {
        LOGGER.error(mensajeError, e);
    }

}
