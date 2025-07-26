package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.DetalleConsultaReporteCierreDTO;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaReporteTotalizadoDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.exception.DataBaseException;
import com.novatronic.masivas.backoffice.exception.GenericException;
import com.novatronic.masivas.backoffice.repository.ArchivoDirectorioRepository;
import com.novatronic.masivas.backoffice.repository.ArchivoMasivasRepository;
import com.novatronic.masivas.backoffice.repository.ArchivoTitularidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import com.novatronic.masivas.backoffice.util.ServicesUtil;
import com.novatronic.novalog.audit.logger.NovaLogger;
import com.novatronic.novalog.audit.util.Estado;
import com.novatronic.novalog.audit.util.Evento;
import jakarta.transaction.RollbackException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 *
 * @author Obi Consulting
 */
@Service
public class ReporteService {

    @Autowired
    private final ArchivoDirectorioRepository archivoDirectorioRepository;
    @Autowired
    private final ArchivoMasivasRepository archivoMasivasRepository;
    @Autowired
    private final ArchivoTitularidadRepository archivoTitularidadRepository;

    private static final NovaLogger LOGGER = NovaLogger.getLogger(ReporteService.class);

    public ReporteService(ArchivoDirectorioRepository archivoDirectorioRepository, ArchivoMasivasRepository archivoMasivasRepository, ArchivoTitularidadRepository archivoTitularidadRepository) {
        this.archivoDirectorioRepository = archivoDirectorioRepository;
        this.archivoMasivasRepository = archivoMasivasRepository;
        this.archivoTitularidadRepository = archivoTitularidadRepository;
    }

    /**
     * Método que realiza el reporte de cierre diario
     *
     * @param request
     * @param usuario
     * @return
     */
    public DetalleConsultaReporteCierreDTO reporteCierre(FiltroMasivasRequest request, String usuario) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD_ACCION, ConstantesServices.REPORTE_CIERRE, request.toStringReporteCierreObtener());

            LocalDateTime fechaInicio = request.getFecha().atStartOfDay();
            LocalDateTime fechaFin = request.getFecha().atTime(LocalTime.MAX);

            List<Object[]> listaTotalDirectorio = archivoDirectorioRepository.totalesPorEstado(fechaInicio, fechaFin);
            List<Object[]> listaTotalMasivas = archivoMasivasRepository.totalesPorEstado(fechaInicio, fechaFin);
            List<Object[]> listaTotalTitularidad = archivoTitularidadRepository.totalesPorEstado(fechaInicio, fechaFin);

            DetalleConsultaReporteCierreDTO reporte = new DetalleConsultaReporteCierreDTO();
            reporte.setDetalleArchivoDirectorio(listaTotalDirectorio);
            calcularTotales(reporte, listaTotalDirectorio);
            reporte.setDetalleArchivoMasivas(listaTotalMasivas);
            calcularTotales(reporte, listaTotalMasivas);
            reporte.setDetalleArchivoTitularidad(listaTotalTitularidad);
            calcularTotales(reporte, listaTotalTitularidad);

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD_RESULTADOS_REPORTE, "Total Obtenido Cliente", reporte.getTotalObtenidoCliente());
            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD_RESULTADOS_REPORTE, "Total Enviado CCE", reporte.getTotalEnviadoCCE());
            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD_RESULTADOS_REPORTE, "Total Obtenido CCE", reporte.getTotalObtenidoCCE());
            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD_RESULTADOS_REPORTE, "Total Enviado Cliente", reporte.getTotalEnviadoCliente());

            return reporte;

        } catch (Exception e) {
            Throwable excepcion = e.getCause();
            if (excepcion instanceof RollbackException) {
                throw new DataBaseException(e);
            }
            throw new GenericException(e);
        }

    }

    /**
     * Método que realiza el reporte de cierre diario
     *
     * @param request
     * @param usuario
     * @return
     */
    public DetalleConsultaReporteTotalizadoDTO reporteTotalizado(FiltroMasivasRequest request, String usuario) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD_ACCION, ConstantesServices.REPORTE_TOTALIZADO, request.toStringReporteCierreObtener());

            LocalDateTime fechaInicio = request.getFecha().atStartOfDay();
            LocalDateTime fechaFin = request.getFecha().atTime(LocalTime.MAX);

            List<Object[]> listaTotalMasivas = archivoMasivasRepository.reporteTotalizado(fechaInicio, fechaFin);

            DetalleConsultaReporteTotalizadoDTO reporte = new DetalleConsultaReporteTotalizadoDTO();
            procesarTotalizado(reporte, listaTotalMasivas);

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD_RESULTADOS_REPORTE, "Total Procesado", reporte.getTotalProcesado());
            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD_RESULTADOS_REPORTE, "Total Pendiente por Procesar", reporte.getTotalPendiente());

            return reporte;

        } catch (Exception e) {
            Throwable excepcion = e.getCause();
            if (excepcion instanceof RollbackException) {
                throw new DataBaseException(e);
            }
            throw new GenericException(e);
        }

    }

    private void calcularTotales(DetalleConsultaReporteCierreDTO reporte, List<Object[]> listaArchivo) {

        listaArchivo.forEach((Object[] resultado) -> {

            String estado = (String) resultado[0];
            Long cantidad = (Long) resultado[1];

            switch (estado) {

                case "Obtenido Cliente" ->
                    reporte.setTotalObtenidoCliente(reporte.getTotalObtenidoCliente() + cantidad);
                case "Enviado CCE" ->
                    reporte.setTotalEnviadoCCE(reporte.getTotalEnviadoCCE() + cantidad);
                case "Obtenido CCE" ->
                    reporte.setTotalObtenidoCCE(reporte.getTotalObtenidoCCE() + cantidad);
                case "Enviado Cliente" ->
                    reporte.setTotalEnviadoCliente(reporte.getTotalEnviadoCliente() + cantidad);
            }

        });
    }

    private void procesarTotalizado(DetalleConsultaReporteTotalizadoDTO reporte, List<Object[]> listaArchivo) {

        listaArchivo.forEach((Object[] resultado) -> {

            String estado = (String) resultado[0];
            Long cantidad = (Long) resultado[1];

            switch (estado) {

                case "Procesado" -> {
                    reporte.setTotalProcesado(reporte.getTotalProcesado() + cantidad);
                    BigDecimal montoProcesado = ServicesUtil.convertirABigDecimal(resultado[2]);
                    BigDecimal montoRechazado = ServicesUtil.convertirABigDecimal(resultado[3]);
                    reporte.setMontoProcesado(montoProcesado);
                    reporte.setMontoRechazado(montoRechazado);
                }
                case "Pendiente por Procesar" ->
                    reporte.setTotalPendiente(reporte.getTotalPendiente() + cantidad);

            }

        });
    }

    public void logEvento(String mensaje, Object... param) {
        LOGGER.info(mensaje, param);
    }

    public <T> void logAuditoria(T request, Evento evento, Estado estado, UserContext userContext, String nombreTabla, String accion, String mensajeExito) {
        LOGGER.audit(null, request, evento, estado, userContext.getUsername(), userContext.getScaProfile(), nombreTabla, userContext.getIp(),
                null, accion, null, null, mensajeExito);
    }

}
