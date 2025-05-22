package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.entity.TpEntidad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import com.novatronic.masivas.backoffice.repository.EntidadRepository;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import jakarta.transaction.Transactional;
import java.util.Date;
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

    @Transactional
    public void crearEntidad(MasivasRequestDTO request, String usuario) {
        try {
            TpEntidad entidad = new TpEntidad();
            entidad.setCodEntidad(request.getCodigoEntidad());
            entidad.setDescEntidad(request.getDescripcionEntidad());
            entidad.setEstado(request.getEstado());
            entidad.setIdPerfil(1L);
            entidad.setFecCreacion(new Date());
            entidad.setUsuCreacion(usuario);

            System.out.println("before insert: ");
            TpEntidad tpEntidadSaved = entidadRepository.save(entidad);
            System.out.println("result insert: " + tpEntidadSaved);

        } catch (Exception e) {
            System.out.println("Error" + e);
        }
    }

    public Page<TpEntidad> buscar(FiltroMasivasRequest request, String usuario) {

        try {

            Pageable pageable = null;
            if (request.getCampoOrdenar().isEmpty()) {
                pageable = PageRequest.of(request.getNumeroPagina(), request.getRegistrosPorPagina());
            } else {
                if (ConstantesServices.ORDEN_ASC.equals(request.getSentidoOrdenar())) {
                    pageable = PageRequest.of(request.getNumeroPagina(), request.getRegistrosPorPagina(), Sort.by(request.getCampoOrdenar()).ascending());
                } else if (ConstantesServices.ORDEN_DESC.equals(request.getSentidoOrdenar())) {
                    pageable = PageRequest.of(request.getNumeroPagina(), request.getRegistrosPorPagina(), Sort.by(request.getCampoOrdenar()).descending());
                }
            }

            Page<TpEntidad> objPageable = entidadRepository.buscarPorFiltros(request.getCodigo(), request.getDescripcion(), request.getEstado(), pageable);
            return objPageable;

        } catch (Exception e) {
            System.out.println("Error" + e);
            return null;
        }

    }

}
