package com.novatronic.masivas.backoffice.repository;

import com.novatronic.masivas.backoffice.entity.TpConceptoLiquidacionBcrp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author obi
 */
@Repository
public interface TpConceptoLiquidacionBcrpRepository extends JpaRepository<TpConceptoLiquidacionBcrp, String> {

    @Query(value = """
    SELECT 
        c.COD_CONCEPTO,
        c.COD_CONCEPTO ||' - '|| c.DES_CONCEPTO,
        tx.COD_TRANSACCION,
        tct.COD_PANEL,
        m.COD_MONEDA,
        m.DESCRIPCION,
        m.SIMBOLO
    FROM 
        TR_CONCEPTO_TRANSACCION tct
    JOIN 
        TP_CONCEPTO_LIQUIDACION_BCRP c ON tct.COD_CONCEPTO = c.COD_CONCEPTO
    JOIN 
        TP_TRANSACCION tx ON tct.COD_TRANSACCION = tx.COD_TRANSACCION
    JOIN 
        TM_MONEDA m ON c.COD_MONEDA = m.COD_MONEDA
""", nativeQuery = true)
    List<Object[]> findAllConceptosRaw();

}
