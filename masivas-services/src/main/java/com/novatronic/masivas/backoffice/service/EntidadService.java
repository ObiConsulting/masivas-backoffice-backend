package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaEntidadDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroEntidadDTO;
import com.novatronic.masivas.backoffice.entity.TpEntidad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import com.novatronic.masivas.backoffice.repository.EntidadRepository;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import jakarta.transaction.Transactional;
import java.util.Date;
import org.modelmapper.ModelMapper;
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
    public Long crearEntidad(MasivasRequestDTO request, String usuario) {
        TpEntidad entidad = new TpEntidad(
                request.getCodigo(),
                request.getNombre(),
                ConstantesServices.ESTADO_INACTIVO,
                1L,
                new Date(),
                usuario
        );
        System.out.println("before insert: ");
        entidad = entidadRepository.save(entidad);
        System.out.println("result insert: " + entidad);
        return entidad.getIdEntidad();
    }

    public CustomPaginate<DetalleConsultaEntidadDTO> buscar(FiltroMasivasRequest request, String usuario) {

        ModelMapper modelMapper = new ModelMapper();
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

        Page<TpEntidad> objPageable = entidadRepository.buscarPorFiltros(request.getCodigo(), request.getNombre(), request.getEstado(), pageable);

        Page<DetalleConsultaEntidadDTO> dtoPage = objPageable.map(e -> modelMapper.map(e, DetalleConsultaEntidadDTO.class));

        int totalPaginas = objPageable.getTotalPages();
        long totalRegistrosLong = objPageable.getTotalElements();

        int totalRegistros = (totalRegistrosLong > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) totalRegistrosLong;

        CustomPaginate customPaginate = new CustomPaginate<>(
                totalPaginas,
                totalRegistros,
                dtoPage.getContent()
        );

        return customPaginate;

    }

    @Transactional
    public Long editarEntidad(MasivasRequestDTO request, String usuario) {

        TpEntidad entidad = entidadRepository.findById(request.getIdEntidad()).get();
        updateEntidad(entidad, request, usuario, ConstantesServices.OPERACION_EDITAR);

        System.out.println("before update: ");
        TpEntidad tpEntidadSaved = entidadRepository.save(entidad);
        System.out.println("result update: " + tpEntidadSaved);
        return entidad.getIdEntidad();
    }

    @Transactional
    public DetalleRegistroEntidadDTO obtenerEntidad(FiltroMasivasRequest request, String usuario) {

        ModelMapper modelMapper = new ModelMapper();
        System.out.println("before findById: ");
        TpEntidad entidad = entidadRepository.findById(request.getIdEntidad()).get();
        System.out.println("result findById: " + entidad);
        DetalleRegistroEntidadDTO entidadDTO = new DetalleRegistroEntidadDTO();

        modelMapper.map(entidad, entidadDTO);
        return entidadDTO;
    }

    @Transactional
    public Long eliminarEntidad(MasivasRequestDTO request, String usuario) {

        TpEntidad entidad = entidadRepository.findById(request.getIdEntidad()).get();
        updateEntidad(entidad, request, usuario, ConstantesServices.BLANCO);

        System.out.println("before update: ");
        TpEntidad tpEntidadSaved = entidadRepository.save(entidad);
        System.out.println("result update: " + tpEntidadSaved);
        return entidad.getIdEntidad();
    }

    private void updateEntidad(TpEntidad entidad, MasivasRequestDTO request, String usuario, String operacion) {

        if (operacion.equals(ConstantesServices.OPERACION_EDITAR)) {
            entidad.setCodigo(request.getCodigo());
            entidad.setNombre(request.getNombre());
        } else {
            entidad.setEstado(request.getEstado());
        }

        entidad.setFecModificacion(new Date());
        entidad.setUsuModificacion(usuario);
    }

}
