package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.entity.TpEntidad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import com.novatronic.masivas.backoffice.repository.EntidadRepository;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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

    public Page<TpEntidad> buscar(FiltroMasivasRequest request, String usuario) {
        try {
            Pageable pageable = null;
            if (request.getCampoOrdenamiento().isEmpty()) {
                pageable = PageRequest.of(request.getNumeroPagina(), request.getRegistrosPorPagina());
            } else {
                if (ConstantesServices.ORDEN_ASC.equals(request.getSentidoOrdenamiento())) {
                    pageable = PageRequest.of(request.getNumeroPagina(), request.getRegistrosPorPagina(), Sort.by(request.getCampoOrdenamiento()).ascending());
                } else if (ConstantesServices.ORDEN_DESC.equals(request.getSentidoOrdenamiento())) {
                    pageable = PageRequest.of(request.getNumeroPagina(), request.getRegistrosPorPagina(), Sort.by(request.getCampoOrdenamiento()).descending());
                }

            }
            Page<TpEntidad> objPageable = entidadRepository.buscarPorFiltros(request.getCodigoEntidad(), request.getDescripcionEntidad(), request.getEstado(), pageable);
            return objPageable;
        } catch (Exception e) {
            System.out.println("Error" + e);
            return null;
        }

    }

}
