package com.novatronic.masivas.backoffice.repository;

import com.novatronic.masivas.backoffice.entity.TpEntidad;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Obi Consulting
 */
@Repository
public interface EntidadRepository extends JpaRepository<TpEntidad, BigDecimal> {

}
