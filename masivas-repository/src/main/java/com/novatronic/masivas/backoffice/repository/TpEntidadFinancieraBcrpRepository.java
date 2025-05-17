/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.novatronic.masivas.backoffice.repository;

import com.novatronic.masivas.backoffice.entity.TpEntidadFinancieraBcrp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author obi
 */
@Repository
public interface TpEntidadFinancieraBcrpRepository extends JpaRepository<TpEntidadFinancieraBcrp, String> {
    
    
    @Query("SELECT ef FROM TpEntidadFinancieraBcrp ef LEFT JOIN FETCH ef.cuentasCorrientes")
    List<TpEntidadFinancieraBcrp> findAllWithCuentasCorrientes();
    
}
