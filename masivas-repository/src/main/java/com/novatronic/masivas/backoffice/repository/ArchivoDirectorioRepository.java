package com.novatronic.masivas.backoffice.repository;

import com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoDirectorioDTO;
import com.novatronic.masivas.backoffice.entity.TpArchivoDirectorio;
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
public interface ArchivoDirectorioRepository extends JpaRepository<TpArchivoDirectorio, Long> {

    public Optional<TpArchivoDirectorio> getByNombre(String nombre);

    @Query("    SELECT NEW com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoDirectorioDTO("
            + "d.idArchivo, d.nombre, d.cantidadDeclarado, d.fechaObtencion,"
            + "    CAST(CASE "
            + "        WHEN d.estadoEnviado IN (600L,601L) THEN (SELECT p.valor FROM TpParametro p WHERE p.codigo = '0703') "
            + "        WHEN d.estadoObtenido =400L THEN (SELECT p.valor FROM TpParametro p WHERE p.codigo = '0702')  "
            + "        ELSE 'Pendiente' "
            + "    END AS string), "
            + "    CASE "
            + "        WHEN d.estadoEnviado = 600L AND d.estadoFisico IS NULL AND FUNCTION('TO_CHAR', d.fecCreacion, 'YYYY-MM-DD') <> FUNCTION('TO_CHAR', CURRENT_DATE, 'YYYY-MM-DD') THEN true"
            + "        ELSE false"
            + "    END, "
            + "    CASE "
            + "        WHEN d.estadoEnviado = 600L AND (d.estadoFisico = 1000L or d.estadoFisico = 1002L) THEN true"
            + "        ELSE false"
            + "    END)"
            + "FROM TpArchivoDirectorio d\n"
            + "     WHERE (d.fechaObtencion IS NULL OR d.fechaObtencion >= :fechaInicio)\n"
            + "      AND (d.fechaObtencion IS NULL OR d.fechaObtencion <= :fechaFin)\n"
            + "      AND (:estado IS NULL OR "
            + "         CAST(CASE "
            + "            WHEN d.estadoEnviado IN (600L,601L)  THEN '0703' "
            + "            WHEN d.estadoObtenido =400L THEN '0702' "
            + "            ELSE 'Pendiente' "
            + "         END AS string) = :estado)")
    Page<DetalleConsultaArchivoDirectorioDTO> buscarPorFiltros(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin,
            @Param("estado") String estado,
            Pageable pageable
    );

    @Query("SELECT "
            + "CAST(CASE "
            + "    WHEN d.estadoEnviado IN (600L,601L) THEN 'Enviado Cliente' "
            + "    WHEN d.estadoProcesado IS NOT NULL THEN 'Obtenido CCE' "
            + "    WHEN d.estadoObtenido =400L THEN 'Obtenido CCE' "
            + "    ELSE 'Pendiente' "
            + "END AS string), "
            + "COUNT(d.idArchivo) "
            + "FROM TpArchivoDirectorio d "
            + "WHERE (COALESCE(:fechaInicio, d.fechaObtencion) IS NULL OR d.fechaObtencion >= :fechaInicio) "
            + "AND (COALESCE(:fechaFin, d.fechaObtencion) IS NULL OR d.fechaObtencion <= :fechaFin) "
            + "GROUP BY "
            + "CAST(CASE "
            + "    WHEN d.estadoEnviado IN (600L,601L)  THEN 'Enviado Cliente' "
            + "    WHEN d.estadoProcesado IS NOT NULL THEN 'Obtenido CCE' "
            + "    WHEN d.estadoObtenido =400L THEN 'Obtenido CCE' "
            + "    ELSE 'Pendiente' "
            + "END AS string)")
    List<Object[]> totalesPorEstado(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

}
