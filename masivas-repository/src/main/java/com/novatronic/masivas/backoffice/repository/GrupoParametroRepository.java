package com.novatronic.masivas.backoffice.repository;

import com.novatronic.masivas.backoffice.entity.TpGrupoParametro;
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
public interface GrupoParametroRepository extends JpaRepository<TpGrupoParametro, Long> {

    @Query("    SELECT g FROM TpGrupoParametro g\n"
            + "    WHERE (:codigo IS NULL OR LOWER(g.codigo) LIKE LOWER(CONCAT('%', :codigo, '%')))\n"
            + "      AND (:descripcion IS NULL OR LOWER(g.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%')))\n"
            + "      AND (:estado IS NULL OR g.estado = :estado)\n")
    Page<TpGrupoParametro> buscarPorFiltros(
            @Param("codigo") String codigo,
            @Param("descripcion") String descripcion,
            @Param("estado") String estado,
            Pageable pageable
    );

}
