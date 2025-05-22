package com.novatronic.masivas.backoffice.repository;

import com.novatronic.masivas.backoffice.entity.TpEntidad;
import java.math.BigDecimal;
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
public interface EntidadRepository extends JpaRepository<TpEntidad, BigDecimal> {

    public Optional<TpEntidad> getByCodEntidad(String codigo);

//    @Query(
//            value = """
//    SELECT * FROM TP_ENTIDAD 
//    WHERE (:codEntidad IS NULL OR COD_ENTIDAD = :codEntidad)
//      AND (:descEntidad IS NULL OR DESC_ENTIDAD LIKE %:descEntidad%)
//      AND (:estado IS NULL OR ESTADO = :estado)
//    ORDER BY 
//      CASE WHEN :sortDir = 'ASC' THEN 
//          CASE WHEN :sortField = 'codEntidad' THEN COD_ENTIDAD
//               WHEN :sortField = 'descEntidad' THEN DESC_ENTIDAD
//               ELSE ID_ENTIDAD
//          END
//      END ASC,
//      CASE WHEN :sortDir = 'DESC' THEN 
//          CASE WHEN :sortField = 'codEntidad' THEN COD_ENTIDAD
//               WHEN :sortField = 'descEntidad' THEN DESC_ENTIDAD
//               ELSE ID_ENTIDAD
//          END
//      END DESC
//  """,
//            countQuery = "SELECT COUNT(*) FROM TP_ENTIDAD ...",
//            nativeQuery = true
//    )
//    Page<TpEntidad> buscarPorFiltros(
//            @Param("codEntidad") String codEntidad,
//            @Param("descEntidad") String descEntidad,
//            @Param("estado") String estado,
//            @Param("sortField") String sortField,
//            @Param("sortDir") String sortDir,
//            Pageable pageable
//    );
//    
    @Query("    SELECT e FROM TpEntidad e\n"
            + "    WHERE (:codEntidad IS NULL OR e.codEntidad = :codEntidad)\n"
            + "      AND (:descEntidad IS NULL OR LOWER(e.descEntidad) LIKE LOWER(CONCAT('%', :descEntidad, '%')))\n"
            + "      AND (:estado IS NULL OR e.estado = :estado)\n")
    Page<TpEntidad> buscarPorFiltros(
            @Param("codEntidad") String codEntidad,
            @Param("descEntidad") String descEntidad,
            @Param("estado") String estado,
            Pageable pageable
    );

}
