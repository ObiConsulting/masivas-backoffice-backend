package com.novatronic.masivas.backoffice.repository;

import com.novatronic.masivas.backoffice.entity.TpEntidad;
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
public interface EntidadRepository extends JpaRepository<TpEntidad, Long> {

    public Optional<TpEntidad> getByCodigo(String codigo);

    @Query("    SELECT e FROM TpEntidad e\n"
            + "    WHERE (:codigo IS NULL OR LOWER(e.codigo) LIKE LOWER(CONCAT('%', :codigo, '%')))\n"
            + "      AND (:nombre IS NULL OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))\n"
            + "      AND (:estado IS NULL OR e.estado = :estado)\n")
    Page<TpEntidad> buscarPorFiltros(
            @Param("codigo") String codigo,
            @Param("nombre") String nombre,
            @Param("estado") String estado,
            Pageable pageable
    );

}
