package com.novatronic.masivas.backoffice.repository;

import com.novatronic.masivas.backoffice.entity.TpAplicacion;
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
public interface AplicacionRepository extends JpaRepository<TpAplicacion, Long> {

    public Optional<TpEntidad> getByCodigo(String codigo);

    @Query("    SELECT a FROM TpAplicacion a\n"
            + "    WHERE (:codigo IS NULL OR LOWER(a.codigo) LIKE LOWER(CONCAT('%', :codigo, '%')))\n"
            + "      AND (:nombre IS NULL OR LOWER(a.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))\n"
            + "      AND (:estado IS NULL OR a.estado = :estado)\n")
    Page<TpAplicacion> buscarPorFiltros(
            @Param("codigo") String codigo,
            @Param("nombre") String nombre,
            @Param("estado") String estado,
            Pageable pageable
    );

}
