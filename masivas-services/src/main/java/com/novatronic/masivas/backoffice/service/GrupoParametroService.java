package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaGrupoParametroDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroGrupoParametroDTO;
import com.novatronic.masivas.backoffice.entity.TpGrupoParametro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import com.novatronic.masivas.backoffice.repository.GrupoParametroRepository;
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
public class GrupoParametroService {

    private final GrupoParametroRepository grupoParametroRepository;
    private final MessageSource messageSource;

    @Autowired
    public GrupoParametroService(GrupoParametroRepository grupoParametroRepository, MessageSource messageSource) {
        this.grupoParametroRepository = grupoParametroRepository;
        this.messageSource = messageSource;
    }

    @Transactional
    public Long crearGrupoParametro(MasivasRequestDTO request, String usuario) {
        TpGrupoParametro grupoParametro = new TpGrupoParametro(
                request.getCodigo(),
                request.getDescripcion(),
                ConstantesServices.ESTADO_INACTIVO,
                new Date(),
                usuario
        );
        System.out.println("before insert: ");
        grupoParametro = grupoParametroRepository.save(grupoParametro);
        System.out.println("result insert: " + grupoParametro);
        return grupoParametro.getIdGrupoParametro();
    }

    public CustomPaginate<DetalleConsultaGrupoParametroDTO> buscarGrupoParametro(FiltroMasivasRequest request, String usuario) {

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

        Page<TpGrupoParametro> objPageable = grupoParametroRepository.buscarPorFiltros(request.getCodigo(), request.getDescripcion(), request.getEstado(), pageable);

        Page<DetalleConsultaGrupoParametroDTO> dtoPage = objPageable.map(e -> modelMapper.map(e, DetalleConsultaGrupoParametroDTO.class));

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
    public Long editarGrupoParametro(MasivasRequestDTO request, String usuario) {

        TpGrupoParametro grupoParametro = grupoParametroRepository.findById(request.getIdGrupoParametro()).get();
        updateGrupoParametro(grupoParametro, request, usuario, ConstantesServices.OPERACION_EDITAR);

        System.out.println("before update: ");
        TpGrupoParametro tpGrupoParametroSaved = grupoParametroRepository.save(grupoParametro);
        System.out.println("result update: " + tpGrupoParametroSaved);
        return grupoParametro.getIdGrupoParametro();
    }

    @Transactional
    public DetalleRegistroGrupoParametroDTO obtenerGrupoParametro(FiltroMasivasRequest request, String usuario) {

        ModelMapper modelMapper = new ModelMapper();
        System.out.println("before findById: ");
        TpGrupoParametro grupoParametro = grupoParametroRepository.findById(request.getIdGrupoParametro()).get();
        System.out.println("result findById: " + grupoParametro);
        DetalleRegistroGrupoParametroDTO grupoParametroDTO = new DetalleRegistroGrupoParametroDTO();

        modelMapper.map(grupoParametro, grupoParametroDTO);
        return grupoParametroDTO;
    }

    @Transactional
    public Long eliminarGrupoParametro(MasivasRequestDTO request, String usuario) {

        TpGrupoParametro grupoParametro = grupoParametroRepository.findById(request.getIdGrupoParametro()).get();
        updateGrupoParametro(grupoParametro, request, usuario, ConstantesServices.BLANCO);

        System.out.println("before update: ");
        TpGrupoParametro tpGrupoParametroSaved = grupoParametroRepository.save(grupoParametro);
        System.out.println("result update: " + tpGrupoParametroSaved);
        return grupoParametro.getIdGrupoParametro();
    }

    private void updateGrupoParametro(TpGrupoParametro grupoParametro, MasivasRequestDTO request, String usuario, String operacion) {

        if (operacion.equals(ConstantesServices.OPERACION_EDITAR)) {
            grupoParametro.setCodigo(request.getCodigo());
            grupoParametro.setDescripcion(request.getDescripcion());
        } else {
            grupoParametro.setEstado(request.getEstado());
        }

        grupoParametro.setFecModificacion(new Date());
        grupoParametro.setUsuModificacion(usuario);
    }

}
