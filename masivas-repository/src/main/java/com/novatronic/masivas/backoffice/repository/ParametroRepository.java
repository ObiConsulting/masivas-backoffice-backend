package com.novatronic.masivas.backoffice.repository;

import com.novatronic.masivas.backoffice.dto.DetalleConsultaParametroDTO;
import com.novatronic.masivas.backoffice.entity.TpParametro;
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
public interface ParametroRepository extends JpaRepository<TpParametro, Long> {

    @Query("    SELECT NEW com.novatronic.masivas.backoffice.dto.DetalleConsultaParametroDTO("
            + "p.idParametro, p.codigo, p.valor, gp.descripcion, p.estado) "
            + "FROM TpParametro p JOIN p.grupoParametro gp\n"
            + "    WHERE (:codigo IS NULL OR LOWER(p.codigo) LIKE LOWER(CONCAT('%', :codigo, '%')))\n"
            + "      AND (:idGrupoParametro IS NULL OR gp.idGrupoParametro = :idGrupoParametro)\n"
            + "      AND (:estado IS NULL OR p.estado = :estado)\n")
    Page<DetalleConsultaParametroDTO> buscarPorFiltros(
            @Param("codigo") String codigo,
            @Param("idGrupoParametro") Long idGrupoParametro,
            @Param("estado") String estado,
            Pageable pageable
    );

    @Query("    SELECT p FROM TpParametro p\n"
            + "    WHERE (:listaCodigo IS NULL OR p.codigo IN :listaCodigo)\n"
            + "      AND (:estado IS NULL OR p.estado = :estado)\n")
    List<TpParametro> buscarEstadoArchivo(
            @Param("listaCodigo") List<String> listaCodigo,
            @Param("estado") String estado
    );

}
