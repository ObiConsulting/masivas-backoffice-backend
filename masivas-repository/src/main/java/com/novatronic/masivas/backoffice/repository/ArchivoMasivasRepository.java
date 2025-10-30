package com.novatronic.masivas.backoffice.repository;

import com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoMasivasDTO;
import com.novatronic.masivas.backoffice.entity.TpArchivoMasivas;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

    public Optional<TpArchivoMasivas> getByNombre(String nombre);

    @Query("    SELECT NEW com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoMasivasDTO("
            + "m.idArchivo, m.nombre, m.fechaObtencion, m.fechaProcesada, m.cantidadDeclarado, m.cantidadProcesado, m.montoProcesadoDolar, m.montoProcesadoSol, m.montoRechazadoDolar, m.montoRechazadoSol, "
            + "    CAST(CASE "
            + "        WHEN m.estadoEnviadoCliente IS NOT NULL THEN (SELECT p.valor FROM TpParametro p WHERE p.codigo = '0703') "
            + "        WHEN m.estadoObtenidoCCE IS NOT NULL THEN (SELECT p.valor FROM TpParametro p WHERE p.codigo = '0702') "
            + "        WHEN m.estadoEnviadoCCE IS NOT NULL THEN (SELECT p.valor FROM TpParametro p WHERE p.codigo = '0701') "
            + "        WHEN m.estadoObtenidoCliente IS NOT NULL THEN (SELECT p.valor FROM TpParametro p WHERE p.codigo = '0700') "
            + "        ELSE 'Pendiente' "
            + "    END AS string), "
            + "    CASE "
            + "        WHEN m.estadoEnviadoCliente = 600L AND m.estadoFisico IS NULL AND FUNCTION('TO_CHAR', m.fecCreacion, 'YYYY-MM-DD') <> FUNCTION('TO_CHAR', CURRENT_DATE, 'YYYY-MM-DD') THEN true"
            + "        ELSE false"
            + "    END, "
            + "    CASE "
            + "        WHEN m.estadoEnviadoCliente = 600L AND (m.estadoFisico = 1000L or m.estadoFisico = 1002L) THEN true"
            + "        ELSE false"
            + "    END)"
            + "FROM TpArchivoMasivas m\n"
            + "     WHERE (COALESCE(:fechaInicioObtencion, m.fechaObtencion) IS NULL OR m.fechaObtencion >= :fechaInicioObtencion)\n"
            + "      AND (COALESCE(:fechaFinObtencion, m.fechaObtencion) IS NULL OR m.fechaObtencion <= :fechaFinObtencion)\n"
            + "      AND (COALESCE(:fechaInicioProcesada, m.fechaProcesada) IS NULL OR m.fechaProcesada >= :fechaInicioProcesada)\n"
            + "      AND (COALESCE(:fechaFinProcesada, m.fechaProcesada) IS NULL OR m.fechaProcesada <= :fechaFinProcesada)\n"
            + "      AND (:estado IS NULL OR "
            + "         CAST(CASE "
            + "            WHEN m.estadoEnviadoCliente IS NOT NULL THEN '0703' "
            + "            WHEN m.estadoObtenidoCCE IS NOT NULL THEN '0702' "
            + "            WHEN m.estadoEnviadoCCE IS NOT NULL THEN '0701' "
            + "            WHEN m.estadoObtenidoCliente IS NOT NULL THEN '0700' "
            + "            ELSE 'Pendiente' "
            + "         END AS string) = :estado)")
    Page<DetalleConsultaArchivoMasivasDTO> buscarPorFiltros(
            @Param("fechaInicioObtencion") LocalDateTime fechaInicioObtencion,
            @Param("fechaFinObtencion") LocalDateTime fechaFinObtencion,
            @Param("fechaInicioProcesada") LocalDateTime fechaInicioProcesada,
            @Param("fechaFinProcesada") LocalDateTime fechaFinProcesada,
            @Param("estado") String estado,
            Pageable pageable
    );

    @Query("SELECT "
            + "CAST(CASE "
            + "    WHEN m.estadoEnviadoCliente IS NOT NULL THEN 'Enviado Cliente' "
            + "    WHEN m.estadoProcesadoCCE IS NOT NULL THEN 'Obtenido CCE' "
            + "    WHEN m.estadoObtenidoCCE IS NOT NULL THEN 'Obtenido CCE' "
            + "    WHEN m.estadoEnviadoCCE IS NOT NULL THEN 'Enviado CCE' "
            + "    WHEN m.estadoProcesadoCliente IS NOT NULL THEN 'Obtenido Cliente' "
            + "    WHEN m.estadoObtenidoCliente IS NOT NULL THEN 'Obtenido Cliente' "
            + "    ELSE 'Pendiente' "
            + "END AS string), "
            + "COUNT(m.idArchivo)"
            + "FROM TpArchivoMasivas m "
            + "WHERE (COALESCE(:fechaInicio, m.fechaObtencion) IS NULL OR m.fechaObtencion >= :fechaInicio) "
            + "AND (COALESCE(:fechaFin, m.fechaObtencion) IS NULL OR m.fechaObtencion <= :fechaFin) "
            + "GROUP BY "
            + "CAST(CASE "
            + "    WHEN m.estadoEnviadoCliente IS NOT NULL THEN 'Enviado Cliente' "
            + "    WHEN m.estadoProcesadoCCE IS NOT NULL THEN 'Obtenido CCE' "
            + "    WHEN m.estadoObtenidoCCE IS NOT NULL THEN 'Obtenido CCE' "
            + "    WHEN m.estadoEnviadoCCE IS NOT NULL THEN 'Enviado CCE' "
            + "    WHEN m.estadoProcesadoCliente IS NOT NULL THEN 'Obtenido Cliente' "
            + "    WHEN m.estadoObtenidoCliente IS NOT NULL THEN 'Obtenido Cliente' "
            + "    ELSE 'Pendiente' "
            + "END AS string)")
    List<Object[]> totalesPorEstado(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    @Query("SELECT "
            + "CAST(CASE "
            + "    WHEN m.estadoEnviadoCliente IS NOT NULL THEN 'Procesado' "
            + "    WHEN m.estadoProcesadoCCE IS NOT NULL THEN 'Pendiente por Procesar' "
            + "    WHEN m.estadoObtenidoCCE IS NOT NULL THEN 'Pendiente por Procesar' "
            + "    WHEN m.estadoEnviadoCCE IS NOT NULL THEN 'Pendiente por Procesar' "
            + "    WHEN m.estadoProcesadoCliente IS NOT NULL THEN 'Pendiente por Procesar' "
            + "    WHEN m.estadoObtenidoCliente IS NOT NULL THEN 'Pendiente por Procesar' "
            + "    ELSE 'Pendiente' "
            + "END AS string), "
            + "COUNT(m.idArchivo), "
            + "SUM(CASE WHEN m.estadoEnviadoCliente IS NOT NULL THEN m.montoProcesadoDolar ELSE 0 END), "
            + "SUM(CASE WHEN m.estadoEnviadoCliente IS NOT NULL THEN m.montoProcesadoSol ELSE 0 END), "
            + "SUM(CASE WHEN m.estadoEnviadoCliente IS NOT NULL THEN m.montoRechazadoDolar ELSE 0 END), "
            + "SUM(CASE WHEN m.estadoEnviadoCliente IS NOT NULL THEN m.montoRechazadoSol ELSE 0 END) "
            + "FROM TpArchivoMasivas m "
            + "WHERE (COALESCE(:fechaInicio, m.fechaObtencion) IS NULL OR m.fechaObtencion >= :fechaInicio) "
            + "AND (COALESCE(:fechaFin, m.fechaObtencion) IS NULL OR m.fechaObtencion <= :fechaFin) "
            + "GROUP BY "
            + "CAST(CASE "
            + "    WHEN m.estadoEnviadoCliente IS NOT NULL THEN 'Procesado' "
            + "    WHEN m.estadoProcesadoCCE IS NOT NULL THEN 'Pendiente por Procesar' "
            + "    WHEN m.estadoObtenidoCCE IS NOT NULL THEN 'Pendiente por Procesar' "
            + "    WHEN m.estadoEnviadoCCE IS NOT NULL THEN 'Pendiente por Procesar' "
            + "    WHEN m.estadoProcesadoCliente IS NOT NULL THEN 'Pendiente por Procesar' "
            + "    WHEN m.estadoObtenidoCliente IS NOT NULL THEN 'Pendiente por Procesar' "
            + "    ELSE 'Pendiente' "
            + "END AS string)")
    List<Object[]> reporteTotalizado(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

}
