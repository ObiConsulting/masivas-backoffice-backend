package com.novatronic.masivas.backoffice.repository;

import com.novatronic.masivas.backoffice.entity.TpFacilidadBcrp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author obi
 */
@Repository
public interface TpFacilidadBcrpRepository extends JpaRepository<TpFacilidadBcrp, String> {

    @Query(value = "SELECT f.COD_FACILIDAD, f.DESCRIPCION, f.COD_CONCEPTO, f.COD_MONEDA, m.DESCRIPCION AS DESC_MONEDA, f.COD_ESTADO "
            + "FROM TP_FACILIDAD_BCRP f "
            + "LEFT JOIN TM_MONEDA m ON f.COD_MONEDA = m.COD_MONEDA",
            nativeQuery = true)
    List<Object[]> findAllFacilidadesConDescripcionMoneda();

}
