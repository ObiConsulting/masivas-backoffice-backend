package com.novatronic.masivas.backoffice.repository;

import com.novatronic.masivas.backoffice.dto.DetalleConsultaRutaDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroRutaDTO;
import com.novatronic.masivas.backoffice.entity.TpRuta;
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
public interface RutaRepository extends JpaRepository<TpRuta, Long> {

    @Query("    SELECT NEW com.novatronic.masivas.backoffice.dto.DetalleConsultaRutaDTO("
            + "     r.idRuta, td.valor, ta.valor, r.ruta) "
            + "    FROM TpRuta r "
            + "    JOIN TpParametro td ON r.codServer = td.codigo " // Join para el directorio
            + "    JOIN TpParametro ta ON r.codigoOperacion = ta.codigo " // Join para el tipo de archivo
            + "    WHERE ("
            + "        (:codCategoriaDirectorio IS NOT NULL AND r.codServer = :codCategoriaDirectorio)" // Si se pasa un valor, filtra por ese valor
            + "        OR "
            + "        (:codCategoriaDirectorio IS NULL AND r.codServer IN ('1000', '2000'))" // Si no se pasa, filtra por 1000 o 2000
            + "    )\n"
            + "    AND (:codTipoArchivo IS NULL OR r.codigoOperacion = :codTipoArchivo)\n")
    Page<DetalleConsultaRutaDTO> buscarPorFiltros(
            @Param("codTipoArchivo") String codTipoArchivo,
            @Param("codCategoriaDirectorio") String codCategoriaDirectorio,
            Pageable pageable
    );

    @Query("    SELECT NEW com.novatronic.masivas.backoffice.dto.DetalleRegistroRutaDTO("
            + "     r.idRuta, td.valor, ta.valor, r.ruta, r.usuCreacion, r.fecCreacion, r.usuModificacion, r.fecModificacion) "
            + " FROM TpRuta r "
            + " JOIN TpParametro td ON r.codServer = td.codigo " // Join para el directorio
            + " JOIN TpParametro ta ON r.codigoOperacion = ta.codigo " // Join para el tipo de archivo
            + "WHERE (r.idRuta = :idRuta)")
    DetalleRegistroRutaDTO buscarPorId(
            @Param("idRuta") Long idRuta
    );

}
