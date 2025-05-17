package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.entity.TpEntidad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import com.novatronic.masivas.backoffice.repository.EntidadRepository;
import java.util.List;

/**
 *
 * @author Obi Consulting
 */
@Service
public class EntidadService {

    private final EntidadRepository entidadRepository;
    private final MessageSource messageSource;

    @Autowired
    public EntidadService(EntidadRepository entidadRepository, MessageSource messageSource) {
        this.entidadRepository = entidadRepository;
        this.messageSource = messageSource;
    }

    public void crearEntidad(MasivasRequestDTO request, String usuario) {
//        TpEntidad entidad = new TpEntidad();

//        entidadRepository.save(this);
    }

//    public ResultadoOperacionConsulta buscar(FiltroMasivasRequest request, String usuario) {
    public String buscar(FiltroMasivasRequest request, String usuario) {
        List<TpEntidad> lista = entidadRepository.findAll();
        return null;
    }

}
