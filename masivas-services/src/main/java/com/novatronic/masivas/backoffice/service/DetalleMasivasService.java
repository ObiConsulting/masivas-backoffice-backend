package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroArchivoMasivasDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.ReporteDTO;
import com.novatronic.masivas.backoffice.exception.DataBaseException;
import com.novatronic.masivas.backoffice.exception.GenericException;
import com.novatronic.masivas.backoffice.exception.JasperReportException;
import com.novatronic.masivas.backoffice.repository.DetalleArchivoMasivasRepository;
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

/**
 *
 * @author Obi Consulting
 */
@Service
public class DetalleMasivasService {

    @Autowired
    private final DetalleArchivoMasivasRepository detalleArchivoMasivasRepository;
    @Value("${reporte.logo}")
    private String logo;

    private static final NovaLogger LOGGER = NovaLogger.getLogger(DetalleMasivasService.class);

    public DetalleMasivasService(DetalleArchivoMasivasRepository detalleArchivoMasivasRepository) {
        this.detalleArchivoMasivasRepository = detalleArchivoMasivasRepository;
    }

    /**
     * Método que realiza la búsqueda del detalle de los archivos de tipo
     * masivas según filtros de búsqueda
     *
     * @param request
     * @return
     */
    public CustomPaginate<DetalleRegistroArchivoMasivasDTO> buscarDetalleMasivas(FiltroMasivasRequest request) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.DETALLE_MASIVAS, ConstantesServices.METODO_CONSULTAR, request.toStringDetalleMasivas());

            Pageable pageable = ServicesUtil.configurarPageSort(request);

            LocalDateTime fechaInicioObtencion = request.getFechaObtencion().atStartOfDay();
            LocalDateTime fechaFinObtencion = request.getFechaObtencion().atTime(LocalTime.MAX);
            LocalDateTime fechaInicioProcesada = request.getFechaProcesada().atStartOfDay();
            LocalDateTime fechaFinProcesada = request.getFechaProcesada().atTime(LocalTime.MAX);

            Page<DetalleRegistroArchivoMasivasDTO> objPageable = detalleArchivoMasivasRepository.buscarPorFiltros(request.getNombreArchivo(), fechaInicioObtencion, fechaFinObtencion, fechaInicioProcesada, fechaFinProcesada,
                    request.getCuentaOrigen(), request.getCuentaDestino(), request.getMotivoRechazo(), request.getTipoTransaccion(), pageable);

            int totalPaginas = objPageable.getTotalPages();
            long totalRegistrosLong = objPageable.getTotalElements();

            int totalRegistros = (totalRegistrosLong > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) totalRegistrosLong;

            CustomPaginate<DetalleRegistroArchivoMasivasDTO> customPaginate = new CustomPaginate<>(totalPaginas, totalRegistros, objPageable.getContent());

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
     * Método que realiza la búsqueda del detalle de los archivos de tipo
     * masivas según filtros de búsqueda y los exporta a un archivo pdf/xlsx
     *
     * @param request
     * @param usuario
     * @param tipoArchivo
     * @return
     */
    public ReporteDTO descargarDetalleArchivoMasivas(FiltroMasivasRequest request, String usuario, String tipoArchivo) {

        try {
            request.setNumeroPagina(0);
            request.setRegistrosPorPagina(0);

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.DETALLE_MASIVAS, ConstantesServices.METODO_DESCARGAR, request.toStringDetalleMasivas());
            CustomPaginate<DetalleRegistroArchivoMasivasDTO> resultado = buscarDetalleMasivas(request);

            HashMap<String, Object> parameters = new HashMap<>();
            //Filtros
            parameters.put("IN_NOMBRE_ARCHIVO", request.getNombreArchivo());
            parameters.put("IN_FECHA_OBTENCION", ServicesUtil.formatearLocalDateToString(request.getFechaObtencion()));
            parameters.put("IN_FECHA_PROCESADA", ServicesUtil.formatearLocalDateToString(request.getFechaProcesada()));
            parameters.put("IN_CUENTA_ORIGEN", request.getCuentaOrigen());
            parameters.put("IN_CUENTA_DESTINO", request.getCuentaDestino());
            parameters.put("IN_MOTIVO_RECHAZO", request.getMotivoRechazo());//Todo
            parameters.put("IN_TIPO_TRANSACCION", request.getTipoTransaccion());//Todo
            return GenerarReporte.generarReporte(resultado.getContenido(), parameters, usuario, tipoArchivo, "reportes/reporteDetalleArchivoMasivas.jrxml", "detalleMasivas", logo);

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

    public void logEvento(String mensaje, Object... param) {
        LOGGER.info(mensaje, param);
    }

    public <T> void logAuditoria(T request, Evento evento, Estado estado, UserContext userContext, String nombreTabla, String accion, String mensajeExito) {
        LOGGER.audit(null, request, evento, estado, userContext.getUsername(), userContext.getScaProfile(), nombreTabla, userContext.getIp(),
                null, accion, null, null, mensajeExito);
    }

}
