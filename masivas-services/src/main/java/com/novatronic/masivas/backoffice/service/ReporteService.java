package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.DetalleConsultaReporteCierreDTO;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaReporteTotalizadoDTO;
import com.novatronic.masivas.backoffice.dto.DetalleReporteConsolidadoDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.ReporteDTO;
import com.novatronic.masivas.backoffice.exception.DataBaseException;
import com.novatronic.masivas.backoffice.exception.GenericException;
import com.novatronic.masivas.backoffice.exception.JasperReportException;
import com.novatronic.masivas.backoffice.repository.ArchivoDirectorioRepository;
import com.novatronic.masivas.backoffice.repository.ArchivoMasivasRepository;
import com.novatronic.masivas.backoffice.repository.ArchivoTitularidadRepository;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;

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
    @Autowired
    private final DetalleArchivoMasivasRepository detalleArchivoMasivasRepository;
    @Autowired
    private final GenericService genericService;

    @Value("${reporte.logo}")
    private String logo;

    private static final NovaLogger LOGGER = NovaLogger.getLogger(ReporteService.class);

    public ReporteService(ArchivoDirectorioRepository archivoDirectorioRepository, ArchivoMasivasRepository archivoMasivasRepository, ArchivoTitularidadRepository archivoTitularidadRepository,
            DetalleArchivoMasivasRepository detalleArchivoMasivasRepository, GenericService genericService) {
        this.archivoDirectorioRepository = archivoDirectorioRepository;
        this.archivoMasivasRepository = archivoMasivasRepository;
        this.archivoTitularidadRepository = archivoTitularidadRepository;
        this.detalleArchivoMasivasRepository = detalleArchivoMasivasRepository;
        this.genericService = genericService;
    }

    /**
     * Método que realiza el reporte de cierre de un día especifico.
     *
     * @param request
     * @return
     */
    public DetalleConsultaReporteCierreDTO reporteCierre(FiltroMasivasRequest request) {

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

        } catch (DataAccessException e) {
            throw new DataBaseException(e);
        } catch (Exception e) {
            throw new GenericException(e);
        }

    }

    /**
     * Método que realiza el reporte totalizado de un día especifico.
     *
     * @param request
     * @return
     */
    public DetalleConsultaReporteTotalizadoDTO reporteTotalizado(FiltroMasivasRequest request) {

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

        } catch (DataAccessException e) {
            throw new DataBaseException(e);
        } catch (Exception e) {
            throw new GenericException(e);
        }

    }

    /**
     * Método que realiza el reporte consolidado por entidad destino según la
     * fecha indicada
     *
     * @param request
     * @return
     */
    public List<DetalleReporteConsolidadoDTO> reporteConsolidadoPorEntidadDestino(FiltroMasivasRequest request) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD_ACCION, ConstantesServices.REPORTE_CONSOLIDADO, request.toStringReporteConsolidado());

            LocalDateTime fechaInicio = request.getFecha().atStartOfDay();
            LocalDateTime fechaFin = request.getFecha().atTime(LocalTime.MAX);

            List<DetalleReporteConsolidadoDTO> listaReporte = detalleArchivoMasivasRepository.totalesPorEntidadDestino(fechaInicio, fechaFin, request.getMoneda());

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD_RESULTADOS, listaReporte.size());

            return listaReporte;

        } catch (DataAccessException e) {
            throw new DataBaseException(e);
        } catch (Exception e) {
            throw new GenericException(e);
        }

    }

    /**
     * Método que realiza el reporte consolidado por entidad destino según
     * filtros de búsqueda y lo exporta a un archivo pdf/xlsx
     *
     * @param request
     * @param usuario
     * @param tipoArchivo
     * @return
     */
    public ReporteDTO descargarConsolidado(FiltroMasivasRequest request, String usuario, String tipoArchivo) {

        try {
            request.setNumeroPagina(0);
            request.setRegistrosPorPagina(0);

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.REPORTE_CONSOLIDADO, ConstantesServices.METODO_DESCARGAR, request.toStringReporteConsolidado());
            List<DetalleReporteConsolidadoDTO> resultado = reporteConsolidadoPorEntidadDestino(request);

            HashMap<String, Object> parameters = new HashMap<>();
            //Filtros
            parameters.put("IN_FECHA", ServicesUtil.formatearLocalDateToString(request.getFecha()));
            parameters.put("IN_MONEDA", genericService.getNombreMoneda(request.getMoneda()));

            return GenerarReporte.generarReporte(resultado, parameters, usuario, tipoArchivo, "reportes/reporteConsolidado.jrxml", "reporteConsolidado", logo);

        } catch (JasperReportException | DataBaseException | GenericException e) {
            throw e;
        } catch (Exception e) {
            throw new GenericException(e);
        }

    }

    /**
     * Método que realiza el calculo de los totales según su estado para los
     * diferentes tipos de archivos.
     *
     * @param request
     * @param listaArchivo
     * @return
     */
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
                default ->
                    logError("Estado no mapeado", null);
            }

        });
    }

    /**
     * Método que procesa los montos según su estado.
     *
     * @param reporte
     * @param listaArchivo
     * @return
     */
    private void procesarTotalizado(DetalleConsultaReporteTotalizadoDTO reporte, List<Object[]> listaArchivo) {

        listaArchivo.forEach((Object[] resultado) -> {

            String estado = (String) resultado[0];
            Long cantidad = (Long) resultado[1];

            switch (estado) {

                case "Procesado" -> {
                    reporte.setTotalProcesado(reporte.getTotalProcesado() + cantidad);
                    BigDecimal montoProcesadoDolar = ServicesUtil.convertirABigDecimal(resultado[2]).movePointLeft(2);
                    BigDecimal montoProcesadoSol = ServicesUtil.convertirABigDecimal(resultado[3]).movePointLeft(2);
                    BigDecimal montoRechazadoDolar = ServicesUtil.convertirABigDecimal(resultado[4]).movePointLeft(2);
                    BigDecimal montoRechazadoSol = ServicesUtil.convertirABigDecimal(resultado[5]).movePointLeft(2);
                    reporte.setMontoProcesadoDolar(montoProcesadoDolar);
                    reporte.setMontoProcesadoSol(montoProcesadoSol);
                    reporte.setMontoRechazadoDolar(montoRechazadoDolar);
                    reporte.setMontoRechazadoSol(montoRechazadoSol);
                }
                case "Pendiente por Procesar" ->
                    reporte.setTotalPendiente(reporte.getTotalPendiente() + cantidad);
                default ->
                    logError("Estado no mapeado", null);
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

    public void logError(String mensajeError, Exception e) {
        LOGGER.error(mensajeError, e);
    }

}
