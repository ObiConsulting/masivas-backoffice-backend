package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaAplicacionDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroArchivoMasivasDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.exception.DataBaseException;
import com.novatronic.masivas.backoffice.exception.GenericException;
import com.novatronic.masivas.backoffice.repository.DetalleArchivoMasivasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import com.novatronic.novalog.audit.logger.NovaLogger;
import com.novatronic.novalog.audit.util.Estado;
import com.novatronic.novalog.audit.util.Evento;
import jakarta.transaction.RollbackException;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
public class DetalleMasivasService {

    @Autowired
    private final DetalleArchivoMasivasRepository detalleArchivoMasivasRepository;
    private final MessageSource messageSource;

    private static final NovaLogger LOGGER = NovaLogger.getLogger(DetalleMasivasService.class);

    public DetalleMasivasService(DetalleArchivoMasivasRepository detalleArchivoMasivasRepository, MessageSource messageSource) {
        this.detalleArchivoMasivasRepository = detalleArchivoMasivasRepository;
        this.messageSource = messageSource;
    }

    /**
     * Método que realiza la búsqueda del detalles de los archivos de tipo
     * masivas según filtros de búsqueda
     *
     * @param request
     * @param usuario
     * @return
     */
    public CustomPaginate<DetalleConsultaAplicacionDTO> buscarDetalleMasivas(FiltroMasivasRequest request, String usuario) {

        try {

            logEvento(ConstantesServices.MENSAJE_TRAZABILIDAD, ConstantesServices.DETALLE_MASIVAS, ConstantesServices.METODO_CONSULTAR, request.toStringDetalleMasivas());

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

            LocalDateTime fechaInicioObtencion = request.getFechaObtencion().atStartOfDay();
            LocalDateTime fechaFinObtencion = request.getFechaObtencion().atTime(LocalTime.MAX);
            LocalDateTime fechaInicioProcesada = request.getFechaProcesada().atStartOfDay();
            LocalDateTime fechaFinProcesada = request.getFechaProcesada().atTime(LocalTime.MAX);

            Page<DetalleRegistroArchivoMasivasDTO> objPageable = detalleArchivoMasivasRepository.buscarPorFiltros(request.getNombreArchivo(), fechaInicioObtencion, fechaFinObtencion, fechaInicioProcesada, fechaFinProcesada,
                    request.getCuentaOrigen(), request.getCuentaDestino(), request.getMotivoRechazo(), request.getTipoTransaccion(), pageable);

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

    public void logEvento(String mensaje, Object... param) {
        LOGGER.info(mensaje, param);
    }

    public <T> void logAuditoria(T request, Evento evento, Estado estado, UserContext userContext, String nombreTabla, String accion, String mensajeExito) {
        LOGGER.audit(null, request, evento, estado, userContext.getUsername(), userContext.getScaProfile(), nombreTabla, userContext.getIp(),
                null, accion, null, null, mensajeExito);
    }

}
