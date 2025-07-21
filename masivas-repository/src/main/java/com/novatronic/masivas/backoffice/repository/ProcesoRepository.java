package com.novatronic.masivas.backoffice.repository;

import com.novatronic.masivas.backoffice.entity.TpProceso;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Obi Consulting
 */
@Repository
public interface ProcesoRepository extends JpaRepository<TpProceso, Long> {

    List<TpProceso> findByCodServerInAndCodigoAccionIn(List<String> codServers, List<String> codigoAcciones);

    List<TpProceso> findByCodServerInAndCodigoAccionInAndCodigoOperacion(List<String> codServers, List<String> codigoAcciones, String codigoOperacion);

}
