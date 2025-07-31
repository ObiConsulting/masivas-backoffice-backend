package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaRutaDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroRutaDTO;
import com.novatronic.masivas.backoffice.dto.ReporteDTO;
import com.novatronic.masivas.backoffice.entity.TpRuta;
import com.novatronic.masivas.backoffice.exception.DataBaseException;
import com.novatronic.masivas.backoffice.exception.GenericException;
import com.novatronic.masivas.backoffice.exception.JasperReportException;
import com.novatronic.masivas.backoffice.exception.NoOperationExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.novatronic.masivas.backoffice.repository.RutaRepository;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Obi Consulting
 */
@Service
public class RutaService {

    @Autowired
    private final RutaRepository rutaRepository;
    private final GenericService genericService;

    @Value("${reporte.logo}")
    private String logo;

    private static final NovaLogger LOGGER = NovaLogger.getLogger(RutaService.class);

    public RutaService(RutaRepository rutaRepository, GenericService genericService) {
        this.rutaRepository = rutaRepository;
        this.genericService = genericService;
    }

    /**
     * Método que realiza la búsqueda de las rutas según filtros de búsqueda
     *
     * @param request
     * @return
     */
    public CustomPaginate<DetalleConsultaRutaDTO> buscarRuta(FiltroMasivasRequest request) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.RUTA, ConstantesServices.METODO_CONSULTAR, request.toStringRuta());

            Pageable pageable = ServicesUtil.configurarPageSort(request);

            Page<DetalleConsultaRutaDTO> objPageable = rutaRepository.buscarPorFiltros(request.getCodTipoArchivo(), request.getCodCategoriaDirectorio(), pageable);

            int totalPaginas = objPageable.getTotalPages();
            long totalRegistrosLong = objPageable.getTotalElements();
            int totalRegistros = (totalRegistrosLong > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) totalRegistrosLong;

            CustomPaginate<DetalleConsultaRutaDTO> customPaginate = new CustomPaginate<>(totalPaginas, totalRegistros, objPageable.getContent());

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
     * Método que realiza la actualización de una ruta
     *
     * @param request
     * @param usuario
     * @return
     */
    public Long editarRuta(MasivasRequestDTO request, String usuario) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.RUTA, ConstantesServices.METODO_ACTUALIZAR, request.toStringRuta());

            TpRuta ruta = rutaRepository.findById(request.getIdRuta())
                    .orElseThrow(() -> new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA));
            updateRuta(ruta, request, usuario, ConstantesServices.OPERACION_EDITAR);
            rutaRepository.save(ruta);

            return ruta.getIdRuta();

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
     * Método que obtiene el registro de una ruta según id
     *
     * @param request
     * @return
     */
    public DetalleRegistroRutaDTO obtenerRuta(FiltroMasivasRequest request) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.RUTA, ConstantesServices.METODO_OBTENER, request.toStringRutaObtener());
            return rutaRepository.buscarPorId(request.getIdRuta());

        } catch (Exception e) {
            Throwable excepcion = e.getCause();
            if (excepcion instanceof RollbackException) {
                throw new DataBaseException(e);
            }
            throw new GenericException(e);
        }
    }

    /**
     * Método que realiza la búsqueda de las rutas según filtros de búsqueda y
     * las exporta a un archivo pdf/xlsx
     *
     * @param request
     * @param usuario
     * @param tipoArchivo
     * @return
     */
    public ReporteDTO descargarRutas(FiltroMasivasRequest request, String usuario, String tipoArchivo) {

        try {
            request.setNumeroPagina(0);
            request.setRegistrosPorPagina(0);

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.RUTA, ConstantesServices.METODO_DESCARGAR, request.toStringRuta());
            CustomPaginate<DetalleConsultaRutaDTO> resultado = buscarRuta(request);

            HashMap<String, Object> parameters = new HashMap<>();
            //Filtros
            parameters.put("IN_CATEGORIA", genericService.getNombreCategoriaDirectorio(request.getCodCategoriaDirectorio()));
            parameters.put("IN_TIPO_ARCHIVO", genericService.getNombreTipoArchivo(request.getCodTipoArchivo()));

            return GenerarReporte.generarReporte(resultado.getContenido(), parameters, usuario, tipoArchivo, "reportes/reporteRutaArchivos.jrxml", "rutaArchivo", logo);

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

    private void updateRuta(TpRuta ruta, MasivasRequestDTO request, String usuario, String operacion) {

        if (operacion.equals(ConstantesServices.OPERACION_EDITAR)) {
            ruta.setRuta(request.getRuta());
        } else {
            ruta.setEstado(request.getEstado());
        }

        ruta.setFecModificacion(LocalDateTime.now());
        ruta.setUsuModificacion(usuario);
    }

    public void logEvento(String mensaje, Object... param) {
        LOGGER.info(mensaje, param);
    }

    public <T> void logAuditoria(T request, Evento evento, Estado estado, UserContext userContext, String nombreTabla, String accion, String mensajeExito) {
        LOGGER.audit(null, request, evento, estado, userContext.getUsername(), userContext.getScaProfile(), nombreTabla, userContext.getIp(),
                null, accion, null, null, mensajeExito);
    }

}
