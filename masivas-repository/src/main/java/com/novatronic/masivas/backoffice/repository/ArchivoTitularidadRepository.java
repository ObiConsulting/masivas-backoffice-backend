package com.novatronic.masivas.backoffice.repository;

import com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoTitularidadDTO;
import com.novatronic.masivas.backoffice.entity.TpArchivoTitularidad;
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
public interface ArchivoTitularidadRepository extends JpaRepository<TpArchivoTitularidad, Long> {

    @Query("    SELECT NEW com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoTitularidadDTO("
            + "t.idArchivo, t.nombre, t.fechaObtencion, t.fechaProcesada, t.cantidadDeclarado, t.cantidadProcesado, "
            + "    CASE "
            + "        WHEN t.estadoEnviadoCliente IS NOT NULL THEN (SELECT p.valor FROM TpParametro p WHERE p.codigo = '0703') "
            + "        WHEN t.estadoObtenidoCCE IS NOT NULL THEN (SELECT p.valor FROM TpParametro p WHERE p.codigo = '0702') "
            + "        WHEN t.estadoEnviadoCCE IS NOT NULL THEN (SELECT p.valor FROM TpParametro p WHERE p.codigo = '0701') "
            + "        WHEN t.estadoObtenidoCliente IS NOT NULL THEN (SELECT p.valor FROM TpParametro p WHERE p.codigo = '0700') "
            + "        ELSE 'Pendiente' "
            + "    END)"
            + "FROM TpArchivoTitularidad t\n"
            + "     WHERE (:fechaInicioObtencion IS NULL OR t.fechaObtencion >= :fechaInicioObtencion)\n"
            + "      AND (:fechaFinObtencion IS NULL OR t.fechaObtencion <= :fechaFinObtencion)\n"
            + "      AND (:fechaInicioProcesada IS NULL OR t.fechaProcesada >= :fechaInicioProcesada)\n"
            + "      AND (:fechaFinProcesada IS NULL OR t.fechaProcesada <= :fechaFinProcesada)\n"
            + "      AND (:estado IS NULL OR "
            + "         CASE "
            + "            WHEN t.estadoEnviadoCliente IS NOT NULL THEN '0703' "
            + "            WHEN t.estadoObtenidoCCE IS NOT NULL THEN '0702' "
            + "            WHEN t.estadoEnviadoCCE IS NOT NULL THEN '0701' "
            + "            WHEN t.estadoObtenidoCliente IS NOT NULL THEN '0700' "
            + "            ELSE 'Pendiente' "
            + "         END = :estado)")
    Page<DetalleConsultaArchivoTitularidadDTO> buscarPorFiltros(
            @Param("fechaInicioObtencion") LocalDateTime fechaInicioObtencion,
            @Param("fechaFinObtencion") LocalDateTime fechaFinObtencion,
            @Param("fechaInicioProcesada") LocalDateTime fechaInicioProcesada,
            @Param("fechaFinProcesada") LocalDateTime fechaFinProcesada,
            @Param("estado") String estado,
            Pageable pageable
    );

    @Query("SELECT "
            + "CASE "
            + "    WHEN t.estadoEnviadoCliente IS NOT NULL THEN 'Enviado Cliente' "
            + "    WHEN t.estadoProcesadoCCE IS NOT NULL THEN 'Obtenido CCE' "
            + "    WHEN t.estadoObtenidoCCE IS NOT NULL THEN 'Obtenido CCE' "
            + "    WHEN t.estadoEnviadoCCE IS NOT NULL THEN 'Enviado CCE' "
            + "    WHEN t.estadoProcesadoCliente IS NOT NULL THEN 'Obtenido Cliente' "
            + "    WHEN t.estadoObtenidoCliente IS NOT NULL THEN 'Obtenido Cliente' "
            + "    ELSE 'Pendiente' "
            + "END, "
            + "COUNT(t.idArchivo) "
            + "FROM TpArchivoTitularidad t "
            + "WHERE (:fechaInicio IS NULL OR t.fechaObtencion >= :fechaInicio) "
            + "AND (:fechaFin IS NULL OR t.fechaObtencion <= :fechaFin) "
            + "GROUP BY "
            + "CASE "
            + "    WHEN t.estadoEnviadoCliente IS NOT NULL THEN 'Enviado Cliente' "
            + "    WHEN t.estadoProcesadoCCE IS NOT NULL THEN 'Obtenido CCE' "
            + "    WHEN t.estadoObtenidoCCE IS NOT NULL THEN 'Obtenido CCE' "
            + "    WHEN t.estadoEnviadoCCE IS NOT NULL THEN 'Enviado CCE' "
            + "    WHEN t.estadoProcesadoCliente IS NOT NULL THEN 'Obtenido Cliente' "
            + "    WHEN t.estadoObtenidoCliente IS NOT NULL THEN 'Obtenido Cliente' "
            + "    ELSE 'Pendiente' "
            + "END")
    List<Object[]> totalesPorEstado(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );
}
