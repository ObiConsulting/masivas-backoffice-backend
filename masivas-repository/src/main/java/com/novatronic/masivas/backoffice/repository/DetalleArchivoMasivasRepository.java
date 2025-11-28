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
            + "d.idArchivo, m.nombre, d.cuentaOrigen, d.numeroDocumentoCliente, d.cuentaDestino, d.importe,"
            + "CONCAT (d.moneda, ' - ', mo.valor), CONCAT(d.codTipoTransaccion, ' - ', t.valor), d.fechaTransaccion, d.codigoRespuesta, d.mensajeRespuesta, "
            + "    CAST(CASE "
            + "        WHEN d.codigoRespuesta = '00' THEN 'Aceptada' "
            + "        WHEN d.codigoRespuesta = '05' THEN 'Rechazada' "
            + "        WHEN d.codigoRespuesta IS NULL and d.mensajeRespuesta IS NULL THEN 'En Proceso' "
            + "        WHEN d.codigoRespuesta IS NULL and d.mensajeRespuesta IS NOT NULL THEN 'TimeOut' "
            + "        ELSE 'Pendiente' "
            + "    END AS string)) "
            + "FROM TpDetalleMasivas d JOIN d.archivoMasivas m JOIN d.tipoTransaccion t JOIN d.monedaDesc mo\n"
            + "     WHERE (COALESCE(:fechaInicioObtencion, m.fechaObtencion) IS NULL OR m.fechaObtencion >= :fechaInicioObtencion)\n"
            + "      AND (COALESCE(:fechaFinObtencion, m.fechaObtencion) IS NULL OR m.fechaObtencion <= :fechaFinObtencion)\n"
            + "      AND (COALESCE(:fechaInicioProcesada, m.fechaProcesada) IS NULL OR m.fechaProcesada >= :fechaInicioProcesada)\n"
            + "      AND (COALESCE(:fechaFinProcesada, m.fechaProcesada) IS NULL OR m.fechaProcesada <= :fechaFinProcesada)\n"
            + "      AND (:nombreArchivo IS NULL OR LOWER(m.nombre) LIKE LOWER(CONCAT('%', :nombreArchivo, '%')))\n"
            + "      AND (:cuentaOrigen IS NULL OR LOWER(d.cuentaOrigenHash) LIKE LOWER(CONCAT('%', :cuentaOrigen, '%')))\n"
            + "      AND (:cuentaDestino IS NULL OR LOWER(d.cuentaDestinoHash) LIKE LOWER(CONCAT('%', :cuentaDestino, '%')))\n"
            + "      AND (:motivoRechazo IS NULL OR d.mensajeRespuesta = :motivoRechazo)\n"
            + "      AND (:codTipoTransaccion IS NULL OR d.codTipoTransaccion = :codTipoTransaccion)")
    Page<DetalleRegistroArchivoMasivasDTO> buscarPorFiltros(
            @Param("nombreArchivo") String nombreArchivo,
            @Param("fechaInicioObtencion") LocalDateTime fechaInicioObtencion,
            @Param("fechaFinObtencion") LocalDateTime fechaFinObtencion,
            @Param("fechaInicioProcesada") LocalDateTime fechaInicioProcesada,
            @Param("fechaFinProcesada") LocalDateTime fechaFinProcesada,
            @Param("cuentaOrigen") String cuentaOrigen,
            @Param("cuentaDestino") String cuentaDestino,
            @Param("motivoRechazo") String motivoRechazo,
            @Param("codTipoTransaccion") String codTipoTransaccion,
            Pageable pageable
    );

    @Query("  SELECT NEW com.novatronic.masivas.backoffice.dto.DetalleReporteConsolidadoDTO("
            + "CONCAT(d.codEntidadDestino, ' - ', e.nombre), "
            + "CONCAT (d.moneda, ' - ', mo.valor), "
            + "COUNT(d.idArchivo), "
            + "CAST(SUM(CASE WHEN d.codigoRespuesta = '00' THEN d.importe ELSE 0 END) AS java.math.BigDecimal)/100, "//Monto Procesado
            + "CAST(SUM(CASE WHEN d.codigoRespuesta = '05' THEN d.importe ELSE 0 END) AS java.math.BigDecimal)/100 "//Monto Rechazado
            + ") "
            + "FROM TpDetalleMasivas d JOIN d.entidad e JOIN d.monedaDesc mo\n"
            + "WHERE (COALESCE(:fechaInicio, d.fechaTransaccion) IS NULL OR d.fechaTransaccion >= :fechaInicio) "
            + "      AND (COALESCE(:fechaFin, d.fechaTransaccion) IS NULL OR d.fechaTransaccion <= :fechaFin) "
            + "      AND (:codMoneda IS NULL OR d.moneda = :codMoneda)\n"
            + "GROUP BY CONCAT(d.codEntidadDestino, ' - ', e.nombre), CONCAT (d.moneda, ' - ', mo.valor)\n"
            + "ORDER BY CONCAT(d.codEntidadDestino, ' - ', e.nombre) ASC")
    List<DetalleReporteConsolidadoDTO> totalesPorEntidadDestino(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin,
            @Param("codMoneda") String codMoneda
    );
}
