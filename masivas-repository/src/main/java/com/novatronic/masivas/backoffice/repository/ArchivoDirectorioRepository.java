package com.novatronic.masivas.backoffice.repository;

import com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoDirectorioDTO;
import com.novatronic.masivas.backoffice.entity.TpArchivoDirectorio;
import java.time.LocalDateTime;
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

    @Query("    SELECT NEW com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoDirectorioDTO("
            + "d.idArchivo, d.nombre, d.cantidadDeclarado, d.fechaObtencion,"
            + "    CASE "
            + "        WHEN d.estadoProcesado IS NOT NULL THEN (SELECT p.valor FROM TpParametro p WHERE p.codigo = '0703') "
            + "        WHEN d.estadoObtencion IS NOT NULL THEN (SELECT p.valor FROM TpParametro p WHERE p.codigo = '0702')  "
            + "        ELSE 'Pendiente' "
            + "    END)"
            + "FROM TpArchivoDirectorio d\n"
            + "     WHERE (:fechaInicio IS NULL OR d.fechaObtencion >= :fechaInicio)\n"
            + "      AND (:fechaFin IS NULL OR d.fechaObtencion <= :fechaFin)\n"
            + "      AND (:estado IS NULL OR "
            + "         CASE "
            + "            WHEN d.estadoProcesado IS NOT NULL THEN (SELECT p.valor FROM TpParametro p WHERE p.codigo = '0703') "
            + "            WHEN d.estadoObtencion IS NOT NULL THEN (SELECT p.valor FROM TpParametro p WHERE p.codigo = '0702') "
            + "            ELSE 'Pendiente' "
            + "         END = :estado)")
    Page<DetalleConsultaArchivoDirectorioDTO> buscarPorFiltros(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin,
            @Param("estado") String estado,
            Pageable pageable
    );

}
