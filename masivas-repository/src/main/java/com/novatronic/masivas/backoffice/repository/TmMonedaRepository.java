/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.novatronic.masivas.backoffice.repository;

import com.novatronic.masivas.backoffice.entity.TmMoneda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author obi
 */
@Repository
public interface TmMonedaRepository extends JpaRepository<TmMoneda, String> {
    
    //List<TmMoneda> findAll();
}
