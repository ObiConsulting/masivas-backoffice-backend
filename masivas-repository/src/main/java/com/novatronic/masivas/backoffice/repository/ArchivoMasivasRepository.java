package com.novatronic.masivas.backoffice.repository;

import com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoMasivasDTO;
import com.novatronic.masivas.backoffice.entity.TpArchivoMasivas;
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
public interface ArchivoMasivasRepository extends JpaRepository<TpArchivoMasivas, Long> {

    @Query("    SELECT NEW com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoMasivasDTO("
            + "m.idArchivo, m.nombre, m.fechaObtencion, m.fechaProcesada, m.cantidadDeclarado, m.cantidadProcesado, m.montoProcesado, m.montoRechazado, "
            + "    CASE "
            + "        WHEN m.estadoEnviadoCliente IS NOT NULL THEN (SELECT p.valor FROM TpParametro p WHERE p.codigo = '0703') "
            + "        WHEN m.estadoObtenidoCCE IS NOT NULL THEN (SELECT p.valor FROM TpParametro p WHERE p.codigo = '0702') "
            + "        WHEN m.estadoEnviadoCCE IS NOT NULL THEN (SELECT p.valor FROM TpParametro p WHERE p.codigo = '0701') "
            + "        WHEN m.estadoObtenidoCliente IS NOT NULL THEN (SELECT p.valor FROM TpParametro p WHERE p.codigo = '0700') "
            + "        ELSE 'Pendiente' "
            + "    END)"
            + "FROM TpArchivoMasivas m\n"
            + "     WHERE (:fechaInicioObtencion IS NULL OR m.fechaObtencion >= :fechaInicioObtencion)\n"
            + "      AND (:fechaFinObtencion IS NULL OR m.fechaObtencion <= :fechaFinObtencion)\n"
            + "      AND (:fechaInicioProcesada IS NULL OR m.fechaProcesada >= :fechaInicioProcesada)\n"
            + "      AND (:fechaFinProcesada IS NULL OR m.fechaProcesada <= :fechaFinProcesada)\n"
            + "      AND (:estado IS NULL OR "
            + "         CASE "
            + "            WHEN m.estadoEnviadoCliente IS NOT NULL THEN '0703' "
            + "            WHEN m.estadoObtenidoCCE IS NOT NULL THEN '0702' "
            + "            WHEN m.estadoEnviadoCCE IS NOT NULL THEN '0701' "
            + "            WHEN m.estadoObtenidoCliente IS NOT NULL THEN '0700' "
            + "            ELSE 'Pendiente' "
            + "         END = :estado)")
    Page<DetalleConsultaArchivoMasivasDTO> buscarPorFiltros(
            @Param("fechaInicioObtencion") LocalDateTime fechaInicioObtencion,
            @Param("fechaFinObtencion") LocalDateTime fechaFinObtencion,
            @Param("fechaInicioProcesada") LocalDateTime fechaInicioProcesada,
            @Param("fechaFinProcesada") LocalDateTime fechaFinProcesada,
            @Param("estado") String estado,
            Pageable pageable
    );

    @Query("SELECT "
            + "CASE "
            + "    WHEN m.estadoEnviadoCliente IS NOT NULL THEN 'Enviado Cliente' "
            + "    WHEN m.estadoProcesadoCCE IS NOT NULL THEN 'Obtenido CCE' "
            + "    WHEN m.estadoObtenidoCCE IS NOT NULL THEN 'Obtenido CCE' "
            + "    WHEN m.estadoEnviadoCCE IS NOT NULL THEN 'Enviado CCE' "
            + "    WHEN m.estadoProcesadoCliente IS NOT NULL THEN 'Obtenido Cliente' "
            + "    WHEN m.estadoObtenidoCliente IS NOT NULL THEN 'Obtenido Cliente' "
            + "    ELSE 'Pendiente' "
            + "END, "
            + "COUNT(m.idArchivo)"
            + "FROM TpArchivoMasivas m "
            + "WHERE (:fechaInicio IS NULL OR m.fechaObtencion >= :fechaInicio) "
            + "AND (:fechaFin IS NULL OR m.fechaObtencion <= :fechaFin) "
            + "GROUP BY "
            + "CASE "
            + "    WHEN m.estadoEnviadoCliente IS NOT NULL THEN 'Enviado Cliente' "
            + "    WHEN m.estadoProcesadoCCE IS NOT NULL THEN 'Obtenido CCE' "
            + "    WHEN m.estadoObtenidoCCE IS NOT NULL THEN 'Obtenido CCE' "
            + "    WHEN m.estadoEnviadoCCE IS NOT NULL THEN 'Enviado CCE' "
            + "    WHEN m.estadoProcesadoCliente IS NOT NULL THEN 'Obtenido Cliente' "
            + "    WHEN m.estadoObtenidoCliente IS NOT NULL THEN 'Obtenido Cliente' "
            + "    ELSE 'Pendiente' "
            + "END")
    List<Object[]> totalesPorEstado(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    @Query("SELECT "
            + "CASE "
            + "    WHEN m.estadoEnviadoCliente IS NOT NULL THEN 'Procesado' "
            + "    WHEN m.estadoProcesadoCCE IS NOT NULL THEN 'Pendiente por Procesar' "
            + "    WHEN m.estadoObtenidoCCE IS NOT NULL THEN 'Pendiente por Procesar' "
            + "    WHEN m.estadoEnviadoCCE IS NOT NULL THEN 'Pendiente por Procesar' "
            + "    WHEN m.estadoProcesadoCliente IS NOT NULL THEN 'Pendiente por Procesar' "
            + "    WHEN m.estadoObtenidoCliente IS NOT NULL THEN 'Pendiente por Procesar' "
            + "    ELSE 'Pendiente' "
            + "END, "
            + "COUNT(m.idArchivo), "
            + "SUM(CASE WHEN m.estadoEnviadoCliente IS NOT NULL THEN m.montoProcesado ELSE 0 END), "
            + "SUM(CASE WHEN m.estadoEnviadoCliente IS NOT NULL THEN m.montoRechazado ELSE 0 END) "
            + "FROM TpArchivoMasivas m "
            + "WHERE (:fechaInicio IS NULL OR m.fechaObtencion >= :fechaInicio) "
            + "AND (:fechaFin IS NULL OR m.fechaObtencion <= :fechaFin) "
            + "GROUP BY "
            + "CASE "
            + "    WHEN m.estadoEnviadoCliente IS NOT NULL THEN 'Procesado' "
            + "    WHEN m.estadoProcesadoCCE IS NOT NULL THEN 'Pendiente por Procesar' "
            + "    WHEN m.estadoObtenidoCCE IS NOT NULL THEN 'Pendiente por Procesar' "
            + "    WHEN m.estadoEnviadoCCE IS NOT NULL THEN 'Pendiente por Procesar' "
            + "    WHEN m.estadoProcesadoCliente IS NOT NULL THEN 'Pendiente por Procesar' "
            + "    WHEN m.estadoObtenidoCliente IS NOT NULL THEN 'Pendiente por Procesar' "
            + "    ELSE 'Pendiente' "
            + "END")
    List<Object[]> reporteTotalizado(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

}
