package com.novatronic.masivas.backoffice.repository;

import com.novatronic.masivas.backoffice.dto.DetalleRegistroArchivoMasivasDTO;
import com.novatronic.masivas.backoffice.dto.DetalleReporteConsolidadoDTO;
import com.novatronic.masivas.backoffice.entity.TpDetalleMasivas;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Obi Consulting
 */
@Repository
public interface DetalleArchivoMasivasRepository extends JpaRepository<TpDetalleMasivas, Long> {

    @Query("    SELECT NEW com.novatronic.masivas.backoffice.dto.DetalleRegistroArchivoMasivasDTO("
            + "d.idArchivo, m.nombre, d.cuentaOrigen, d.numeroDocumentoCliente, d.cuentaDestino, d.importe, d.tipoTransaccion, d.fechaTransaccion, d.codigoRespuesta, d.mensajeRespuesta, "
            + "    CASE "
            + "        WHEN d.codigoRespuesta = '00' THEN 'Aceptada' "
            + "        WHEN d.codigoRespuesta = '05' THEN 'Rechazada' "
            + "        WHEN d.codigoRespuesta IS NULL and d.mensajeRespuesta IS NULL THEN 'En Proceso' "
            + "        WHEN d.codigoRespuesta IS NULL and d.mensajeRespuesta IS NOT NULL THEN 'TimeOut' "
            + "        ELSE 'Pendiente' "
            + "    END) "
            + "FROM TpDetalleMasivas d JOIN d.archivoMasivas m\n"
            + "     WHERE (:fechaInicioObtencion IS NULL OR m.fechaObtencion >= :fechaInicioObtencion)\n"
            + "      AND (:fechaFinObtencion IS NULL OR m.fechaObtencion <= :fechaFinObtencion)\n"
            + "      AND (:fechaInicioProcesada IS NULL OR m.fechaProcesada >= :fechaInicioProcesada)\n"
            + "      AND (:fechaFinProcesada IS NULL OR m.fechaProcesada <= :fechaFinProcesada)\n"
            + "      AND (:nombreArchivo IS NULL OR LOWER(m.nombre) LIKE LOWER(CONCAT('%', :nombreArchivo, '%')))\n"
            + "      AND (:cuentaOrigen IS NULL OR LOWER(d.cuentaOrigen) LIKE LOWER(CONCAT('%', :cuentaOrigen, '%')))\n"
            + "      AND (:cuentaDestino IS NULL OR LOWER(d.cuentaDestino) LIKE LOWER(CONCAT('%', :cuentaDestino, '%')))\n"
            + "      AND (:motivoRechazo IS NULL OR d.mensajeRespuesta = :motivoRechazo)\n"
            + "      AND (:tipoTransaccion IS NULL OR d.tipoTransaccion = :tipoTransaccion)")
    Page<DetalleRegistroArchivoMasivasDTO> buscarPorFiltros(
            @Param("nombreArchivo") String nombreArchivo,
            @Param("fechaInicioObtencion") LocalDateTime fechaInicioObtencion,
            @Param("fechaFinObtencion") LocalDateTime fechaFinObtencion,
            @Param("fechaInicioProcesada") LocalDateTime fechaInicioProcesada,
            @Param("fechaFinProcesada") LocalDateTime fechaFinProcesada,
            @Param("cuentaOrigen") String cuentaOrigen,
            @Param("cuentaDestino") String cuentaDestino,
            @Param("motivoRechazo") String motivoRechazo,
            @Param("tipoTransaccion") String tipoTransaccion,
            Pageable pageable
    );

    @Query("SELECT NEW com.novatronic.masivas.backoffice.dto.DetalleReporteConsolidadoDTO("
            + "d.codEntidadDestino, "
            + "COUNT(d.idArchivo), "
            + "CAST(SUM(CASE WHEN d.codigoRespuesta = '00' THEN d.importe ELSE 0 END) AS java.math.BigDecimal), "
            + "CAST(SUM(CASE WHEN d.codigoRespuesta = '05' THEN d.importe ELSE 0 END) AS java.math.BigDecimal) "
            + ") "
            + "FROM TpDetalleMasivas d "
            //            + "FROM TpDetalleMasivas d JOIN d.entidad e "
            + "WHERE (:fechaInicio IS NULL OR d.fechaTransaccion >= :fechaInicio) "
            + "AND (:fechaFin IS NULL OR d.fechaTransaccion <= :fechaFin) "
            + "GROUP BY d.codEntidadDestino")
//            + "GROUP BY CONCAT(d.cuentaDestino, ' - ', e.nombre)")
    List<DetalleReporteConsolidadoDTO> totalesPorEntidadDestino(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );
}
