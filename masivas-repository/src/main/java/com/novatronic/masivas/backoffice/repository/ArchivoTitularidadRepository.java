package com.novatronic.masivas.backoffice.repository;

import com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoTitularidadDTO;
import com.novatronic.masivas.backoffice.entity.TpArchivoTitularidad;
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
public interface ArchivoTitularidadRepository extends JpaRepository<TpArchivoTitularidad, Long> {

    @Query("    SELECT NEW com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoTitularidadDTO("
            + "m.idArchivo, m.nombre, m.fechaObtencion, m.fechaProcesada, m.cantidadDeclarado, m.cantidadProcesado, "
            + "    CASE "
            + "        WHEN m.estadoProcesadoCCE IS NOT NULL THEN '0703' "
            + "        WHEN m.estadoObtencionCCE IS NOT NULL THEN '0702' "
            + "        WHEN m.estadoProcesadoCliente IS NOT NULL THEN '0701' "
            + "        WHEN m.estadoObtencionCliente IS NOT NULL THEN '0700' "
            + "        ELSE 'Pendiente' "
            + "    END)"
            + "FROM TpArchivoMasivas m\n"
            + "     WHERE (:fechaInicioObtencion IS NULL OR m.fechaObtencion >= :fechaInicioObtencion)\n"
            + "      AND (:fechaFinObtencion IS NULL OR m.fechaObtencion <= :fechaFinObtencion)\n"
            + "      AND (:fechaInicioProcesada IS NULL OR m.fechaProcesada >= :fechaInicioProcesada)\n"
            + "      AND (:fechaFinProcesada IS NULL OR m.fechaProcesada <= :fechaFinProcesada)\n"
            + "      AND (:estado IS NULL OR "
            + "         CASE "
            + "            WHEN m.estadoProcesadoCCE IS NOT NULL THEN '0703' "
            + "            WHEN m.estadoObtencionCCE IS NOT NULL THEN '0702' "
            + "            WHEN m.estadoProcesadoCliente IS NOT NULL THEN '0701' "
            + "            WHEN m.estadoObtencionCliente IS NOT NULL THEN '0700' "
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

}
