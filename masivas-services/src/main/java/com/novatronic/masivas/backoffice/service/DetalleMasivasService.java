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
import com.novatronic.masivas.backoffice.security.service.CryptoService;
import com.novatronic.masivas.backoffice.security.util.MaskUtil;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import com.novatronic.masivas.backoffice.util.GenerarReporte;
import com.novatronic.masivas.backoffice.util.ServicesUtil;
import com.novatronic.novalog.audit.logger.NovaLogger;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
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
    private final CryptoService cryptoServiceImpl;
    private final MaskUtil maskUtil;
    private final GenericService genericService;

    @Value("${reporte.logo}")
    private String logo;
    @Value("${mask.active}")
    private String maskActive;

    private static final NovaLogger LOGGER = NovaLogger.getLogger(DetalleMasivasService.class);

    public DetalleMasivasService(DetalleArchivoMasivasRepository detalleArchivoMasivasRepository, CryptoService cryptoServiceImpl, MaskUtil maskUtil, GenericService genericService) {
        this.detalleArchivoMasivasRepository = detalleArchivoMasivasRepository;
        this.cryptoServiceImpl = cryptoServiceImpl;
        this.maskUtil = maskUtil;
        this.genericService = genericService;
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

            //Generamos el hash para cuenta origen y cuenta destino
            request.setCuentaOrigen(ServicesUtil.hashData(request.getCuentaOrigen()));
            request.setCuentaDestino(ServicesUtil.hashData(request.getCuentaDestino()));

            logEvento("Hash Cuenta Origen {}", request.getCuentaOrigen());
            logEvento("Hash Cuenta Destino {}", request.getCuentaDestino());

            Page<DetalleRegistroArchivoMasivasDTO> objPageable = detalleArchivoMasivasRepository.buscarPorFiltros(request.getNombreArchivo(), fechaInicioObtencion, fechaFinObtencion, fechaInicioProcesada, fechaFinProcesada,
                    request.getCuentaOrigen(), request.getCuentaDestino(), request.getMotivoRechazo(), request.getTipoTransaccion(), pageable);

            int totalPaginas = objPageable.getTotalPages();
            long totalRegistrosLong = objPageable.getTotalElements();
            int totalRegistros = (totalRegistrosLong > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) totalRegistrosLong;

            for (DetalleRegistroArchivoMasivasDTO detalle : objPageable.getContent()) {

                detalle.setCuentaOrigen(cryptoServiceImpl.decrypt(detalle.getCuentaOrigen()));
                detalle.setNumeroDocumento(cryptoServiceImpl.decrypt(detalle.getNumeroDocumento()));
                detalle.setCuentaDestino(cryptoServiceImpl.decrypt(detalle.getCuentaDestino()));

                if (ConstantesServices.ESTADO_ACTIVO.equals(maskActive)) {

                    detalle.setCuentaOrigen(maskUtil.format(detalle.getCuentaOrigen()));
                    detalle.setNumeroDocumento(maskUtil.format(detalle.getNumeroDocumento()));
                    detalle.setCuentaDestino(maskUtil.format(detalle.getCuentaDestino()));

                }
            }

            CustomPaginate<DetalleRegistroArchivoMasivasDTO> customPaginate = new CustomPaginate<>(totalPaginas, totalRegistros, objPageable.getContent());

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD_RESULTADOS, customPaginate.getTotalRegistros());

            return customPaginate;

        } catch (DataAccessException e) {
            throw new DataBaseException(e);
        } catch (Exception e) {
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
            parameters.put("IN_MOTIVO_RECHAZO", genericService.getNombreMotivoRechazo(request.getMotivoRechazo()));
            parameters.put("IN_TIPO_TRANSACCION", genericService.getNombreTipoTransaccion(request.getTipoTransaccion()));
            return GenerarReporte.generarReporte(resultado.getContenido(), parameters, usuario, tipoArchivo, "reportes/reporteDetalleArchivoMasivas.jrxml", "detalleMasivas", logo);

        } catch (JasperReportException | DataBaseException | GenericException e) {
            throw e;
        } catch (Exception e) {
            throw new GenericException(e);
        }

    }

    public void logEvento(String mensaje, Object... param) {
        LOGGER.info(mensaje, param);
    }

    public <T> void logAuditoria(T request, UserContext userContext, String mensajeExito) {
        LOGGER.auditSuccess(null, request,userContext.getUsername(),userContext.getScaProfile(),
                userContext.getIp(), ConstantesServices.VACIO,mensajeExito,ConstantesServices.RESPUESTA_OK_API);
    }

}
